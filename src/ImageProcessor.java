import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: baydet
 * Date: 5/1/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */

class Segment
{
    public int l, r, b;

    public Segment(int _l, int _r, int _b)
    {
        super();
        l = _l; r = _r; b = _b;
    }

    public Segment() {}
}

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
        this.gistoArr = new int[256];
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
        int border = Integer.MAX_VALUE;
        while (true)
        {
            round();
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

    private void round() {
        for (int i = 1; i < COLOR_COUNT-1; ++i)
        {
            gistoArr[i] = (gistoArr[i-1]+gistoArr[i+1])/2;
        }
    }

    public void simplySegmentation(int segments)
    {
        ArrayList<Segment> s = new ArrayList<Segment>();
        int step = COLOR_COUNT/segments;
        int l = 0;
        int r = 0;
        while (r < COLOR_COUNT-1)
        {
            r += step;
            if (r > 255)
                r = 255;
            int m = (l + r)/2;
            s.add(new Segment(l, r, m));
            l += step;
        }
        this.applySegmentation(s);
    }

    public void roundNAndSegment(int times)
    {
        restoreImage();
        toGrayscale();
        buildGisto();

        for (int i = 0; i < times; ++i)
            round();

        ArrayList<Segment> list =  makeSegments();
        applySegmentation(list);
    }

    public void roundTillNSegments(int segments) {
        restoreImage();
        toGrayscale();
        buildGisto();

        ArrayList<Segment> list = null;

        do
        {
            round();
            list = makeSegments();
        }while (list.size() > segments);

        applySegmentation(list);
    }


    private ArrayList<Segment> makeSegments()
    {
        ArrayList<Segment> list = new ArrayList<Segment>();
        Segment s = new Segment();
        s.l = gistoArr[0];
        for (int i = 1; i < COLOR_COUNT; ++i)
        {
            if (gistoArr[i] < gistoArr[i-1] && gistoArr[i] < gistoArr[i+1])
                s.b = i;
            if (gistoArr[i] > gistoArr[i-1] && gistoArr[i] > gistoArr[i+1])
            {
                s.r = i;
                list.add(s);
                s = new Segment();
                s.l = i;
            }
        }
        return list;
    }

    private void applySegmentation(ArrayList<Segment> list)
    {
        int [] arr = new int[COLOR_COUNT];
        for (Segment s: list)
        {
            for (int i = s.l; i < s.r; ++i)
            {
                arr[i] = s.b;
            }
        }
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight(); ++j)
            {
                Color c = new Color(image.getRGB(i, j));
                int gray = c.getRed();
                gray = arr[gray];
                image.setRGB(i, j, new Color(gray, gray, gray).getRGB());
            }
        }
        ip.repaint();
    }

    public void getBinaryContour()
    {
        findBorder();
        int [][]arr = new int[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 1; j < image.getHeight(); ++j)
            {
                Color c1 = new Color(image.getRGB(i, j));
                Color c2 = new Color(image.getRGB(i, j-1));
                int black = c1.getRed();
                if (c1.getRed() != c2.getRed()){
                    if (black == COLOR_COUNT - 1){
                        arr[i][j] += 1;
                    }
                    else
                    {
                        arr[i][j-1] += 1;
                    }
                }
            }
        }
        for (int j = 0; j < image.getHeight(); ++j)
        {
            for (int i = 1; i < image.getWidth(); ++i)
            {
                Color c1 = new Color(image.getRGB(i, j));
                Color c2 = new Color(image.getRGB(i-1, j));
                int black = c1.getRed();
                if (c1.getRed() != c2.getRed()){
                    if (black == COLOR_COUNT - 1){
                        arr[i][j] += 1;
                    }
                    else
                    {
                        arr[i-1][j] += 1;
                    }
                }
            }
        }
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight(); ++j)
            {
                int c = 255;
                if (arr[i][j] > 0)
                    c = 0;
                image.setRGB(i, j, new Color(c, c, c).getRGB());
            }
        }
        ip.repaint();
    }

    public void getGrayContour(int step, int deepLevel) {
        restoreImage();
        toGrayscale();

        int [][]arr = new int[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight() - step; ++j)
            {
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                for (int k = j; k < j + step; ++k)
                {
                    Color c1 = new Color(image.getRGB(i, k));
                    if (c1.getRed() < min)
                        min = c1.getRed();
                    if (c1.getRed() > max)
                        max = c1.getRed();
                }
                if (max - min > deepLevel)
                {
                    arr[i][j + step/2] += 1;
                }
            }
        }
        for (int j = 0; j < image.getHeight(); ++j)
        {
            for (int i = 0; i < image.getWidth() - step; ++i)
            {
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                for (int k = i; k < i + step; ++k)
                {
                    Color c1 = new Color(image.getRGB(k, j));
                    if (c1.getRed() < min)
                        min = c1.getRed();
                    if (c1.getRed() > max)
                        max = c1.getRed();
                }
                if (max - min > deepLevel)
                {
                    arr[i + step/2][j] += 1;
                }
            }
        }
        for (int i = 0; i < image.getWidth(); ++i)
        {
            for (int j = 0; j < image.getHeight(); ++j)
            {
                int c = new Color(image.getRGB(i, j)).getRed();
                if (arr[i][j] == 0)
                    c = 255;
                image.setRGB(i, j, new Color(c, c, c).getRGB());
            }
        }
        ip.repaint();
    }
}


