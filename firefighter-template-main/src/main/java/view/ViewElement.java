package view;

import javafx.scene.paint.Color;

//this class declare three constants with each constants is related to a specified color like fire to red
public enum ViewElement {
  FIREFIGHTER(Color.DARKBLUE), FIRE(Color.RED), EMPTY(Color.WHITE) ,NUAGE(Color.LIGHTBLUE);
  final Color color;
  ViewElement(Color color) {
    this.color = color;
  }
}
