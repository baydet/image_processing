import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: baydet
 * Date: 5/1/13
 * Time: 1:36 PM
 * To change this template use File | Settings | File Templates.
 */


public class MainWnd extends JFrame implements ActionListener {
    private final ArrayList<JInternalFrame> internalFrames;
    private int internalFrameCounter;
    private JDesktopPane desktop;
    private JMenu menu;
    private JMenuBar menubar;
    private InternalFrame curFrame;
    private String path;

    public MainWnd()
    {
        this.internalFrameCounter = 0;
        this.setSize(500, 500);
        this.createDesktop();
        this.createMenus();
        this.internalFrames = new ArrayList<JInternalFrame>();
        try {
            this.createInternalWnd();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    private void createMenus()
    {
        menu = new JMenu("File");
        JMenuItem newWnd = new JMenuItem("New Window");
        newWnd.addActionListener(this);
        JMenuItem openIm = new JMenuItem("Open Image");
        openIm.addActionListener(this);

        curFrame = null;

        menu.add(newWnd);
        menu.add(openIm);

        menubar = new JMenuBar();
        menubar.add(menu);

        this.setJMenuBar(menubar);
    }

    private void createDesktop()
    {
        this.desktop = new JDesktopPane();
        this.getContentPane().add(new JScrollPane(this.desktop));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JMenuItem item = (JMenuItem)actionEvent.getSource();
        if (item.getText().equals("New Window")) {
            try {
                MainWnd.this.createInternalWnd();
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        else if(item.getText() == "Open Image")
        {
            this.openFileDialog();
        }
    }

    private void openFileDialog() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            path = fc.getSelectedFile().getAbsolutePath();
            if (curFrame != null)
            {
                curFrame.setImage(path);
            }

        }
    }

    private void createInternalWnd() throws PropertyVetoException {
        this.internalFrameCounter += 1;

        InternalFrame iframe = new InternalFrame("new wnd");
        iframe.setSize(new Dimension(500, 500));
        iframe.setVisible(true);
        iframe.setEnabled(true);
        iframe.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                MainWnd.this.internalFrames.remove(e.getInternalFrame());
            }
        });
        this.internalFrames.add(iframe);

        this.desktop.add(iframe);
        iframe.setSelected(true);
        iframe.moveToFront();
        curFrame = iframe;
    }

    public static void main(String [] args)
    {
        MainWnd wnd = new MainWnd();
        wnd.setLocation(30, 30);
        wnd.setVisible(true);
        wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
