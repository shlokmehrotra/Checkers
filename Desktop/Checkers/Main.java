import java.awt.*;
import java.awt.image.BufferStrategy;

class Main extends Canvas implements Runnable {
  private Thread thread;
  private static boolean running = false;
  private static boolean over = false;

  public static Window window;
  public static Main main;
  
  public static int[][] board;
  
  private Mouse mouse;
  private Keyboard keyboard;
  
  private static int selX = -1;
  private static int selY = -1;
  private static boolean piece = false;
  private static int[][] moves;
  //Turning indicates which player is allowed to move. (0, 1)
  private static int turn = 0;

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
    //Init board
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
    //adds pieces.
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
          //Red piece
          g.setColor(new Color(160, 30, 30));
          g.fillOval(450 + j * 130, 10 + i * 130, 110, 110);
          break;
        case 2:
          //Black Piece
          g.setColor(Color.black);
          g.fillOval(450 +  j * 130, 10 + i * 130, 110, 110);
          break;
        case 3:
          //Red King
          g.setColor(new Color(160, 30, 30));
          g.fillOval(450 + j * 130, 10 + i * 130, 110, 110);
          g.setColor(Color.white);
          g.setFont(new Font("TimesRoman", Font.PLAIN, 40)); 
          g.drawString("K", 495 + j * 130, 75 + i * 130);
          break;
        case 4: 
          g.setColor(Color.black);
          g.fillOval(450 +  j * 130, 10 + i * 130, 110, 110);
          g.setColor(Color.white);
          g.setFont(new Font("TimesRoman", Font.PLAIN, 40)); 
          g.drawString("K", 495 + j * 130, 75 + i * 130);
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
    //turn 0 = black turn, 1 = red turn
    if(!over) {
      if(!piece && board[(my / 130)][(mx - 440) / 130] % 2 == turn) {
        selX = (mx - 440) / 130;
        selY = my / 130;
        if(selX >= 8 || selX < 0) clearSelected();
        if(selY >= 8 || selY < 0) clearSelected();
        
        if(board[selY][selX] != 0) {
          piece = true;
          int p = board[selY][selX];
          moves = new int[4][2];
          
          if(isLegalMove(new Move(selY, selX, selY + 1, selX + 1), turn)) {
            moves[0][0] = selX + 1;
            moves[0][1] = selY + 1;
          } else if(isLegalJump(new Move(selY, selX, selY + 2, selX + 2), turn)) {
            moves[0][0] = selX + 2;
            moves[0][1] = selY + 2;
          } else {
            moves[0][0] = -1;
            moves[0][1] = -1;
          }
          if(isLegalMove(new Move(selY, selX, selY - 1, selX + 1), turn)) {
            moves[1][0] = selX + 1;
            moves[1][1] = selY - 1;
          } else if(isLegalJump(new Move(selY, selX, selY - 2, selX + 2), turn)) {
            moves[1][0] = selX + 2;
            moves[1][1] = selY - 2;
          } else {
            moves[1][0] = -1;
            moves[1][1] = -1;
          }
          if(isLegalMove(new Move(selY, selX, selY + 1, selX - 1), turn)) {
            moves[2][0] = selX - 1;
            moves[2][1] = selY + 1;
          } else if(isLegalJump(new Move(selY, selX, selY + 2, selX - 2), turn)) {
            moves[2][0] = selX - 2;
            moves[2][1] = selY + 2;
          } else {
            moves[2][0] = -1;
            moves[2][1] = -1;
          }
          if(isLegalMove(new Move(selY, selX, selY - 1, selX - 1), turn)) {
            moves[3][0] = selX - 1;
            moves[3][1] = selY - 1;
          } else if(isLegalJump(new Move(selY, selX, selY - 2, selX - 2), turn)) {
            moves[3][0] = selX - 2;
            moves[3][1] = selY - 2;
          } else {
            moves[3][0] = -1;
            moves[3][1] = -1;
          }
            /*moves[0][0] = selX - 1;
            moves[1][0] = selX + 1;
            
            if(p == 1) {
              moves[0][1] = selY + 1;
              moves[1][1] = selY + 1;
            } else {
              moves[0][1] = selY - 1;
              moves[1][1] = selY - 1;
            }*/
            
            
            /*if(moves[0][0] >= 0 && moves[0][1] >= 0 && moves[0][0] < 8 && moves[0][1] < 8) {
              if(board[moves[0][1]][moves[0][0]] != 0) {
                moves[0][0] = -1;
              }
            }
            
            if(moves[1][0] >= 0 && moves[1][1] >= 0 && moves[1][0] < 8 && moves[1][1] < 8) {
              if(board[moves[1][1]][moves[1][0]] != 0) {
                moves[1][0] = -1;
              }
            }*/
        }
      } else if (moves != null) {
        int x = (mx - 440) / 130;
        int y = my / 130;
        
        boolean legal = false;
        
        for(int i = 0; i < 4; i++) {
          if(moves[i][0] == x && moves[i][1] == y)
            legal = true;
        }
        
        if(legal) {
          Move move = new Move(selY, selX, y, x);
          if(x == selX + 1 || x == selX - 1)
            doMove(move);
          else
            doJump(move);
          
          if(turn == 0)
            turn = 1;
          else
            turn = 0;
          
          clearSelected();
          
          if(gameOver()) over = true;
        }
      } else {
        selX = (mx - 440) / 130;
        selY = my / 130;
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
  
  public static String viewBoardString(){
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

    public static void doMove(Move move){
        int piece = board[move.startingRow][move.startingColumn];
        board[move.startingRow][move.startingColumn] = 0;
        board[move.endingRow][move.endingColumn] = piece;
        if(move.endingRow == 0 && piece == 2){
            board[move.endingRow][move.endingColumn] = 4;
        }
        else if(move.endingRow == 7 && piece == 1){
            board[move.endingRow][move.endingColumn] = 3;
        }
    }

    public static void doJump(Move move){
        int piece = board[move.startingRow][move.startingColumn];
        int r = move.startingRow;
        int c = move.startingColumn;
        while(Math.abs(move.endingRow - r) != 1 && Math.abs(move.endingColumn - c) != 1){
            if (r < move.endingRow) {
                r++;
            } else {
                r--;
            }
            if (c < move.endingColumn) {
                c++;
            } else {
                c--;
            }
        }
        if(((board[r][c] == 2 || board[r][c] == 4) && (board[move.startingRow][move.startingColumn] == 1 ||
                board[move.startingRow][move.startingColumn] == 3)) || ((board[r][c] == 3 || board[r][c] == 1) &&
                (board[move.startingRow][move.startingColumn] == 2 || board[move.startingRow][move.startingColumn] == 4))){
            board[r][c] = 0;
        }
        board[move.startingRow][move.startingColumn] = 0;
        board[move.endingRow][move.endingColumn] = piece;
        if(move.endingRow == 0 && piece == 2){
            board[move.endingRow][move.endingColumn] = 4;
        }
        else if(move.endingRow == 7 && piece == 1){
            board[move.endingRow][move.endingColumn] = 3;
        }
    }

    public static boolean isLegalJump(Move move, int player){
        if(board[move.startingRow][move.startingColumn] == 2 || board[move.startingRow][move.startingColumn] == 1) {
            if (move.endingRow >= 8 || move.endingRow < 0 || move.startingColumn >= 8 || move.endingColumn < 0) {
                return false;
            }
            if(move.endingColumn >= 8 || move.endingRow >= 8) return false;
            if (board[move.endingRow][move.endingColumn] != 0) {
                return false;
            }
            if (!((move.endingRow == move.startingRow + 2 && move.endingColumn == move.startingColumn + 2) ||
                    (move.endingRow == move.startingRow - 2 && move.endingColumn == move.startingColumn + 2) ||
                    (move.endingRow == move.startingRow + 2 && move.endingColumn == move.startingColumn - 2) ||
                    (move.endingRow == move.startingRow - 2 && move.endingColumn == move.startingColumn - 2))) {
                return false;
            }
            int a = move.startingRow;
            int b = move.startingColumn;
            if (a < move.endingRow) {
                a++;
            } else {
                a--;
            }
            if (b < move.endingColumn) {
                b++;
            } else {
                b--;
            }
            if(board[a][b] == 0){
                return false;
            }
            if (player == 0) {
                if(!(board[move.startingRow][move.startingColumn] == 2)){
                    return false;
                }
                if(move.endingRow > move.startingRow){
                    return false;
                }
                return true;
            }
            else{
                if(!(board[move.startingRow][move.startingColumn] == 1)){
                    return false;
                }
                if(move.endingRow < move.startingRow){
                    return false;
                }
                return true;
            }
        }
        else if(board[move.startingRow][move.startingColumn] == 3 || board[move.startingRow][move.startingColumn] == 4){
            if (move.endingRow >= 8 || move.endingRow < 0 || move.startingColumn >= 8 || move.endingColumn < 0) {
                return false;
            }
            if(move.endingColumn >= 8 || move.endingRow >= 8) return false;
            if (board[move.endingRow][move.endingColumn] != 0) {
                return false;
            }
            if(!(Math.abs(move.startingRow - move.endingRow) == Math.abs(move.startingColumn - move.endingColumn))){
                return false;
            }
            if(player == 0) {
                if (!(board[move.startingRow][move.startingColumn] == 4)) {
                    return false;
                }
            }
            if(player == 1) {
                if (!(board[move.startingRow][move.startingColumn] == 3)) {
                    return false;
                }
            }
            int r = move.startingRow;
            int c = move.startingColumn;
            if (r < move.endingRow) {
                r++;
            } else {
                r--;
            }
            if (c < move.endingColumn) {
                c++;
            } else {
                c--;
            }
            while ((r != move.endingRow) && (c != move.endingColumn)) {
                if (board[r][c] != 0 && Math.abs(move.endingRow - r) != 1 ) {
                    return false;
                }
                if (r < move.endingRow) {
                    r++;
                } else {
                    r--;
                }
                if (c < move.endingColumn) {
                    c++;
                } else {
                    c--;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isLegalMove(Move move, int player){
        if(board[move.startingRow][move.startingColumn] == 2 || board[move.startingRow][move.startingColumn] == 1) {

            // Check if ending tile is out of board
            if (move.endingRow >= 8 || move.endingRow < 0 || move.startingColumn >= 8 || move.endingColumn < 0) {
                return false;
            }
            if(move.endingColumn >= 8 || move.endingRow >= 8) return false;
            // Check  if ending tile is occupied
            if (board[move.endingRow][move.endingColumn] != 0) {
                return false;
            }
            // Check if ending tile is adjacent to starting tile
            if (!((move.endingRow == move.startingRow + 1 && move.endingColumn == move.startingColumn + 1) ||
                    (move.endingRow == move.startingRow - 1 && move.endingColumn == move.startingColumn + 1) ||
                    (move.endingRow == move.startingRow + 1 && move.endingColumn == move.startingColumn - 1) ||
                    (move.endingRow == move.startingRow - 1 && move.endingColumn == move.startingColumn - 1))) {
                return false;
            }
            if (player == 0) {
                if(!(board[move.startingRow][move.startingColumn] == 2)){
                    return false;
                }
                if(move.endingRow > move.startingRow){
                    return false;
                }
                return true;
            }
            else{
                if(!(board[move.startingRow][move.startingColumn] == 1)){
                    return false;
                }
                if(move.endingRow < move.startingRow){
                    return false;
                }
                return true;
            }
        }
        // If the piece is a king
        else if(board[move.startingRow][move.startingColumn] == 3 || board[move.startingRow][move.startingColumn] == 4){
            // Check if ending tile is out of board
            if (move.endingRow >= 8 || move.endingRow < 0 || move.startingColumn >= 8 || move.endingColumn < 0) {
                return false;
            }
            if(move.endingColumn >= 8 || move.endingRow >= 8) return false;
            // Check  if ending tile is occupied
            if (board[move.endingRow][move.endingColumn] != 0) {
                return false;
            }
            // Check if ending tile is on same diagonal
            if(!(Math.abs(move.startingRow - move.endingRow) == Math.abs(move.startingColumn - move.endingColumn))){
                return false;
            }
            if(player == 0) {
                if (!(board[move.startingRow][move.startingColumn] == 4)) {
                    return false;
                }
            }
            if(player == 1) {
                if (!(board[move.startingRow][move.startingColumn] == 3)) {
                    return false;
                }
            }
            // Check if there are any pieces in between the starting and ending squares
            int r = move.startingRow;
            int c = move.startingColumn;
            if (r < move.endingRow) {
                r++;
            } else {
                r--;
            }
            if (c < move.endingColumn) {
                c++;
            } else {
                c--;
            }
            while ((r != move.endingRow) && (c != move.endingColumn)) {
                if (board[r][c] != 0) {
                    return false;
                }
                if (r < move.endingRow) {
                    r++;
                } else {
                    r--;
                }
                if (c < move.endingColumn) {
                    c++;
                } else {
                    c--;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean gameOver(){
        boolean whiteWin = true;
        boolean blackWin = true;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                if(board[i][j] == 1 || board[i][j] == 3){
                    whiteWin = false;
                }
            }
        }
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                if(board[i][j] == 2 || board[i][j] == 4){
                    blackWin = false;
                }
            }
        }
        if(whiteWin || blackWin){
            return true;
        }
        return false;
    }
}