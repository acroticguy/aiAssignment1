import java.util.ArrayList;
import java.util.Random;

public class Player
{
    private int maxDepth;
    private int playerLetter;
    //private ArrayList<Move> moves = new ArrayList<>();

    Player(int maxDepth, int playerLetter)
    {
        this.maxDepth = maxDepth;
        this.playerLetter = playerLetter;
    }

    Turn expectiMiniMax(Board board, Dice dice) {
        if (playerLetter == Board.W) {
            return max(new Board(board), 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        } else {
            return min(new Board(board), 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
    }

    // The max and min functions are called one after another until a max depth is reached or we have a terminal board.
    // We create a tree using backtracking DFS.
    Turn max(Board board, int depth, double alpha, double beta)
    {
        /* If MAX is called on a state that is terminal or after a maximum depth is reached,
         * then a heuristic is calculated on the state and the move returned.
         */
        Turn maxTurn = new Turn(new Move(Double.NEGATIVE_INFINITY));

        ArrayList<Board> children = new ArrayList<>();
        Random r = new Random();

        if(board.isTerminal() || (depth == this.maxDepth))
        {
            return new Turn(board.getLastTurn());
        }

        children = board.getChildren(Board.W);

        for (Board child: children) {
            Turn result = chance(child, depth + 1, alpha, beta);
                //The child-move with the greatest value is selected and returned by max
                if(result.getScore() >= maxTurn.getScore())
                {
                    //If the heuristic has the save value then we randomly choose one of the two moves
                    if(result.getScore() == maxTurn.getScore()) {

                        if(r.nextInt(2) == 0)
                        {
                            child.updateTurnScore(result);
                            maxTurn = new Turn(child.getLastTurn());
                        }
                    } else {
                        child.updateTurnScore(result);
                        maxTurn = new Turn(child.getLastTurn());
                    }
                }
                alpha = Math.max(alpha, maxTurn.getScore());
                if (alpha >= beta) {
                    break; // Beta cutoff
                }
            }
        System.out.println("Turn selected: " + maxTurn.printTurn() + " with value " + maxTurn.getScore());
        return maxTurn;
    }

    //Min works similarly to max
    Turn min(Board board, int depth, double alpha, double beta)
    {
        Turn minTurn = new Turn(new Move(Double.POSITIVE_INFINITY));
        ArrayList<Board> children = new ArrayList<>();

        Random r = new Random();

        if(board.isTerminal() || (depth == this.maxDepth))
        {
            return new Turn(board.getLastTurn());
        }

        // FOR DEBUG                               System.out.println("Entering MIN. Getting children from board with player " + board.getLastPlayer() + "then calling CHANCE. Depth: " + depth);

        children = board.getChildren(Board.B);

        for (Board child: children) {
                Turn result = chance(child, depth + 1, alpha, beta);
                if((result.getScore()) <= minTurn.getScore()) {
                    //If the heuristic has the same value then we randomly choose one of the two moves
                    if((result.getScore()) == minTurn.getScore())
                    {
                        if(r.nextInt(2) == 0)
                        {
                            child.updateTurnScore(result);
                            minTurn = new Turn(child.getLastTurn());
                        }
                    }
                    else
                    {
                        child.updateTurnScore(result);
                        minTurn = new Turn(child.getLastTurn());
                    }
                }
                beta = Math.min(beta, minTurn.getScore());
                if (alpha >= beta) {
                    break; // Alpha cutoff
                }
            }
        System.out.println("Turn selected: " + minTurn.printTurn() + " with final score: " + minTurn.getScore());
        return minTurn;
    }

    Turn chance(Board board, int depth, double alpha, double beta) {
        ArrayList<Board> children = new ArrayList<>();
        Turn result;
        double totalScore = 0.0;

        for (int i = 1; i < 7; i++) {
            for (int j = i; j < 7; j++) {
                Board temp = new Board(board);
                temp.setDice(new Dice(i, j));
                if (board.getLastPlayer() == Board.W) { // After a turn is calculated, the board switches the lastPlayer variable
                    result = max(temp, depth, alpha, beta);
                } else {
                    result = min(temp, depth, alpha, beta);
                }
                totalScore += result.getScore() * temp.getDice().prob();
                children.add(temp);
            }
        }
        return new Turn(new Move(totalScore));
    }

}