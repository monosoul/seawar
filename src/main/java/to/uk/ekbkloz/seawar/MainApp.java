package to.uk.ekbkloz.seawar;

import java.util.Map;

import javax.swing.JOptionPane;

import to.uk.ekbkloz.seawar.model.Coordinates;
import to.uk.ekbkloz.seawar.model.FieldStatus;
import to.uk.ekbkloz.seawar.model.GamePhase;
import to.uk.ekbkloz.seawar.model.Player;
import to.uk.ekbkloz.seawar.model.ships.Ship;
import to.uk.ekbkloz.seawar.model.ui.GameWindow;

public class MainApp {

    public static void main(final String[] args) throws InterruptedException {
        final Player players[] = new Player[2];
        players[0] = new Player(Player.PlayerType.Human, "Player");
        players[1] = new Player(Player.PlayerType.AI, "AI");
        
        final GameWindow w = new GameWindow();
        
        if (players[0].getPlayerType().equals(Player.PlayerType.AI)) {
            System.out.println("Игрок " + players[0].getName() + " расставляет корабли");
        }
        else {
            JOptionPane.showMessageDialog(w, "Игрок " + players[0].getName() + " расставляет корабли");
        }
        w.invokeTurn(players[0]);
        while(!players[0].isTurnEnded()) {
            Thread.sleep(100);
        }
        
        if (players[1].getPlayerType().equals(Player.PlayerType.AI)) {
            System.out.println("Игрок " + players[1].getName() + " расставляет корабли");
        }
        else {
            JOptionPane.showMessageDialog(w, "Игрок " + players[1].getName() + " расставляет корабли");
        }
        w.invokeTurn(players[1]);
        while(!players[1].isTurnEnded()) {
            Thread.sleep(100);
        }
        
        //объединяем карты
        final Map<Coordinates, Ship> p1shipsMap = players[0].getOwnShipsPlacement().getShipsMap();
        players[0].getOpponentShipsPlacement().setShipsMap(players[1].getOwnShipsPlacement().getShipsMap());
        players[1].getOpponentShipsPlacement().setShipsMap(p1shipsMap);
        
        int i = 0;
        int s = 0;
        Map<Coordinates, FieldStatus> shotsMapBuffer = null;
        while(!w.getGamePhase().equals(GamePhase.GameOver)) {
            if (shotsMapBuffer != null) {
                players[i].getOwnShipsPlacement().setShotsMap(shotsMapBuffer);
            }
            w.invokeTurn(players[i]);
            s++;
            if (s >= players.length) {
                s = 0;
            }
            if (players[s].getPlayerType().equals(Player.PlayerType.Human) && players[i].getPlayerType().equals(Player.PlayerType.Human)) {
                JOptionPane.showMessageDialog(w, "Ход игрока " + players[i].getName());
            }
            else {
                System.out.println("Ход игрока " + players[i].getName());
            }
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
