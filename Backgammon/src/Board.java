/*package lab3;*/
import java.lang.Math;
import java.util.Arrays;
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
    private Turn lastTurn;

    private Dice dice;

    Board(Dice dice)
    {
        this.lastMove = new Move();
        this.lastTurn = new Turn();
        this.lastPlayer = B;
        this.gameBoard = new int[28];
        this.dice = dice;

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
        this.lastTurn = new Turn(board.lastTurn); // DEEP COPY IS REQUIRED
        this.lastPlayer = board.lastPlayer;
        this.gameBoard = new int[28];
        this.dice = board.dice;
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
        
        this.lastMove = new Move(this.lastPlayer, starting_index, dice, this.evaluate());
        this.lastTurn.addMove(this.lastMove);
    }

    void makeMove(Move move) {
        if (move.getTarget() > 25) {
            this.gameBoard[move.getStart()] = (Math.abs(this.gameBoard[move.getStart()]) - 1) * this.lastPlayer; //Remove the piece from the original position
            this.gameBoard[ESCAPE_W] = (Math.abs(this.gameBoard[ESCAPE_W]) + 1) * this.lastPlayer; //Add the piece to the target index
        } else if (move.getTarget() < 2) {
            this.gameBoard[move.getStart()] = (Math.abs(this.gameBoard[move.getStart()]) - 1) * this.lastPlayer; //Remove the piece from the original position
            this.gameBoard[ESCAPE_B] = (Math.abs(this.gameBoard[ESCAPE_B]) + 1) * this.lastPlayer; //Add the piece to the target index
        } else {

            //Target is within bounds

            if (targetIsEnemy(move.getTarget())) { 
                this.gameBoard[move.getStart()] = (Math.abs(this.gameBoard[move.getStart()]) - 1) * this.lastPlayer; //Remove the piece from the original position
                this.gameBoard[move.getTarget()] = this.lastPlayer; //Replace the existing piece with our own

                //Add opponent's piece to the graveyard
                if (this.lastPlayer == W) {
                    this.gameBoard[GRAVE_B] -= 1;
                } else {
                    this.gameBoard[GRAVE_W] += 1;
                }
            } else {
                this.gameBoard[move.getStart()] = (Math.abs(this.gameBoard[move.getStart()]) - 1) * this.lastPlayer; //Remove the piece from the original position
                this.gameBoard[move.getTarget()] = (Math.abs(this.gameBoard[move.getTarget()]) + 1) * this.lastPlayer; //Add the piece to the target index
            }
        }
        
        this.lastMove = new Move(move.getPlayer(), move.getStart(), move.getDice(), move.getValue());
        this.lastTurn.addMove(this.lastMove);
    }

    void makeTurn(Turn turn) {
        this.getLastTurn().clearTurn();
        for (int i = 0; i < turn.getTurn().size(); i++) {
            makeMove(turn.getTurn().get(i));
        }

        this.lastPlayer *= -1; // Change player after turn
        this.lastTurn = turn;
    }

    boolean targetIsEnemy(int target_index) {return (this.lastPlayer * this.gameBoard[target_index] == -1);} //targetIsEnemy is only executed in blocks where isValidMove is TRUE

    //Checks whether a move is valid
    boolean isValidMove(int start, int target)
    {
        if (this.gameBoard[start] * this.lastPlayer > 0 && !isTerminal()) //We are picking up a friendly piece
        {
            if (target > 25) { // If White is trying to escape, if we find at least 1 piece outside of the home area, return false
                for (int i = 2; i < 20; i++) {
                    if (this.gameBoard[i] > 0) return false;
                }
            } else if (target < 2) { // If Black is trying to escape, if we find at least 1 piece outside of the home area, return false
                for (int i = 25; i > 8; i--) {
                    if (this.gameBoard[i] < 0) return false;
                }
            }
            if(this.gameBoard[target] == EMPTY || this.gameBoard[target] + this.lastPlayer == 0 || this.gameBoard[target] * this.lastPlayer > 0) return true; //Target Tile is either empty, has 1 opponent or has player's pieces
        }
        return false;
    }

    boolean isGraveyard() {
        if (this.lastPlayer == W && this.gameBoard[GRAVE_W] > 0) {
            return true;
        } else if (this.lastPlayer == B && this.gameBoard[GRAVE_B] < 0) {
            return true;
        }
        return false;
    }

    /* Generates the children of the state
     * Any square in the board that is empty results to a child
     */
    ArrayList<Board> getChildren(int player, Dice dice)
    {
        int target_position;
        int id = 0;

        this.lastTurn.clearTurn(); //Empty the list, so that the potential moves build on lastTurn
        if (dice.isDouble()) {
            target_position = dice.getDice()[0] * player;
            ArrayList<Board> temp = new ArrayList<>();
            temp.add(this);
            return doubleGetChildren(temp, target_position, 0);
        } else {
            ArrayList<Board> parents = new ArrayList<>();
            ArrayList<Board> children = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                target_position = dice.getDice()[i] * player;
                if (this.isGraveyard() && player == W) {
                    if(this.isValidMove(GRAVE_W, GRAVE_W + target_position)) //Only valid moves are added
                    {
                        Board child = new Board(this);
                        System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                        id++;
                        child.makeMove(GRAVE_W, dice.getDice()[i]);
                        parents.add(child);
                        System.out.println("Move From: White Grave -> " + target_position + " Successfully Added to possible moves");
                    }
                } else if (this.isGraveyard() && player == B){
                    if(this.isValidMove(GRAVE_B, GRAVE_B + target_position)) //Only valid moves are added
                    {
                        Board child = new Board(this);
                        System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                        id++;
                        child.makeMove(GRAVE_B, dice.getDice()[i]);
                        parents.add(child);
                        System.out.println("Move From: Black Grave -> " + target_position + " Successfully Added to possible moves");
                    }
                } else {
                    for(int tile = 2; tile < this.gameBoard.length - 2; tile++)
                    {
                        if(this.isValidMove(tile, tile + target_position)) //Only valid moves are added
                        {
                            Board child = new Board(this);
                            System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                            id++;
                            child.makeMove(tile, dice.getDice()[i]);
                            parents.add(child);
                            System.out.println("Move From: " + (tile - 1) + " -> " + (tile - 1 + target_position) + " Successfully Added to possible moves");
                        }
                    }
                }
 
            }

            for (Board child_i: parents) {
                if (child_i.getLastMove().getDice() == dice.getDice()[0]) { // If our last move was using dice 1, now calculate next moves with dice 2
                    target_position = dice.getDice()[1] * player;
                    if (child_i.isGraveyard() && player == W) {
                        if(child_i.isValidMove(GRAVE_W, GRAVE_W + target_position)) //Only valid moves are added
                        {
                            Board baby = new Board(child_i);
                            System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                            id++;
                            baby.makeMove(GRAVE_W, dice.getDice()[1]);
                            if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY 
                                continue;
                            }
                            children.add(baby);
                            System.out.println("Move From: White Grave -> " + target_position + " Successfully Added to possible moves");
                        }
                    } else if (child_i.isGraveyard() && player == B){
                        if(child_i.isValidMove(GRAVE_B, GRAVE_B + target_position)) //Only valid moves are added
                        {
                            Board baby = new Board(child_i);
                            System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                            id++;
                            baby.makeMove(GRAVE_B, dice.getDice()[1]);
                            if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                                continue;
                            }
                            children.add(baby);
                            System.out.println("Move From: Black Grave -> " + target_position + " Successfully Added to possible moves");
                        }
                    } else {
                        for(int tile = 2; tile < child_i.gameBoard.length - 2; tile++)
                        {
                            if(child_i.isValidMove(tile, tile + target_position)) //Only valid moves are added
                            {
                                Board baby = new Board(child_i);
                                System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                                id++;
                                baby.makeMove(tile, dice.getDice()[1]);
                                if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                                    continue;
                                }
                                children.add(baby);
                                System.out.println("Move From: " + (tile - 1) + " -> " + (tile - 1 + target_position) + " Successfully Added to possible moves");
                            }
                        }
                    }
                } else { // If our last move was using dice 2, now calculate next moves with dice 1
                    target_position = dice.getDice()[0] * player;
                    if (child_i.isGraveyard() && player == W) {
                        if(child_i.isValidMove(GRAVE_W, GRAVE_W + target_position)) //Only valid moves are added
                        {
                            Board baby = new Board(child_i);
                            System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                            id++;
                            baby.makeMove(GRAVE_W, dice.getDice()[0]);
                            if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                                System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                                continue;
                            }
                            children.add(baby);
                            System.out.println("Move From: White Grave -> " + target_position + " Successfully Added to possible moves");
                        }
                    } else if (child_i.isGraveyard() && player == B){
                        if(child_i.isValidMove(GRAVE_B, GRAVE_B + target_position)) //Only valid moves are added
                        {
                            Board baby = new Board(child_i);
                            System.out.println("NEW BOARD CREATED, BOARD ID: " + id + "\n");
                            baby.print(dice);
                            id++;
                            baby.makeMove(GRAVE_B, dice.getDice()[0]);
                            if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                                System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                                continue;
                            }
                            children.add(baby);
                            System.out.println("Move From: Black Grave -> " + target_position + " Successfully Added to possible moves");
                        }
                    } else {
                        for(int tile = 2; tile < child_i.gameBoard.length - 2; tile++)
                        {
                            if(child_i.isValidMove(tile, tile + target_position)) //Only valid moves are added
                            {
                                Board baby = new Board(child_i);
                                System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                                id++;
                                baby.makeMove(tile, dice.getDice()[0]);
                                if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                                    System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                                    continue;
                                }
                                children.add(baby);
                                System.out.println("Move From: " + (tile - 1) + " -> " + (tile - 1 + target_position) + " Successfully Added to possible moves");
                            }
                        }
                    }
                }
            }
            System.out.println("Calculated " + children.size() + " moves.");
            return children;
        }
        
    }

    


    /*
     * The heuristic we use to evaluate is:
     * - The further your pieces are, the more points you get.
     * - The home area (last 6 tiles) all give the same points.
     * - Escaped pieces give +0.5, in order to incentivize the bot to first bring all pieces to the home area, then start the escape.
     */
    double evaluate()
    {
        double scoreW = 0;
        double scoreB = 0;

        for (int i = 2; i < 26; i++) {
            if (i <= 7) {
                if (this.gameBoard[i] < 0) { // If we are in Black's home area, count all spaces as the first one
                    scoreB += 18 * this.gameBoard[i];
                } else {
                    scoreW += (i - 1) * this.gameBoard[i];
                }
            } else if (i >= 20) { // If we are in White's home area, count all spaces as the first one
                if (this.gameBoard[i] > 0) {
                    scoreW += 18 * this.gameBoard[i];
                } else {
                    scoreB += (26 - i) * this.gameBoard[i];
                }
            } else {
                 if (this.gameBoard[i] > 0) {
                    scoreW += (i - 1) * this.gameBoard[i];
                } else {
                    scoreB += (26 - i) * this.gameBoard[i];
                }
            }
        }
        
        scoreW += 18.5 * this.gameBoard[ESCAPE_W];
        scoreB += 18.5 * this.gameBoard[ESCAPE_B];



        return scoreW + scoreB;
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

    void print(Dice dice) {
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
        System.out.print("              " + dice.getDice()[0] + " : " + dice.getDice()[1]);
        System.out.print("\n\n\n");
        System.out.println(katw_orofos);
        System.out.println("WHITE GRAVE: " + Math.abs(this.gameBoard[GRAVE_W]) + " || " + "BLACK GRAVE: " + Math.abs(this.gameBoard[GRAVE_B]));
        System.out.println("WHITE ESCAPED: " + Math.abs(this.gameBoard[ESCAPE_W]) + " || " + "BLACK ESCAPED: " + Math.abs(this.gameBoard[ESCAPE_B]));
        System.out.println("\nCurrent Score Evaluation: " + this.evaluate());
        System.out.println("******************************");
    }

    /* doubleGetChildren() is a wild method.
     * 
     * We use recursion to avoid writing the same things over and over,
     * but it works very similarly to getChildren() for non-double turns.
     * 
     * We calculate all possible moves on 4 dice of the same number, making sure we skip duplicate boards.
     * 
     */

    ArrayList<Board> doubleGetChildren(ArrayList<Board> boards, int target_position, int depth) {
        ArrayList<Board> children = new ArrayList<>();
        int id = 0;
        if (depth == 4) {
            System.out.println("Calculated " + boards.size() + " moves.");
            return boards;
        }
        for (Board child_i: boards) {
            if (child_i.isGraveyard() && child_i.getLastPlayer() == W) {
                if(child_i.isValidMove(GRAVE_W, GRAVE_W + target_position)) //Only valid moves are added
                {
                    Board baby = new Board(child_i);
                    baby.makeMove(GRAVE_W, dice.getDice()[0]);
                    System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                    id++;
                    if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                        System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                        continue;
                    }
                    children.add(baby);
                    System.out.println("Move From: White Grave -> " + target_position + " Successfully Added to possible moves");
                }
            } else if (child_i.isGraveyard() && child_i.getLastPlayer() == B){
                if(child_i.isValidMove(GRAVE_B, GRAVE_B + target_position)) //Only valid moves are added
                {
                    Board baby = new Board(child_i);
                    baby.makeMove(GRAVE_B, dice.getDice()[0]);
                    System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                    id++;
                    if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                        System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                        continue;
                    }
                    children.add(baby);
                    System.out.println("Move From: Black Grave -> " + target_position + " Successfully Added to possible moves");
                }
            } else {
                for(int tile = 2; tile < child_i.gameBoard.length - 2; tile++)
                {
                    if(child_i.isValidMove(tile, tile + target_position)) //Only valid moves are added
                    {
                        Board baby = new Board(child_i);
                        baby.makeMove(tile, dice.getDice()[0]);
                        System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                        id++;
                        if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                            System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                            continue;
                        }
                        children.add(baby);
                        System.out.println("Move From: " + (tile - 1) + " -> " + (tile - 1 + target_position) + " Successfully Added to possible moves");
                    }
                }
            }
        }

        return doubleGetChildren(children, target_position, depth + 1);
        
    }

    Move getLastMove()
    {
        return this.lastMove;
    }

    Turn getLastTurn() {
        return this.lastTurn;
    }

    int getLastPlayer()
    {
        return this.lastPlayer;
    }

    int[] getGameBoard()
    {
        return this.gameBoard;
    }

    void setGameBoard(int[] gameBoard)
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

    public boolean equals(Object e) { // Method contains() in ArrayList uses equals() to compare objects. Overrode this function in order for contains() to work.
        if (this == e) {
            return true;
        }
        if (e == null || getClass() != e.getClass()) {
            return false;
        }
        Board other = (Board) e;
        return Arrays.equals(this.gameBoard, other.gameBoard);
    }

}
