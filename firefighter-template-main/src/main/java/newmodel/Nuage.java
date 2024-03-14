package newmodel;

import model.ModelElement;
import util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Nuage extends Extinguisher {
    private final ModelElement state;
    private List<Position> NuagePositions;
    private final Random randomGenerator = new Random();
    private final int initialNuageCount;

    public Nuage(int initialNuageCount) {
        this.state = ModelElement.NUAGE;
        this.initialNuageCount=initialNuageCount;
    }

    public List<Position> update(Fboard board) {
        int i = 0;
        List<Position> result = new ArrayList<>();
        while (i < this.initialNuageCount) {
            Position pos = randomPosition();
            boolean positionIsValid = true;

            for (item it : board.getItems()) {
                if (it.getID() == 0 && it.getPositions().contains(pos)) {
                    positionIsValid = false;
                    break;
                }
            }

            if (positionIsValid) {
                i++;
                result.add(pos);
            }
        }
        for(Position p : result){
            extinguish(p,board);
        }
        this.NuagePositions=result;
        return this.NuagePositions;
    }

    private Position randomPosition() {
        return new Position(randomGenerator.nextInt(Fboard.rowCount), randomGenerator.nextInt(Fboard.columnCount));
    }



    public void extinguish(Position position,Fboard board) {
        board.getFire().extinguish(position);
    }


    public ModelElement getState() {
        return this.state;
    }


    public int getID() {
        return ID;
    }

 // A modifier pour ne pas avoir une superpositione entre firefighter et nuage
    public void initialize() {
        NuagePositions = new ArrayList<>();
        for (int index = 0; index < initialNuageCount; index++)
            NuagePositions.add(randomPosition());
    }

    @Override
    public List<Position> getPositions() {
        return NuagePositions;
    }
}
