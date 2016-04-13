package to.uk.ekbkloz.seawar.model;

import java.util.HashMap;
import java.util.Map;

import to.uk.ekbkloz.seawar.model.ships.Ship;

//карта размещения кораблей
public class ShipsPlacement {
    private final FieldStatus fields[][]; //"карта" полей, используется для отрисовки
    private Map<Coordinates, Ship> shipsMap; //хэш кораблей с ключами из координат. Удобно использовать для определния принадлежности координаты какому-либо из кораблей. Используется в первую очередь на этапе размещения кораблей.
    private Map<Coordinates, FieldStatus> shotsMap; //хэш статусов полей (результатов выстрелов) с ключами из координат
    private boolean fieldsFilled; //флаг, указывающий на то, что "карта" полей заполнена
    
    public ShipsPlacement(final int mapSize) {
        fields = new FieldStatus[mapSize][mapSize];
        shipsMap = new HashMap<Coordinates, Ship>();
        shotsMap = new HashMap<Coordinates, FieldStatus>();
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

    public Map<Coordinates, FieldStatus> getShotsMap() {
        return shotsMap;
    }

    public void setShotsMap(final Map<Coordinates, FieldStatus> shotsMap) {
        this.shotsMap = shotsMap;
    }
    
}
