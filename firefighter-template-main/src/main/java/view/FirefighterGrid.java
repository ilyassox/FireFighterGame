package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import util.Position;

import java.util.List;

public class FirefighterGrid extends Canvas implements Grid<ViewElement>{

    //this function paint a certain position passed into the arguments with corresponding element color
    private void paintElementAtPosition(ViewElement element, Position position) {
        paintSquare(position.row(), position.column(), element.color);
    }
    private int squareWidth;
    private int squareHeight;
    private int columnCount;
    private int rowCount;

//this function clears the colors of the postions passed in the arguments and repaint the positions with the corresponding
    //colors of the elements then print the lines of the board
    @Override
    public void repaint(List<Pair<Position, ViewElement>> positionedElements) {
        clear(positionedElements);
        paint(positionedElements);
        paintLines();
    }

//this function clears the colors of the positions passed in the arguments
    private void clear(List<Pair<Position, ViewElement>> positionedElements) {
        for (Pair<Position, ViewElement> positionElement : positionedElements) {
            Position position = positionElement.getKey();
            clearSquare(position.row(), position.column());
        }
    }
    //this function combines the element with it's position and paint it with the corresponding color of the elements
    private void paint(List<Pair<Position, ViewElement>> positionedElements) {
        for(Pair<Position, ViewElement> pair : positionedElements){
            paintElementAtPosition(pair.getValue(), pair.getKey());
        }
    }

    //this function repaints everything from the lines to the positions
    @Override
    public void repaint(ViewElement[][] elements) {
        clear();
        paint(elements);
        paintLines();
    }

    //this function clears all the colors in the board for a fresh frame
    private void clear() {
        getGraphicsContext2D().clearRect(0,0,getWidth(), getHeight());
    }

    //this function call another function to paint all the positions of the board with the corresponding color of each item fire or firefighter
    // in the list passed in the arguments
    private void paint(ViewElement[][] elements) {
        for(int column = 0; column < columnCount; column++)
            for(int row = 0; row < rowCount; row++){
                paintElementAtPosition(elements[row][column], new Position(row, column));
            }
    }

    public int columnCount() {
        return columnCount;
    }

    public int rowCount() {
        return rowCount;
    }

    //This Function initialise all the parameteres(int columnCount, int rowCount, int squareWidth, int squareHeight)
    @Override
    public void setDimensions(int columnCount, int rowCount, int squareWidth, int squareHeight) {
        this.squareWidth = squareWidth;
        this.squareHeight = squareHeight;
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        super.setWidth(squareWidth*columnCount);
        super.setHeight(squareHeight*rowCount);
    }

    private void paintLines(){
        paintHorizontalLines();
        paintVerticalLines();
    }
//this function draws the vertical lines
    private void paintVerticalLines() {
        for(int column = 0; column < columnCount; column++)
            getGraphicsContext2D().strokeLine(column*squareWidth, 0,column*squareWidth, getHeight());
    }
//this function draws the horizontal lines
    private void paintHorizontalLines() {
        for(int row = 0; row < rowCount; row++)
            getGraphicsContext2D().strokeLine(0, row*squareHeight, getWidth(), row*squareHeight);
    }

    //this function color each case in the board with corresponding elments fire or firefighter
    private void paintSquare(int row, int column, Color color){
        getGraphicsContext2D().setFill(color);
        getGraphicsContext2D().fillRect(column*squareHeight,row*squareWidth,squareHeight,squareWidth);
    }
    //this function clear the color in each case in the board passed in the arguments
    private void clearSquare(int row, int column){
        getGraphicsContext2D().clearRect(column*squareHeight,row*squareWidth,squareHeight,squareWidth);
    }
}