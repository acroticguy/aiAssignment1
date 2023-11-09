/* A class describing a move in the board
 * Every produced child corresponds to a move
 * and we need to keep the moves as well as the states.
 */
public class Move
{
    private int player;
    private int starting_position;
    private int dice;
    private double score;

    Move()
    {
        this.starting_position = 0;
        this.dice = 0;
        this.player = -1;
        this.score = 0;
    }

    Move(int player, int starting_position, int dice, double score)
    {
        this.player = player;
        this.starting_position = starting_position;
        this.dice = dice;
        this.score = score;
    }

    Move(int value)
    {
        this.starting_position = -1;
        this.score = value;
    }

    int getStart()
    {
        return this.starting_position;
    }

    int getDice() {
        return this.dice;
    }

    int getPlayer()
    {
        return this.player;
    }

    int getTarget()
    {
        return starting_position + (this.dice * this.player);
    }

    double getValue()
    {
        return this.score;
    }

    void setStart(int starting_position) {
        this.starting_position = starting_position;
    }
    void setValue(double value)
    {
        this.score = value;
    }
}
