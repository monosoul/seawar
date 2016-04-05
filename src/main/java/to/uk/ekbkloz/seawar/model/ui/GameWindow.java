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

import to.uk.ekbkloz.seawar.model.players.HumanPlayer;
import to.uk.ekbkloz.seawar.model.players.Player;
import to.uk.ekbkloz.seawar.model.ships.Battleship;
import to.uk.ekbkloz.seawar.model.ships.Carrier;
import to.uk.ekbkloz.seawar.model.ships.Cruiser;
import to.uk.ekbkloz.seawar.model.ships.Destroyer;
import to.uk.ekbkloz.seawar.model.ships.Ship;

public class GameWindow extends JFrame {
    private static final long serialVersionUID = -202136959207752221L;

    public GameWindow() {
        this.setTitle("Sea War");
        this.setSize(813, 600);
        this.setLocation(0, 0);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        
        //верхняя панель
        final JPanel topPanel = new JPanel(new BorderLayout());
        this.add(topPanel, BorderLayout.SOUTH);
        
        //игровое поле
        final JPanel middlePanel = new JPanel(new GridBagLayout());
        this.add(middlePanel, BorderLayout.CENTER);
        final GridBagConstraints c = new GridBagConstraints();
        
        //поле игрока 1
        c.fill = GridBagConstraints.BOTH;
        c.ipady = 400;
        c.ipadx = 400;
        c.gridx = 0;
        c.gridy = 0;
        final SeaMap firstPlayerMap = new SeaMap();
        middlePanel.add(firstPlayerMap, c);
        //final Ship ship1 = new Ship(4);
        //ship1.place(firstPlayerMap, new Coordinates(5, 5), ShipOrientation.SOUTH);
        //кнопки игрока 1
        c.fill = GridBagConstraints.BOTH;
        c.ipady = 0;
        c.ipadx = 40;
        c.gridx = 0;
        c.gridy = 1;
        final Player player = new HumanPlayer();
        final ShipsAdditionPanel firstPlayerShipsAdditionPanel = new ShipsAdditionPanel();
        middlePanel.add(firstPlayerShipsAdditionPanel, c);
        
        
        //поле игрока 2
        c.fill = GridBagConstraints.BOTH;
        c.ipady = 400;
        c.ipadx = 400;
        c.gridx = 1;
        c.gridy = 0;
        final SeaMap secondPlayerMap = new SeaMap();
        middlePanel.add(secondPlayerMap, c);
        
        //кнопки игрока 2
        c.fill = GridBagConstraints.BOTH;
        c.ipady = 0;
        c.ipadx = 40;
        c.gridx = 1;
        c.gridy = 1;
        final ShipsAdditionPanel secondPlayerShipsAdditionPanel = new ShipsAdditionPanel();
        middlePanel.add(secondPlayerShipsAdditionPanel, c);
        
        //нижняя панель
        final JPanel bottomPanel = new JPanel(new BorderLayout());
        this.add(bottomPanel, BorderLayout.SOUTH);
        
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
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Ship ship = null;;

                    if (firstPlayerShipsAdditionPanel.getShipToAdd() != null) {
                        if (firstPlayerShipsAdditionPanel.getShipToAdd().equals(Carrier.class)) {
                            ship = player.getCarrier();
                        }
                        if (firstPlayerShipsAdditionPanel.getShipToAdd().equals(Battleship.class)) {
                            ship = player.getBattleship();
                        }
                        if (firstPlayerShipsAdditionPanel.getShipToAdd().equals(Cruiser.class)) {
                            ship = player.getCruiser();
                        }
                        if (firstPlayerShipsAdditionPanel.getShipToAdd().equals(Destroyer.class)) {
                            ship = player.getDestroyer();
                        }
                    }
                    
                    if (ship != null) {
                        if (firstPlayerMap.checkShipPlacement(ship, null)) {
                            firstPlayerMap.placeShip(ship);
                        }
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON2) {
                    firstPlayerMap.rotateShip();
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    final Ship ship = firstPlayerMap.removeShip();
                    
                    if (ship != null) {
                        player.returnShip(ship);
                    }
                }
            }
            
        });
        
        this.setVisible(true);
    }
}
