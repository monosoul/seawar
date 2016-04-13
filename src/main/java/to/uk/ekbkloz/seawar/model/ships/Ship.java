package to.uk.ekbkloz.seawar.model.ships;

import to.uk.ekbkloz.seawar.model.Coordinates;
import to.uk.ekbkloz.seawar.model.ShipOrientation;

//класс кораблей
public abstract class Ship {
    protected Integer size; //размер корабля (кол-во занимаемых полей)
    protected ShipOrientation orientation = null; //ориентация корабля
    protected Coordinates faceCoordinates = null; //координаты носа корабля (точки, от которой размещается корабль)
    
    
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
  
}
