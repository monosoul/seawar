package to.uk.ekbkloz.seawar.model.ships;

import to.uk.ekbkloz.seawar.model.Coordinates;
import to.uk.ekbkloz.seawar.model.ShipOrientation;

public abstract class Ship {
    protected Integer size;
    protected ShipOrientation orientation = null;
    protected Coordinates faceCoordinates = null;
    protected Coordinates backCoordinates = null;
    
    
    public Integer getSize() {
        return size;
    }


    public ShipOrientation getOrientation() {
        return orientation;
    }
    
    public void setOrientation(final ShipOrientation orientation) {
        this.orientation = orientation;
    }


    public Ship() {
    }


    public Coordinates getFaceCoordinates() {
        return faceCoordinates;
    }


    public void setFaceCoordinates(final Coordinates faceCoordinates) {
        this.faceCoordinates = faceCoordinates;
    }


    public Coordinates getBackCoordinates() {
        return backCoordinates;
    }


    public void setBackCoordinates(final Coordinates backCoordinates) {
        this.backCoordinates = backCoordinates;
    }
    
    
    
    

    
}
