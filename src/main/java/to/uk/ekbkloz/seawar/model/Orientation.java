package to.uk.ekbkloz.seawar.model;

public enum Orientation {
    NORTH(0,-1),
    NorthEast(1,-1),
    EAST( 1, 0),
    SouthEast(1, 1),
    SOUTH(0, 1),
    SouthWest(-1, 1),
    WEST(-1, 0),
    NorthWest(-1, -1);
    
    private final int xStep;
    private final int yStep;
    


    public int getXStep() {
        return xStep;
    }


    public int getYStep() {
        return yStep;
    }


    private Orientation(final int xStep, final int yStep) {
        this.xStep = xStep;
        this.yStep = yStep;
    }
}
