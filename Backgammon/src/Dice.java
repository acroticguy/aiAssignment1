// A class describing the dice functionality.
import java.lang.Math;

public class Dice {

    private int[] dice;

    Dice() {
        this.dice = new int[2];
    }

    Dice(int dice_1, int dice_2) {
        this.dice = new int[] {dice_1, dice_2};
    }

    void roll(){
        for (int i = 0; i < 2; i++) {
            this.dice[i] = (int) (Math.random() * 6 + 1);
        }
    }

    boolean isDouble(){
        return this.dice[0] == this.dice[1];
    }

    int[] getDice() {
        return this.dice;
    }
}
