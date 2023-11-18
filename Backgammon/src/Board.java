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
    private int currPlayer;

    //Immediate move that lead to this board
    private Move lastMove;
    private Turn lastTurn;

    private Dice dice;

    Board(Dice dice)
    {
        this.lastMove = new Move();
        this.lastTurn = new Turn();
        this.currPlayer = W;
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
        this.currPlayer = board.currPlayer;
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
        int target_index = starting_index + (dice * this.currPlayer); 

        if (target_index > 25) {
            this.gameBoard[starting_index] = (Math.abs(this.gameBoard[starting_index]) - 1) * this.currPlayer; //Remove the piece from the original position
            this.gameBoard[ESCAPE_W] = (Math.abs(this.gameBoard[ESCAPE_W]) + 1) * this.currPlayer; //Add the piece to the target index
        } else if (target_index < 2) {
            this.gameBoard[starting_index] = (Math.abs(this.gameBoard[starting_index]) - 1) * this.currPlayer; //Remove the piece from the original position
            this.gameBoard[ESCAPE_B] = (Math.abs(this.gameBoard[ESCAPE_B]) + 1) * this.currPlayer; //Add the piece to the target index
        } else {

            //Target is within bounds

            if (targetIsEnemy(target_index)) { 
                this.gameBoard[starting_index] = (Math.abs(this.gameBoard[starting_index]) - 1) * this.currPlayer; //Remove the piece from the original position
                this.gameBoard[target_index] = this.currPlayer; //Replace the existing piece with our own

                //Add opponent's piece to the graveyard
                if (this.currPlayer == W) {
                    this.gameBoard[GRAVE_B] -= 1; // Black is negative, this adds a black piece.
                } else {
                    this.gameBoard[GRAVE_W] += 1;
                }
            } else {
                this.gameBoard[starting_index] = (Math.abs(this.gameBoard[starting_index]) - 1) * this.currPlayer; //Remove the piece from the original position
                this.gameBoard[target_index] = (Math.abs(this.gameBoard[target_index]) + 1) * this.currPlayer; //Add the piece to the target index
            }
        }
        
        this.lastMove = new Move(this.currPlayer, starting_index, dice, this.evaluate());
        this.lastTurn.addMove(this.lastMove);
    }

    void makeMove(Move move) {
        if (move.getTarget() > 25) {
            this.gameBoard[move.getStart()] = (Math.abs(this.gameBoard[move.getStart()]) - 1) * this.currPlayer; //Remove the piece from the original position
            this.gameBoard[ESCAPE_W] = (Math.abs(this.gameBoard[ESCAPE_W]) + 1) * this.currPlayer; //Add the piece to the target index
        } else if (move.getTarget() < 2) {
            this.gameBoard[move.getStart()] = (Math.abs(this.gameBoard[move.getStart()]) - 1) * this.currPlayer; //Remove the piece from the original position
            this.gameBoard[ESCAPE_B] = (Math.abs(this.gameBoard[ESCAPE_B]) + 1) * this.currPlayer; //Add the piece to the target index
        } else {

            //Target is within bounds

            if (targetIsEnemy(move.getTarget())) { 
                this.gameBoard[move.getStart()] = (Math.abs(this.gameBoard[move.getStart()]) - 1) * this.currPlayer; //Remove the piece from the original position
                this.gameBoard[move.getTarget()] = this.currPlayer; //Replace the existing piece with our own

                //Add opponent's piece to the graveyard
                if (this.currPlayer == W) {
                    this.gameBoard[GRAVE_B] -= 1; // Black is negative, this adds a black piece.
                } else {
                    this.gameBoard[GRAVE_W] += 1;
                }
            } else {
                this.gameBoard[move.getStart()] = (Math.abs(this.gameBoard[move.getStart()]) - 1) * this.currPlayer; //Remove the piece from the original position
                this.gameBoard[move.getTarget()] = (Math.abs(this.gameBoard[move.getTarget()]) + 1) * this.currPlayer; //Add the piece to the target index
            }
        }
        
        this.lastMove = new Move(move.getPlayer(), move.getStart(), move.getDice(), move.getScore());
        this.lastTurn.addMove(this.lastMove);
    }

    void makeTurn(Turn turn) {
        this.getLastTurn().clearTurn();
        for (int i = 0; i < turn.getTurn().size(); i++) {
            if (turn.getTurn().get(i).getStart() == -1) { // All moves from -1 to -1 are invalid. Empty board arrays give -1 -> -1 moves.
                System.out.println("NO AVAILABLE MOVES. TURN SKIPPED!");
                continue;
            }
            this.makeMove(turn.getTurn().get(i));
        }
        this.currPlayer *= -1;
        this.lastTurn = turn;
    }

    boolean targetIsEnemy(int target_index) {return (this.currPlayer * this.gameBoard[target_index] == -1);} //targetIsEnemy is only executed in blocks where isValidMove is TRUE, so there's no need for extra checks.

    //Checks whether a move is valid
    boolean isValidMove(int start, int target)
    {
        if (start > 26 || start < 1) { // Starting position out of playable bounds
            return false;
        }
        if (this.gameBoard[start] * this.currPlayer > 0 && !this.isTerminal()) //We are picking up a friendly piece
        {
            if (target > 25) { // If White is trying to escape, if we find at least 1 piece outside of the home area, return false
                for (int i = 2; i < 20; i++) {
                    if (this.gameBoard[i] > 0) return false;
                }
                return true;
            } else if (target < 2) { // If Black is trying to escape, if we find at least 1 piece outside of the home area, return false
                for (int i = 25; i > 8; i--) {
                    if (this.gameBoard[i] < 0) return false;
                }
                return true;
            }
            if(this.gameBoard[target] == EMPTY || this.gameBoard[target] + this.currPlayer == 0 || this.gameBoard[target] * this.currPlayer > 0) return true; //Target Tile is either empty, has 1 opponent or has player's pieces
        }
        return false;
    }

    boolean isGraveyard() {
        if (this.currPlayer == W && this.gameBoard[GRAVE_W] > 0) { // If we are white and have pieces in Graveyard
            return true;
        } else if (this.currPlayer == B && this.gameBoard[GRAVE_B] < 0) { // If we are black and have pieces in Graveyard
            return true;
        }
        return false;
    }

    /* Generates the children of the state
     * Each unique combination of valid dice moves results in another child.
     */
    ArrayList<Board> getChildren(int player)
    {
        int target_position;
        int id = 0;
        int validMoveCounter = 0;
        Turn temp_turn = new Turn();

        this.lastTurn.clearTurn(); //Empty the list, so that the potential moves build on lastTurn
        if (this.dice.isDouble()) {
            target_position = this.dice.getDice()[0] * player;
            ArrayList<Board> temp = new ArrayList<>();
            temp.add(this);
            temp = doubleGetChildren(temp, target_position, 0, validMoveCounter);
            for (Board baby: temp) {
                temp_turn = baby.getLastTurn();
                baby = new Board(this);
                baby.makeTurn(temp_turn);
            }
            return temp;
        } else {
            ArrayList<Board> parents = new ArrayList<>();
            ArrayList<Board> children = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                target_position = this.dice.getDice()[i] * player;
                if (this.isGraveyard() && player == W) {
                    if(this.isValidMove(GRAVE_W, GRAVE_W + target_position)) //Only valid moves are added
                    {
                        Board child = new Board(this);
                        // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                        id++;
                        child.makeMove(GRAVE_W, this.dice.getDice()[i]);
                        parents.add(child);
                        // FOR DEBUG                               System.out.println("Move From: White Grave -> " + target_position + " Successfully Added to possible moves");
                    }
                } else if (this.isGraveyard() && player == B){
                    if(this.isValidMove(GRAVE_B, GRAVE_B + target_position)) //Only valid moves are added
                    {
                        Board child = new Board(this);
                        // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                        id++;
                        child.makeMove(GRAVE_B, this.dice.getDice()[i]);
                        parents.add(child);
                        // FOR DEBUG                               System.out.println("Move From: Black Grave -> " + target_position + " Successfully Added to possible moves");
                    }
                } else {
                    for(int tile = 2; tile < this.gameBoard.length - 2; tile++)
                    {
                        if(this.isValidMove(tile, tile + target_position)) //Only valid moves are added
                        {
                            Board child = new Board(this);
                            // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                            id++;
                            child.makeMove(tile, this.dice.getDice()[i]);
                            parents.add(child);
                            // FOR DEBUG                               System.out.println("Move From: " + (tile - 1) + " -> " + (tile - 1 + target_position) + " Successfully Added to possible moves");
                        }
                    }
                }
 
            }

            for (Board child_i: parents) {
                if (child_i.getLastMove().getDice() == this.dice.getDice()[0]) { // If our last move was using dice 1, now calculate next moves with dice 2
                    target_position = this.dice.getDice()[1] * player;
                    if (child_i.isGraveyard() && player == W) {
                        if(child_i.isValidMove(GRAVE_W, GRAVE_W + target_position)) //Only valid moves are added
                        {
                            validMoveCounter++;
                            Board baby = new Board(child_i);
                            // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                            id++;
                            baby.makeMove(GRAVE_W, this.dice.getDice()[1]);
                            if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY 
                                continue;
                            }
                            children.add(baby);
                            // FOR DEBUG                               System.out.println("Move From: White Grave -> " + target_position + " Successfully Added to possible moves");
                        }
                    } else if (child_i.isGraveyard() && player == B){
                        if(child_i.isValidMove(GRAVE_B, GRAVE_B + target_position)) //Only valid moves are added
                        {
                            validMoveCounter++;
                            Board baby = new Board(child_i);
                            // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                            id++;
                            baby.makeMove(GRAVE_B, this.dice.getDice()[1]);
                            if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                                continue;
                            }
                            children.add(baby);
                            // FOR DEBUG                               System.out.println("Move From: Black Grave -> " + target_position + " Successfully Added to possible moves");
                        }
                    } else {
                        for(int tile = 2; tile < child_i.gameBoard.length - 2; tile++)
                        {
                            if(child_i.isValidMove(tile, tile + target_position)) //Only valid moves are added
                            {
                                validMoveCounter++;
                                Board baby = new Board(child_i);
                                // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                                id++;
                                baby.makeMove(tile, this.dice.getDice()[1]);
                                if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                                    continue;
                                }
                                children.add(baby);
                                // FOR DEBUG                               System.out.println("Move From: " + (tile - 1) + " -> " + (tile - 1 + target_position) + " Successfully Added to possible moves");
                            }
                        }
                    }
                } else { // If our last move was using dice 2, now calculate next moves with dice 1
                    target_position = this.dice.getDice()[0] * player;
                    if (child_i.isGraveyard() && player == W) {
                        if(child_i.isValidMove(GRAVE_W, GRAVE_W + target_position)) //Only valid moves are added
                        {
                            validMoveCounter++;
                            Board baby = new Board(child_i);
                            // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                            id++;
                            baby.makeMove(GRAVE_W, this.dice.getDice()[0]);
                            if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                                // FOR DEBUG                               System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                                continue;
                            }
                            children.add(baby);
                            // FOR DEBUG                               System.out.println("Move From: White Grave -> " + target_position + " Successfully Added to possible moves");
                        }
                    } else if (child_i.isGraveyard() && player == B){
                        if(child_i.isValidMove(GRAVE_B, GRAVE_B + target_position)) //Only valid moves are added
                        {
                            validMoveCounter++;
                            Board baby = new Board(child_i);
                            // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id + "\n");
                            baby.print(this.dice);
                            id++;
                            baby.makeMove(GRAVE_B, this.dice.getDice()[0]);
                            if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                               // FOR DEBUG                                System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                                continue;
                            }
                            children.add(baby);
                            // FOR DEBUG                               System.out.println("Move From: Black Grave -> " + target_position + " Successfully Added to possible moves");
                        }
                    } else {
                        for(int tile = 2; tile < child_i.gameBoard.length - 2; tile++)
                        {
                            if(child_i.isValidMove(tile, tile + target_position)) //Only valid moves are added
                            {
                                validMoveCounter++;
                                Board baby = new Board(child_i);
                                // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                                id++;
                                baby.makeMove(tile, this.dice.getDice()[0]);
                                if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                                    // FOR DEBUG                               System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                                    continue;
                                }
                                children.add(baby);
                                // FOR DEBUG                               System.out.println("Move From: " + (tile - 1) + " -> " + (tile - 1 + target_position) + " Successfully Added to possible moves");
                            }
                        }
                    }
                }
            }
            if (validMoveCounter == 0) {
                for (Board parent: parents) {
                    temp_turn = parent.getLastTurn();
                    parent = new Board(this);
                    parent.makeTurn(temp_turn);
                }
                return parents;
            }
            System.out.println("Calculated " + children.size() + " moves. Dice: " + this.dice.printDice());
            for (Board child: children) {
                    temp_turn = child.getLastTurn();
                    child = new Board(this);
                    child.makeTurn(temp_turn);
            }

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
        if (this.gameBoard[ESCAPE_W] >= 15 || this.gameBoard[ESCAPE_B] <= -15) {
            return true;
        }
        return false;
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

    ArrayList<Board> doubleGetChildren(ArrayList<Board> boards, int target_position, int depth, int validMoveCounter) {
        ArrayList<Board> children = new ArrayList<>();
        int id = 0;
        if (depth == 4 || (depth > 0 && depth < 4 && validMoveCounter == 0) ) {
            // FOR DEBUG                               System.out.println("Calculated " + boards.size() + " moves. (Double)");
            return boards;
        }
        for (Board child_i: boards) {
            if (child_i.isGraveyard() && child_i.getLastPlayer() == W) {
                if(child_i.isValidMove(GRAVE_W, GRAVE_W + target_position)) //Only valid moves are added
                {
                    validMoveCounter++;
                    Board baby = new Board(child_i);
                    baby.makeMove(GRAVE_W, dice.getDice()[0]);
                    // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                    id++;
                    if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                        // FOR DEBUG                               System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                        continue;
                    }
                    children.add(baby);
                    // FOR DEBUG                               System.out.println("Move From: White Grave -> " + target_position + " Successfully Added to possible moves");
                }
            } else if (child_i.isGraveyard() && child_i.getLastPlayer() == B){
                if(child_i.isValidMove(GRAVE_B, GRAVE_B + target_position)) //Only valid moves are added
                {
                    validMoveCounter++;
                    Board baby = new Board(child_i);
                    baby.makeMove(GRAVE_B, dice.getDice()[0]);
                    // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                    id++;
                    if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                        // FOR DEBUG                               System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                        continue;
                    }
                    children.add(baby);
                    // FOR DEBUG                               System.out.println("Move From: Black Grave -> " + target_position + " Successfully Added to possible moves");
                }
            } else {
                for(int tile = 2; tile < child_i.gameBoard.length - 2; tile++)
                {
                    if(child_i.isValidMove(tile, tile + target_position)) //Only valid moves are added
                    {
                        validMoveCounter++;
                        Board baby = new Board(child_i);
                        baby.makeMove(tile, dice.getDice()[0]);
                        // FOR DEBUG                               System.out.println("NEW BOARD CREATED, BOARD ID: " + id);
                        id++;
                        if (children.contains(baby)) { // IF GAMESTATE IS ALREADY PROVIDED WITH ONE WAY OR ANOTHER, JUST KEEP A SINGLE COPY
                            // FOR DEBUG                               System.out.println("CHILDREN CONTAINS BABY, #" + id + " removed");
                            continue;
                        }
                        children.add(baby);
                        // FOR DEBUG                               System.out.println("Move From: " + (tile - 1) + " -> " + (tile - 1 + target_position) + " Successfully Added to possible moves");
                    }
                }
            }
        }

        return doubleGetChildren(children, target_position, depth + 1, validMoveCounter);
        
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
        return this.currPlayer;
    }

    int[] getGameBoard()
    {
        return this.gameBoard;
    }

    Dice getDice() {
        return this.dice;
    }

    void setDice(Dice dice) {
        this.dice = dice;
    }

    void setLastMove(Move lastMove)
    {
        this.lastMove.setStart(lastMove.getStart());
        this.lastMove.setScore(lastMove.getScore());
    }

    void updateTurnScore(Turn turn) {
        this.lastTurn.setScore(turn.getScore());
    }

    void setLastPlayer(int currPlayer)
    {
        this.currPlayer = currPlayer;
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
