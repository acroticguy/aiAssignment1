import java.util.ArrayList;

public class Turn {
    private ArrayList<Move> moveset;

    Turn() {
        this.moveset = new ArrayList<>();
    }

    Turn(Move move) {
        this.moveset = new ArrayList<>();
        this.moveset.add(move);
    }

    Turn(Turn turn) {
        this.moveset = new ArrayList<>(turn.getTurn().size());
        for (int i = 0; i < turn.getTurn().size(); i++) {
            this.moveset.add(i, turn.getTurn().get(i));
        }
    }

    void addMove(Move move) {
        this.moveset.add(move);
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
        return this.moveset.get(this.moveset.size() - 1).getScore();
    }
}
