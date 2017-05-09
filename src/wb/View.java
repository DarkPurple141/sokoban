package wb;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class View {
    private JFrame mainFrame;

    public View() {
        mainFrame = new JFrame("WarehouseBoss");
        mainFrame.setSize(400,400);
        mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }
      });
        mainFrame.addKeyListener(new WBListener());
        mainFrame.setVisible(true);
    }

    public JFrame getFrame() {
        return this.mainFrame;
    }

    public void render() {
        
    }

}
