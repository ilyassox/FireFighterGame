package model;

import util.Position;

import java.util.*;


public class FirefighterBoard implements Board<List<ModelElement>> {
  private final int columnCount;
  private final int rowCount;
  private final int initialFireCount;
  private final int initialFirefighterCount;
  private List<Position> firefighterPositions;
  private Set<Position> firePositions;
  private int step = 0;
  private final Random randomGenerator = new Random();

  // FireFighterBoard Class Constructor
  public FirefighterBoard(int columnCount, int rowCount, int initialFireCount, int initialFirefighterCount) {
    this.columnCount = columnCount;
    this.rowCount = rowCount;
    this.initialFireCount = initialFireCount;
    this.initialFirefighterCount = initialFirefighterCount;
    initializeElements();
  }

  // Intialise parametres(firefighterPositions,firePositions into random positions,firefighterPositions into random positions)
  public void   initializeElements() {
    firefighterPositions = new ArrayList<>();
    firePositions = new HashSet<>();
    for (int index = 0; index < initialFireCount; index++)
      firePositions.add(randomPosition());
    for (int index = 0; index < initialFirefighterCount; index++)
      firefighterPositions.add(randomPosition());
  }

  // Function that returns (Random(<Rowcount,<columnCount)
  private Position randomPosition() {
    return new Position(randomGenerator.nextInt(rowCount), randomGenerator.nextInt(columnCount));
  }

  // Function that return a state (Fire or Firefighter) of a certain position
  @Override
  public List<ModelElement> getState(Position position) {
    List<ModelElement> result = new ArrayList<>();
    for(Position firefighterPosition : firefighterPositions)
      if (firefighterPosition.equals(position))
        result.add(ModelElement.FIREFIGHTER);
    if(firePositions.contains(position))
      result.add(ModelElement.FIRE);
    return result;
  }

  @Override
  public int rowCount() {
    return rowCount;
  }

  @Override
  public int columnCount() {
    return columnCount;
  }


  //this function calls both update firefighters and updatefire functions so they can be updated to their correponding
  // new position and sum up the results and returns them also it increments the step variable
  public List<Position> updateToNextGeneration() {
    List<Position> result = updateFirefighters();
    result.addAll(updateFires());
    step++;
    return result;
  }

  // this function add fire in the neighbor of fires in every 2 steps and update the list of fires
  private List<Position> updateFires() {
    List<Position> result = new ArrayList<>();
    if (step % 2 == 0) {
      List<Position> newFirePositions = new ArrayList<>();
      for (Position fire : firePositions) {
        newFirePositions.addAll(neighbors(fire));
      }
      firePositions.addAll(newFirePositions);
      result.addAll(newFirePositions);
    }
    return result;

  }
 // show the number of steps
  @Override
  public int stepNumber() {
    return step;
  }

  //result contient FirefighterPosition , newfigherfighterposition ,neighborfirepositions
  //this function update the firefighter position to the nearest fire position and extinguish fire from this psoition and all the neighbor positions to this fire
//**
  private List<Position> updateFirefighters() {
    List<Position> result = new ArrayList<>();
    List<Position> firefighterNewPositions = new ArrayList<>();
    for (Position firefighterPosition : firefighterPositions) {
      Position newFirefighterPosition = neighborClosestToFire(firefighterPosition);
      firefighterNewPositions.add(newFirefighterPosition);
      extinguish(newFirefighterPosition);
      result.add(firefighterPosition);
      result.add(newFirefighterPosition);
      List<Position> neighborFirePositions = neighbors(newFirefighterPosition).stream()
              .filter(firePositions::contains).toList(); //this code create a list filtred with only the fire positions that are close to the newfirefighter position
      for(Position firePosition : neighborFirePositions) //and exist in the firepositions list also
        extinguish(firePosition);
      result.addAll(neighborFirePositions);
    }
    firefighterPositions = firefighterNewPositions;
    return result;
  }

  // function that calls the initialise elements
  @Override
  public void reset() {
    step = 0;
    initializeElements();
  }

  // Function that removes fire from a certain position
  private void extinguish(Position position) {
    firePositions.remove(position);
  }

  //Function that returns a list of positions neighbors of a certain position

  private List<Position> neighbors(Position position) {
    List<Position> list = new ArrayList<>();
    if (position.row() > 0) list.add(new Position(position.row() - 1, position.column()));
    if (position.column() > 0) list.add(new Position(position.row(), position.column() - 1));
    if (position.row() < rowCount - 1) list.add(new Position(position.row() + 1, position.column()));
    if (position.column() < columnCount - 1) list.add(new Position(position.row(), position.column() + 1));
    return list;
  }

    //HashMAp is a data structure that store elements in a key-value
    //Set is a data structure that containts a collection of unique elements with no duplicata
    //Queue is a FIFO structure


  //This function searches for the nearest fire  to the position passed in arguments and returns this position
    private Position neighborClosestToFire(Position position) {
      Set<Position> seen = new HashSet<>();
      HashMap<Position, Position> firstMove = new HashMap<>();
      Queue<Position> toVisit = new LinkedList<>(neighbors(position)); //Queue is initialised with the neighbors of position
      for (Position initialMove : toVisit)
        firstMove.put(initialMove, initialMove);
      while (!toVisit.isEmpty()) {
        Position current = toVisit.poll();
        if (firePositions.contains(current))
          return firstMove.get(current);
        for (Position adjacent : neighbors(current)) {
          if (seen.contains(adjacent)) continue;
          toVisit.add(adjacent);
          seen.add(adjacent);
          firstMove.put(adjacent, firstMove.get(current));
        }
      }
      return position;
    }


    //Update the state of the fire and firefighters positions

  @Override
  public void setState(List<ModelElement> state, Position position) {
    firePositions.remove(position);
    for (;;) {
      if (!firefighterPositions.remove(position)) break;
    }
    for(ModelElement element : state){
      switch (element){
        case FIRE -> firePositions.add(position);
        case FIREFIGHTER -> firefighterPositions.add(position);
      }
    }
  }

}
