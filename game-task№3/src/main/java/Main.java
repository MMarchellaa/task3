import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.grid.Border;
import com.inamik.text.tables.grid.Util;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static int algorithm(int playerMove, int comp, int size) {
        if ((Math.abs(playerMove - comp) <= size && playerMove > comp) || (playerMove < comp && Math.abs(playerMove - comp) > size)) {
            return -1;
        } else if (Math.abs(playerMove - comp) == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    public static byte[] keyAndHMAC(String[] args, int rand) throws NoSuchAlgorithmException, InvalidKeyException {
        SecureRandom random = new SecureRandom();
        byte[] seed = random.generateSeed(16);

        SecretKeySpec key = new SecretKeySpec(seed, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);

        System.out.println("HMAC: " + HexBin.encode(mac.doFinal(args[rand].getBytes(StandardCharsets.UTF_8))));
        return seed;
    }

    public static void table(String[] args) {
        GridTable table = GridTable.of(args.length+1, args.length+1);
        for (int i = 1; i <= args.length; i++) {
            for (int k = 1; k <= args.length; k++) {
                if (k == 1) {
                    table.put(i, 0, Cell.of(args[i-1]));
                }
                if (i == 1) {
                    table.put(0, k, Cell.of(args[k-1]));
                }
                int winner = algorithm(i - 1, k - 1, args.length / 2);
                if (winner == 1) {
                    table.put(i, k, Cell.of(args[i - 1]));
                } else if (winner == 0) {
                    table.put(i, k, Cell.of("draw"));
                } else {
                    table.put(i,k,Cell.of(args[k - 1]));
                }
            }
        }
        System.out.println("Table of winners:");
        Util.print(Border.DOUBLE_LINE.apply(table));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        if (args.length % 2 == 0 || args.length < 3) {
            System.out.println("Wrong number of arguments. Should be odd and more than 2.\nExample: Rock Paper Scissors");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            for (int j = i + 1; j < args.length; j++) {
                if (Objects.equals(args[i], args[j])) {
                    System.out.println("Passed parameters shouldn't be repeated. \nExample: Rock Paper Scissors");
                    return;
                }
            }
        }
        Scanner in = new Scanner(System.in);
        while (true) {
            SecureRandom random = new SecureRandom();
            int rand = random.nextInt(args.length);
            byte[] seed = keyAndHMAC(args, rand);
            String opponentMove = args[rand];
            String playerMove;
            int winner;
            while (true) {
                System.out.println("Select the option: ");
                for (int i = 0; i < args.length; i++)
                    System.out.println((i + 1) + " - " + args[i]);
                System.out.println("0 - Exit");
                System.out.println("? - Help");
                System.out.print("Your move: ");
                playerMove = in.next();
                while (!playerMove.equals("?") && !playerMove.matches(("[-+]?\\d+"))) {
                    System.out.print("\nThat is not an available option. Please enter ? to call the help table or a number from 0 to " + args.length + " to select the move\n\n");
                    System.out.println("Select the option: ");
                    for (int i = 0; i < args.length; i++)
                        System.out.println((i + 1) + " - " + args[i]);
                    System.out.println("0 - Exit");
                    System.out.println("? - Help");
                    System.out.print("Your move: ");
                    playerMove = in.next();
                }
                if (playerMove.equals("?")) {
                    table(args);
                } else if (Integer.parseInt(playerMove) > 0 && Integer.parseInt(playerMove) <= args.length) {
                    System.out.println("Your move: " + args[Integer.parseInt(playerMove) - 1]);
                    System.out.println("Computer move: " + opponentMove);
                    break;
                } else if (Integer.parseInt(playerMove) == 0) {
                    System.out.print("See you later!\nGoodbye!\n");
                    return;
                } else {
                    System.out.println("Please enter ? to call the help table or a number from 0 to " + args.length + " to select the move: ");
                }
            }
            winner = algorithm(Integer.parseInt(playerMove) - 1, rand, args.length / 2);
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