// A class describing the dice functionality.
import java.lang.Math;

public class Dice {

    private int[] dice;

    Dice() {
        this.dice = new int[2];
    }

    int[] roll(){
        for (int i = 0; i < 2; i++) {
            this.dice[i] = (int) (Math.random() * 6 + 1);
        }
        return this.dice;
    }

    boolean isDouble(){
        return this.dice[0] == this.dice[1];
    }
}
