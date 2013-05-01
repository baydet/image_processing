import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 * Created with IntelliJ IDEA.
 * User: baydet
 * Date: 5/1/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageProcessor
{
    public static final int COLOR_COUNT = 256;
    private ImagePanel ip;
    private BufferedImage image;
    private BufferedImage originalImage;
    private int [] gistoArr =  new int[COLOR_COUNT];


    public ImageProcessor(BufferedImage image)
    {
        super();
    }

    public ImageProcessor() {
        super();
    }

    public ImageProcessor(ImagePanel imagePanel) {
        super();
        ip = imagePanel;
    }

    public void setImage(BufferedImage im)
    {
        image = im;
        originalImage = deepCopy(im);
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void toGrayscale() {
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight(); ++j)
            {
                Color c = new Color(image.getRGB(i, j));
                int gray = (int) (0.3*c.getRed() + 0.59*c.getGreen() + 0.11*c.getBlue());
                image.setRGB(i, j, new Color(gray, gray, gray).getRGB());
            }
        }
        ip.repaint();
    }

    public void restoreImage() {
        image = originalImage;
        originalImage = deepCopy(originalImage);
        gistoArr =  new int[COLOR_COUNT];
        ip.repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void invert()
    {
        this.toGrayscale();
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight(); ++j)
            {
                Color c = new Color(image.getRGB(i, j));
                int gray = 255 - c.getRed();
                image.setRGB(i, j, new Color(gray, gray, gray).getRGB());
            }
        }
        ip.repaint();
    }

    public void getGistogramm()
    {
        buildGisto();
        GistoDialog d = new GistoDialog(gistoArr);
    }

    public void buildGisto()
    {
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight(); ++j)
            {
                Color c = new Color(image.getRGB(i, j));
                int index = c.getRed();
                gistoArr[index]++;
            }
        }
    }

    public void toBinary(int bound)
    {
        toBinary(bound, image.getWidth(), image.getHeight());
    }

    public void toBinary(int bound, int width, int height)
    {
        for (int i = 0; i < width; ++i)
        {
            for (int j = 0; j < height; ++j)
            {
                Color c = new Color(image.getRGB(i, j));
                int gray = c.getRed();
                gray = gray < bound ? 0 : 255;
                image.setRGB(i, j, new Color(gray, gray, gray).getRGB());
            }
        }
        ip.repaint();
    }


    public void findBorder() {
        findBorder(0, 0, image.getWidth(), image.getHeight());
    }

    public void findBorder(int x, int y, int width, int height)
    {
        restoreImage();
        toGrayscale();
        buildGisto();

        int peakCount = Integer.MAX_VALUE;
        int border = Integer.MAX_VALUE;;
        while (true)
        {
            for (int i = 1; i < COLOR_COUNT-1; ++i)
            {
                gistoArr[i] = (gistoArr[i-1]+gistoArr[i+1])/2;
            }
            peakCount = 0;
            border = COLOR_COUNT;
            for (int i = 1; i < COLOR_COUNT-1; ++i)
            {
                if (gistoArr[i] > gistoArr[i-1] && gistoArr[i] > gistoArr[i+1])
                {
                    ++peakCount;
                    continue;
                }
                if (peakCount > 0 && gistoArr[i] < gistoArr[i-1] && gistoArr[i] < gistoArr[i+1])
                    border = i;
            }
            if (peakCount <= 2)
                break;
        }
        toBinary(border);
    }
}
