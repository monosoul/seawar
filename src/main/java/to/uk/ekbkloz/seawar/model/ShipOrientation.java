package to.uk.ekbkloz.seawar.model;

//перечисление возможных ориентаций кораблей, содержит шаги по осям X и Y для размещения кораблей  
public enum ShipOrientation {
    NORTH(0,-1),
    EAST( 1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);
    
    private final int xStep;
    private final int yStep;
    


    public int getXStep() {
        return xStep;
    }


    public int getYStep() {
        return yStep;
    }


    private ShipOrientation(final int xStep, final int yStep) {
        this.xStep = xStep;
        this.yStep = yStep;
    }
}
