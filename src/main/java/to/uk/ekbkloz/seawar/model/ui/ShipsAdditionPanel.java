package to.uk.ekbkloz.seawar.model.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import to.uk.ekbkloz.seawar.model.ships.Battleship;
import to.uk.ekbkloz.seawar.model.ships.Carrier;
import to.uk.ekbkloz.seawar.model.ships.Cruiser;
import to.uk.ekbkloz.seawar.model.ships.Destroyer;
import to.uk.ekbkloz.seawar.model.ships.Ship;

public class ShipsAdditionPanel extends JPanel {

    private static final long serialVersionUID = -7436304367252831567L;
    
    private Ship shipToAdd = null;
    
    
    public Ship getShipToAdd() {
        return shipToAdd;
    }



    public ShipsAdditionPanel() {
        this.setBackground(Color.WHITE);
        this.setSize(400, 200);
        
        final JButton carrierAddButton = new JButton("���������");
        carrierAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = new Carrier();
            }
            
        });
        this.add(carrierAddButton);
        
        final JButton battleshipAddButton = new JButton("������");
        battleshipAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = new Battleship();
            }
            
        });
        this.add(battleshipAddButton);
        
        final JButton cruiserAddButton = new JButton("�������");
        cruiserAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = new Cruiser();
            }
            
        });
        this.add(cruiserAddButton);
        
        final JButton destroyerAddButton = new JButton("�������");
        destroyerAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = new Destroyer();
            }
            
        });
        this.add(destroyerAddButton);
    }

}
