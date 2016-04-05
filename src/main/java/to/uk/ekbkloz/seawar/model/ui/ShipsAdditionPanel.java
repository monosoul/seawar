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

public class ShipsAdditionPanel extends JPanel {

    private static final long serialVersionUID = -7436304367252831567L;
    
    private Class shipToAdd = null;
    
    
    public Class getShipToAdd() {
        return shipToAdd;
    }


    public ShipsAdditionPanel() {
        this.setBackground(Color.WHITE);
        this.setSize(400, 200);
        
        final JButton carrierAddButton = new JButton("авианосец");
        carrierAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = Carrier.class;
            }
            
        });
        this.add(carrierAddButton);
        
        final JButton battleshipAddButton = new JButton("линкор");
        battleshipAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = Battleship.class;
            }
            
        });
        this.add(battleshipAddButton);
        
        final JButton cruiserAddButton = new JButton("крейсер");
        cruiserAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = Cruiser.class;
            }
            
        });
        this.add(cruiserAddButton);
        
        final JButton destroyerAddButton = new JButton("эсминец");
        destroyerAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = Destroyer.class;
            }
            
        });
        this.add(destroyerAddButton);
    }

}
