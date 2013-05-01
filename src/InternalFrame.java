import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    public static final String MANUAL = "manual";
    public static final String GLOBAL_AUTO = "global auto";
    public static final String LOCAL_AUTO = "local auto";
    public static final String SIMPLY_SEGM = "simply";
    public static final String ROUND_N_TIMES = "round N times";
    public static final String ROUND_TO_N_SEGM = "round to N segments";
    private final JMenu secondMenu;
    private ImagePanel sp;
    private final JMenu firstMenu;
    private JMenu thirdMenu;

    public void addToFirstMenu(String name)
    {
        JMenuItem invert = new JMenuItem(name);
        invert.addActionListener(this);
        firstMenu.add(invert);
    }

    public void addToSecondMenu(String name)
    {
        JMenuItem invert = new JMenuItem(name);
        invert.addActionListener(this);
        secondMenu.add(invert);
    }

    public void addToThirdMenu(String name)
    {
        JMenuItem invert = new JMenuItem(name);
        invert.addActionListener(this);
        thirdMenu.add(invert);
    }

    public InternalFrame(String s) {
        super(s);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setResizable(true);

        firstMenu = new JMenu("Actions");
        secondMenu = new JMenu("Binarisation");
        thirdMenu = new JMenu("Segmentation");

        addToFirstMenu(TO_GRAYSCALE);
        addToFirstMenu(INVERT);
        addToFirstMenu(GISTOGRAMM);
        addToFirstMenu(RESTORE);

        addToSecondMenu(MANUAL);
        addToSecondMenu(GLOBAL_AUTO);
        addToSecondMenu(LOCAL_AUTO);

        addToThirdMenu(SIMPLY_SEGM);
        addToThirdMenu(ROUND_N_TIMES);
        addToThirdMenu(ROUND_TO_N_SEGM);



        JMenuBar menuBar = new JMenuBar();
        menuBar.add(firstMenu);
        menuBar.add(secondMenu);
        menuBar.add(thirdMenu);

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
        else if(item.getText().equals(MANUAL))
            InternalFrame.this.sp.manualBinarisation();
        else if(item.getText().equals(GLOBAL_AUTO))
            InternalFrame.this.sp.imageProcessor.findBorder();
        else if(item.getText().equals(MANUAL))    {}
        else if(item.getText().equals(SIMPLY_SEGM))
            InternalFrame.this.sp.makeSegmentation(ImagePanel.SIMPLY_SEGM);
        else if(item.getText().equals(ROUND_N_TIMES))
            InternalFrame.this.sp.makeSegmentation(ImagePanel.ROUND_N_TIMES);
        else if(item.getText().equals(ROUND_TO_N_SEGM))
            InternalFrame.this.sp.makeSegmentation(ImagePanel.ROUND_TO_N_SEGM);
    }
}
