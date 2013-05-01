import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

/**
 * Created with IntelliJ IDEA.
 * User: baydet
 * Date: 5/1/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class GistoDialog extends JDialog
{

    private static final int PAD = 20;
    private final JPanel panel;
    private int[] data;

    public GistoDialog(int [] arr) {
        super();
        data = arr;
        setSize(300, 300);
        setVisible(true);

        panel = new JPanel();
        this.add(panel, BorderLayout.CENTER);
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);    //To change body of overridden methods use File | Settings | File Templates.
        Graphics2D g2 = (Graphics2D)panel.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD*3, w-PAD, h-PAD*3));
        double xInc = (double)(w - 2*PAD)/(data.length-1);
        double scale = (double)(h - 2*PAD)/getMax();
        // Mark data points.
        g2.setPaint(Color.DARK_GRAY);
        for(int i = 0; i < data.length; i++) {
            double x = PAD + i*xInc;
            double y = h - PAD * 3 - scale*data[i];
            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
            g2.draw(new Line2D.Double(x , h-PAD*3, x , y + 3));
        }
    }


    private int getMax() {
        int max = -Integer.MAX_VALUE;
        for(int i = 0; i < data.length; i++) {
            if(data[i] > max)
                max = data[i];
        }
        return max;
    }
}
