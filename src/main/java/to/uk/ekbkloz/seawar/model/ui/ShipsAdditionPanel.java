package to.uk.ekbkloz.seawar.model.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JButton;
import javax.swing.JPanel;

import to.uk.ekbkloz.seawar.model.ships.Battleship;
import to.uk.ekbkloz.seawar.model.ships.Carrier;
import to.uk.ekbkloz.seawar.model.ships.Cruiser;
import to.uk.ekbkloz.seawar.model.ships.Destroyer;
import to.uk.ekbkloz.seawar.model.ships.Ship;

public class ShipsAdditionPanel extends JPanel {

    private static final long serialVersionUID = -7436304367252831567L;
    
    private final Queue<Ship> shipToAdd = new ArrayBlockingQueue<Ship>(1);
    
    
    public Ship getShipToAdd() {
        return shipToAdd.poll();
    }
    
    
    public void setShipToAdd(final Ship ship) {
        shipToAdd.offer(ship);
    }


    public ShipsAdditionPanel() {
        this.setBackground(Color.WHITE);
        this.setSize(400, 200);
        
        final JButton carrierAddButton = new JButton("авианосец");
        carrierAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setShipToAdd(new Carrier());
            }
            
        });
        this.add(carrierAddButton);
        
        final JButton battleshipAddButton = new JButton("линкор");
        battleshipAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setShipToAdd(new Battleship());
            }
            
        });
        this.add(battleshipAddButton);
        
        final JButton cruiserAddButton = new JButton("крейсер");
        cruiserAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setShipToAdd(new Cruiser());
            }
            
        });
        this.add(cruiserAddButton);
        
        final JButton destroyerAddButton = new JButton("эсминец");
        destroyerAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                setShipToAdd(new Destroyer());
            }
            
        });
        this.add(destroyerAddButton);
    }

}
