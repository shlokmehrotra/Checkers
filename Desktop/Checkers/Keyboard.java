import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Keyboard implements KeyListener {
  public void keyPressed(KeyEvent e) {
    int key = e.getKeyCode();
    switch(key) {
      case KeyEvent.VK_ESCAPE:
        Main.clearSelected();
        break;
    }
  }
  public void keyReleased(KeyEvent e) {
    int key = e.getKeyCode();
  }
  public void keyTyped(KeyEvent e) {
    int key = e.getKeyCode();
  }
}