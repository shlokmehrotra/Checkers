import java.awt.*;
import java.awt.image.BufferStrategy;

class Main extends Canvas implements Runnable {
  private Thread thread;
  private boolean running = false;

  public static Window window;
  public static Main main;
  
  public static int[][] board;
  
  private Mouse mouse;
  private Keyboard keyboard;
  
  private static int selX = -1;
  private static int selY = -1;
  private static boolean piece = false;
  private static int[][] moves;

  public Main() {
    System.out.println("Initializing...");
    window = new Window(1920, 1080, "Checkers", this);

    main = this;
    mouse = new Mouse();
    keyboard = new Keyboard();
    
    this.addMouseListener(mouse);
    this.addKeyListener(keyboard);
    
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
      g.setColor(new Color(250, 60, 60));
      g.fillRect(440 + selX * 130, selY * 130, 130, 130);
    }
    if(moves != null) {
      for(int i = 0; i < moves.length; i++) {
        if(moves[i][0] >= 0 && moves[i][0] < 8 && moves[i][1] >= 0 && moves[i][1] < 8) {
          g.fillRect(440 + moves[i][0] * 130, moves[i][1] * 130, 130, 130);
        }
      }
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
  
  public static void selectTile(int mx, int my) {
    /*
     * moves[0][0]: X of left possible move
     * moves[0][1]: Y of left possible move
     * moves[1][0]: X of right possible move
     * moves[1][1]: Y of right possible move
     */
    if(!piece) {
      selX = (mx - 440) / 130;
      selY = my / 130;
      if(selX >= 8 || selX < 0) clearSelected();
      if(selY >= 8 || selY < 0) clearSelected();
      
      if(board[selY][selX] != 0) {
        piece = true;
        int p = board[selY][selX];
        if(p == 1 || p == 2) {
          moves = new int[2][2];
          moves[0][0] = selX - 1;
          moves[1][0] = selX + 1;
          
          if(p == 1) {
            moves[0][1] = selY + 1;
            moves[1][1] = selY + 1;
          } else {
            moves[0][1] = selY - 1;
            moves[1][1] = selY - 1;
          }
          
          
          if(moves[0][0] >= 0 && moves[0][1] >= 0 && moves[0][0] < 8 && moves[0][1] < 8) {
            if(board[moves[0][1]][moves[0][0]] != 0) {
              moves[0][0] = -1;
            }
          }
          
          if(moves[1][0] >= 0 && moves[1][1] >= 0 && moves[1][0] < 8 && moves[1][1] < 8) {
            if(board[moves[1][1]][moves[1][0]] != 0) {
              moves[1][0] = -1;
            }
          }
        }
      }
    } else {
      int x = (mx - 440) / 130;
      int y = my / 130;
      
      if(moves[0][1] == y && (moves[0][0] == x || moves[1][0] == x)) {
        board[y][x] = board[selY][selX];
        board[selY][selX] = 0;
        clearSelected();
      }
    }
  }
  public static void clearSelected() {
    selX = -1;
    selY = -1;
    moves = null;
    piece = false;
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