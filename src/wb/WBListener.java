package wb;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class WBListener extends KeyAdapter {

    private Controller c;
    public WBListener (Controller c) {
        this.c = c;
    }

    public void update(KeyEvent e) {
        c.processEvent(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.update(e);
    }

}
