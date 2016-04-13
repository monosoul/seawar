package to.uk.ekbkloz.seawar.model.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JPanel;

import to.uk.ekbkloz.seawar.model.Coordinates;
import to.uk.ekbkloz.seawar.model.FieldStatus;
import to.uk.ekbkloz.seawar.model.Player;
import to.uk.ekbkloz.seawar.model.ShipOrientation;
import to.uk.ekbkloz.seawar.model.ShipsPlacement;
import to.uk.ekbkloz.seawar.model.ships.Ship;

public class SeaMap extends JPanel {

    private static final long serialVersionUID = -6039792179649871752L;
       
    public static final int MAPSIZE = 10;
    
    private ShipsPlacement shipsPlacement = null;



    public SeaMap() {
        this.setBackground(Color.WHITE);
        this.setSize(400, 400);
        this.setEnabled(false);
        //this.setVisible(false);
        /*
        for (int i = 0; i < MAPSIZE; i++) {
            for (int j = 0; j < MAPSIZE; j++) {
                shipsPlacement.getFields()[i][j] = FieldStatus.WATER;
            }
        }
        */
    }
    
    public void drawMap(final ShipsPlacement shipsPlacement, final Player.PlayerType playerType) {
        this.shipsPlacement = shipsPlacement;
        if (shipsPlacement.getShipsMap().size() <= 0 || !this.shipsPlacement.isFieldsFilled()) {
            for (int i = 0; i < MAPSIZE; i++) {
                for (int j = 0; j < MAPSIZE; j++) {
                    this.shipsPlacement.getFields()[i][j] = FieldStatus.WATER;
                }
            }
            this.shipsPlacement.setFieldsFilled(true);
        }
        for (final Entry<Coordinates, FieldStatus> entry : this.shipsPlacement.getShotsMap().entrySet()) {
            this.shipsPlacement.getFields()[entry.getKey().getX()][entry.getKey().getY()] = entry.getValue();
        }
        
        if (playerType.equals(Player.PlayerType.Human)) {
            this.setEnabled(true);
            //this.setVisible(true);
        }
        this.repaint();
    }
    
    public void cleanMap() {
        if (this.shipsPlacement != null) {
            this.setEnabled(false);
            //this.setVisible(false);
            shipsPlacement = null;
            super.paintComponent(this.getGraphics());
        }
    }

    
    public Boolean checkField(final Coordinates coordinates, final FieldStatus newStatus) {
        if (coordinates.getX() >= SeaMap.MAPSIZE || coordinates.getX() < 0 || coordinates.getY() >= SeaMap.MAPSIZE || coordinates.getY() < 0) {
            return false;
        }
        System.out.println("Check field: " + coordinates + "; Status: " + shipsPlacement.getFields()[coordinates.getX()][coordinates.getY()].name());
        switch(shipsPlacement.getFields()[coordinates.getX()][coordinates.getY()]) {
            case WATER: if (newStatus.equals(FieldStatus.SHIP) || newStatus.equals(FieldStatus.MISS)) {
                          return true;
                        }
                        break;
            case SHIP:  if (newStatus.equals(FieldStatus.HIT)) {
                          return true;
                        }
                        break;
            default:    break;
        }
        return false;
    }
    
    private Boolean checkNearbyFields(final Coordinates currentField, final Ship ship) {
        boolean placeable = true;
        for (final ShipOrientation orientation : ShipOrientation.values()) {
            final Coordinates nextField = new Coordinates(currentField.getX()+orientation.getXStep(), currentField.getY()+orientation.getYStep());
            System.out.println("nextField: " + nextField);
            //если в пределах карты
            if (nextField.getX() < SeaMap.MAPSIZE && nextField.getX() >= 0 && nextField.getY() < SeaMap.MAPSIZE && nextField.getY() >= 0) {
                //если не поле, занятое текущим кораблём
                if (!ship.equals(shipsPlacement.getShipsMap().get(nextField))) {
                    //если нельзя разместить корабль
                    if (!checkField(nextField, FieldStatus.SHIP)) {
                        placeable = false;
                        break;
                    }
                    else {
                        //проверяем диагонали
                        final Coordinates diagFields[] = new Coordinates[2];
                        if (orientation.getXStep() == 0) {
                            diagFields[0] = new Coordinates(nextField.getX() + 1, nextField.getY());
                            diagFields[1] = new Coordinates(nextField.getX() - 1, nextField.getY());
                        }
                        if (orientation.getYStep() == 0) {
                            diagFields[0] = new Coordinates(nextField.getX(), nextField.getY() + 1);
                            diagFields[1] = new Coordinates(nextField.getX(), nextField.getY() - 1);
                        }
                        for (final Coordinates diagField : diagFields) {
                            if (diagField.getX() < SeaMap.MAPSIZE && diagField.getX() >= 0 && diagField.getY() < SeaMap.MAPSIZE && diagField.getY() >= 0) {
                                if (!ship.equals(shipsPlacement.getShipsMap().get(diagField))) {
                                    if (!checkField(diagField, FieldStatus.SHIP)) {
                                        placeable = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("placeable = " + placeable);
        return placeable;
    }
    
    
    public void setField(final Coordinates coordinates, final FieldStatus newStatus) {
        shipsPlacement.getFields()[coordinates.getX()][coordinates.getY()] = newStatus;
        System.out.println(coordinates.toString() + " - " + newStatus.name());
        this.repaint();
    }
    
    public Boolean checkShipPlacement(final Ship ship, Coordinates faceCoordinates) {
        if (faceCoordinates == null) {
            faceCoordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
        }
        if (faceCoordinates.getX() >= SeaMap.MAPSIZE || faceCoordinates.getX() < 0 || faceCoordinates.getY() >= SeaMap.MAPSIZE || faceCoordinates.getY() < 0) {
            return false;
        }
        final List<ShipOrientation> shipOrientations = Arrays.asList(ShipOrientation.values());
        System.out.println("shipOrientations.size() = " + shipOrientations.size());
        int orientationIndex;
        if (ship.getOrientation() != null) {
            orientationIndex = shipOrientations.indexOf(ship.getOrientation());
        }
        else {
            orientationIndex = 0;
        }
        final int initialOrientationIndex = orientationIndex;
        int iterations = 0;
        
        //for(final ShipOrientation orientation : shipOrientations) {
        while(iterations<shipOrientations.size()) {
            if (orientationIndex >= shipOrientations.size()) {
                orientationIndex = 0;
            }
            System.out.println("initialOrientationIndex = " + initialOrientationIndex);
            System.out.println("orientationIndex = " + orientationIndex);
            
            final ShipOrientation orientation = shipOrientations.get(orientationIndex);
            
            final Coordinates backCoordinates = new Coordinates(faceCoordinates.getX() + (ship.getSize()-1)*orientation.getXStep(), faceCoordinates.getY() + (ship.getSize()-1)*orientation.getYStep());
            
            System.out.println("Orientation: " + orientation.name());
            System.out.println("faceCoordinates: (" + faceCoordinates.getX() + ";" + faceCoordinates.getY() + ")");
            System.out.println("backCoordinates: (" + backCoordinates.getX() + ";" + backCoordinates.getY() + ")");
            
            if (backCoordinates.getX() < MAPSIZE && backCoordinates.getX() >= 0 && backCoordinates.getY() < MAPSIZE && backCoordinates.getY() >= 0) {
                // Проверяем возможность поставить корабль
                int availableFields;
                int x;
                int y;
                if (ship.getFaceCoordinates() != null) {
                    availableFields = 1;
                    System.out.println("Корабль уже есть на карте");
                    x = faceCoordinates.getX() + orientation.getXStep();
                    y = faceCoordinates.getY() + orientation.getYStep();
                }
                else {
                    availableFields = 0;
                    x = faceCoordinates.getX();
                    y = faceCoordinates.getY();
                }
                
                while(Math.abs(faceCoordinates.getX() - x) < ship.getSize() && Math.abs(faceCoordinates.getY() - y) < ship.getSize()) {
                    final Coordinates currentCoordinates = new Coordinates(x, y);
                    if(!checkField(currentCoordinates, FieldStatus.SHIP)) {
                        break;
                    }
                    else {
                        if (!checkNearbyFields(currentCoordinates, ship)) {
                            break;
                        }
                        else {
                            availableFields++;
                        }
                        
                    }
                    System.out.println("тест (" + x + ";" + y + ")");
                    
                    x+=orientation.getXStep();
                    y+=orientation.getYStep();
                }
                if (availableFields == ship.getSize()) {
                    System.out.println("Подходящее место найдено");
                    ship.setOrientation(orientation);
                    ship.setFaceCoordinates(faceCoordinates);
                    ship.setBackCoordinates(backCoordinates);
                    return true;
                }
            }
            orientationIndex++;
            iterations++;
        }
        
        return false;
    }

    public void placeShip(final Ship ship) {
        int x = ship.getFaceCoordinates().getX();
        int y = ship.getFaceCoordinates().getY();
        while(Math.abs(ship.getFaceCoordinates().getX() - x) < ship.getSize() && Math.abs(ship.getFaceCoordinates().getY() - y) < ship.getSize()) {
            final Coordinates coordinates = new Coordinates(x, y);
            setField(coordinates, FieldStatus.SHIP);
            shipsPlacement.getShipsMap().put(coordinates, ship);
                
            x += ship.getOrientation().getXStep();
            y += ship.getOrientation().getYStep();
        }
    }
    
    public void rotateShip() {
        final Coordinates coordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
        
        try{
            final Ship ship = shipsPlacement.getShipsMap().get(coordinates);
            final Coordinates shipFaceCoordinates = ship.getFaceCoordinates();
            final ShipOrientation shipOrientation = ship.getOrientation();
            
            if (checkShipPlacement(ship, ship.getFaceCoordinates())) {
                int x = shipFaceCoordinates.getX() + shipOrientation.getXStep();
                int y = shipFaceCoordinates.getY() + shipOrientation.getYStep();
                while(Math.abs(shipFaceCoordinates.getX() - x) < ship.getSize() && Math.abs(shipFaceCoordinates.getY() - y) < ship.getSize()) {
                    final Coordinates fieldCoordinates = new Coordinates(x, y);
                    setField(fieldCoordinates, FieldStatus.WATER);
                    shipsPlacement.getShipsMap().remove(fieldCoordinates);
                    System.out.println("Тест2");
                        
                    x += shipOrientation.getXStep();
                    y += shipOrientation.getYStep();
                }
                
                placeShip(ship);
            }
        }
        catch(final Exception e){
            System.out.println("Здесь корабля нет!");
        }
    }
    
    public Ship removeShip() {
        final Coordinates coordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
            
        try{
            final Ship ship = shipsPlacement.getShipsMap().get(coordinates);

            int x = ship.getFaceCoordinates().getX();
            int y = ship.getFaceCoordinates().getY();
            while(Math.abs(ship.getFaceCoordinates().getX() - x) < ship.getSize() && Math.abs(ship.getFaceCoordinates().getY() - y) < ship.getSize()) {
                final Coordinates fieldCoordinates = new Coordinates(x, y);
                setField(fieldCoordinates, FieldStatus.WATER);
                shipsPlacement.getShipsMap().remove(fieldCoordinates);
                System.out.println("Тест3");
                        
                x += ship.getOrientation().getXStep();
                y += ship.getOrientation().getYStep();
            }
            ship.setFaceCoordinates(null);
            ship.setBackCoordinates(null);
            ship.setOrientation(null);
            return ship;
        }
        catch(final Exception e) {
            System.out.println("Здесь корабля нет!");
            return null;
        }
    }

    public boolean shoot(Coordinates coordinates) {
        if (coordinates == null) {
            coordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
        }
        if (shipsPlacement.getShotsMap().containsKey(coordinates)) {
            return true;
        }
        else {
            if (checkField(coordinates, FieldStatus.HIT) || shipsPlacement.getShipsMap().containsKey(coordinates)) {
                setField(coordinates, FieldStatus.HIT);
                shipsPlacement.getShipsMap().remove(coordinates);
                shipsPlacement.getShotsMap().put(coordinates, FieldStatus.HIT);
                return true;
            }
            else {
                setField(coordinates, FieldStatus.MISS);
                shipsPlacement.getShotsMap().put(coordinates, FieldStatus.MISS);
                return false;
            }
        }
    }
    
    public void reset() {
        for (int i = 0; i < MAPSIZE; i++) {
            for (int j = 0; j < MAPSIZE; j++) {
                this.shipsPlacement.getFields()[i][j] = FieldStatus.WATER;
            }
        }
        this.shipsPlacement.setFieldsFilled(false);
        this.shipsPlacement.getShipsMap().clear();
        this.shipsPlacement.getShotsMap().clear();
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        
        if (shipsPlacement != null) {
            for (int i = 0; i < MAPSIZE; i++) {
                for (int j = 0; j < MAPSIZE; j++) {
                    //fields[i][j]
                    try{
                      g.setColor(shipsPlacement.getFields()[i][j].getColor());
                    }
                    catch(final Exception e){
                        e.printStackTrace();
                    }
                    g.fillRect(i*40, j*40, 40, 40);
                }
            }
            
            g.setColor(Color.BLACK);
            for (int i = 0; i <= MAPSIZE; i++) {
                g.drawLine(0, i*40, 400, i*40);
                g.drawLine(i*40, 0, i*40, 400);
            }
        }
    }
    
    
}
