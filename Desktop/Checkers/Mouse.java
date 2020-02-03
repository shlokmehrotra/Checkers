import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {
	private int mx;
	private int my;
	private int mb;
	
	public void mouseClicked(MouseEvent arg0) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		mb = e.getButton();
		
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}
}