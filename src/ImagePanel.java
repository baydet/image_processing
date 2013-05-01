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
}
