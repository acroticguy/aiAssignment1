/*package lab3;*/
import java.lang.Math;
import java.util.ArrayList;

public class Board
{
    public static final int W = 1;
    public static final int B = -1;
    public static final int EMPTY = 0;

    public static final int GRAVE_W = 1;
    public static final int GRAVE_B = 26;
    public static final int ESCAPE_W = 0;
    public static final int ESCAPE_B = 27;

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

    //Make a move; it moves a piece by the dice's number (since Black goes left, the dice movements need to be backwards, hence dice * player)
    void makeMove(int starting_index, int dice)
    {
        int target_index = starting_index + (dice * this.lastPlayer);

        if (target_index > 25) {
            this.gameBoard[starting_index] = (Math.abs(this.gameBoard[starting_index]) - 1) * this.lastPlayer; //Remove the piece from the original position
            this.gameBoard[ESCAPE_W] = (Math.abs(this.gameBoard[ESCAPE_W]) + 1) * this.lastPlayer; //Add the piece to the target index
        } else if (target_index < 2) {
            this.gameBoard[starting_index] = (Math.abs(this.gameBoard[starting_index]) - 1) * this.lastPlayer; //Remove the piece from the original position
            this.gameBoard[ESCAPE_B] = (Math.abs(this.gameBoard[ESCAPE_B]) + 1) * this.lastPlayer; //Add the piece to the target index
        } else {

            //Target is within bounds

            if (targetIsEnemy(target_index)) {
                this.gameBoard[starting_index] = (Math.abs(this.gameBoard[starting_index]) - 1) * this.lastPlayer; //Remove the piece from the original position
                this.gameBoard[target_index] = this.lastPlayer; //Replace the existing piece with our own

                //Add opponent's piece to the graveyard
                if (this.lastPlayer == W) {
                    this.gameBoard[GRAVE_B] -= 1;
                } else {
                    this.gameBoard[GRAVE_W] += 1;
                }
            } else {
                this.gameBoard[starting_index] = (Math.abs(this.gameBoard[starting_index]) - 1) * this.lastPlayer; //Remove the piece from the original position
                this.gameBoard[target_index] = (Math.abs(this.gameBoard[target_index]) + 1) * this.lastPlayer; //Add the piece to the target index
            }
        }

        
        this.lastMove = new Move(this.lastPlayer, starting_index, target_index);
    }

    boolean targetIsEnemy(int target_index) {return (this.lastPlayer * this.gameBoard[target_index] == -1);}

    //Checks whether a move is valid
    boolean isValidMove(int start, int target)
    {
        if (this.gameBoard[start] * this.lastPlayer > 0) //We are picking up a friendly piece
        {
            if (target > 25 || target < 2) return true;
            if(this.gameBoard[target] == EMPTY || this.gameBoard[target] + this.lastPlayer == 0 || this.gameBoard[target] * this.lastPlayer > 0) return true; //Target Tile is either empty, has 1 opponent or has player's pieces
        }
        
        return false;
    }

    boolean isGraveyard() {
        if (this.lastPlayer == W && this.gameBoard[1] > 0) {
            return true;
        } else if (this.lastPlayer == B && this.gameBoard[26] < 0) {
            return true;
        }
        return false;
    }

    /* Generates the children of the state
     * Any square in the board that is empty results to a child
     */
    ArrayList<Board> getChildren(int player, int dice)
    {
        ArrayList<Board> children = new ArrayList<>();
        for(int tile = 0; tile < this.gameBoard.length; tile++)
        {
            if(this.isValidMove(tile, tile + dice * player)) //is valid move
            {
                Board child = new Board(this);
                child.makeMove(tile, 1);
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

        return scoreW - scoreB;
    }

    /*
     * A state is terminal if one of the players' pieces are all in his escape tile
     */
    boolean isTerminal()
    {
        if (this.gameBoard[ESCAPE_W] != 15 && this.gameBoard[ESCAPE_B] != 15) {
            return false;
        }
        return true;
    }

    void print(int dice_1, int dice_2){
        System.out.println("******************************");
        String panw_orofos = "";
        String katw_orofos = "";
        for (int i = 2; i<14;i++){
            if(this.gameBoard[i] == 0){
                panw_orofos +=" E ";
            }
            else if(this.gameBoard[i] < 0){
                panw_orofos +=" " + Math.abs(this.gameBoard[i]) + "B ";
            }else{
                panw_orofos +=" " + Math.abs(this.gameBoard[i]) + "W ";
            }
            if(this.gameBoard[27 - i] == 0){
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
        System.out.print("              " + dice_1 + " : " + dice_2);
        System.out.print("\n\n\n");
        System.out.println(katw_orofos);
        System.out.println("WHITE GRAVE: " + Math.abs(this.gameBoard[GRAVE_W]) + " || " + "BLACK GRAVE: " + Math.abs(this.gameBoard[GRAVE_B]));
        System.out.println("WHITE ESCAPED: " + Math.abs(this.gameBoard[ESCAPE_W]) + " || " + "BLACK ESCAPED: " + Math.abs(this.gameBoard[ESCAPE_B]));
        System.out.println("******************************");
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
        this.lastMove.setStart(lastMove.getStart());
        this.lastMove.setValue(lastMove.getValue());
    }

    void setLastPlayer(int lastPlayer)
    {
        this.lastPlayer = lastPlayer;
    }
}
