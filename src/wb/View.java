package wb;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Color;

public class View {
    private JFrame mainFrame;
    private int rows;
    private int cols;
    private Board b;
    private static int SCREEN_WIDTH = 512;
    private static int SCREEN_HEIGHT = 512;

    public View(WBListener w, int rows, int cols, Board b) {
        this.rows = rows;
        this.cols = cols;
        this.b = b;
        mainFrame = new JFrame("WarehouseBoss");
        mainFrame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        mainFrame.setBackground(Color.BLACK);
        mainFrame.getContentPane().add(new wbPanel(SCREEN_WIDTH,SCREEN_HEIGHT,rows,cols,this));
        mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }
      });
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addKeyListener(w);
        //mainFrame.setLayout(new GridLayout(rows, cols));
        //mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public JFrame getFrame() {
        return this.mainFrame;
    }

    public Board getBoard() {
        return b;
    }

    public void paintTiles() {
        mainFrame.validate();
        mainFrame.repaint();
    }

}
