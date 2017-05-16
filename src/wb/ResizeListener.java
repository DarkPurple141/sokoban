package wb;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ResizeListener extends ComponentAdapter {
	
	private Controller parent;
	
	public ResizeListener(Controller c) {
		this.parent = c;	
	}
	
	public void componentResized(ComponentEvent evt) {
        Component c = (Component)evt.getSource();
        if (c == parent) {
        	parent.resizeView();
        }
	}
}
