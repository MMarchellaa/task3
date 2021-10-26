import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.grid.Border;
import com.inamik.text.tables.grid.Util;

public class Table {
    void createTable(String[] args) {
        Algorithm algorithm = new Algorithm();
        GridTable table = GridTable.of(args.length + 1, args.length + 1);
        for (int i = 1; i <= args.length; i++) {
            for (int k = 1; k <= args.length; k++) {
                if (k == 1) {
                    table.put(i, 0, Cell.of(args[i - 1]));
                }
                if (i == 1) {
                    table.put(0, k, Cell.of(args[k - 1]));
                }
                int winner = algorithm.whoWin(i - 1, k - 1, args.length / 2);
                if (winner == 1) {
                    table.put(i, k, Cell.of(args[i - 1]));
                } else if (winner == 0) {
                    table.put(i, k, Cell.of("draw"));
                } else {
                    table.put(i, k, Cell.of(args[k - 1]));
                }
            }
        }
        System.out.println("Table of winners:");
        Util.print(Border.DOUBLE_LINE.apply(table));
    }
}
