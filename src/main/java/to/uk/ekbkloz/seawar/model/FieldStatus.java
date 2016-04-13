package to.uk.ekbkloz.seawar.model;

import java.awt.Color;

//перечисление статусов полей
public enum FieldStatus {
    WATER(Color.BLUE),
    SHIP(Color.BLACK),
    MISS(Color.GRAY),
    HIT(Color.RED);
    
    private Color color;
    
    

    public Color getColor() {
        return color;
    }



    private FieldStatus(final Color color) {
        this.color = color;
    }
}
