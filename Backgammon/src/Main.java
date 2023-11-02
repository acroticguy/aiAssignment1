import java.lang.Math;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        //We create the players and the board
        //MaxDepth for the MiniMax algorithm is set to 2; feel free to change the values
        // Player playerX = new Player(2, Board.W);
        // Player playerO = new Player(2, Board.B);
        Scanner myScanner = new Scanner(System.in);
        Board board = new Board();
        int dice_1, dice_2;
        String user_input;
        Move current_move;


        switch(board.getLastPlayer())
            {
                //If X played last, then Ο plays now
                case Board.W:
                    board.setLastPlayer(Board.B);
                    System.out.println("Black plays.");
                    dice_1 = (int) (Math.random() * 6 + 1);
                    dice_2 = (int) (Math.random() * 6 + 1);
                    if(dice_1 == dice_2) {
                        System.out.println("You are in big trouble! Your opponent got " + dice_1 + "s!");
                        board.printB(dice_1, dice_2);
                        for (int i =0; i < 4; i++) {
                            //current_move = new Move(board.getLastPlayer(), starting_move, starting_move + dice_1 * board.getLastPlayer());
                            //board.makeMove(current_move.getStart(), dice_1, current_move.getPlayer());
                            System.out.println("Your Opponent played tile " + "(current_move.getStart() - 1)" + "to " + "(current_move.getTarget() - 1)");
                            board.printB(dice_1, dice_2);
                        }
                    } else {
                        board.printB(dice_1, dice_2);
                        
                        //current_move = new Move(board.getLastPlayer(), starting_move, starting_move + dice_1 * board.getLastPlayer());
                        //board.makeMove(current_move.getStart(), dice_1, current_move.getPlayer());
                        System.out.println("Your Opponent played tile " + "(current_move.getStart() - 1)" + "to " + "(current_move.getTarget() - 1)");
                        board.printB(dice_1, dice_2);

                        //current_move = new Move(board.getLastPlayer(), starting_move, starting_move + dice_2 * board.getLastPlayer());
                        //board.makeMove(current_move.getStart(), dice_2, current_move.getPlayer());
                        System.out.println("Your Opponent played tile " + "(current_move.getStart() - 1)" + "to " + "(current_move.getTarget() - 1)");
                        board.printB(dice_1, dice_2);
                    }
                    //current_move = playerO.MiniMax(board);
                    //board.makeMove(moveO.getCol(), Board.B);
                    break;
                //If O played last, then X plays now
                case Board.B:
                    board.setLastPlayer(Board.W);
                    System.out.print("White plays. Press Enter to roll the dice: ");
                    user_input = myScanner.nextLine();  // Read user input
                    dice_1 = (int) (Math.random() * 6 + 1);
                    dice_2 = (int) (Math.random() * 6 + 1);
                    if(dice_1 == dice_2) {
                        System.out.println("DOUBLE TROUBLE! YOU HAVE " + dice_1 + "s");
                        board.printB(dice_1, dice_2);
                        for (int i =0; i < 4; i++) {
                            System.out.print("Move " + (i+1) + ": " + "I wish to move a piece from point: ");
                            user_input = myScanner.nextLine();  // Read user input
                            int starting_move = Integer.parseInt(user_input) + 1;

                            //Input Validation
                            while(!board.isValidMove(starting_move, starting_move + dice_1, Board.W)) {
                                System.out.println("The move you entered is invalid! Please try again.");
                                System.out.print("Move " + (i+1) + ": " + "I wish to move a piece from point: ");
                                user_input = myScanner.nextLine();  // Read user input
                                starting_move = Integer.parseInt(user_input) + 1;
                            }
                            current_move = new Move(board.getLastPlayer(), starting_move, starting_move + dice_1 * board.getLastPlayer());
                            board.makeMove(current_move.getStart(), dice_1, current_move.getPlayer());
                            board.printB(dice_1, dice_2);
                        }
                    } else {
                        board.printB(dice_1, dice_2);
                        System.out.print("Move 1 (First Dice is a "+ dice_1 + "): " + "I wish to move a piece from point: ");
                        user_input = myScanner.nextLine();  // Read user input
                        int starting_move = Integer.parseInt(user_input) + 1;

                        //Input Validation
                        while(!board.isValidMove(starting_move, starting_move + dice_1, Board.W)) {
                            System.out.println("The move you entered is invalid! Please try again.");
                            System.out.print("Move 1 (First Dice is a "+ dice_1 + "): " + "I wish to move a piece from point: ");
                            user_input = myScanner.nextLine();  // Read user input
                            starting_move = Integer.parseInt(user_input) + 1;
                        }
                        current_move = new Move(board.getLastPlayer(), starting_move, starting_move + dice_1 * board.getLastPlayer());
                        board.makeMove(current_move.getStart(), dice_1, current_move.getPlayer());
                        board.printB(dice_1, dice_2);

                        System.out.print("Move 2 (Second Dice is a "+ dice_2 + "): " + "I wish to move a piece from point: ");
                        user_input = myScanner.nextLine();  // Read user input
                        starting_move = Integer.parseInt(user_input) + 1;

                        //Input Validation
                        while(!board.isValidMove(starting_move, starting_move + dice_2, Board.W)) {
                            System.out.println("The move you entered is invalid! Please try again.");
                            System.out.print("Move 2 (Second Dice is a "+ dice_2 + "): " + "I wish to move a piece from point: ");
                            user_input = myScanner.nextLine();  // Read user input
                            starting_move = Integer.parseInt(user_input) + 1;
                        }
                        current_move = new Move(board.getLastPlayer(), starting_move, starting_move + dice_2 * board.getLastPlayer());
                        board.makeMove(current_move.getStart(), dice_2, current_move.getPlayer());
                        board.printB(dice_1, dice_2);
                    }
                    
                    break;
                default:
                    break;
            }
        myScanner.close();
    }
}
