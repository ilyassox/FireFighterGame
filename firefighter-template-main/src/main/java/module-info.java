module firefighter {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  opens controller to javafx.fxml;
  exports app;
  opens app to javafx.fxml;
}
