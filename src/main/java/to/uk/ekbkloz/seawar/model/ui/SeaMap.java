package to.uk.ekbkloz.seawar.model.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import to.uk.ekbkloz.seawar.model.Coordinates;
import to.uk.ekbkloz.seawar.model.FieldStatus;
import to.uk.ekbkloz.seawar.model.ShipOrientation;
import to.uk.ekbkloz.seawar.model.ships.Ship;

public class SeaMap extends JPanel {

    private static final long serialVersionUID = -6039792179649871752L;
    
    private final FieldStatus fields[][] = new FieldStatus[10][10];
    
    public static final int MAPSIZE = 10;
    
    private final Map<Coordinates, Ship> shipsMap = new HashMap<Coordinates, Ship>();

    public FieldStatus[][] getFields() {
        return fields;
    }



    public SeaMap() {
        this.setBackground(Color.WHITE);
        this.setSize(400, 400);
        for (int i = 0; i < MAPSIZE; i++) {
            for (int j = 0; j < MAPSIZE; j++) {
                fields[i][j] = FieldStatus.WATER;
            }
        }
    }

    
    public Boolean checkField(final Coordinates coordinates, final FieldStatus newStatus) {
        switch(fields[coordinates.getX()][coordinates.getY()]) {
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
    
    
    public void setField(final Coordinates coordinates, final FieldStatus newStatus) {
        fields[coordinates.getX()][coordinates.getY()] = newStatus;
        System.out.println(coordinates.toString() + " - " + newStatus.name());
        this.repaint();
    }
    
    public Boolean checkShipPlacement(final Ship ship, Coordinates faceCoordinates) {
        if (faceCoordinates == null) {
            faceCoordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
        }
        final List<ShipOrientation> shipOrientations = Arrays.asList(ShipOrientation.values());
        int orientationIndex;
        if (ship.getOrientation() != null) {
            orientationIndex = shipOrientations.indexOf(ship.getOrientation());
        }
        else {
            orientationIndex = 0;
        }
        final int initialOrientationIndex = orientationIndex;
        boolean fullCycle = false;
        
        //for(final ShipOrientation orientation : shipOrientations) {
        while(!fullCycle) {
            if (orientationIndex >= shipOrientations.size()) {
                orientationIndex = 0;
            }
            
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
                    if(!checkField(new Coordinates(x, y), FieldStatus.SHIP)) {
                        break;
                    }
                    else {
                        availableFields++;
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
            if (orientationIndex == initialOrientationIndex) {
                fullCycle = true;
            }
        }
        
        return false;
    }

    public void placeShip(final Ship ship) {
        int x = ship.getFaceCoordinates().getX();
        int y = ship.getFaceCoordinates().getY();
        while(Math.abs(ship.getFaceCoordinates().getX() - x) < ship.getSize() && Math.abs(ship.getFaceCoordinates().getY() - y) < ship.getSize()) {
            final Coordinates coordinates = new Coordinates(x, y);
            setField(coordinates, FieldStatus.SHIP);
            shipsMap.put(coordinates, ship);
                
            x += ship.getOrientation().getXStep();
            y += ship.getOrientation().getYStep();
        }
    }
    
    public void rotateShip(Coordinates coordinates) {
        if (coordinates == null) {
            coordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
        }
        try{
            final Ship ship = shipsMap.get(coordinates);
            final Coordinates shipFaceCoordinates = ship.getFaceCoordinates();
            final ShipOrientation shipOrientation = ship.getOrientation();
            
            if (checkShipPlacement(ship, ship.getFaceCoordinates())) {
                int x = shipFaceCoordinates.getX() + shipOrientation.getXStep();
                int y = shipFaceCoordinates.getY() + shipOrientation.getYStep();
                while(Math.abs(shipFaceCoordinates.getX() - x) < ship.getSize() && Math.abs(shipFaceCoordinates.getY() - y) < ship.getSize()) {
                    final Coordinates fieldCoordinates = new Coordinates(x, y);
                    setField(fieldCoordinates, FieldStatus.WATER);
                    shipsMap.remove(fieldCoordinates);
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
    
    public Ship removeShip(Coordinates coordinates) {
        if (coordinates == null) {
            coordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
        }
        try{
            final Ship ship = shipsMap.get(coordinates);

            int x = ship.getFaceCoordinates().getX();
            int y = ship.getFaceCoordinates().getY();
            while(Math.abs(ship.getFaceCoordinates().getX() - x) < ship.getSize() && Math.abs(ship.getFaceCoordinates().getY() - y) < ship.getSize()) {
                final Coordinates fieldCoordinates = new Coordinates(x, y);
                setField(fieldCoordinates, FieldStatus.WATER);
                shipsMap.remove(fieldCoordinates);
                System.out.println("Тест3");
                        
                x += ship.getOrientation().getXStep();
                y += ship.getOrientation().getYStep();
            }
            return ship;
        }
        catch(final Exception e) {
            System.out.println("Здесь корабля нет!");
            return null;
        }
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        
        for (int i = 0; i < MAPSIZE; i++) {
            for (int j = 0; j < MAPSIZE; j++) {
                //fields[i][j]
                g.setColor(fields[i][j].getColor());
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
