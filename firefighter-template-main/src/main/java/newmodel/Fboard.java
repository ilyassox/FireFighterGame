package newmodel;
import model.ModelElement;
import util.Position;
import view.ViewElement;

import java.util.*;

public class Fboard {

    protected static int columnCount;
    protected static int rowCount;
    private int step = 0;

    private final List<item> items;

    public Fboard(int columnCount, int rowCount,List<item> item) {
        this.items=item;
        Fboard.columnCount = columnCount;
        Fboard.rowCount = rowCount;
    }

    public int rowCount() {
        return rowCount;
    }

    public int columnCount() {
        return columnCount;
    }

    // Visitor for ID can be added
    public List<Position> updateToNextGeneration() {
        List<Position> result = new ArrayList<>();
        for(item item : items){
            if(item.getID()==0){
                result.addAll(item.update(this));
            }
        }
        for(item item : items){
            if(item.getID()==1 && step % 2 ==0){
                result.addAll(item.update(this));
            }
        }

        step++;
        return result;
    }

    public int stepNumber() {
        return step;
    }
    public void reset() {
        step = 0;
        initializeElements();
    }
    public void initializeElements() {
        for(item e: items) {
            if(e.getID()==1){
              e.initialize();
            }
        }
        for(item e:items){
            if(e.getID()==0){
                e.initialize();
            }
        }
    }

    public List<item> getItems(){
        return this.items;
    }


    public Fire getFire() {
        for (item e : items) {
            if (e.getID() == 1) {
                return (Fire) e;
            }
        }
        return null;
    }
    public List<Position> neighbors(Position position) {
        List<Position> list = new ArrayList<>();
        if (position.row() > 0) list.add(new Position(position.row() - 1, position.column()));
        if (position.column() > 0) list.add(new Position(position.row(), position.column() - 1));
        if (position.row() < rowCount - 1) list.add(new Position(position.row() + 1, position.column()));
        if (position.column() < columnCount - 1) list.add(new Position(position.row(), position.column() + 1));
        return list;
    }

    public Position neighborClosestToFire(Position position) {
        Set<Position> seen = new HashSet<>();
        HashMap<Position, Position> firstMove = new HashMap<>();
        Queue<Position> toVisit = new LinkedList<>(this.neighbors(position));
        for (Position initialMove : toVisit)
            firstMove.put(initialMove, initialMove);
        while (!toVisit.isEmpty()) {
            Position current = toVisit.poll();
            if (getFire().getPositions().contains(current))
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


    public List<ModelElement> getState(Position position){
        List<ModelElement> result = new ArrayList<>();
        for (item e :items) {
            if (e.getPositions().contains(position) && e.getID() == 1) {
                result.add(e.getState());
            }
            else if (e.getPositions().contains(position) && e.getID() == 0) {
                result.add(e.getState());
            }
            else{
                result.add(ModelElement.NOT);
            }
        }
        return result;
    }


}
