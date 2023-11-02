/* A class describing a move in the board
 * Every produced child corresponds to a move
 * and we need to keep the moves as well as the states.
 */
public class Move
{
    private int player;
    private int starting_position;
    private int target_position;
    private int value;

    Move()
    {
        this.starting_position = 0;
        this.target_position = 0;
        this.player = -1;
        this.value = 0;
    }

    Move(int player, int starting_position, int target_position)
    {
        this.player = player;
        this.starting_position = starting_position;
        this.target_position = -1;
    }

    Move(int target_position)
    {
        this.starting_position = -1;
        this.target_position = target_position;
    }

    int getStart()
    {
        return this.starting_position;
    }

    int getTarget()
    {
        return this.target_position;
    }

    int getPlayer()
    {
        return this.player;
    }

    int getValue()
    {
        return this.value;
    }

    void setStart(int starting_position)
    {
        this.starting_position = starting_position;
    }

    void setTarget(int target_position)
    {
        this.target_position = target_position;
    }

    void setValue(int value)
    {
        this.value = value;
    }
}
