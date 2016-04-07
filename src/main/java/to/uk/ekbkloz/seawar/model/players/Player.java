package to.uk.ekbkloz.seawar.model.players;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JLabel;

import to.uk.ekbkloz.seawar.model.ships.Battleship;
import to.uk.ekbkloz.seawar.model.ships.Carrier;
import to.uk.ekbkloz.seawar.model.ships.Cruiser;
import to.uk.ekbkloz.seawar.model.ships.Destroyer;
import to.uk.ekbkloz.seawar.model.ships.Ship;
import to.uk.ekbkloz.seawar.model.ui.SeaMap;
import to.uk.ekbkloz.seawar.model.ui.ShipsAdditionPanel;

public abstract class Player {
    protected Queue<Carrier>    carriers    = new ArrayBlockingQueue<Carrier>(Carrier.MAXAMOUNT);
    protected Queue<Battleship> battleships = new ArrayBlockingQueue<Battleship>(Battleship.MAXAMOUNT);
    protected Queue<Cruiser>    cruisers    = new ArrayBlockingQueue<Cruiser>(Cruiser.MAXAMOUNT);
    protected Queue<Destroyer>  destroyers  = new ArrayBlockingQueue<Destroyer>(Destroyer.MAXAMOUNT);
    protected SeaMap ownMap;
    protected SeaMap opponentMap;
    protected ShipsAdditionPanel shipsAdditionPanel;
    protected JLabel information;
    protected boolean turnEnded;
    
    public Player(){
        for(int i = 0;i<Destroyer.MAXAMOUNT;i++) {
            if (i < Carrier.MAXAMOUNT) {
                carriers.offer(new Carrier());
            }
            if (i < Battleship.MAXAMOUNT) {
                battleships.offer(new Battleship());
            }
            if (i < Cruiser.MAXAMOUNT) {
                cruisers.offer(new Cruiser());
            }
            if (i < Destroyer.MAXAMOUNT) {
                destroyers.offer(new Destroyer());
            }
        }
        ownMap = new SeaMap();
        opponentMap = new SeaMap();
        opponentMap.setEnabled(false);
        opponentMap.setVisible(false);
        shipsAdditionPanel = new ShipsAdditionPanel();
        information = new JLabel();
        information.setText("Добавление кораблей");
        turnEnded = false;
    }
    
    public void endTurn() {
        ownMap.setEnabled(false);
        ownMap.setVisible(false);
        opponentMap.setEnabled(false);
        opponentMap.setVisible(false);
        if (shipsAdditionPanel.isEnabled()) {
            shipsAdditionPanel.setEnabled(false);
            shipsAdditionPanel.setVisible(false);
        }
        information.setVisible(false);
        turnEnded = true;
    }
    
    public void beginTurn() {
        ownMap.setEnabled(true);
        ownMap.setVisible(true);
        opponentMap.setEnabled(true);
        opponentMap.setVisible(true);
        information.setVisible(true);
        turnEnded = false;
    }
    

    public boolean isTurnEnded() {
        return turnEnded;
    }

    public SeaMap getOwnMap() {
        return ownMap;
    }



    public SeaMap getOpponentMap() {
        return opponentMap;
    }



    public ShipsAdditionPanel getShipsAdditionPanel() {
        return shipsAdditionPanel;
    }

    public JLabel getInformation() {
        return information;
    }

    public Carrier getCarrier() {
        return carriers.poll();
    }

    public void returnCarrier(final Carrier carrier) {
        this.carriers.offer(carrier);
    }

    public Battleship getBattleship() {
        return battleships.poll();
    }

    public void returnBattleship(final Battleship battleship) {
        this.battleships.offer(battleship);
    }

    public Cruiser getCruiser() {
        return cruisers.poll();
    }

    public void returnCruiser(final Cruiser cruiser) {
        this.cruisers.offer(cruiser);
    }

    public Destroyer getDestroyer() {
        return destroyers.poll();
    }

    public void returnDestroyer(final Destroyer destroyer) {
        this.destroyers.offer(destroyer);
    }
    
    public int getShipsCount() {
        int shipsCount = 0;
        shipsCount = carriers.size() + battleships.size() + cruisers.size() + destroyers.size();
        return shipsCount;
    }
    
    public void returnShip(final Ship ship) {
        if (ship.getClass().equals(Carrier.class)) {
            returnCarrier((Carrier) ship);
        }
        if (ship.getClass().equals(Battleship.class)) {
            returnBattleship((Battleship) ship);
        }
        if (ship.getClass().equals(Cruiser.class)) {
            returnCruiser((Cruiser) ship);
        }
        if (ship.getClass().equals(Destroyer.class)) {
            returnDestroyer((Destroyer) ship);
        }
    }
}
