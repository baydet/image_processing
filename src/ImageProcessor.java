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
    private ImagePanel ip;
    private BufferedImage image;
    private BufferedImage originalImage;
    private int [] gistoArr =  new int[256];


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
        gistoArr =  new int[256];
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
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight(); ++j)
            {
                Color c = new Color(image.getRGB(i, j));
                int index = c.getRed();
                gistoArr[index]++;
            }
        }
        GistoDialog d = new GistoDialog(gistoArr);
    }

    public void toBinary(int bound)
    {
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight(); ++j)
            {
                Color c = new Color(image.getRGB(i, j));
                int gray = c.getRed();
                gray = gray < bound ? 0 : 255;
                image.setRGB(i, j, new Color(gray, gray, gray).getRGB());
            }
        }
        ip.repaint();
    }
}
