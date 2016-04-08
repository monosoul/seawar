package to.uk.ekbkloz.seawar.model.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import to.uk.ekbkloz.seawar.model.GamePhase;
import to.uk.ekbkloz.seawar.model.players.Player;
import to.uk.ekbkloz.seawar.model.ships.Battleship;
import to.uk.ekbkloz.seawar.model.ships.Carrier;
import to.uk.ekbkloz.seawar.model.ships.Cruiser;
import to.uk.ekbkloz.seawar.model.ships.Destroyer;
import to.uk.ekbkloz.seawar.model.ships.Ship;

public class GameWindow extends JFrame {
    private static final long serialVersionUID = -202136959207752221L;
    private GamePhase gamePhase;
    private Player curPlayerTurn;
    private final JPanel middlePanel;
    private final JPanel bottomPanel;
    private final GridBagConstraints gridBagConstraints;
    private int turnsCount = 0;
    
    protected SeaMap ownMap;
    protected SeaMap opponentMap;
    protected ShipsAdditionPanel shipsAdditionPanel;
    protected JLabel information;

    public GameWindow() {
        this.setTitle("Sea War");
        this.setSize(813, 600);
        this.setLocation(0, 0);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        gamePhase = GamePhase.ShipAddtion;
        
        //верхняя панель
        //final JPanel topPanel = new JPanel(new BorderLayout());
        //this.add(topPanel, BorderLayout.SOUTH);
        
        //игровое поле
        middlePanel = new JPanel(new GridBagLayout());
        this.add(middlePanel, BorderLayout.CENTER);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.ipadx = 400;
        
        //поле с информацией
        information = new JLabel();
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
        final JButton shipsAdditionEndButton = new JButton("Готово!");
        shipsAdditionEndButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                if (curPlayerTurn.getShipsCount() > 0) {
                    System.out.println("Вы добавили не все корабли!");
                }
                else {
                    curPlayerTurn.endTurn();
                    curPlayerTurn = null;
                    ownMap.cleanMap();
                    opponentMap.cleanMap();
                    if (turnsCount >= 2) {
                        shipsAdditionEndButton.setEnabled(false);
                        shipsAdditionEndButton.setVisible(false);
                        shipsAdditionPanel.setEnabled(false);
                        shipsAdditionPanel.setVisible(false);
                        gamePhase = GamePhase.Battle;
                    }
                    middlePanel.repaint();
                }
            }
            
        });
        bottomPanel.add(shipsAdditionEndButton, BorderLayout.CENTER);
        
        //кнопка выхода
        final JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
            
        });
        bottomPanel.add(exitButton, BorderLayout.EAST);
        
        
        //выбор места расположения корабля
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                super.mouseClicked(e);
                if (gamePhase.equals(GamePhase.ShipAddtion)) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        Ship ship = null;;

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
                        
                        if (ship != null) {
                            if (ownMap.checkShipPlacement(ship, null)) {
                                ownMap.placeShip(ship);
                            }
                            else {
                                curPlayerTurn.returnShip(ship);
                            }
                        }
                    }
                    if (e.getButton() == MouseEvent.BUTTON2) {
                        ownMap.rotateShip();
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        final Ship ship = ownMap.removeShip();
                        
                        if (ship != null) {
                            curPlayerTurn.returnShip(ship);
                        }
                    }
                }
                if (gamePhase.equals(GamePhase.Battle)) {
                    if (!opponentMap.shoot(null) || curPlayerTurn.getOpponentShipsPlacement().getShipsMap().isEmpty()) {
                        if (curPlayerTurn.getOpponentShipsPlacement().getShipsMap().isEmpty()) {
                            gamePhase = GamePhase.GameOver;
                        }
                        curPlayerTurn.endTurn();
                        curPlayerTurn = null;
                        ownMap.cleanMap();
                        opponentMap.cleanMap();
                        middlePanel.repaint();
                    }
                }
            }
            
        });
        
        this.setVisible(true);
    }
    
    public void invokeTurn (final Player player) {
        curPlayerTurn = player;
        curPlayerTurn.beginTurn();
        ownMap.drawMap(curPlayerTurn.getOwnShipsPlacement());
        if(gamePhase.equals(GamePhase.Battle)) {
            ownMap.setEnabled(false);
        }
        if(!gamePhase.equals(GamePhase.ShipAddtion)) {
            opponentMap.drawMap(curPlayerTurn.getOpponentShipsPlacement());
        }
        middlePanel.repaint();
        turnsCount++;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    
}
