import java.awt.*;
import java.awt.image.BufferStrategy;

class Main extends Canvas implements Runnable {
  private Thread thread;
  private boolean running = false;

  public static Window window;
  public static Main main;
  
  public static int[][] board;
  
  private int selX = 0;
  private int selY = 0;

  public Main() {
    System.out.println("Initializing...");
    window = new Window(1920, 1080, "Checkers", this);

    main = this;
    
    board = createBoard();

    window.startWindow();
  }

  public void run() {
    this.requestFocus();
    long lastTime = (long) System.nanoTime();
    double ticks = 30.0;
    double ns = 1000000000 / ticks;
    double delta = 0;
    long timer = System.currentTimeMillis();
    while(running) {
      long now = System.nanoTime();
      delta += (now - lastTime) / ns;
      lastTime = now;
      while(delta >= 1) {
          delta--;
          render();
      }
      
      if(System.currentTimeMillis() - timer > 1000) {
        timer += 1000;
      }
    }
    stop();
  }

  private void render() {
    BufferStrategy bs = this.getBufferStrategy();
    if(bs == null) {
      this.createBufferStrategy(3);
      return;
    }

    Graphics g = bs.getDrawGraphics();

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, 1920, 1080);
    renderBoard(g);

    g.dispose();
    bs.show();
  }
  
  private void renderBoard(Graphics g) {
	  g.setColor(new Color(60, 60, 58));
	  g.fillRect(440, 0, 1040, 1040);
	  
	  for(int i = 0; i < 8; i++) {
		  for(int j = 0; j < 8; j++) {
			  if((i + j) % 2 == 0) {
				  g.setColor(Color.white);
				  g.fillRect(440 + j * 130, i * 130 , 130, 130);
			  }
		  }
	  }
	  if(selX != -1 && selY != -1) {
		  g.setColor(Color.red);
		  g.fillRect(440 + selX * 130, selY * 130, 130, 130);
	  }
	  for(int i = 0; i < 8; i++) {
		  for(int j = 0; j < 8; j++) {
			  switch(board[i][j]) {
			  case 1:
				  g.setColor(new Color(160, 30, 30));
				  g.fillOval(450 + j * 130, 10 + i * 130, 110, 110);
				  break;
			  case 2:
				  g.setColor(Color.black);
				  g.fillOval(450 +  j * 130, 10 + i * 130, 110, 110);
				  break;
			  }
		  }
	  }
  }
  
  public static void selectTile() {
	  
  }
  
  public synchronized void start() {
    thread = new Thread(this);
    thread.start();
    running = true;
  }
  public synchronized void stop() {
    try {
      thread.join();
      running = false;
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args) {
    new Main();
  }

  public int[][] createBoard(){
    int[][] board = new int[8][8];
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[i].length; j++){
        if( i == 0 || i == 1 || i == 2)
          if((i+j) % 2 == 0)
            board[i][j] = 1; //red
          else
            board[i][j] = 0; //blank
        if(i == 5 || i == 6 || i==7)
          if((i + j) % 2 == 0)
            board[i][j] = 2; // black
          else
            board[i][j] = 0; // blank
      }
    }
    
    return board;
  }
  
  public static String viewBoardString(int[][] board){
    //0 = blank
    //1 = red
    //2 = black
    //3 = red king
    //4 = black king
    //Assume board is a square.
    String[][] boardString = new String[board.length][];
    for(int i = 0; i < board.length; i++){
      for(int j = 0; j < board[i].length; j++){
        if(board[i][j] == 0){
          boardString[i][j] = "_";
        }
        if(board[i][j] == 1){
          boardString[i][j] = "R";
        }
        if(board[i][j] == 2){
          boardString[i][j] = "B";
        }
      }
    }
    return "";
  }
}