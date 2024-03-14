package view;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FirefighterGridTest {
  @Test
  void testColumnCount(){
    Grid<ViewElement> grid = new FirefighterGrid();
    grid.setDimensions(20,10,10,10);
    assertThat(grid.columnCount()).isEqualTo(20);
  }
  @Test
  void testRowCount(){
    Grid<ViewElement> grid = new FirefighterGrid();
    grid.setDimensions(20,10,10,10);
    assertThat(grid.rowCount()).isEqualTo(10);
  }
}
