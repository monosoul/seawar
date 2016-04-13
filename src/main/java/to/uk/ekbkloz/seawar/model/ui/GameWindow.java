package to.uk.ekbkloz.seawar.model.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import to.uk.ekbkloz.seawar.model.Coordinates;
import to.uk.ekbkloz.seawar.model.GamePhase;
import to.uk.ekbkloz.seawar.model.Player;
import to.uk.ekbkloz.seawar.model.ships.Battleship;
import to.uk.ekbkloz.seawar.model.ships.Carrier;
import to.uk.ekbkloz.seawar.model.ships.Cruiser;
import to.uk.ekbkloz.seawar.model.ships.Destroyer;
import to.uk.ekbkloz.seawar.model.ships.Ship;

//основное игровое окно
public class GameWindow extends JFrame {
    private static final long serialVersionUID = -202136959207752221L;
    private GamePhase gamePhase; //фаза игры
    private Player curPlayerTurn; //игрок, который ходит в данный момент
    private final JPanel middlePanel; //средняя панель, содержит строку с информацией о ходе, поля с кораблям и кнопки добавления кораблей
    private final JPanel bottomPanel; //нижняя панель, содержит кнопку выхода и кнопку завершения добавления кораблей
    private final GridBagConstraints gridBagConstraints; //параметры layout'а gridBag
    private int turnsCount = 0; //счётчик ходов
    
    protected SeaMap ownMap; //поле с кораблями текущего игрока
    protected SeaMap opponentMap; //поле с кораблями противника
    protected ShipsAdditionPanel shipsAdditionPanel; //панель с кнопками добавления кораблей
    protected final JButton shipsAdditionEndButton; //кнопка завершения добавления кораблей
    protected JLabel information; //строка с информацией о ходе

    public GameWindow() {
        this.setTitle("Sea War");
        this.setSize(813, 600);
        this.setLocation(0, 0);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        gamePhase = GamePhase.ShipAddtion; //первая фаза - добавление кораблей
        
        //игровое поле
        middlePanel = new JPanel(new GridBagLayout());
        this.add(middlePanel, BorderLayout.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        
        //поле с информацией
        information = new JLabel();
        information.setText("Sea War");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        middlePanel.add(information, gridBagConstraints);
        
        //поле игрока
        ownMap = new SeaMap();
        gridBagConstraints.ipady = 400;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        middlePanel.add(ownMap, gridBagConstraints);
        
        //поле противника
        opponentMap = new SeaMap();
        gridBagConstraints.ipady = 400;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        middlePanel.add(opponentMap, gridBagConstraints);
        
        //кнопки добавления кораблей
        shipsAdditionPanel = new ShipsAdditionPanel();
        gridBagConstraints.ipady = 0;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        middlePanel.add(shipsAdditionPanel, gridBagConstraints);
        
        //нижняя панель
        bottomPanel = new JPanel(new BorderLayout());
        this.add(bottomPanel, BorderLayout.SOUTH);
        
        //кнопка завершения добавления кораблей
        shipsAdditionEndButton = new JButton("Готово!");
        shipsAdditionEndButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (curPlayerTurn.getShipsCount() > 0) {
                    //System.out.println("Вы добавили не все корабли!");
                    JOptionPane.showMessageDialog(middlePanel, "Вы добавили не все корабли!");
                }
                else {
                    // если все корабли добавлены, то завершаем ход
                    curPlayerTurn.endTurn();
                    curPlayerTurn = null;
                    shipsAdditionPanel.reset();
                    ownMap.cleanMap();
                    opponentMap.cleanMap();
                    if (turnsCount >= 2) {
                        //если текущий ход - второй, то убираем панель добавления кораблей и кнопку завершения добавления
                        shipsAdditionEndButton.setEnabled(false);
                        shipsAdditionEndButton.setVisible(false);
                        shipsAdditionPanel.setEnabled(false);
                        shipsAdditionPanel.setVisible(false);
                        gamePhase = GamePhase.Battle;
                    }
                }
            }
            
        });
        bottomPanel.add(shipsAdditionEndButton, BorderLayout.CENTER);
        
        //кнопка выхода
        final JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
            
        });
        bottomPanel.add(exitButton, BorderLayout.EAST);
        
        
        //действия по клику мыши
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                super.mouseClicked(e);
                // если фаза - добавление кораблей
                if (gamePhase.equals(GamePhase.ShipAddtion)) {
                    // если нажатая кнопка - ЛКМ
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Ship ship = null;;
                        
                        //получаем из объекта игрока корабли в зависимости от того, какой корабль выбран в панели добавления кораблей
                        if (shipsAdditionPanel.getShipToAdd() != null) {
                            if (shipsAdditionPanel.getShipToAdd().equals(Carrier.class)) {
                                ship = curPlayerTurn.getCarrier();
                            }
                            if (shipsAdditionPanel.getShipToAdd().equals(Battleship.class)) {
                                ship = curPlayerTurn.getBattleship();
                            }
                            if (shipsAdditionPanel.getShipToAdd().equals(Cruiser.class)) {
                                ship = curPlayerTurn.getCruiser();
                            }
                            if (shipsAdditionPanel.getShipToAdd().equals(Destroyer.class)) {
                                ship = curPlayerTurn.getDestroyer();
                            }
                        }
                        
                        //если ссылка на корабль не пустая
                        if (ship != null) {
                            //если корабль возможно разместить в указанном поле
                            if (ownMap.checkShipPlacement(ship, null)) {
                                //добавляем корабль
                                ownMap.placeShip(ship);
                            }
                            //если корабль разместить не возможно
                            else {
                                //возвращаем корабль объекту игрока
                                curPlayerTurn.returnShip(ship);
                            }
                        }
                    }
                    //если нажата СКМ
                    if (e.getButton() == MouseEvent.BUTTON2) {
                        //разворачиваем корабль
                        ownMap.rotateShip();
                    }
                    //если нажата ПКМ
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        //удаляем корабль с карты
                        final Ship ship = ownMap.removeShip();
                        
                        if (ship != null) {
                            //возвращаем объект корабля объекту игрока
                            curPlayerTurn.returnShip(ship);
                        }
                    }
                }
                //если фаза - бой
                if (gamePhase.equals(GamePhase.Battle)) {
                    //производится выстрел по указанному полю. Если он произведён неудачно или у противника закончились корабли
                    if (!opponentMap.shoot(null) || curPlayerTurn.getOpponentShipsPlacement().getShipsMap().isEmpty()) {
                        //если у противника закончились корабли
                        if (curPlayerTurn.getOpponentShipsPlacement().getShipsMap().isEmpty()) {
                            //игра завершена
                            gamePhase = GamePhase.GameOver;
                        }
                        //завершаем текущий ход
                        curPlayerTurn.endTurn();
                        curPlayerTurn = null;
                        ownMap.cleanMap();
                        opponentMap.cleanMap();
                    }
                }
            }
            
        });
        
        this.setVisible(true);
    }
    
    //метод совершения хода
    public void invokeTurn (final Player player) {
        //получаем игрока, для которого выполняется ход
        curPlayerTurn = player;
        //начинаем ход
        curPlayerTurn.beginTurn();
        //рисуем в поле игрока размещение кораблей
        ownMap.drawMap(curPlayerTurn.getOwnShipsPlacement(), player.getPlayerType());
        //если фаза - добавление кораблей
        if(gamePhase.equals(GamePhase.ShipAddtion)) {
            //в строке информации сообщаем, кто добавляет корабли, а так же инструкцию
            information.setText("Игрок " + curPlayerTurn.getName() + " размещает корабли. ЛКМ - добавить корабль; СКМ - повернуть; ПКМ - удалить.");
        }
        //если фаза - добавление бой
        if(gamePhase.equals(GamePhase.Battle)) {
            //в строке информации сообщаем, кто ходит
            information.setText("Ход игрока " + curPlayerTurn.getName());
            ownMap.setEnabled(false);
        }
        //если фаза - не добавление кораблей
        if(!gamePhase.equals(GamePhase.ShipAddtion)) {
            //рисуем в поле противника размещение кораблей
            opponentMap.drawMap(curPlayerTurn.getOpponentShipsPlacement(), player.getPlayerType());
        }
        //увеличиваем счётчик ходов
        turnsCount++;
        
        //если текущий игрок - AI и фаза - добавление кораблей
        if (curPlayerTurn.getPlayerType().equals(Player.PlayerType.AI) && gamePhase.equals(GamePhase.ShipAddtion)) {
            final Random rand = new Random();
            //максимальное количество попыток размещения каждого из кораблей (площадь карты, т.е. по попытке на каждую клетку)
            final int maxTries = SeaMap.MAPSIZE * SeaMap.MAPSIZE;
            //максимальное количество попыток размещения всех кораблей (площадь карты на количество кораблей)
            final int allMaxTries = curPlayerTurn.getShipsCount() * maxTries;
            int allTriesCounter = 1; //счётчик общего количества попыток
            boolean allShipsPlaced = false; //все корабли размещены
            //пока все корабли не размещены
            while(!allShipsPlaced) {
                //пока количество оставшихся кораблей у текущего игрока - больше 0 и количество попыток - меньше максимального
                while(curPlayerTurn.getShipsCount() > 0 && allTriesCounter <= allMaxTries) {
                    //получаем корабль
                    final Ship ship = curPlayerTurn.getShip();
                    boolean placed = false; //корабль размещён
                    int triesCounter = 1; //счётчик количества попыток размещения текущего корабля
                    //пока корабль не размещён и количество попыток - меньше максимального
                    while(!placed && triesCounter <= maxTries) {
                        //получаем рандомные координаты в пределах рамзера поля
                        final Coordinates newCoordinates = new Coordinates(rand.nextInt(SeaMap.MAPSIZE), rand.nextInt(SeaMap.MAPSIZE));
                        //пытаемся разместить корабль
                        if (ownMap.checkShipPlacement(ship, newCoordinates)) {
                            ownMap.placeShip(ship);
                            placed = true;
                        }
                        triesCounter++;
                        allTriesCounter++;
                    }
                    //если корабль не размещён
                    if (!placed) {
                        //то возвращаем его игроку
                        curPlayerTurn.returnShip(ship);
                    }
                }
                //если цикл завершился, но размещены не все корабли, то сбрасываем поле игрока и список кораблей игрока
                if (curPlayerTurn.getShipsCount() > 0) {
                    ownMap.reset();
                    curPlayerTurn.resetAllShips();
                }
                //цикл завершился и все корабли размещены
                else {
                    allShipsPlaced = true;
                }
            }
            
            //завершаем ход
            curPlayerTurn.endTurn();
            curPlayerTurn = null;
            ownMap.cleanMap();
            opponentMap.cleanMap();
            if (turnsCount >= 2) {
              //если текущий ход - второй, то убираем панель добавления кораблей и кнопку завершения добавления 
                shipsAdditionEndButton.setEnabled(false);
                shipsAdditionEndButton.setVisible(false);
                shipsAdditionPanel.setEnabled(false);
                shipsAdditionPanel.setVisible(false);
                gamePhase = GamePhase.Battle;
            }
            //завершаем выполнение хода
            return;
        }
        
        //если текущий игрок - AI и фаза - бой
        if (curPlayerTurn.getPlayerType().equals(Player.PlayerType.AI) && gamePhase.equals(GamePhase.Battle)) {
            final Random rand = new Random();
            //делаем выстрел по рандомным координатам, пока не промахнёмся или пока у противника не закончатся корабли
            while (opponentMap.shoot(new Coordinates(rand.nextInt(SeaMap.MAPSIZE), rand.nextInt(SeaMap.MAPSIZE))) && !curPlayerTurn.getOpponentShipsPlacement().getShipsMap().isEmpty()) {
                
            }
            //если у противника закончились корабли
            if (curPlayerTurn.getOpponentShipsPlacement().getShipsMap().isEmpty()) {
                //игра завершена
                gamePhase = GamePhase.GameOver;
            }
            //заканчиваем ход
            curPlayerTurn.endTurn();
            curPlayerTurn = null;
            ownMap.cleanMap();
            opponentMap.cleanMap();
            //завершаем выполнение хода
            return;
        }
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public int getTurnsCount() {
        return turnsCount;
    }

    
    
}
