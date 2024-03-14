package newmodel;

import model.ModelElement;
import util.Position;

import java.util.*;

public class Fire implements item {

    private final int initialFireCount;

    private List<Position> firePositions;
    private final ModelElement state;
    private final Random randomGenerator = new Random();

    private final int ID=1;

    public Fire(int initialFireCount) {
        this.initialFireCount = initialFireCount;
        this.state=ModelElement.FIRE;
    }


    public List<Position> update(Fboard board) {
            List<Position> newFirePositions = new ArrayList<>();
            for (Position fire : firePositions) {
                newFirePositions.addAll(board.neighbors(fire));
            }
            for (Position f : newFirePositions) {
                if(!firePositions.contains(f)) {
                    firePositions.add(f);
                }
            }
        return firePositions;
        // firePositions contient last fireposition after the last extinguish and new fire positions
    }

    public ModelElement getState() {
        return this.state;
    }

    public int getID() {
        return this.ID;
    }

    public void initialize() {
        firePositions = new ArrayList<>();
        for (int index = 0; index < initialFireCount; index++)
            firePositions.add(randomPosition());
    }


    public List<Position> getPositions() {
        return firePositions;
    }

    public Position randomPosition() {
        return new Position(randomGenerator.nextInt(Fboard.rowCount), randomGenerator.nextInt(Fboard.columnCount));
    }

    public void extinguish(Position position) {
        firePositions.remove(position);
    }


}
