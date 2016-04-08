package to.uk.ekbkloz.seawar.model.players;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import to.uk.ekbkloz.seawar.model.ShipsPlacement;
import to.uk.ekbkloz.seawar.model.ships.Battleship;
import to.uk.ekbkloz.seawar.model.ships.Carrier;
import to.uk.ekbkloz.seawar.model.ships.Cruiser;
import to.uk.ekbkloz.seawar.model.ships.Destroyer;
import to.uk.ekbkloz.seawar.model.ships.Ship;
import to.uk.ekbkloz.seawar.model.ui.SeaMap;

public abstract class Player {
    protected Queue<Carrier>    carriers    = new ArrayBlockingQueue<Carrier>(Carrier.MAXAMOUNT);
    protected Queue<Battleship> battleships = new ArrayBlockingQueue<Battleship>(Battleship.MAXAMOUNT);
    protected Queue<Cruiser>    cruisers    = new ArrayBlockingQueue<Cruiser>(Cruiser.MAXAMOUNT);
    protected Queue<Destroyer>  destroyers  = new ArrayBlockingQueue<Destroyer>(Destroyer.MAXAMOUNT);
    
    protected ShipsPlacement ownShipsPlacement;
    protected ShipsPlacement opponentShipsPlacement;
    
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
        
        ownShipsPlacement = new ShipsPlacement(SeaMap.MAPSIZE);
        opponentShipsPlacement = new ShipsPlacement(SeaMap.MAPSIZE);
        
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
