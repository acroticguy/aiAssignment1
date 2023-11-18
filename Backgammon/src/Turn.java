import java.util.ArrayList;

public class Turn {
    private ArrayList<Move> moveset;
    private Dice dice = new Dice();
    private double score = 0;

    Turn() {
        this.moveset = new ArrayList<>();
    }

    Turn(Move move) {
        this.moveset = new ArrayList<>();
        this.moveset.add(move);
        score = this.moveset.get(this.moveset.size() - 1).getScore();
    }

    Turn(Turn turn) {
        this.moveset = new ArrayList<>(turn.getTurn().size());
        for (int i = 0; i < turn.getTurn().size(); i++) {
            this.moveset.add(i, new Move(turn.getTurn().get(i)));
        }
        if (this.moveset.size() > 0) {
            this.score = this.moveset.get(this.moveset.size() - 1).getScore();
        }
        this.dice = turn.getDice();
    }

    void addMove(Move move) {
        this.moveset.add(move);
        score = this.moveset.get(this.moveset.size() - 1).getScore();
    }

    void clearTurn() {
        this.moveset.clear();
    }

    ArrayList<Move> getTurn() {
        return this.moveset;
    }

    String printTurn() {
        String str = "";
        for (Move move: this.moveset) {
            str += "(" + move.getStart() + "->" + move.getTarget() + ")";
        }
        return str;
    }

    double getScore() {
        return this.score;
    }

    Dice getDice() {
        return this.dice;
    }

    void setScore(double score) {
        this.score = score;
    }
}
