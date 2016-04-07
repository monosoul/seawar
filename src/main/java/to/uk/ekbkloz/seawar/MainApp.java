package to.uk.ekbkloz.seawar;

import to.uk.ekbkloz.seawar.model.players.HumanPlayer;
import to.uk.ekbkloz.seawar.model.players.Player;
import to.uk.ekbkloz.seawar.model.ui.GameWindow;

public class MainApp {

    public static void main(final String[] args) throws InterruptedException {
        System.out.println("Добавляем игрока 1");
        final Player player1 = new HumanPlayer();
        
        final GameWindow w = new GameWindow();
        w.initializeMap(player1);
        while(!player1.isTurnEnded()) {
            Thread.sleep(1000);
        }
        System.out.println("Добавляем игрока 2");
        final Player player2 = new HumanPlayer();
        w.initializeMap(player2);
    }

}
