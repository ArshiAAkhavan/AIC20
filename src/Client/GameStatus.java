package Client;
import static Client.Constants.*;
public enum GameStatus {
    WIN, LOOSE, UNFINISHED, DRAW;

    public static GameStatus setGameStatusForSingleGame(int yourHP, int oppHP, int turnNumber) {
        if ((yourHP == 0 && oppHP == 0) || (yourHP == oppHP && turnNumber >= FINAL_TURN)) {
            return GameStatus.DRAW;
        }
        if (yourHP == 0 || (yourHP < LOOSE_HP_PEAK && oppHP > WIN_OPPONENT_HP_PEAK) || (yourHP < oppHP && turnNumber >= FINAL_TURN)) {
            return GameStatus.LOOSE;
        }
        if (oppHP == 0 || (yourHP > oppHP && turnNumber >= FINAL_TURN)) {
            return GameStatus.WIN;
        }
        return GameStatus.UNFINISHED;
    }

}

