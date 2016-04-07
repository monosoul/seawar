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
    private final GamePhase gamePhase;
    private Player curPlayerTurn;
    private final JPanel middlePanel;
    private final JPanel bottomPanel;
    private final GridBagConstraints gridBagConstraints;

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
                    middlePanel.removeAll();
                    curPlayerTurn.endTurn();
                    curPlayerTurn = null;
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

                        if (curPlayerTurn.getShipsAdditionPanel().getShipToAdd() != null) {
                            if (curPlayerTurn.getShipsAdditionPanel().getShipToAdd().equals(Carrier.class)) {
                                ship = curPlayerTurn.getCarrier();
                            }
                            if (curPlayerTurn.getShipsAdditionPanel().getShipToAdd().equals(Battleship.class)) {
                                ship = curPlayerTurn.getBattleship();
                            }
                            if (curPlayerTurn.getShipsAdditionPanel().getShipToAdd().equals(Cruiser.class)) {
                                ship = curPlayerTurn.getCruiser();
                            }
                            if (curPlayerTurn.getShipsAdditionPanel().getShipToAdd().equals(Destroyer.class)) {
                                ship = curPlayerTurn.getDestroyer();
                            }
                        }
                        
                        if (ship != null) {
                            if (curPlayerTurn.getOwnMap().checkShipPlacement(ship, null)) {
                                curPlayerTurn.getOwnMap().placeShip(ship);
                            }
                            else {
                                curPlayerTurn.returnShip(ship);
                            }
                        }
                    }
                    if (e.getButton() == MouseEvent.BUTTON2) {
                        curPlayerTurn.getOwnMap().rotateShip();
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        final Ship ship = curPlayerTurn.getOwnMap().removeShip();
                        
                        if (ship != null) {
                            curPlayerTurn.returnShip(ship);
                        }
                    }
                }
            }
            
        });
        
        this.setVisible(true);
    }
    
    public void initializeMap (final Player player) {
        //поле с информацией
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        middlePanel.add(player.getInformation(), gridBagConstraints);
        
        //игровое поле
        gridBagConstraints.ipady = 400;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        middlePanel.add(player.getOwnMap(), gridBagConstraints);
        
        //поле противника
        gridBagConstraints.ipady = 400;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        middlePanel.add(player.getOpponentMap(), gridBagConstraints);
        
        //кнопки добавления кораблей
        gridBagConstraints.ipady = 0;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        middlePanel.add(player.getShipsAdditionPanel(), gridBagConstraints);
        
        curPlayerTurn = player;
        middlePanel.repaint();
    }

}
