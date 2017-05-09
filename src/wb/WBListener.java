package wb;

import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WBListener extends KeyAdapter {

    private Controller c;
    public WBListener (Controller n) {
        c = n;
    }

    public void update(KeyEvent e) {
        c.processEvent(e);
    }

    /*
    @Override
    public void keyTyped(KeyEvent e) {
        c.processEvent(e);
        System.out.println(e.getKeyCode());
    }
    */

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        this.update(e);
    }
    /*
    @Override
    public void keyReleased(KeyEvent e) {
        c.processEvent(e);
    }
    */


}
