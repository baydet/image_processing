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
    public static final String MEDIAN = "median";
    public static final String MIDDLE = "middle";

    public ImageProcessor imageProcessor;
    public TextProcessor textProcessor;

    public ImagePanel() throws IOException {
        super();
        imageProcessor = new ImageProcessor(this);
        textProcessor = new TextProcessor(this);
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
            else if(type.equals(ROUND_N_TIMES))
                imageProcessor.roundNAndSegment(r);
            else if(type.equals(ROUND_TO_N_SEGM))
                imageProcessor.roundTillNSegments(r);
        }
        catch (Exception ignored)
        {
        }
    }

    public void getGrayContour()
    {
        JPanel panel=new JPanel();
        panel.setLayout(new GridLayout(4,1));
        JLabel username=new JLabel("step");
        JLabel password=new JLabel("deep level");
        JTextField textField=new JTextField(12);
        JTextField passwordField=new JTextField(12);
        panel.add(username);
        panel.add(textField);
        panel.add(password);
        panel.add(passwordField);
        int a=JOptionPane.showConfirmDialog(this, panel,"Put username and password",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);

        if(a==JOptionPane.OK_OPTION)
        {
            try{
                int step = Integer.parseInt(textField.getText());
                int deepLevel = Integer.parseInt(passwordField.getText());
                imageProcessor.getGrayContour(step, deepLevel);
            }
            catch (Exception ignored)
            {
            }
        }


        else if(a==JOptionPane.CANCEL_OPTION)
        {
            return;
        }
    }

    public void filterImage(String type)
    {
        Object result = JOptionPane.showInputDialog(this, "enter grid size");
        try{
            int r = Integer.parseInt((String) result);
            if (r > 255 || r < 0)
                return;
            if(type.equals(MEDIAN))
                imageProcessor.simplySegmentation(r);
            else if(type.equals(MIDDLE)){}
        }
        catch (Exception ignored)
        {
        }
    }

    public void recognize() {
        this.imageProcessor.restoreImage();
        this.textProcessor.recognize(this.imageProcessor.getImage());
    }
}
