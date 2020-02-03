Collaborators:
Abhiram Yakkali
Shlok Mehrotra
Raghava Madireddy


https://repl.it/join/quihpzrl-shlokmehrotra

Class Tile
Description: This class will represent each tile on the board, which will later be sorted into an 8x8 array

Variables: 
int row, int column- Represent the row and column of the tile
Piece occupant- Represents the piece that is on the board

Methods:
Piece getOccupant()- Return the Piece that is on this Tile
void setOccupant(Piece p)- Set occupant of Tile to the p
boolean isOccupied()- Return true if it’s occupied and false if its not


Class Piece
Description: Every Piece object represents a piece in the checkers game

Variables:
Player player- The player that this piece belongs to
Tile tile- The tile this piece is on
boolean kinged- True is the piece is a king, else false

Methods:
Boolean canMoveto(Tile tile)- Returns true if the piece can move to the specified tile. Regular pieces can only move one square diagonally to the other end of the board at a time. Kinged pieces can move or jump diagonally in any direction.
















Class Move
Description: Moves are encapsulated in a move object, which simply represents moving a piece to another square. Moves should take in two tiles and from them infer the piece that was moved and if any pieces were captured in the process. Do not need to take into account double or triple jumps.

Variables:
Tile startingTile- The tile at which the piece starts 
Tile endingTile- The tile at which the piece ends
Piece piece- The piece that was moved 
Piece captured- The piece that was captured (if there was any)
boolean isJump- True is if the move was a jump

Methods:
(Constructor) Move(Tile startingTile, Tile endingTile)- Takes both tiles and finds the piece that was moved and the piece that was captured (if any)


Class Player
Description: The player class contains all variables and methods which relate to making moves throughout the game

Variables:
Piece[] pieces- An array of all the players pieces
Game game- The game that the player is playing in
int color- Color of the player (1 is red 2 is black)

Methods:
void addPiece(Tile tile)- Creates a new piece on a tile and adds it to the players list of pieces
void setGame(Game game)- Sets the Player’s specified game to the game provided
void getPieces- Gets a list of the player’s pieces
void king(Piece piece)- Kings the piece
void executeMove(Move move)- Executes the supplied move










Class Main
Description: This class contains all information pertaining to the game, as well as the main method

Variables:
boolean 

Methods:






GUI
void printBoard()
-Prints out the board with all pieces displayed
-String printing (for dev purposes) - Shlok
-Board Display (printing for UI) - Shlok
-UI - Abhiram
For string printing numbers correspond as so:
0 = space
1 = black
2 = red
3 = black king
4 = red king
In actual UI this must be converted to figures but for String array it can be left as 
