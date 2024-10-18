package edu.grinnell.csc207.util;

import static edu.grinnell.csc207.util.MatrixAssertions.assertMatrixEquals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

/**
 * Specialized tests for the Matrix class.
 *
 * @author Andrew N. Fargo
 */
class TestsByMe {
  /**
   * Manipulate matrices with height or width 0.
   */
  @Test
  public void testZeroDimensions() throws ArraySizeException {
    // Initialize with width zero
    Matrix<Integer> matrix0 = new MatrixV0<Integer>(0, 5);

    // Add a column
    matrix0.insertCol(0, new Integer[] {1, 2, 3, 4, 5});

    // Check to see if column is right height
    assertEquals(5, matrix0.height(),
		 "0 width matrix preserves height");
    
    // Check to see if column has right contents
    assertMatrixEquals(new Integer[][] {{1},{2},{3},{4},{5}}, matrix0,
		       "Insertion of column on 0 width.");
    
    // Initialize with height zero
    Matrix<Integer> matrix1 = new MatrixV0<Integer>(5, 0);
    
    // Add a row
    matrix1.insertRow(0, new Integer[] {1, 2, 3, 4, 5});
    
    // Check to see if row is right width
    assertEquals(5, matrix1.width(),
		 "0 height matrix preserves width");

    // Check to see if row has right contents
    assertMatrixEquals(new Integer[][] {{1, 2, 3, 4, 5}}, matrix1,
		       "Insertion of row on 0 height.");
    
    // Initialize with width and height zero
    Matrix<Integer> matrix2 = new MatrixV0<Integer>(0, 0);

    // Add a column, then a row
    matrix2.insertCol(0, new Integer[] {});
    matrix2.insertRow(0, new Integer[] {1});

    // Check to see if we are right width and height
    assertEquals(1, matrix2.width(),
		 "Matrix width proper on 0-dimensional input.");
    assertEquals(1, matrix2.height(),
		 "Matrix height proper on 0-dimensional input.");

    // Check the cell
    assertEquals(1, matrix2.get(0, 0),
		 "Expansion of zero-dimensional matrix.");
  } // testZeroDimensions()

  /**
   * Test the ability to clone matrices.
   */
  @Test
  public void testClone() {

  } // testClone()

  /**
   * Test for side effects in erroneous operations.
   * This test could certainly be more robust, with
   * multiple types of bad input, but I tried to pick
   * examples that would likely produce side effects.
   */
  @Test
  public void testSideEffects() {
    // Define two identical matrices
    Matrix<Integer> control = new MatrixV0<Integer>(3, 3, 0);
    Matrix<Integer> test = new MatrixV0<Integer>(3, 3, 0);

    // Insert a column erroneously
    try {
      test.insertCol(0, new Integer[] {1, 2, 3, 4});
    } catch (Exception e) {
      assertTrue(control.equals(test),
		 "No side effects when inserting column erroneously.");
    } // try/catch

    // Insert a row erroneously
    try {
      test.insertRow(0, new Integer[] {1, 2, 3, 4});
    } catch (Exception e) {
      assertTrue(control.equals(test),
		 "No side effects when inserting row erroneously.");
    } // try/catch

    // Fill a region erroneously
    try {
      test.fillRegion(0, 0, 4, 4, 1);
    } catch (Exception e) {
      assertTrue(control.equals(test),
		 "No side effects when filling region erroneously.");
    } // try/catch

    // Fill a line erroneously
    try {
      test.fillLine(0, 0, 1, 1, 4, 4, 1);
    } catch (Exception e) {
      assertTrue(control.equals(test),
		 "No side effects when filling line erroneously.");
    } // try/catch

    // Delete a column erroneously
    try {
      test.deleteCol(3);
    } catch (Exception e) {
      assertTrue(control.equals(test),
		 "No side effects when deleting column erroneously.");
    } // try/catch

    // Delete a row erroneously
    try {
      test.deleteRow(3);
    } catch (Exception e) {
      assertTrue(control.equals(test),
		 "No side effects when deleting row erroneously.");
    } // try/catch
  } // testSideEffects()

  /**
   * Test inverse operations.
   */
  @Test
  public void testInverse() throws ArraySizeException {
    // Define two identical matrices.
    Matrix<Integer> control = new MatrixV0<Integer>(3, 3, 0);
    Matrix<Integer> test = new MatrixV0<Integer>(3, 3, 0);

    // Delete then add a row
    test.deleteRow(2);
    test.insertRow(2);

    test.insertRow(0, new Integer[] {1, 1, 1});
    test.deleteRow(0);

    assertTrue(control.equals(test), "Deleting and inserting a row works.");

    // Delete then add a column
    test.deleteCol(2);
    test.insertCol(2);

    test.insertCol(0, new Integer[] {1, 1, 1});
    test.deleteCol(0);

    assertTrue(control.equals(test), "Deleting and inserting a col works.");

    // Set then set back a value

    for (int row = 0; row < test.height(); row++) {
      for (int col = 0; col < test.width(); col++) {
	test.set(row, col, 1);
	test.set(row, col, 0);
      } // for col
    } // for row

    assertTrue(control.equals(test), "Setting and unsetting every value works.");

    // Fill and unfill a line

    test.fillLine(0, 0, 1, 1, 3, 3, 1);
    test.fillLine(0, 0, 1, 1, 3, 3, 0);
    assertTrue(control.equals(test), "Filling and unfilling a line works.");

    // Fill and unfill a region

    test.fillRegion(0, 0, 3, 3, 1);
    test.fillRegion(0, 0, 3, 3, 0);
    assertTrue(control.equals(test), "Filling and unfilling a region works.");
  } // testInverse()
}
