public class Algorithm {
    int whoWin(int playerMove, int comp, int size) {
        if ((Math.abs(playerMove - comp) <= size && playerMove > comp) || (playerMove < comp && Math.abs(playerMove - comp) > size)) {
            return -1;
        } else if (Math.abs(playerMove - comp) == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}