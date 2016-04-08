package to.uk.ekbkloz.seawar.model;

import java.util.HashMap;
import java.util.Map;

import to.uk.ekbkloz.seawar.model.ships.Ship;

public class ShipsPlacement {
    private final FieldStatus fields[][];
    private Map<Coordinates, Ship> shipsMap;
    private boolean fieldsFilled;
    
    public ShipsPlacement(final int mapSize) {
        fields = new FieldStatus[mapSize][mapSize];
        shipsMap = new HashMap<Coordinates, Ship>();
        fieldsFilled = false;
    }

    public FieldStatus[][] getFields() {
        return fields;
    }

    public Map<Coordinates, Ship> getShipsMap() {
        return shipsMap;
    }

    public void setShipsMap(final Map<Coordinates, Ship> shipsMap) {
        this.shipsMap = shipsMap;
    }

    public boolean isFieldsFilled() {
        return fieldsFilled;
    }

    public void setFieldsFilled(final boolean fieldsFilled) {
        this.fieldsFilled = fieldsFilled;
    }
    
    
    
    
}
