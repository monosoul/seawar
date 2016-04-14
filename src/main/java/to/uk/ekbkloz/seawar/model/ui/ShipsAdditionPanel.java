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

//класс панели добавления кораблей
public class ShipsAdditionPanel extends JPanel {

    private static final long serialVersionUID = -7436304367252831567L;
    
    private final JButton carrierAddButton = new JButton("авианосец");
    private final JButton battleshipAddButton = new JButton("линкор");
    private final JButton cruiserAddButton = new JButton("крейсер");
    private final JButton destroyerAddButton = new JButton("эсминец");
    
    @SuppressWarnings("rawtypes")
    private Class shipToAdd = null;
    
    
    @SuppressWarnings("rawtypes")
    public Class getShipToAdd() {
        return shipToAdd;
    }


    public ShipsAdditionPanel() {
        //в зависимости от того, какая кнопка была нажата, метод getShipToAdd() будет возвращать класс корабля
        this.setBackground(Color.WHITE);
        this.setSize(400, 200);
        
        //кнопка с авианосцами
        carrierAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = Carrier.class;
            }
            
        });
        this.add(carrierAddButton);
        
        //кнопка с линкорами
        battleshipAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = Battleship.class;
            }
            
        });
        this.add(battleshipAddButton);
        
        //кнопка с крейсерами
        cruiserAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = Cruiser.class;
            }
            
        });
        this.add(cruiserAddButton);
        
        //кнопка с эсминцами
        destroyerAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                shipToAdd = Destroyer.class;
            }
            
        });
        this.add(destroyerAddButton);
    }


    public JButton getCarrierAddButton() {
        return carrierAddButton;
    }


    public JButton getBattleshipAddButton() {
        return battleshipAddButton;
    }


    public JButton getCruiserAddButton() {
        return cruiserAddButton;
    }


    public JButton getDestroyerAddButton() {
        return destroyerAddButton;
    }
    
    public void reset() {
        shipToAdd = null;
    }

}
