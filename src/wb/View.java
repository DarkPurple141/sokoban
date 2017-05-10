package wb;
import javax.swing.JFrame;
import java.awt.Color;

public class View {
    private JFrame mainFrame;
    //private Board b;
    private static int SCREEN_WIDTH = 512;
    private static int SCREEN_HEIGHT = 512;

    public View(WBListener w, Board b) {
        //this.b = b;
        mainFrame = new JFrame("WarehouseBoss");
        mainFrame.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        mainFrame.setBackground(Color.BLACK);
        mainFrame.getContentPane().add(new WBPanel(b));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addKeyListener(w);
        //mainFrame.setLayout(new GridLayout(rows, cols));
        //mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public JFrame getFrame() {
        return this.mainFrame;
    }

    public void paintTiles() {
        mainFrame.validate();
        mainFrame.repaint();
    }

}
