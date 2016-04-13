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
        players[1] = new Player(Player.PlayerType.AI, "Player 2");
        
        final GameWindow w = new GameWindow();
        
        for (final Player player : players) {
            if (player.getPlayerType().equals(Player.PlayerType.AI)) {
                System.out.println("Игрок " + player.getName() + " расставляет корабли");
            }
            else {
                JOptionPane.showMessageDialog(w, "Игрок " + player.getName() + " расставляет корабли");
            }
            w.invokeTurn(player);
            while(!player.isTurnEnded()) {
                Thread.sleep(100);
            }
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

            if (players[Math.abs(i-1)].getPlayerType().equals(Player.PlayerType.Human) && players[i].getPlayerType().equals(Player.PlayerType.Human)) {
                JOptionPane.showMessageDialog(w, "Ход игрока " + players[i].getName());
            }
            else {
                System.out.println("Ход игрока " + players[i].getName());
            }
            while(!players[i].isTurnEnded()) {
                Thread.sleep(400);
            }
            shotsMapBuffer = players[i].getOpponentShipsPlacement().getShotsMap();
            i = Math.abs(i-1);
        }
        JOptionPane.showMessageDialog(w, "Конец игры! Победил игрок " + players[Math.abs(i-1)].getName() + "!");
    }

}
