import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        //We create the players and the board
        //MaxDepth for the MiniMax algorithm is set to 2; feel free to change the values
        Player playerB = new Player(2, Board.B);
        // Player playerO = new Player(2, Board.B);
        Scanner myScanner = new Scanner(System.in);
        Dice dice = new Dice();
        Turn currTurn = new Turn();
        Board board = new Board(dice);
        String user_input;
        Move current_move;
        //ArrayList<Move> current_moveset = new ArrayList<>();
        
        while(!board.isTerminal()) {
            switch(board.getLastPlayer()) {
                //If White played last, then Black plays now
                case Board.W:
                    board.setLastPlayer(Board.B);
                    System.out.println("Black plays.");
                    dice.roll();
                    board.print(dice);
                    currTurn = playerB.expectiMiniMax(board, dice);
                    board.makeTurn(currTurn);
                    board.print(dice);

                //If Black played last, then White plays now
                case Board.B:
                    board.setLastPlayer(Board.W);
                    System.out.print("White plays. Press Enter to roll the dice: ");
                    user_input = myScanner.nextLine();  // Give the user the impression that he is rolling the dice.
                    dice.roll();
                    if(dice.isDouble()) {
                        System.out.println("DOUBLE TROUBLE! YOU HAVE " + dice.getDice()[0] + "s");
                        board.print(dice);
                        for (int i =1; i < 5; i++) {
                            if (board.isGraveyard()) { // When we are in graveyard, since it's only 1 move either it's made, or we lose the dice.
                                System.out.println("You have pieces in the Graveyard! You have to get them out before making any other move.");
                                if (!board.isValidMove(Board.GRAVE_W, Board.GRAVE_W + dice.getDice()[0])) {
                                    System.out.println("You have no valid moves! Turn skipped.");
                                    break;
                                } else {
                                    System.out.println("Placed a piece on position " + dice.getDice()[0] + ".");
                                    current_move = new Move(board.getLastPlayer(), Board.GRAVE_W, Board.GRAVE_W + dice.getDice()[0] * board.getLastPlayer(), board.evaluate());
                                    board.makeMove(current_move.getStart(), dice.getDice()[0]);
                                    board.print(dice);
                                }
                            } else { //We do not have pieces in our grave
                                System.out.print("Move " + (i) + ": " + "I wish to move a piece from point: ");
                                user_input = myScanner.nextLine();  // Read user input
                                int starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1; // If string has at least 1 digit, parse into int. if not, just put 1 (it's regarded as an invalid move anyway)

                                //Input Validation
                                while(!board.isValidMove(starting_move, starting_move + dice.getDice()[0] * board.getLastPlayer())) {
                                    System.out.println("The move you entered is invalid! Please try again.");
                                    System.out.print("Move " + (i) + ": " + "I wish to move a piece from point: ");
                                    user_input = myScanner.nextLine();  // Read user input
                                    starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1; // If string has at least 1 digit, parse into int. if not, just put 1 (it's regarded as an invalid move anyway)
                                }
                                board.makeMove(starting_move, dice.getDice()[0]);
                                board.print(dice);
                            }
                            
                        }
                    } else {
                        board.print(dice);
                        System.out.print("Please select which dice you wanna play (press 1 for dice 1, or 2 for dice 2): ");
                        user_input = myScanner.nextLine();  // Read user input
                        int dice_selected = user_input.matches("\\d+") ? Integer.parseInt(user_input): 1;
                        if (dice_selected == 1) {
                            for (int i = 0; i < 2; i++) {
                                if (board.isGraveyard()) { // When we are in graveyard, since it's only 1 move either it's made, or we lose the dice.
                                    System.out.println("You have pieces in the Graveyard! You have to get them out before making any other move.");
                                    if (!board.isValidMove(Board.GRAVE_W, Board.GRAVE_W + dice.getDice()[i])) {
                                        System.out.println("This dice is not a valid move! Dice skipped.");
                                    } else {
                                        System.out.println("Placed a piece on position " + dice.getDice()[0] + ".");
                                        current_move = new Move(board.getLastPlayer(), Board.GRAVE_W, Board.GRAVE_W + dice.getDice()[i] * board.getLastPlayer(), board.evaluate());
                                        board.makeMove(current_move.getStart(), dice.getDice()[i]);
                                        board.print(dice);
                                    }
                                } else {
                                    System.out.print("Move " + (i+1) + " (Dice is a "+ dice.getDice()[i] + "): " + "I wish to move a piece from point: ");
                                    user_input = myScanner.nextLine();  // Read user input
                                    int starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1; // If string has at least 1 digit, parse into int. if not, just put 1 (it's regarded as an invalid move anyway)

                                    //Input Validation
                                    while(!board.isValidMove(starting_move, starting_move + dice.getDice()[i] * board.getLastPlayer())) {
                                        System.out.println("The move you entered is invalid! Please try again.");
                                        System.out.print("Move " + (i+1) + " (Dice is a "+ dice.getDice()[i] + "): " + "I wish to move a piece from point: ");
                                        user_input = myScanner.nextLine();  // Read user input
                                        starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1; // If string has at least 1 digit, parse into int. if not, just put 1 (it's regarded as an invalid move anyway)
                                    }
                                    current_move = new Move(board.getLastPlayer(), starting_move, starting_move + dice.getDice()[i] * board.getLastPlayer(), board.evaluate());
                                    board.makeMove(current_move.getStart(), dice.getDice()[i]);
                                    board.print(dice);
                                }
                            }
                        } else if (dice_selected == 2){
                            for (int j = 1; j > -1; j--) {
                                if (board.isGraveyard()) { // When we are in graveyard, since it's only 1 move either it's made, or we lose the dice.
                                    System.out.println("You have pieces in the Graveyard! You have to get them out before making any other move.");
                                    if (!board.isValidMove(Board.GRAVE_W, Board.GRAVE_W + dice.getDice()[j] * board.getLastPlayer())) {
                                        System.out.println("This dice is not a valid move! Dice skipped.");
                                    } else {
                                        System.out.println("Placed a piece on position " + dice.getDice()[j] + ".");
                                        current_move = new Move(board.getLastPlayer(), Board.GRAVE_W, Board.GRAVE_W + dice.getDice()[j] * board.getLastPlayer(), board.evaluate());
                                        board.makeMove(current_move.getStart(), dice.getDice()[j]);
                                        board.print(dice);
                                    }
                                } else {
                                    System.out.print("Move " + (2-j) + " (Dice is a "+ dice.getDice()[j] + "): " + "I wish to move a piece from point: ");
                                    user_input = myScanner.nextLine();  // Read user input
                                    int starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1;

                                    //Input Validation
                                    while(!board.isValidMove(starting_move, starting_move + dice.getDice()[j] * board.getLastPlayer())) {
                                        System.out.println("The move you entered is invalid! Please try again.");
                                        System.out.print("Move " + (2-j) + " (Dice is a "+ dice.getDice()[j] + "): " + "I wish to move a piece from point: ");
                                        user_input = myScanner.nextLine();  // Read user input
                                        starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1; // If string has at least 1 digit, parse into int. if not, just put 1 (it's regarded as an invalid move anyway)
                                    }
                                    current_move = new Move(board.getLastPlayer(), starting_move, starting_move + dice.getDice()[j] * board.getLastPlayer(), board.evaluate());
                                    board.makeMove(current_move.getStart(), dice.getDice()[j]);
                                    board.print(dice);
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
