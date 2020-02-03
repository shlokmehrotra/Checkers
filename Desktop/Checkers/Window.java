import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas {
  private static JFrame frame;
  private Main main;

  public Window(int width, int height, String name, Main main) {
    this.main = main;

    frame = new JFrame(name);

    frame.setPreferredSize(new Dimension(width, height));
    frame.setMinimumSize(new Dimension(width, height));
    frame.setMaximumSize(new Dimension(width, height));

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(true);
    frame.setLocationRelativeTo(null);
    frame.add(main);
    frame.setVisible(false);
  }

  public JFrame getFrame() {
    return frame;
  }

  public void startWindow() {
    System.out.println("Starting checkers... ");
    frame.setVisible(true);
    main.start();
  }
}