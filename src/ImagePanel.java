import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: baydet
 * Date: 5/1/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePanel extends JPanel
{
    public static final String SIMPLY_SEGM = "to grayscale";
    public static final String ROUND_N_TIMES = "restore";
    public static final String ROUND_TO_N_SEGM = "invert";

    public ImageProcessor imageProcessor;

    public ImagePanel() {
        super();
        imageProcessor = new ImageProcessor(this);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(imageProcessor.getImage(), 0, 0, null);
    }

    public void setImage(String path) {
        try {
            this.imageProcessor.setImage(ImageIO.read(new File(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.repaint();
    }

    public ImageProcessor getImageProcessor() {
        return imageProcessor;
    }

    public void toGrayscale() {
        imageProcessor.toGrayscale();
        this.repaint();
    }

    public void restore() {
        imageProcessor.restoreImage();
    }

    public void manualBinarisation() {
        Object result = JOptionPane.showInputDialog(this, "enter binarisation bound:");
        try{
            int r = Integer.parseInt((String) result);
            if (r > 255 || r < 0)
                return;
            imageProcessor.toBinary(r);
        }
        catch (Exception ignored){}

    }

    public void makeSegmentation(String type) {
        String request = "";
        if(type.equals(SIMPLY_SEGM))
            request = "Enter simply segments count";
        else if(type.equals(ROUND_N_TIMES))
            request = "Enter rounding count";
        else if(type.equals(ROUND_TO_N_SEGM))
            request = "Enter smart segments count";
        Object result = JOptionPane.showInputDialog(this, request);
        try{
            int r = Integer.parseInt((String) result);
            if (r > 255 || r < 0)
                return;
            if(type.equals(SIMPLY_SEGM))
                imageProcessor.simplySegmentation(r);
            else if(type.equals(ROUND_N_TIMES)){}
//                request = "Enter rounding count";
            else if(type.equals(ROUND_TO_N_SEGM))    {}
//                request = "Enter smart segments count";
        }
        catch (Exception ignored)
        {
        }
    }
}
