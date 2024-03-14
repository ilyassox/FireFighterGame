package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;
import javafx.util.Pair;
import model.Board;
import model.ModelElement;
import model.FirefighterBoard;
import newmodel.*;
import util.Position;
import view.Grid;
import view.ViewElement;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class Controller {

  public static final int PERIOD_IN_MILLISECONDS = 50;
  @FXML
  public Button restartButton;
  @FXML
  public Button oneStepButton;
  @FXML
  public Label generationNumberLabel;
  @FXML
  private ToggleButton pauseToggleButton;
  @FXML
  private ToggleButton playToggleButton;
  @FXML
  private Grid<ViewElement> grid;
  private Timeline timeline;
  private Fboard board;


  //this function initialise the frame of the execution of updateboard function and adds the play&pause buttons
  @FXML
  private void initialize() {
    initializePlayAndPauseToggleButtons();
    initializeTimeline();
  }
// function that add the play and pause button
  private void initializePlayAndPauseToggleButtons() {
    ToggleGroup toggleGroup = new PersistentToggleGroup();
    toggleGroup.getToggles().addAll(playToggleButton, pauseToggleButton);
    pauseToggleButton.setSelected(true);
  }

  //this function checks if our model of the class firefighterboard is not null and raises an exception if so
  // and affect the model to board
  private void setModel(Fboard board) {
    this.board = requireNonNull(board, "firefighter.model is null");
  }

  //this function is a combination of multiple functions that re update the board elements get the new positions and colors of new elements
  //and then repaint the new board and increment the generation label
  private void updateBoard(){
    List<Position> updatedPositions = board.updateToNextGeneration();
    //this function calls both update firefighters and updatefire functions so they can be updated to their correponding
    // new position and sum up the results and returns them also it increments the step variable
//         // (*firePositions contient last fireposition after the last extinguish and new fire positions +
// *modifiedPosition contient Last fire fighter positions and New Firefighterpositions with neighbor of every new firefighter)
    List<Pair<Position, ViewElement>> updatedSquares = new ArrayList<>();
    for(Position updatedPosition : updatedPositions){
      List<ModelElement> squareState = board.getState(updatedPosition);
      // Function that return the state (Fire or Firefighter) of a certain position
      ViewElement viewElement = getViewElement(squareState);
      // Function that return the state (Fire or Firefighter or empty) of an element

      updatedSquares.add(new Pair<>(updatedPosition, viewElement));
    }
    grid.repaint(updatedSquares);
    updateGenerationLabel(board.stepNumber());
  }

  //this function takes all the states from the model then clears the actual the grid and repaints everything from the positions to the
  //grid lines and update the number of generation label also
  private void repaintGrid(){
    int columnCount = board.columnCount();
    int rowCount = board.rowCount();
    ViewElement[][] viewElements = new ViewElement[rowCount][columnCount];
    for(int column = 0; column < columnCount; column++)
      for(int row = 0; row < rowCount; row++)
        viewElements[row][column] = getViewElement(board.getState(new Position(row, column)));
    grid.repaint(viewElements);
    updateGenerationLabel(board.stepNumber());
  }

//this function check a modelelement and returns if this model is a firefighter or fire or empty
  private ViewElement getViewElement(List<ModelElement> squareState) {
    if(squareState.contains(ModelElement.FIREFIGHTER)){
      return ViewElement.FIREFIGHTER;
    }
    if (squareState.contains(ModelElement.FIRE)){
      return ViewElement.FIRE;
    }
    if(squareState.contains(ModelElement.NUAGE)){
      return ViewElement.NUAGE;
    }
    return ViewElement.EMPTY;
  }

  //this function set the action to updateboard for each duration of the frame and looping for infinite amount of time
  private void initializeTimeline() {
    Duration duration = new Duration(Controller.PERIOD_IN_MILLISECONDS);
    EventHandler<ActionEvent> eventHandler =
            event -> updateBoard();
    KeyFrame keyFrame = new KeyFrame(duration, eventHandler);
    timeline = new Timeline(keyFrame);
    timeline.setCycleCount(Animation.INDEFINITE);
  }

  // this function causes to play any ongoing animation or sequences
  public void play() {
    timeline.play();
  }

  // this function causes to stop any ongoing animation or sequences
  public void pause() {
    timeline.pause();
  }

  //this function execute the pause function while toggling the button
  public void pauseToggleButtonAction() {
    this.pause();
  }

  //this function execute the play function while toggling the button
  public void playToggleButtonAction() {
    this.play();
  }

  //this function does a pause to the animation and sequences then does a rest to the parametres and repaint the board based on the new
  //parametres that are random positions for fire and firefighters
  public void restartButtonAction() {
    this.pause();
    board.reset();
    pauseToggleButton.setSelected(true);
    repaintGrid();
  }

  //this function initialise all parametres of the games including the firefighter model and the grids
  public void initialize(int squareWidth, int squareHeight, int columnCount,
                                int rowCount, int initialFireCount, int initialFirefighterCount) {
    grid.setDimensions(columnCount, rowCount, squareWidth, squareHeight);
    List<item> items= new ArrayList<>();
    items.add(new Fire(initialFireCount));
    items.add(new FireFighter(initialFirefighterCount));
    items.add(new Nuage(initialFirefighterCount));
    Fboard b=new Fboard(columnCount,rowCount,items);
    b.initializeElements();
    this.setModel(b);
    repaintGrid();
  }

  //this function causes the sequences and animnation to stop then does a signle update to the board
  public void oneStepButtonAction() {
    this.pause();
    updateBoard();
  }
// Function that updates the value of variable generatioNumberLAbel
  private void updateGenerationLabel(int value){
    generationNumberLabel.setText(Integer.toString(value));
  }
}