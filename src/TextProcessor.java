import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: baydet
 * Date: 22.5.13
 * Time: 9.51
* To change this template use File | Settings | File Templates.
        */
public class TextProcessor {


    public static final int H_STEP = 1;
    public static final int W_STEP = 3;
    public static final double EMPTY_EPS = 0.0001;
    public static final double LET_EPS = 0.85;
    private final int letHeight;
    private final int letWidth;
    private final int heightCount;
    private final int widthCount;
    private final ArrayList<Letter> letList;
    private BufferedImage symbImage;
    private StringBuilder returnText;
    private BufferedImage image;
    private int imw;
    private int imh;


    class Letter
    {
        private String value;
        private Point pos;


        public Letter(String s, Point point) {
            this.value = s;
            this.pos = point;
        }
    }

    public TextProcessor(ImagePanel imagePanel)  {
        String [] symSet =  {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "0","1","2","3","4","5","6","7","8","9",",","?","!"," ",":",";","'","[","]","{","}","(",")","-","_", " "};
        this.letList = new ArrayList<Letter>();
        this.letWidth = 16;
        this.letHeight = 27;
        this.heightCount = 9;
        this.widthCount = 11;
        int index = 0;
        int x = 0;
        int y = 0;
        for (int j = 0; j < this.heightCount; j++) {
            for (int i = 0; i < this.widthCount; i++) {
                if (index >= symSet.length)
                    break;
                this.letList.add(new Letter(symSet[index], new Point(x, y)));
                x += this.letWidth;
                index++;
            }
            x = 0;
            y += this.letHeight;
        }

        try {
            this.symbImage = ImageIO.read(new File("symbols.png"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String recognize(BufferedImage image)
    {
        this.image = image;
        this.imw = image.getWidth();
        this.imh = image.getHeight();
        int i;
        this.returnText = new StringBuilder("");

        for (i = 0; i < imh; i += letHeight)
        {
//            isEmptyLine(i);
            if (isEmptyLine(i))
            {
                returnText.append("\n");
            }
            else
            {
                for (int j = 0; j < imw; j += letWidth) {
                    this.returnText.append(analyzeLetter(j, i));
//                    System.out.println(returnText);
                }
                returnText.append("\n");

            }
            System.out.println(returnText);
        }

        System.out.println("done");
        return returnText.toString();
    }

    private String analyzeLetter(int x0, int y0) {
        int k = x0;
        int l;
        int percentCounter = 0;
        int blackCounter = 0;
        float pmax = (float) 0.0;
        Letter candLet = null;
        for (Letter let: letList)
        {
            percentCounter = 0;
            blackCounter = 0;
            for (int i = let.pos.x; i < let.pos.x + letWidth; ++i)
            {
                if (i >= symbImage.getWidth() || k >= imw)
                    break;
                l = y0;
                for (int j = let.pos.y; j < let.pos.y + letHeight; j++)
                {
                    if (j >= symbImage.getHeight() || l >= imh)
                        break;
                    Color c = new Color(image.getRGB(k, l));
                    int gray = c.getRed();
//                    if (gray == 255)
//                        gray = this.hasBlackAround(k, l);

                    Color c2 = new Color(symbImage.getRGB(i, j));
                    int gray2 = c2.getRed();

                    if (gray2 == 0)
                        blackCounter++;

                    if (gray == gray2)
                        ++percentCounter;
                    else
                    {
                        gray = this.hasBlackAround(k, l);
                        if (gray == gray2)
                            ++percentCounter;
                    }
                    ++l;
                }
                ++k;
            }
            k = x0;

            float p = (float)percentCounter/((float)letHeight * letWidth);
            if (p > pmax)
            {
                candLet = let;
                pmax = p;
            }
//            if (p > LET_EPS)
//            {
//            if (!let.value.equals("_"))
//                System.out.println(let.value + " - " + p + ", " + let.pos.x + ", " + let.pos.y);
//                return let.value;
//            }
        }
        if (candLet != null)
        {
//            if (candLet.value.equals(";"))
//                System.out.println("wowowow");
            if (pmax > 0.5)
                return candLet.value;
        }
        return "";
    }

    private int hasBlackAround(int k, int l) {
        int res = 255;
        res *= this.getColor(k-1, l-1);
        res *= this.getColor(k-1, l);
        res *= this.getColor(k-1, l+1);
        res *= this.getColor(k+1, l-1);
        res *= this.getColor(k+1, l);
        res *= this.getColor(k+1, l+1);
        if (res > 0)
            return 255;
        else
            return 0;
    }

    private int getColor(int i, int j) {
        if (i < imw && j < imh && i > 0 && j > 0)
        {
            Color c = new Color(image.getRGB(i, j));
            if (c.getRed() == 0) {
                return 0;
            }
        }
        return 255;
    }


    private boolean isEmptyLine(int index) {
        int blackCounter = 0;
        for (int i = index; i < index + letHeight; i += H_STEP) {
            if (i >= imh)
                break;
            for (int j = 0; j < this.imw; j += W_STEP) {
                if (j >= imw)
                    break;
                try
                {
                    Color c = new Color(image.getRGB(j, i));
                    int gray = c.getRed();
                    if (gray == 0)
                        blackCounter++;
                }
                catch (Exception e)
                {
                    System.out.println("error in empty line");
                    return false;
                }
            }
        }
        int totalCount = letHeight * imw;
        System.out.println((float)blackCounter);
        if (blackCounter == 0)
//        if ((float)blackCounter/(float)totalCount < EMPTY_EPS)
            return true;
        else
            return false;
    }
}
