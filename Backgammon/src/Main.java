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
        int[] curr_roll = new int[2];
        String user_input;
        Move current_move;
        Dice dice = new Dice();

        while(!board.isTerminal()) {
            switch(board.getLastPlayer()) {
                //If White played last, then Black plays now
                case Board.W:
                    board.setLastPlayer(Board.B);
                    System.out.println("Black plays.");
                    curr_roll = dice.roll();
                    if(dice.isDouble()) {
                        System.out.println("You are in big trouble! Your opponent got " + curr_roll[0] + "s!");
                        board.print(curr_roll[0], curr_roll[1]);
                        for (int i =1; i < 5; i++) {
                            //current_move = NEW MOVE MINIMAX
                            //BOARD.MAKEMOVE(MINIMAX_MOVE)
                            System.out.println("Your Opponent played tile " + "(current_move.getStart() - 1)" + "to " + "(current_move.getTarget() - 1)");
                            board.print(curr_roll[0], curr_roll[1]);





                            //TEST FOR 1V1

                            if (board.isGraveyard()) { //Do we have pieces in our grave?
                                System.out.println("Your opponent has pieces in the Graveyard! They have to get them out before making any other move.");
                                if (!board.isValidMove(1, 1 + curr_roll[0])) {
                                    System.out.println("No valid moves! Turn skipped.");
                                    break;
                                } else {
                                    System.out.println("Placed a piece on position " + (25 - curr_roll[0]) + ".");
                                    current_move = new Move(board.getLastPlayer(), 1, 1 + curr_roll[0] * board.getLastPlayer());
                                    board.makeMove(current_move.getStart(), curr_roll[0]);
                                    board.print(curr_roll[0], curr_roll[1]);
                                }
                            } else { //We do not have pieces in our grave
                                System.out.print("Move " + (i) + ": " + "Moving a piece from point: ");
                                user_input = myScanner.nextLine();  // Read user input
                                int starting_move = Integer.parseInt(user_input) + 1;

                                //Input Validation
                                while(!board.isValidMove(starting_move, starting_move + curr_roll[0] * board.getLastPlayer())) {
                                    System.out.println("The move entered is invalid! Please try again.");
                                    System.out.print("Move " + (i) + ": " + "Moving a piece from point: ");
                                    user_input = myScanner.nextLine();  // Read user input
                                    starting_move = Integer.parseInt(user_input) + 1;
                                }
                                current_move = new Move(board.getLastPlayer(), starting_move, starting_move + curr_roll[0] * board.getLastPlayer());
                                board.makeMove(current_move.getStart(), curr_roll[0]);
                                board.print(curr_roll[0], curr_roll[1]);
                            }
                        }
                    } else {
                        board.print(curr_roll[0], curr_roll[1]);
                        System.out.print("Please select which dice Black plays (press 1 for dice 1, or 2 for dice 2): ");
                        user_input = myScanner.nextLine();  // Read user input
                        int dice_selected = Integer.parseInt(user_input);
                        if (dice_selected == 1) {
                            for (int i = 0; i < 2; i++) {
                                if (board.isGraveyard()) {
                                    System.out.println("Your opponent has pieces in the Graveyard! They have to get them out before making any other move.");
                                    if (!board.isValidMove(Board.GRAVE_B, Board.GRAVE_B + curr_roll[i] * board.getLastPlayer())) {
                                        System.out.println("This dice is not a valid move! Dice skipped.");
                                    } else {
                                        System.out.println("Placed a piece on position " + (25 - curr_roll[i]) + ".");
                                        current_move = new Move(board.getLastPlayer(), 26, 26 + curr_roll[i] * board.getLastPlayer());
                                        board.makeMove(current_move.getStart(), curr_roll[i]);
                                        board.print(curr_roll[0], curr_roll[1]);
                                    }
                                } else {
                                    System.out.print("Move " + (i+1) + " (Dice is a "+ curr_roll[i] + "): " + "I wish to move a piece from point: ");
                                    user_input = myScanner.nextLine();  // Read user input
                                    int starting_move = Integer.parseInt(user_input) + 1;

                                    //Input Validation
                                    while(!board.isValidMove(starting_move, starting_move + curr_roll[i] * board.getLastPlayer())) {
                                        System.out.println("The move you entered is invalid! Please try again.");
                                        System.out.print("Move " + (i+1) + " (Dice is a "+ curr_roll[i] + "): " + "I wish to move a piece from point: ");
                                        user_input = myScanner.nextLine();  // Read user input
                                        starting_move = Integer.parseInt(user_input) + 1;
                                    }
                                    current_move = new Move(board.getLastPlayer(), starting_move, starting_move + curr_roll[i] * board.getLastPlayer());
                                    board.makeMove(current_move.getStart(), curr_roll[i]);
                                    board.print(curr_roll[0], curr_roll[1]);
                                }
                            }
                        } else if (dice_selected == 2){
                            for (int j = 1; j > -1; j--) {
                                if (board.isGraveyard()) {
                                    System.out.println("Your opponent has pieces in the Graveyard! They have to get them out before making any other move.");
                                    if (!board.isValidMove(Board.GRAVE_B, Board.GRAVE_B + curr_roll[j] * board.getLastPlayer())) {
                                        System.out.println("This dice is not a valid move! Dice skipped.");
                                    } else {
                                        System.out.println("Placed a piece on position " + (25 - curr_roll[j]) + ".");
                                        current_move = new Move(board.getLastPlayer(), 26, 26 + curr_roll[j] * board.getLastPlayer());
                                        board.makeMove(current_move.getStart(), curr_roll[j]);
                                        board.print(curr_roll[0], curr_roll[1]);
                                    }
                                } else {
                                    System.out.print("Move " + (2-j) + " (Dice is a "+ curr_roll[j] + "): " + "I wish to move a piece from point: ");
                                    user_input = myScanner.nextLine();  // Read user input
                                    int starting_move = Integer.parseInt(user_input) + 1;

                                    //Input Validation
                                    while(!board.isValidMove(starting_move, starting_move + curr_roll[j] * board.getLastPlayer())) {
                                        System.out.println("The move you entered is invalid! Please try again.");
                                        System.out.print("Move " + (2-j) + " (Dice is a "+ curr_roll[j] + "): " + "I wish to move a piece from point: ");
                                        user_input = myScanner.nextLine();  // Read user input
                                        starting_move = Integer.parseInt(user_input) + 1;
                                    }
                                    current_move = new Move(board.getLastPlayer(), starting_move, starting_move + curr_roll[j] * board.getLastPlayer());
                                    board.makeMove(current_move.getStart(), curr_roll[j]);
                                    board.print(curr_roll[0], curr_roll[1]);
                                }
                            }
                        }
                        
                    }
                    break;
                //If Black played last, then White plays now
                case Board.B:
                    board.setLastPlayer(Board.W);
                    System.out.print("White plays. Press Enter to roll the dice: ");
                    user_input = myScanner.nextLine();  // Read user input
                    curr_roll = dice.roll();
                    if(dice.isDouble()) {
                        System.out.println("DOUBLE TROUBLE! YOU HAVE " + curr_roll[0] + "s");
                        board.print(curr_roll[0], curr_roll[1]);
                        for (int i =1; i < 5; i++) {
                            if (board.isGraveyard()) { //Do we have pieces in our grave?
                                System.out.println("You have pieces in the Graveyard! You have to get them out before making any other move.");
                                if (!board.isValidMove(1, 1 + curr_roll[0])) {
                                    System.out.println("You have no valid moves! Turn skipped.");
                                    break;
                                } else {
                                    System.out.println("Placed a piece on position " + curr_roll[0] + ".");
                                    current_move = new Move(board.getLastPlayer(), 1, 1 + curr_roll[0] * board.getLastPlayer());
                                    board.makeMove(current_move.getStart(), curr_roll[0]);
                                    board.print(curr_roll[0], curr_roll[1]);
                                }
                            } else { //We do not have pieces in our grave
                                System.out.print("Move " + (i) + ": " + "I wish to move a piece from point: ");
                                user_input = myScanner.nextLine();  // Read user input
                                int starting_move = Integer.parseInt(user_input) + 1;

                                //Input Validation
                                while(!board.isValidMove(starting_move, starting_move + curr_roll[0] * board.getLastPlayer())) {
                                    System.out.println("The move you entered is invalid! Please try again.");
                                    System.out.print("Move " + (i) + ": " + "I wish to move a piece from point: ");
                                    user_input = myScanner.nextLine();  // Read user input
                                    starting_move = Integer.parseInt(user_input) + 1;
                                }
                                current_move = new Move(board.getLastPlayer(), starting_move, starting_move + curr_roll[0] * board.getLastPlayer());
                                board.makeMove(current_move.getStart(), curr_roll[0]);
                                board.print(curr_roll[0], curr_roll[1]);
                            }
                            
                        }
                    } else {
                        board.print(curr_roll[0], curr_roll[1]);
                        System.out.print("Please select which dice you wanna play (press 1 for dice 1, or 2 for dice 2): ");
                        user_input = myScanner.nextLine();  // Read user input
                        int dice_selected = Integer.parseInt(user_input);
                        if (dice_selected == 1) {
                            for (int i = 0; i < 2; i++) {
                                if (board.isGraveyard()) {
                                    System.out.println("You have pieces in the Graveyard! You have to get them out before making any other move.");
                                    if (!board.isValidMove(1, 1 + curr_roll[i])) {
                                        System.out.println("This dice is not a valid move! Dice skipped.");
                                    } else {
                                        System.out.println("Placed a piece on position " + curr_roll[0] + ".");
                                        current_move = new Move(board.getLastPlayer(), 1, 1 + curr_roll[i] * board.getLastPlayer());
                                        board.makeMove(current_move.getStart(), curr_roll[i]);
                                        board.print(curr_roll[0], curr_roll[1]);
                                    }
                                } else {
                                    System.out.print("Move " + (i+1) + " (Dice is a "+ curr_roll[i] + "): " + "I wish to move a piece from point: ");
                                    user_input = myScanner.nextLine();  // Read user input
                                    int starting_move = Integer.parseInt(user_input) + 1;

                                    //Input Validation
                                    while(!board.isValidMove(starting_move, starting_move + curr_roll[i] * board.getLastPlayer())) {
                                        System.out.println("The move you entered is invalid! Please try again.");
                                        System.out.print("Move " + (i+1) + " (Dice is a "+ curr_roll[i] + "): " + "I wish to move a piece from point: ");
                                        user_input = myScanner.nextLine();  // Read user input
                                        starting_move = Integer.parseInt(user_input) + 1;
                                    }
                                    current_move = new Move(board.getLastPlayer(), starting_move, starting_move + curr_roll[i] * board.getLastPlayer());
                                    board.makeMove(current_move.getStart(), curr_roll[i]);
                                    board.print(curr_roll[0], curr_roll[1]);
                                }
                            }
                        } else if (dice_selected == 2){
                            for (int j = 1; j > -1; j--) {
                                if (board.isGraveyard()) {
                                    System.out.println("You have pieces in the Graveyard! You have to get them out before making any other move.");
                                    if (!board.isValidMove(1, 1 + curr_roll[j] * board.getLastPlayer())) {
                                        System.out.println("This dice is not a valid move! Dice skipped.");
                                    } else {
                                        System.out.println("Placed a piece on position " + curr_roll[j] + ".");
                                        current_move = new Move(board.getLastPlayer(), 1, 1 + curr_roll[j] * board.getLastPlayer());
                                        board.makeMove(current_move.getStart(), curr_roll[j]);
                                        board.print(curr_roll[0], curr_roll[1]);
                                    }
                                } else {
                                    System.out.print("Move " + (2-j) + " (Dice is a "+ curr_roll[j] + "): " + "I wish to move a piece from point: ");
                                    user_input = myScanner.nextLine();  // Read user input
                                    int starting_move = Integer.parseInt(user_input) + 1;

                                    //Input Validation
                                    while(!board.isValidMove(starting_move, starting_move + curr_roll[j] * board.getLastPlayer())) {
                                        System.out.println("The move you entered is invalid! Please try again.");
                                        System.out.print("Move " + (2-j) + " (Dice is a "+ curr_roll[j] + "): " + "I wish to move a piece from point: ");
                                        user_input = myScanner.nextLine();  // Read user input
                                        starting_move = Integer.parseInt(user_input) + 1;
                                    }
                                    current_move = new Move(board.getLastPlayer(), starting_move, starting_move + curr_roll[j] * board.getLastPlayer());
                                    board.makeMove(current_move.getStart(), curr_roll[j]);
                                    board.print(curr_roll[0], curr_roll[1]);
                                }
                            }
                        }
                        
                    }
                    
                    break;
                default:
                    break;
            }
        }
        if (board.getGameBoard()[Board.ESCAPE_W] > board.getGameBoard()[Board.ESCAPE_B]) {
            System.out.println("\n\n\n\nTHE WINNER IS: WHITE! \nCONGRATULATIONS!");
        } else {
            System.out.println("\n\n\n\nTHE WINNER IS: Black! \nBetter luck next time :)");
        }
        
        
        myScanner.close();
    }
}
