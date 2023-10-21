/*package lab3;*/
import java.lang.Math;
import java.util.ArrayList;

public class Board
{
    public static final int W = 1;
    public static final int B = -1;
    public static final int EMPTY = 0;

    public static final int GRAVE_W = 0;
    public static final int GRAVE_B = 27;
    public static final int ESCAPE_W = 1;
    public static final int ESCAPE_B = 26;

    private int[] gameBoard; // The board has length 28, Spaces 0 and 27 indicate the graveyard, and spaces 1 and 26 indicate the "escaped" pieces. 2-25 are the playable tiles.

    /* Variable containing who played last; whose turn resulted in this board
     * Even a new Board has lastLetterPlayed value; it denotes which player will play first
     */
    private int lastPlayer;

    //Immediate move that lead to this board
    private Move lastMove;

    Board()
    {
        this.lastMove = new Move();
        this.lastPlayer = B;
        this.gameBoard = new int[28];

        //INITIAL POSITIONS
        this.gameBoard[2] = 2 * W;
        this.gameBoard[7] = 5 * B;
        this.gameBoard[9] = 3 * B;
        this.gameBoard[13] = 5 * W;
        this.gameBoard[14] = 5 * B;
        this.gameBoard[18] = 3 * W;
        this.gameBoard[20] = 5 * W;
        this.gameBoard[25] = 2 * B;
    }

    Board(Board board)
    {
        this.lastMove = board.lastMove;
        this.lastPlayer = board.lastPlayer;
        this.gameBoard = new int[28];
        for(int i = 0; i < this.gameBoard.length; i++)
        {
            this.gameBoard[i] = board.gameBoard[i];
        }
    }

    //Make a move; it places a letter in the board
    void makeMove(int col, int letter)
    {
        this.gameBoard[col] = letter;
        this.lastMove = new Move(col);
        this.lastPlayer = letter;
    }

    //Checks whether a move is valid; whether a square is empty
    boolean isValidMove(int pos, int letter)
    {
        int thesi_pou_katalhgw = this.gameBoard[pos];
        if (pos <= 25 && pos >= 2) //Target Tile is within bounds
        {
            if(thesi_pou_katalhgw == EMPTY || thesi_pou_katalhgw == letter * (-1) || thesi_pou_katalhgw * letter > 0) return true; //Target Tile is either empty, has 1 opponent or has player's pieces
        }
        
        return false;
    }

    /* Generates the children of the state
     * Any square in the board that is empty results to a child
     */
    ArrayList<Board> getChildren(int letter)
    {
        ArrayList<Board> children = new ArrayList<>();
        for(int col = 0; col < this.gameBoard.length; col++)
        {
            if(this.isValidMove(col, letter))
            {
                Board child = new Board(this);
                child.makeMove(col, letter);
                children.add(child);
            }
        }
        return children;
    }


    /*
     * The heuristic we use to evaluate is
     * the number of almost complete tic-tac-toes (having 2 letter in a row, column or diagonal)
     * minus the number of the opponent's almost complete tic-tac-toes
     * Special case: if a complete tic-tac-toe is present it counts as ten
     */
    int evaluate()
    {
        int scoreW = 0;
        int scoreB = 0;
        int sum;

        //checking rows

        //checking diagonals

        return scoreW - scoreB;
    }

    /*
     * A state is terminal if there is a tic-tac-toe
     * or no empty tiles are available
     */
    boolean isTerminal()
    {
    	// horizontal tic-tac-toe
        if (this.gameBoard[ESCAPE_W] != 15 && this.gameBoard[ESCAPE_B] != 15) {
            return false;
        }
        return true;
    }

    void print()
    {
        for (int i: this.gameBoard){
            System.out.println(i);
        }
        System.out.println("WORK IN PROGRESS");
    }

    void printB(){
        System.out.println("******************************");
        String panw_orofos = "";
        String katw_orofos = "";
        for (int i = 2; i<14;i++){
            if(this.gameBoard[i]==0){
                panw_orofos +=" E ";
            }
            else if(this.gameBoard[i] < 0){
                panw_orofos +=" " + Math.abs(this.gameBoard[i]) + "B ";
            }else{
                panw_orofos +=" " + Math.abs(this.gameBoard[i]) + "W ";
            }
            if(this.gameBoard[27 - i]== 0){
                katw_orofos +=" E ";
            }
            else if(this.gameBoard[27 - i] < 0){
                katw_orofos +=" " + Math.abs(this.gameBoard[27 - i]) + "B ";
            }else{
                katw_orofos +=" " + Math.abs(this.gameBoard[27 - i]) + "W ";
            }
        }
        System.out.print(panw_orofos);
        System.out.print("\n\n\n");
        System.out.print("              KAPOU EDW MPAINOUN TA ZARIA");
        System.out.print("\n\n\n");
        System.out.println(katw_orofos);
    }


    Move getLastMove()
    {
        return this.lastMove;
    }

    int getLastPlayer()
    {
        return this.lastPlayer;
    }

    int[] getGameBoard()
    {
        return this.gameBoard;
    }

    void setGameBoard(int[][] gameBoard)
    {
        //Work in progress
    }

    void setLastMove(Move lastMove)
    {
        this.lastMove.setCol(lastMove.getCol());
        this.lastMove.setValue(lastMove.getValue());
    }

    void setLastPlayer(int lastPlayer)
    {
        this.lastPlayer = lastPlayer;
    }
}
