import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        //We create the players and the board
        //MaxDepth for the MiniMax algorithm is set to 2; feel free to change the values
        Player playerB = new Player(2, Board.B);
        Player playerW = new Player(2, Board.W);
        Scanner myScanner = new Scanner(System.in);
        Dice dice = new Dice();
        Turn currTurn = new Turn();
        Board board = new Board(dice);
        String user_input;
        Move current_move;
        int turn_count = 0;
        
        System.out.print("Select game mode (type \"player\" to play against the bot, or press Enter to watch the bots fight it out): ");
        user_input = myScanner.nextLine();

        if (user_input.matches("player")) { // White is player
            while(!board.isTerminal()) {
                switch(board.getLastPlayer()) {
                    case Board.W:

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
                                    if (user_input.matches("skip")) {
                                        break;
                                    }
                                    int starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1; // If string has at least 1 digit, parse into int. if not, just put 1 (it's regarded as an invalid move anyway)

                                    //Input Validation
                                    while(!board.isValidMove(starting_move, starting_move + dice.getDice()[0] * board.getLastPlayer())) {
                                        System.out.println("The move you entered is invalid! Please try again.");
                                        System.out.print("Move " + (i) + ": " + "I wish to move a piece from point: ");
                                        user_input = myScanner.nextLine();  // Read user input
                                        if (user_input.matches("skip")) {
                                            break;
                                        }
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
                            if (user_input.matches("skip")) {
                                break;
                            }
                            int dice_selected = user_input.matches("2") ? Integer.parseInt(user_input): 1;
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
                                        if (user_input.matches("skip")) {
                                            break;
                                        }
                                        int starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1; // If string has at least 1 digit, parse into int. if not, just put 1 (it's regarded as an invalid move anyway)

                                        //Input Validation
                                        while(!board.isValidMove(starting_move, starting_move + dice.getDice()[i] * board.getLastPlayer())) {
                                            System.out.println("The move you entered is invalid! Please try again.");
                                            System.out.print("Move " + (i+1) + " (Dice is a "+ dice.getDice()[i] + "): " + "I wish to move a piece from point: ");
                                            user_input = myScanner.nextLine();  // Read user input
                                            if (user_input.matches("skip")) {
                                                break;
                                            }
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
                                        if (user_input.matches("skip")) {
                                            break;
                                        }
                                        int starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1;

                                        //Input Validation
                                        while(!board.isValidMove(starting_move, starting_move + dice.getDice()[j] * board.getLastPlayer())) {
                                            System.out.println("The move you entered is invalid! Please try again.");
                                            System.out.print("Move " + (2-j) + " (Dice is a "+ dice.getDice()[j] + "): " + "I wish to move a piece from point: ");
                                            user_input = myScanner.nextLine();  // Read user input
                                            if (user_input.matches("skip")) {
                                                break;
                                            }
                                            starting_move = user_input.matches("\\d+") ? (Integer.parseInt(user_input) + 1) : 1; // If string has at least 1 digit, parse into int. if not, just put 1 (it's regarded as an invalid move anyway)
                                        }
                                        current_move = new Move(board.getLastPlayer(), starting_move, starting_move + dice.getDice()[j] * board.getLastPlayer(), board.evaluate());
                                        board.makeMove(current_move.getStart(), dice.getDice()[j]);
                                        board.print(dice);
                                    }
                                }
                            }
                            
                        }
                        if(board.isTerminal()) break; // For some reason without this clause white terminal state (15 pieces in white) does not return true. IDK how to fix it, patchwork until I can. Hopefully.
                        board.setLastPlayer(Board.B);
                        
                            
                    case Board.B:
                        System.out.println("Black plays.");
                        dice.roll();
                        board.print(dice);
                        currTurn = playerB.expectiMiniMax(board, dice);
                        board.makeTurn(currTurn);
                        turn_count++;
                        board.print(dice);
                        System.out.println("TURNS PLAYED: " + turn_count);
                    default:
                        break;
                }
            }
        } else { // White is bot
            while(!board.isTerminal()) {
                switch(board.getLastPlayer()) {
                    case Board.W:
                        System.out.println("White plays.");
                        dice.roll();
                        board.print(dice);
                        currTurn = playerW.expectiMiniMax(board, dice);
                        board.makeTurn(currTurn);
                        turn_count++;
                        board.print(dice);
                        System.out.println("TURNS PLAYED: " + turn_count);
                        if(board.isTerminal()) break; // For some reason without this clause white terminal state (15 pieces in white) does not return true. IDK how to fix it, patchwork until I can. Hopefully.

                    case Board.B:
                        System.out.println("Black plays.");
                        dice.roll();
                        board.print(dice);
                        currTurn = playerB.expectiMiniMax(board, dice);
                        board.makeTurn(currTurn);
                        turn_count++;
                        board.print(dice);
                        System.out.println("TURNS PLAYED: " + turn_count);
                    default:
                        break;
                }
            }
            
        }
        if (Math.abs(board.getGameBoard()[Board.ESCAPE_W]) > Math.abs(board.getGameBoard()[Board.ESCAPE_B])) {
            System.out.println("\n\n\n\nTHE WINNER IS: WHITE! \nCONGRATULATIONS!");
        } else {
            System.out.println("\n\n\n\nTHE WINNER IS: Black! \nBetter luck next time :)");
        }
        
        myScanner.close();
    }

    static void userPlay(Board board, Dice dice, Turn currTurn, Scanner myScanner) {
        Move current_move;
        System.out.print("White plays. Press Enter to roll the dice: ");
            String user_input = myScanner.nextLine();  // Give the user the impression that he is rolling the dice.
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
    }
}
