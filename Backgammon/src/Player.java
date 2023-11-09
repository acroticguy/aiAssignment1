import java.util.ArrayList;
import java.util.Random;

public class Player
{
    private int maxDepth;
    private int playerLetter;
    private ArrayList<Board> uniqueBoards = new ArrayList<>();
    //private ArrayList<Move> moves = new ArrayList<>();

    Player(int maxDepth, int playerLetter)
    {
        this.maxDepth = maxDepth;
        this.playerLetter = playerLetter;
    }

    Turn expectiMiniMax(Board board, Dice dice)
    {
        if(playerLetter == Board.W)
        {
            //If the X plays then it wants to maximize the heuristics value
            return max(new Board(board), 0, dice);
        }
        else
        {
            //If the O plays then it wants to minimize the heuristics value
            return min(new Board(board), 0, dice);
        }
    }

    // The max and min functions are called one after another until a max depth is reached or we have a terminal board.
    // We create a tree using backtracking DFS.
    Turn max(Board board, int depth, Dice dice)
    {
        /* If MAX is called on a state that is terminal or after a maximum depth is reached,
         * then a heuristic is calculated on the state and the move returned.
         */
        uniqueBoards.clear();
        Turn maxTurn = new Turn(new Move(Integer.MIN_VALUE));

        ArrayList<Board> children = new ArrayList<>();
        Random r = new Random();

        if(board.isTerminal() || (depth == this.maxDepth))
        {
            return board.getLastTurn();
        }

        children = board.getChildren(board.getLastPlayer(), dice);

        for (Board child: children) {
                //The child-move with the greatest value is selected and returned by max
                if(child.getLastTurn().getValue() >= maxTurn.getValue())
                {
                    //If the heuristic has the save value then we randomly choose one of the two moves
                    if((child.getLastTurn().getValue()) == maxTurn.getValue())
                    {
                        if(r.nextInt(2) == 0)
                        {
                            maxTurn = child.getLastTurn();
                        }
                    }
                    else
                    {
                        maxTurn = child.getLastTurn();
                    }
                }
            }
        System.out.println("Turn selected: " + maxTurn.printTurn());
        return maxTurn;
    }

    //Min works similarly to max
    Turn min(Board board, int depth, Dice dice)
    {
        uniqueBoards.clear();
        Turn minTurn = new Turn(new Move(Integer.MAX_VALUE));
        ArrayList<Board> children = new ArrayList<>();

        Random r = new Random();

        if(board.isTerminal() || (depth == this.maxDepth))
        {
            return new Turn(board.getLastTurn());
        }

        children = board.getChildren(board.getLastPlayer(), dice);

        for (Board child: children) {
                //The child-move with the greatest value is selected and returned by max
                if(child.getLastTurn().getValue() <= minTurn.getValue())
                {
                    //If the heuristic has the save value then we randomly choose one of the two moves
                    if((child.getLastTurn().getValue()) == minTurn.getValue())
                    {
                        if(r.nextInt(2) == 0)
                        {
                            minTurn = child.getLastTurn();
                        }
                    }
                    else
                    {
                        minTurn = child.getLastTurn();
                    }
                }
            }
        System.out.println("Turn selected: " + minTurn.printTurn());
        return minTurn;
    }

    Move calcTurnDoubleMin(Board board, int turn_depth, Dice dice) {

        ArrayList<Board> children = new ArrayList<>();
        if(board.isTerminal() || turn_depth == 4)
        {
            return new Move(board.getLastPlayer(), board.getLastMove().getStart(), board.getLastMove().getTarget(), board.evaluate());
        }

        Random r = new Random();

        children = board.getChildren(Board.B, dice);
        Move minMove = new Move(Integer.MAX_VALUE);
        for(Board child: children)
        {
            if(uniqueBoards.contains(child)) {
                continue;
            } else {
                Move move = calcTurnDoubleMin(child, turn_depth + 1, dice);
                //The child-move with the greatest value is selected and returned by max
                if(move.getValue() <= minMove.getValue())
                {
                    //If the heuristic has the save value then we randomly choose one of the two moves
                    if((move.getValue()) == minMove.getValue())
                    {
                        if(r.nextInt(2) == 0)
                        {
                            minMove.setStart(child.getLastMove().getStart());
                            minMove.setValue(move.getValue());
                        }
                    }
                    else
                    {
                        minMove.setStart(child.getLastMove().getStart());
                        minMove.setValue(move.getValue());
                    }
                }
                uniqueBoards.add(child);
            }
            //And for each child min is called, on a lower depth
            
        }
        return minMove;
    }

    Move calcTurnMin(Board board, int depth, Dice dice) {
        if(board.isTerminal() || (depth == 2))
        {
            return new Move(board.getLastPlayer(), board.getLastMove().getStart(), board.getLastMove().getTarget(), board.evaluate());
        }

        Random r = new Random();

        ArrayList<Board> children = board.getChildren(Board.B, dice);
        Move minMove = new Move(Integer.MAX_VALUE);
        for(Board child: children)
        {
            if(uniqueBoards.contains(child)) {
                continue;
            } else {
                Move move = calcTurnMin(child, depth + 1, dice);
                //The child-move with the greatest value is selected and returned by max
                if(move.getValue() <= minMove.getValue())
                {
                    //If the heuristic has the save value then we randomly choose one of the two moves
                    if((move.getValue()) == minMove.getValue())
                    {
                        if(r.nextInt(2) == 0)
                        {
                            minMove.setStart(child.getLastMove().getStart());
                            minMove.setValue(move.getValue());
                        }
                    }
                    else
                    {
                        minMove.setStart(child.getLastMove().getStart());
                        minMove.setValue(move.getValue());
                    }
                }
                uniqueBoards.add(child);
            }
            //And for each child min is called, on a lower depth
            
        }
        return minMove;
    }

    Move calcTurnDoubleMax(Board board, int depth, Dice dice) {
        if(board.isTerminal() || (depth == 4))
        {
            return new Move(board.getLastPlayer(), board.getLastMove().getStart(), board.getLastMove().getTarget(), board.evaluate());
        }

        Random r = new Random();

        ArrayList<Board> children = board.getChildren(Board.W, dice);
        Move maxMove = new Move(Integer.MIN_VALUE);
        for(Board child: children)
        {
            if(uniqueBoards.contains(child)) {
                continue;
            } else {
                Move move = calcTurnDoubleMin(child, depth + 1, dice);
                //The child-move with the greatest value is selected and returned by max
                if(move.getValue() >= maxMove.getValue())
                {
                    //If the heuristic has the save value then we randomly choose one of the two moves
                    if((move.getValue()) == maxMove.getValue())
                    {
                        if(r.nextInt(2) == 0)
                        {
                            maxMove.setStart(child.getLastMove().getStart());
                            maxMove.setValue(move.getValue());
                        }
                    }
                    else
                    {
                        maxMove.setStart(child.getLastMove().getStart());
                        maxMove.setValue(move.getValue());
                    }
                }
                uniqueBoards.add(child);
            }
            //And for each child min is called, on a lower depth
            
        }
        return maxMove;
    }

    Move calcTurnMax(Board board, int depth, Dice dice) {
        if(board.isTerminal() || (depth == 2))
        {
            return new Move(board.getLastPlayer(), board.getLastMove().getStart(), board.getLastMove().getTarget(), board.evaluate());
        }

        Random r = new Random();

        ArrayList<Board> children = board.getChildren(Board.W, dice);
        Move maxMove = new Move(Integer.MIN_VALUE);
        for(Board child: children)
        {
            if(uniqueBoards.contains(child)) {
                continue;
            } else {
                Move move = calcTurnMin(child, depth + 1, dice);
                //The child-move with the greatest value is selected and returned by max
                if(move.getValue() >= maxMove.getValue())
                {
                    //If the heuristic has the save value then we randomly choose one of the two moves
                    if((move.getValue()) == maxMove.getValue())
                    {
                        if(r.nextInt(2) == 0)
                        {
                            maxMove.setStart(child.getLastMove().getStart());
                            maxMove.setValue(move.getValue());
                        }
                    }
                    else
                    {
                        maxMove.setStart(child.getLastMove().getStart());
                        maxMove.setValue(move.getValue());
                    }
                }
                uniqueBoards.add(child);
            }
            //And for each child min is called, on a lower depth
            
        }
        return maxMove;
    }

}
