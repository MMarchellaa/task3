import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void menu(String[] args) {
        System.out.println("Select the option: ");
        for (int i = 0; i < args.length; i++)
            System.out.println((i + 1) + " - " + args[i]);
        System.out.println("0 - Exit");
        System.out.println("? - Help");
        System.out.print("Your move: ");
    }

    public static boolean checkingArgs(String[] args) {
        if (args.length % 2 == 0 || args.length < 3) {
            System.out.println("\nWrong number of arguments. Should be odd and more than 2.\nExample: Rock Paper Scissors\n");
            return false;
        }
        for (int i = 0; i < args.length; i++) {
            for (int j = i + 1; j < args.length; j++) {
                if (Objects.equals(args[i], args[j])) {
                    System.out.println("\nPassed parameters shouldn't be repeated. \nExample: Rock Paper Scissors\n");
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        if (!checkingArgs(args)) return;
        Scanner in = new Scanner(System.in);
        SecureRandom random = new SecureRandom();
        KeyAndHMAC keyAndHMAC = new KeyAndHMAC();
        Table table = new Table();
        Algorithm algorithm = new Algorithm();
        while (true) {
            int rand = random.nextInt(args.length);
            byte[] seed = keyAndHMAC.generateKeyAndHMAC(args, rand);
            String opponentMove = args[rand];
            String playerMove;
            int winner;
            while (true) {
                menu(args);
                playerMove = in.next();
                while (!playerMove.equals("?") && !playerMove.matches(("[-+]?\\d+"))) {
                    System.out.print("\nThat is not an available option. Please enter ? to call the help table or a number from 0 to " + args.length + " to select the move\n\n");
                    menu(args);
                    playerMove = in.next();
                }
                if (playerMove.equals("?")) {
                    table.createTable(args);
                } else if (Integer.parseInt(playerMove) > 0 && Integer.parseInt(playerMove) <= args.length) {
                    System.out.println("Your move: " + args[Integer.parseInt(playerMove) - 1]);
                    System.out.println("Computer move: " + opponentMove);
                    break;
                } else if (Integer.parseInt(playerMove) == 0) {
                    System.out.print("See you later!\nGoodbye!\n");
                    return;
                } else {
                    System.out.println("\nPlease enter ? to call the help table or a number from 0 to " + args.length + " to select the move\n");
                }
            }
            winner = algorithm.whoWin(Integer.parseInt(playerMove) - 1, rand, args.length / 2);
            if (winner == 1) {
                System.out.println("You win!");
            } else if (winner == -1) {
                System.out.println("Computer wins!");
            } else {
                System.out.println("It's a draw!");
            }
            System.out.println("HMAC key: " + HexBin.encode(seed) + "\n");
        }
    }
}