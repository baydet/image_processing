import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: baydet
 * Date: 5/1/13
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class InternalFrame extends JInternalFrame implements ActionListener {

    public static final String TO_GRAYSCALE = "to grayscale";
    public static final String RESTORE = "restore";
    public static final String INVERT = "invert";
    public static final String GISTOGRAMM = "gistogramm";
    private ImagePanel sp;
    private final JMenu menu;

    public void addMenu(String name)
    {
        JMenuItem invert = new JMenuItem(name);
        invert.addActionListener(this);
        menu.add(invert);
    }

    public InternalFrame(String s) {
        super(s);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setResizable(true);

        menu = new JMenu("Actions");

        addMenu(TO_GRAYSCALE);
        addMenu(INVERT);
        addMenu(GISTOGRAMM);
        addMenu(RESTORE);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        this.setJMenuBar(menuBar);

        sp = new ImagePanel();
        this.add(sp);

        this.setImage("/Users/astrokin/Desktop/Lenna.png");
    }

    public void setImage(String path)
    {
        sp.setImage(path);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JMenuItem item = (JMenuItem)actionEvent.getSource();
        if (item.getText().equals(TO_GRAYSCALE))
            InternalFrame.this.sp.toGrayscale();
        else if(item.getText().equals(RESTORE))
            InternalFrame.this.sp.restore();
        else if(item.getText().equals(INVERT))
            InternalFrame.this.sp.imageProcessor.invert();
        else if(item.getText().equals(GISTOGRAMM))
            InternalFrame.this.sp.imageProcessor.getGistogramm();
    }
}
