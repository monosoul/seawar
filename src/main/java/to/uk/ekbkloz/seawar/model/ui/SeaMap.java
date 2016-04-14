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

//поле с кораблями
public class SeaMap extends JPanel {

    private static final long serialVersionUID = -6039792179649871752L;
       
    public static final int MAPSIZE = 10; //размер поля
    
    private ShipsPlacement shipsPlacement = null; //карта размещения кораблей


    //при создании поле - пустое
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
    
    //метод для отрисовки поля в соответствии с картой размещения кораблей
    public void drawMap(final ShipsPlacement shipsPlacement, final Player.PlayerType playerType) {
        this.shipsPlacement = shipsPlacement;
        //рисуем поля по "карте" полей
        if (shipsPlacement.getShipsMap().size() <= 0 || !this.shipsPlacement.isFieldsFilled()) {
            for (int i = 0; i < MAPSIZE; i++) {
                for (int j = 0; j < MAPSIZE; j++) {
                    this.shipsPlacement.getFields()[i][j] = FieldStatus.WATER;
                }
            }
            this.shipsPlacement.setFieldsFilled(true);
        }
        //рисуем попадания/промахи
        for (final Entry<Coordinates, FieldStatus> entry : this.shipsPlacement.getShotsMap().entrySet()) {
            this.shipsPlacement.getFields()[entry.getKey().getX()][entry.getKey().getY()] = entry.getValue();
        }
        //если текущий игрок - человек, то включаем поле
        if (playerType.equals(Player.PlayerType.Human)) {
            this.setEnabled(true);
            //this.setVisible(true);
        }
        this.repaint();
    }
    
    //метод очистки поля
    public void cleanMap() {
        //если присутствует карта размещения кораблей
        if (this.shipsPlacement != null) {
            //то делаем поле не активным
            this.setEnabled(false);
            //this.setVisible(false);
            //рисуем пустое поле
            shipsPlacement = null;
            super.paintComponent(this.getGraphics());
        }
    }

    //метод проверки поля
    public Boolean checkField(final Coordinates coordinates, final FieldStatus newStatus) {
        //если координаты за пределами карты, то возвращаем false
        if (coordinates.getX() >= SeaMap.MAPSIZE || coordinates.getX() < 0 || coordinates.getY() >= SeaMap.MAPSIZE || coordinates.getY() < 0) {
            return false;
        }
        //System.out.println("Check field: " + coordinates + "; Status: " + shipsPlacement.getFields()[coordinates.getX()][coordinates.getY()].name());
        switch(shipsPlacement.getFields()[coordinates.getX()][coordinates.getY()]) {
            //если текущий статус поля - вода
            case WATER: if (newStatus.equals(FieldStatus.SHIP) || newStatus.equals(FieldStatus.MISS)) {
                        //если потенциальный статус поля - корабль или промах, то возвращаем true
                          return true;
                        }
                        break;
            //если текущий статус поля - корабль
            case SHIP:  if (newStatus.equals(FieldStatus.HIT)) {
                        //если потенциальный статус поля - попадание, то возвращаем true
                          return true;
                        }
                        break;
            default:    break;
        }
        //в остальных случаях возвращаем false
        return false;
    }
    
    //метод проверки соседних полей
    private Boolean checkNearbyFields(final Coordinates currentField, final Ship ship) {
        boolean placeable = true;
        //перебираем все направления размещения кораблей
        for (final ShipOrientation orientation : ShipOrientation.values()) {
            //получаем потенциальные координаты следующего поля
            final Coordinates nextField = new Coordinates(currentField.getX()+orientation.getXStep(), currentField.getY()+orientation.getYStep());
            //System.out.println("nextField: " + nextField);
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
                        //если сдвиг по отношению к предыдущему полю - по вертикали
                        if (orientation.getXStep() == 0) {
                            diagFields[0] = new Coordinates(nextField.getX() + 1, nextField.getY());
                            diagFields[1] = new Coordinates(nextField.getX() - 1, nextField.getY());
                        }
                        //если сдвиг по отношению к предыдущему полю - по горизонтали
                        if (orientation.getYStep() == 0) {
                            diagFields[0] = new Coordinates(nextField.getX(), nextField.getY() + 1);
                            diagFields[1] = new Coordinates(nextField.getX(), nextField.getY() - 1);
                        }
                        // перебираем потенциальные поля по диагоналям
                        for (final Coordinates diagField : diagFields) {
                            //если в пределах карты
                            if (diagField.getX() < SeaMap.MAPSIZE && diagField.getX() >= 0 && diagField.getY() < SeaMap.MAPSIZE && diagField.getY() >= 0) {
                                //если не поле, занятое текущим кораблём
                                if (!ship.equals(shipsPlacement.getShipsMap().get(diagField))) {
                                    //если нельзя разместить корабль
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
        //System.out.println("placeable = " + placeable);
        return placeable;
    }
    
    //метод изменения статуса поля
    public void setField(final Coordinates coordinates, final FieldStatus newStatus) {
        shipsPlacement.getFields()[coordinates.getX()][coordinates.getY()] = newStatus;
        //System.out.println(coordinates.toString() + " - " + newStatus.name());
        this.repaint();
    }
    
    //метод проверки возможности размещения корабля
    public Boolean checkShipPlacement(final Ship ship, Coordinates faceCoordinates) {
        //если методу не переданы координаты носа корабля, то берём их из текущего расположения курсора мыши
        if (faceCoordinates == null) {
            faceCoordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
        }
        //если координаты за пределами карты, то возвращаем false
        if (faceCoordinates.getX() >= SeaMap.MAPSIZE || faceCoordinates.getX() < 0 || faceCoordinates.getY() >= SeaMap.MAPSIZE || faceCoordinates.getY() < 0) {
            return false;
        }
        //получаем коллекцию возможных направлений размещения корабля
        final List<ShipOrientation> shipOrientations = Arrays.asList(ShipOrientation.values());
        //System.out.println("shipOrientations.size() = " + shipOrientations.size());
        int orientationIndex; //индекс текущего направления
        //если переданный объект корабля уже размещён на карте
        if (ship.getOrientation() != null) {
            //то получаем индекс текущего направления корабля
            orientationIndex = shipOrientations.indexOf(ship.getOrientation());
        }
        //если корабль ещё не размещён на карте
        else {
            //то индекс текущего направления - 0, т.е. первое направление по списку
            orientationIndex = 0;
        }
        int iterations = 0; //переменная с количеством итераций
        
        //пока итераций меньше, чем количество направлений
        while(iterations<shipOrientations.size()) {
            //если индекс направления больше допустимого, то сбрасываем его на 0 (т.е. первое направление). Нужно в случаях, когда перебор начинается не с первого направления.
            if (orientationIndex >= shipOrientations.size()) {
                orientationIndex = 0;
            }
            //System.out.println("initialOrientationIndex = " + initialOrientationIndex);
            //System.out.println("orientationIndex = " + orientationIndex);
            
            final ShipOrientation orientation = shipOrientations.get(orientationIndex); //текущее направление
            
            //получаем потенциальные координаты кормы корабля
            final Coordinates backCoordinates = new Coordinates(faceCoordinates.getX() + (ship.getSize()-1)*orientation.getXStep(), faceCoordinates.getY() + (ship.getSize()-1)*orientation.getYStep());
            
            //System.out.println("Orientation: " + orientation.name());
            //System.out.println("faceCoordinates: (" + faceCoordinates.getX() + ";" + faceCoordinates.getY() + ")");
            //System.out.println("backCoordinates: (" + backCoordinates.getX() + ";" + backCoordinates.getY() + ")");
            
            //если координаты кормы не выходят за пределы поля
            if (backCoordinates.getX() < MAPSIZE && backCoordinates.getX() >= 0 && backCoordinates.getY() < MAPSIZE && backCoordinates.getY() >= 0) {
                // Проверяем возможность поставить корабль
                int availableFields; //счётчик успешно проверенных полей
                int x;
                int y;
                //если корабль уже есть на карте (т.е. выполняется поворот корабля)
                if (ship.getFaceCoordinates() != null) {
                    //то проверку координат начинаем со второй ячейки корабля
                    availableFields = 1;
                    //System.out.println("Корабль уже есть на карте");
                    x = faceCoordinates.getX() + orientation.getXStep();
                    y = faceCoordinates.getY() + orientation.getYStep();
                }
                else {
                    //в противном случае - с первой
                    availableFields = 0;
                    x = faceCoordinates.getX();
                    y = faceCoordinates.getY();
                }
                
                //пока не достигли координат кормы корабля
                while(Math.abs(faceCoordinates.getX() - x) < ship.getSize() && Math.abs(faceCoordinates.getY() - y) < ship.getSize()) {
                    //получаем координаты проверяемого поля
                    final Coordinates currentCoordinates = new Coordinates(x, y);
                    //проверяем поле на возможность размещения корабля
                    if(!checkField(currentCoordinates, FieldStatus.SHIP)) {
                        break;
                    }
                    //если корабль можно разместить, то также проверяем соседние координаты
                    else {
                        if (!checkNearbyFields(currentCoordinates, ship)) {
                            break;
                        }
                        else {
                            //если все проверки выполнены, то увличиваем счётчик проверенных координат
                            availableFields++;
                        }
                        
                    }
                    //System.out.println("тест (" + x + ";" + y + ")");
                    
                    //переходим к следующему полю
                    x+=orientation.getXStep();
                    y+=orientation.getYStep();
                }
                //если количество успешно проверенных полей совпадает с размером корабля
                if (availableFields == ship.getSize()) {
                    //то подходящее место для размещения корабля найдено
                    //System.out.println("Подходящее место найдено");
                    ship.setOrientation(orientation);
                    ship.setFaceCoordinates(faceCoordinates);
                    return true;
                }
            }
            orientationIndex++;
            iterations++;
        }
        
        return false;
    }

    //метод для размещения корабля
    public void placeShip(final Ship ship) {
        int x = ship.getFaceCoordinates().getX();
        int y = ship.getFaceCoordinates().getY();
        //пока не достигли кормы корабля
        while(Math.abs(ship.getFaceCoordinates().getX() - x) < ship.getSize() && Math.abs(ship.getFaceCoordinates().getY() - y) < ship.getSize()) {
            final Coordinates coordinates = new Coordinates(x, y);
            //меняем статус поля на "корабль"
            setField(coordinates, FieldStatus.SHIP);
            //добавляем запись в хэш кораблей
            shipsPlacement.getShipsMap().put(coordinates, ship);
                
            x += ship.getOrientation().getXStep();
            y += ship.getOrientation().getYStep();
        }
    }
    
    //метод разворота корабля
    public void rotateShip() {
        //получаем координаты курсора мыши
        final Coordinates coordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
        
        try{
            //получаем объект корабля по указанным координатам
            final Ship ship = shipsPlacement.getShipsMap().get(coordinates);
            final Coordinates shipFaceCoordinates = ship.getFaceCoordinates();
            final ShipOrientation shipOrientation = ship.getOrientation();
            
            //если найдены координаты, в которых можно разместить корабль
            if (checkShipPlacement(ship, ship.getFaceCoordinates())) {
                int x = shipFaceCoordinates.getX() + shipOrientation.getXStep();
                int y = shipFaceCoordinates.getY() + shipOrientation.getYStep();
                //то пока не достигли кормы корабля
                while(Math.abs(shipFaceCoordinates.getX() - x) < ship.getSize() && Math.abs(shipFaceCoordinates.getY() - y) < ship.getSize()) {
                    final Coordinates fieldCoordinates = new Coordinates(x, y);
                    //удаляем информацию о старом расположении корабля
                    setField(fieldCoordinates, FieldStatus.WATER);
                    shipsPlacement.getShipsMap().remove(fieldCoordinates);
                    //System.out.println("Тест2");
                        
                    x += shipOrientation.getXStep();
                    y += shipOrientation.getYStep();
                }
                //размещаем корабль в новом месте
                placeShip(ship);
            }
        }
        catch(final Exception e){
            //System.out.println("Здесь корабля нет!");
        }
    }
    
    //метод удаления корабля
    public Ship removeShip() {
        //получаем координаты курсора мыши
        final Coordinates coordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
            
        try{
            //получаем объект корабля по указанным координатам
            final Ship ship = shipsPlacement.getShipsMap().get(coordinates);

            int x = ship.getFaceCoordinates().getX();
            int y = ship.getFaceCoordinates().getY();
            //пока не достигли кормы корабля
            while(Math.abs(ship.getFaceCoordinates().getX() - x) < ship.getSize() && Math.abs(ship.getFaceCoordinates().getY() - y) < ship.getSize()) {
                final Coordinates fieldCoordinates = new Coordinates(x, y);
                //удаляем данные о расположении корабля
                setField(fieldCoordinates, FieldStatus.WATER);
                shipsPlacement.getShipsMap().remove(fieldCoordinates);
                //System.out.println("Тест3");
                        
                x += ship.getOrientation().getXStep();
                y += ship.getOrientation().getYStep();
            }
            ship.setFaceCoordinates(null);
            ship.setOrientation(null);
            return ship;
        }
        catch(final Exception e) {
            //System.out.println("Здесь корабля нет!");
            return null;
        }
    }

    //метод для совершения выстрела
    public boolean shoot(Coordinates coordinates) {
        //если в аргументах не переданы координаты
        if (coordinates == null) {
            //то получаем координаты расположения курсора мыши
            coordinates = new Coordinates((int)(this.getMousePosition().getX() / 40),(int)(this.getMousePosition().getY() / 40));
        }
        //если выстрел производится по полю, по которому уже стреляли
        if (shipsPlacement.getShotsMap().containsKey(coordinates)) {
            //то даём право на ещё один ход, т.к. игнорируем выстрел
            return true;
        }
        //если выстрел по полю, по которому ещё не стреляли
        else {
            //если по указанным координатам находится корабль
            if (checkField(coordinates, FieldStatus.HIT) || shipsPlacement.getShipsMap().containsKey(coordinates)) {
                //то отмечаем попадание по кораблю
                setField(coordinates, FieldStatus.HIT);
                shipsPlacement.getShipsMap().remove(coordinates);
                shipsPlacement.getShotsMap().put(coordinates, FieldStatus.HIT);
                //даём право на ещё один ход
                return true;
            }
            //если корабля нет
            else {
                //отмечаем промах
                setField(coordinates, FieldStatus.MISS);
                shipsPlacement.getShotsMap().put(coordinates, FieldStatus.MISS);
                //права на ещё один ход нет
                return false;
            }
        }
    }
    
    //метод сброса поля
    public void reset() {
        //полная очистка поля и сброс всех карт размещения кораблей
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
                        //в некоторых случаях, видимо, карта ещё продолжает рисоваться, когда shipsPlacement уже null
                        //поэтому такой костыль :)
                        if (shipsPlacement != null) {
                            g.setColor(shipsPlacement.getFields()[i][j].getColor());
                        }
                        //e.printStackTrace();
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
