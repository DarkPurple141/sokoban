package wb;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @brief ??? kill me
 *
 * @author Alex Hinds {@literal <z3420752@cse.unsw.edu.au>}
 * @author Ben Lichtman {@literal <z5059760@cse.unsw.edu.au>}
 * @date May 2017
 */
public class WBListener
extends KeyAdapter {

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
