package newmodel;
import model.ModelElement;
import newmodel.Fboard;
import util.Position;
import java.util.List;
public abstract class Extinguisher implements item {

        protected static final int ID = 0 ;

        public abstract List<Position> update(Fboard board);
        public abstract void extinguish(Position position,Fboard board);

        public abstract ModelElement getState();

        public abstract void initialize();
        public abstract List<Position> getPositions();

    }


