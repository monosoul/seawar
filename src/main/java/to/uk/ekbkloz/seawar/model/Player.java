package to.uk.ekbkloz.seawar.model;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import to.uk.ekbkloz.seawar.model.ships.Battleship;
import to.uk.ekbkloz.seawar.model.ships.Carrier;
import to.uk.ekbkloz.seawar.model.ships.Cruiser;
import to.uk.ekbkloz.seawar.model.ships.Destroyer;
import to.uk.ekbkloz.seawar.model.ships.Ship;
import to.uk.ekbkloz.seawar.model.ui.SeaMap;

public class Player {
    private final Queue<Carrier>    carriers    = new ArrayBlockingQueue<Carrier>(Carrier.MAXAMOUNT);
    private final Queue<Battleship> battleships = new ArrayBlockingQueue<Battleship>(Battleship.MAXAMOUNT);
    private final Queue<Cruiser>    cruisers    = new ArrayBlockingQueue<Cruiser>(Cruiser.MAXAMOUNT);
    private final Queue<Destroyer>  destroyers  = new ArrayBlockingQueue<Destroyer>(Destroyer.MAXAMOUNT);
    
    private final ShipsPlacement ownShipsPlacement;
    private final ShipsPlacement opponentShipsPlacement;
    private final PlayerType playerType;
    private final String name;
    
    private boolean turnEnded;
    
    public Player(final PlayerType playerType, final String name){
        resetAllShips();
        
        ownShipsPlacement = new ShipsPlacement(SeaMap.MAPSIZE);
        opponentShipsPlacement = new ShipsPlacement(SeaMap.MAPSIZE);
        this.playerType = playerType;
        this.name = name;
        
        turnEnded = false;
    }
    
    
    
    public ShipsPlacement getOwnShipsPlacement() {
        return ownShipsPlacement;
    }



    public ShipsPlacement getOpponentShipsPlacement() {
        return opponentShipsPlacement;
    }
    

    public void endTurn() {
        turnEnded = true;
    }
    
    public void beginTurn() {
        turnEnded = false;
    }
    

    public boolean isTurnEnded() {
        return turnEnded;
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
    
    public Ship getShip() {
        if (!this.carriers.isEmpty()) {
            return this.getCarrier();
        }
        if (!this.battleships.isEmpty()) {
            return this.getBattleship();
        }
        if (!this.cruisers.isEmpty()) {
            return this.getCruiser();
        }
        if (!this.destroyers.isEmpty()) {
            return this.getDestroyer();
        }
        return null;
    }
    
    public void resetAllShips() {
        carriers.clear();
        battleships.clear();
        cruisers.clear();
        destroyers.clear();
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
    
    
    public PlayerType getPlayerType() {
        return playerType;
    }

    public String getName() {
        return name;
    }



    public enum PlayerType {
        Human,
        AI;
    }
}
