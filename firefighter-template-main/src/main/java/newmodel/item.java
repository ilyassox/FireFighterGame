package newmodel;

import model.ModelElement;
import util.Position;

import java.util.List;

public interface item {
    public List<Position> update(Fboard board);
    public ModelElement getState();

    public int getID();
    public void initialize();
    public List<Position> getPositions();
}
