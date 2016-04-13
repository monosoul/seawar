package to.uk.ekbkloz.seawar;

import java.util.Map;

import javax.swing.JOptionPane;

import to.uk.ekbkloz.seawar.model.Coordinates;
import to.uk.ekbkloz.seawar.model.GamePhase;
import to.uk.ekbkloz.seawar.model.Player;
import to.uk.ekbkloz.seawar.model.ships.Ship;
import to.uk.ekbkloz.seawar.model.ui.GameWindow;

public class MainApp {

    public static void main(final String[] args) throws InterruptedException {
        //добавляем игроков
        final Player players[] = new Player[2];
        players[0] = new Player(Player.PlayerType.Human, "Player");
        players[1] = new Player(Player.PlayerType.AI, "Player 2");
        
        final GameWindow w = new GameWindow();
        
        //расстановка кораблей
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
        // в карту кораблей противника игрока 1 сохраняем карту расстановки кораблей игрока 2
        players[0].getOpponentShipsPlacement().setShipsMap(players[1].getOwnShipsPlacement().getShipsMap());
        // в карту кораблей противника игрока 2 сохраняем карту расстановки кораблей игрока 1
        players[1].getOpponentShipsPlacement().setShipsMap(p1shipsMap);
        
        //начинаем игру
        int i = 0;
        while(!w.getGamePhase().equals(GamePhase.GameOver)) {
            //перед началом хода получаем карту попаданий противника для отрисовки попаданий по своим кораблям
            players[i].getOwnShipsPlacement().setShotsMap(players[Math.abs(i-1)].getOpponentShipsPlacement().getShotsMap());
            
            w.invokeTurn(players[i]);

            //если оба игрока - люди, то отображать сообщение "Ход игрока"
            if (players[Math.abs(i-1)].getPlayerType().equals(Player.PlayerType.Human) && players[i].getPlayerType().equals(Player.PlayerType.Human)) {
                JOptionPane.showMessageDialog(w, "Ход игрока " + players[i].getName());
            }
            else {
                System.out.println("Ход игрока " + players[i].getName());
            }
            //ждём, пока игрок закончит ход
            while(!players[i].isTurnEnded()) {
                Thread.sleep(400);
            }
            i = Math.abs(i-1);
        }
        JOptionPane.showMessageDialog(w, "Конец игры! Победил игрок " + players[Math.abs(i-1)].getName() + "!\r\nИгра завершена за " + w.getTurnsCount() + " ходов.");
    }

}
