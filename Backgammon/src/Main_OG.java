public class Main
{
    public static void main(String[] args)
    {
        //We create the players and the board
        //MaxDepth for the MiniMax algorithm is set to 2; feel free to change the values
        Player playerX = new Player(2, Board.W);
        Player playerO = new Player(2, Board.B);
        Board board = new Board();
        board.print();

        //Put this out of comments for the O to play first
        //board.setLastLetterPlayed(Board.X);

        while(!board.isTerminal())
        {
            System.out.println();
            switch(board.getLastPlayer())
            {
                //If X played last, then Ο plays now
                case Board.W:
                    System.out.println("O plays");
                    Move moveO = playerO.MiniMax(board);
                    board.makeMove(moveO.getCol(), Board.B);
                    break;
                //If O played last, then X plays now
                case Board.B:
                    System.out.println("X plays");
                    Move moveX = playerX.MiniMax(board);
                    board.makeMove(moveX.getCol(), Board.W);
                    break;
                default:
                    break;
            }
            board.print();
        }
    }
}
