package to.uk.ekbkloz.seawar;

import java.util.Map;

import to.uk.ekbkloz.seawar.model.Coordinates;
import to.uk.ekbkloz.seawar.model.FieldStatus;
import to.uk.ekbkloz.seawar.model.GamePhase;
import to.uk.ekbkloz.seawar.model.players.HumanPlayer;
import to.uk.ekbkloz.seawar.model.players.Player;
import to.uk.ekbkloz.seawar.model.ships.Ship;
import to.uk.ekbkloz.seawar.model.ui.GameWindow;

public class MainApp {

    public static void main(final String[] args) throws InterruptedException {
        final Player players[] = new Player[2];
        players[0] = new HumanPlayer();
        players[1] = new HumanPlayer();
        
        final GameWindow w = new GameWindow();
        
        System.out.println("Игрок 1 расставляет корабли");
        w.invokeTurn(players[0]);
        while(!players[0].isTurnEnded()) {
            Thread.sleep(100);
        }
        System.out.println("Игрок 2 расставляет корабли");
        w.invokeTurn(players[1]);
        while(!players[1].isTurnEnded()) {
            Thread.sleep(100);
        }
        
        //объединяем карты
        final Map<Coordinates, Ship> p1shipsMap = players[0].getOwnShipsPlacement().getShipsMap();
        players[0].getOpponentShipsPlacement().setShipsMap(players[1].getOwnShipsPlacement().getShipsMap());
        players[1].getOpponentShipsPlacement().setShipsMap(p1shipsMap);
        
        int i = 0;
        Map<Coordinates, FieldStatus> shotsMapBuffer = null;
        while(!w.getGamePhase().equals(GamePhase.GameOver)) {
            if (shotsMapBuffer != null) {
                players[i].getOwnShipsPlacement().setShotsMap(shotsMapBuffer);
            }
            w.invokeTurn(players[i]);
            System.out.println("Ход игрока " + (i + 1));
            while(!players[i].isTurnEnded()) {
                Thread.sleep(400);
            }
            shotsMapBuffer = players[i].getOpponentShipsPlacement().getShotsMap();
            i++;
            if (i >= players.length) {
                i = 0;
            }
        }
        System.out.println("Конец игры");
        //w.invokeTurn(player2);
    }

}
