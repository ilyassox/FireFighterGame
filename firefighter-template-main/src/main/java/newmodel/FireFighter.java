package newmodel;
import model.ModelElement;
import util.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FireFighter extends Extinguisher {
    private final ModelElement state;
    private List<Position> firefighterPositions;
    private final Random randomGenerator = new Random();
    private List<Position> neighborFirePositions;
    private final int initialFirefighterCount;

    public FireFighter(int initialFirefighterCount){

        this.initialFirefighterCount=initialFirefighterCount;
        this.state=ModelElement.FIREFIGHTER;
    }


    public List<Position> update(Fboard board) {
        List<Position> modifiedPositions = new ArrayList<>();
        List<Position> firefighterNewPositions = new ArrayList<>();

        for (Position firefighterPosition : firefighterPositions) {
            // Find the new position closest to the fire
            Position newFirefighterPosition = board.neighborClosestToFire(firefighterPosition);
            firefighterNewPositions.add(newFirefighterPosition);

            // Extinguish fire at the new position
            extinguish(newFirefighterPosition, board);

            // Record the modified positions
            modifiedPositions.add(firefighterPosition);
            modifiedPositions.add(newFirefighterPosition);

            // Find neighboring fire positions
            List<Position> neighborFirePositions = board.neighbors(newFirefighterPosition)
                    .stream()
                    .filter(position -> board.getFire().getPositions().contains(position))
                    .toList();

            // Extinguish fires in neighboring positions
            for (Position firePosition : neighborFirePositions) {
                extinguish(firePosition, board);
                modifiedPositions.add(firePosition);
            }
        }

        // Update the firefighter positions
        firefighterPositions = firefighterNewPositions;

        return modifiedPositions;
    }



    public void extinguish(Position position,Fboard board) {
        board.getFire().extinguish(position);
    }


    public ModelElement getState() {
        return this.state;
    }

    public int getID() {
        return super.ID;
    }

    public void initialize() {
        firefighterPositions = new ArrayList<>();
        for (int index = 0; index < initialFirefighterCount; index++)
            firefighterPositions.add(randomPosition());
    }
    private Position randomPosition() {
        return new Position(randomGenerator.nextInt(Fboard.rowCount), randomGenerator.nextInt(Fboard.columnCount));
    }

    public List<Position> getPositions(){
        return this.firefighterPositions;
    }

}


