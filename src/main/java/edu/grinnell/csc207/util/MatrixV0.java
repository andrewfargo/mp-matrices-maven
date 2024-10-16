package edu.grinnell.csc207.util;

import java.util.Arrays;

/**
 * An implementation of two-dimensional matrices.
 *
 * @author Your Name Here
 * @author Samuel A. Rebelsky
 *
 * @param <T>
 *   The type of values stored in the matrix.
 */
public class MatrixV0<T> implements Matrix<T> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  T[] values;
  private T defaultValue;
  private int width;
  private int height;

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Get the flattened one-dimensional index from a two-dimensional
   * coordinate.
   *
   * @param i
   *   The first dimension coordinate of the matrix. (Row)
   * @param j
   *   The second dimension coordinate of the matrix. (Col)
   * @return The flattened index into the internal array.
   *
   * @pre 0 <= i < this.height
   * @pre 0 <= j < this.width
   *
   * @post Return is a valid position in internal representation.
   */
  private int getIndex(int i, int j) {
    return i + j * this.width;
  } // getIndex(int, int)

  /**
   * Double the size of the current array capacity until it can
   * fit a specified cell.
   * @param row
   *   The maximum row 
   */
  private void expand(int row, int col) {
    while (this.values.length < this.getIndex(row, col)) {
      this.values = Arrays.copyOf(this.values, this.values.length * 2);
    } // while
  } // expand()

  /**
   * Shift a single row by amount (positive or negative.)
   * Overwrites all previous values in the new row.
   * @param row
   *   The row to shift.
   * @param amount
   *   The amount of rows over it should end up.
   *   Positive is right, negative is left.
   * @post Expands the array until space is available
   *   for the new row.
   */
  private void shiftRow(int row, int amount) {
    // If space is unavailable, expand it
    this.expand(row + amount, this.width - 1);
    for (int col = 0; col < this.width; col++) {
      T val = this.values[this.getIndex(row, col)];
      this.values[this.getIndex(row + amount, col)] = val;
    } // for
  } // shiftRow(int)

  
  /**
   * Shift a single row by amount (positive or negative.)
   * Overwrites all previous values in the new row.
   * @param row
   *   The row to shift.
   * @param amount
   *   The amount of rows over it should end up.
   *   Positive is right, negative is left.
   * @post Expands the array until space is available
   *   for the new row.
   */
  private void shiftCol(int col, int amount) {
    // If space is unavailable, expand it
    this.expand(this.height - 1, col + amount);
    for (int row = 0; row < this.height; row++) {
      T val = this.values[this.getIndex(row, col)];
      this.values[this.getIndex(row, col + amount)] = val;
    } // for
  } // shiftCol(int)
  
  /**
   * Repeats an element of type T into a new array.
   * @param val The value to repeat.
   * @param len The length of the generated array.
   */
  @SuppressWarnings({"unchecked"})
  private T[] repeat(T val, int len) {
    T[] ret = (T[]) new Object[len];
    for (int i = 0; i < len; i++) {
      ret[i] = val;
    } // for
    return ret;
  }
  
  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new matrix of the specified width and height with the
   * given value as the default.
   *
   * @param width
   *   The width of the matrix.
   * @param height
   *   The height of the matrix.
   * @param def
   *   The default value, used to fill all the cells.
   *   NB: It is impossible to guarantee that this generic
   *       value has a clone() method; do not expect this
   *       value to have deep copies for each cell.
   * @see java.lang.Cloneable
   *
   * @throws NegativeArraySizeException
   *   If either the width or height are negative.
   */
  @SuppressWarnings({"unchecked"})
  public MatrixV0(int width, int height, T def) {
    if (width < 0 || height < 0) {
      throw new NegativeArraySizeException();
    } // if
    // Ensure that arrays of size 0 and 1 can expand if doubled.
    this.width = width;
    this.height = height;
    this.defaultValue = def;
    this.values = (T[]) new Object[2];
    this.expand(height - 1, width - 1);

    this.fillRegion(0, 0, height, width, def);
  } // MatrixV0(int, int, T)

  /**
   * Create a new matrix of the specified width and height with
   * null as the default value.
   *
   * @param width
   *   The width of the matrix.
   * @param height
   *   The height of the matrix.
   *
   * @throws NegativeArraySizeException
   *   If either the width or height are negative.
   */
  public MatrixV0(int width, int height) {
    this(width, height, null);
  } // MatrixV0

  // +--------------+------------------------------------------------
  // | Core methods |
  // +--------------+

  /**
   * Get the element at the given row and column.
   *
   * @param row
   *   The row of the element.
   * @param col
   *   The column of the element.
   *
   * @return the value at the specified location.
   *
   * @throws IndexOutOfBoundsException
   *   If either the row or column is out of reasonable bounds.
   */
  public T get(int row, int col) {
    if (this.getIndex(row, col) >= this.values.length) {
      throw new IndexOutOfBoundsException();
    } // if
    return this.values[this.getIndex(row, col)];
  } // get(int, int)

  /**
   * Set the element at the given row and column.
   *
   * @param row
   *   The row of the element.
   * @param col
   *   The column of the element.
   * @param val
   *   The value to set.
   *
   * @throws IndexOutOfBoundsException
   *   If either the row or column is out of reasonable bounds.
   */
  public void set(int row, int col, T val) {
    if (this.getIndex(row, col) >= this.values.length) {
      throw new IndexOutOfBoundsException();
    } // if
    this.values[this.getIndex(row, col)] = val;
  } // set(int, int, T)

  /**
   * Determine the number of rows in the matrix.
   *
   * @return the number of rows.
   */
  public int height() {
    return this.height;
  } // height()

  /**
   * Determine the number of columns in the matrix.
   *
   * @return the number of columns.
   */
  public int width() {
    return this.width;
  } // width()

  /**
   * Insert a row filled with the default value.
   *
   * @param row
   *   The number of the row to insert.
   *
   * @throws IndexOutOfBoundsException
   *   If the row is negative or greater than the height.
   */
  public void insertRow(int row) {
    try {
      this.insertRow(row, this.repeat(this.defaultValue, this.width));
    } catch (ArraySizeException e) {
      // Something has seriously gone wrong
      throw new RuntimeException(e.getMessage());
    } // try/catch
  } // insertRow(int)

  /**
   * Insert a row filled with the specified values.
   *
   * @param row
   *   The number of the row to insert.
   * @param vals
   *   The values to insert.
   *
   * @throws IndexOutOfBoundsException
   *   If the row is negative or greater than the height.
   * @throws ArraySizeException
   *   If the size of vals is not the same as the width of the matrix.
   */
  public void insertRow(int row, T[] vals) throws ArraySizeException {
    if (row < 0 || row > this.height) {
      throw new IndexOutOfBoundsException("Provided row exceeds height of MatrixV0");
    } // if (throws out)
    if (vals.length != this.width) {
      throw new ArraySizeException("Inserted row has invalid width");
    } // if (throws out)

    // Fit the new row
    this.expand(row, this.width - 1);

    // Increase the height
    this.height++;
    
    // Shift all rows down one
    for (int cRow = this.height - 1; cRow >= row; cRow++) {
      this.shiftRow(cRow, 1);
    } // for cRow

    // Copy over the values
    for (int col = 0; col < this.width; col++) {
      this.set(row, col, vals[col]);
    } // for col
  } // insertRow(int, T[])

  /**
   * Insert a column filled with the default value.
   *
   * @param col
   *   The number of the column to insert.
   *
   * @throws IndexOutOfBoundsException
   *   If the column is negative or greater than the width.
   */
  public void insertCol(int col) {
    try {
      this.insertCol(col, this.repeat(this.defaultValue, this.height));
    } catch (ArraySizeException e) {
      // Something has gone (yet again) seriously wrong.
      throw new RuntimeException(e.getMessage());
    } // try/catch
  } // insertCol(int)

  /**
   * Insert a column filled with the specified values.
   *
   * @param col
   *   The number of the column to insert.
   * @param vals
   *   The values to insert.
   *
   * @throws IndexOutOfBoundsException
   *   If the column is negative or greater than the width.
   * @throws ArraySizeException
   *   If the size of vals is not the same as the height of the matrix.
   */
  public void insertCol(int col, T[] vals) throws ArraySizeException {
    if (col < 0 || col > this.width) {
      throw new IndexOutOfBoundsException("Provided col exceeds width of MatrixV0");
    } // if throws out
    if (vals.length != this.height) {
      throw new ArraySizeException("Inserted col has invalid height");
    } // if throws out

    // Fit the new col
    this.expand(this.height - 1, col);

    // Increase the width
    this.width++;

    // Shift all cols right one
    // Accounts for new width
    for (int cCol = this.width - 2; cCol >= col; cCol++) {
      this.shiftCol(cCol, 1);
    } // for

    // Copy over the values
    for (int row = 0; row < this.height; row++) {
      this.set(row, col, vals[row]);
    } // for
  } // insertCol(int, T[])

  /**
   * Delete a row.
   *
   * @param row
   *   The number of the row to delete.
   *
   * @throws IndexOutOfBoundsException
   *   If the row is negative or greater than or equal to the height.
   */
  public void deleteRow(int row) {
    if (row < 0 || row >= this.height) {
      throw new IndexOutOfBoundsException();
    } // if throws out

    // Shift all rows up one
    for (int cRow = row + 1; row < this.height; row++) {
      this.shiftRow(cRow, -1);
    } // for

    // Decrement the height
    this.height--;
  } // deleteRow(int)

  /**
   * Delete a column.
   *
   * @param col
   *   The number of the column to delete.
   *
   * @throws IndexOutOfBoundsException
   *   If the column is negative or greater than or equal to the width.
   */
  public void deleteCol(int col) {
        if (col < 0 || col >= this.width) {
      throw new IndexOutOfBoundsException();
    } // if throws out

    // Shift all cols up one
    for (int cCol = col + 1; col < this.width; col++) {
      this.shiftCol(cCol, -1);
    } // for

    // Decrement the width
    this.width--;
  } // deleteCol(int)

  /**
   * Fill a rectangular region of the matrix.
   *
   * @param startRow
   *   The top edge / row to start with (inclusive).
   * @param startCol
   *   The left edge / column to start with (inclusive).
   * @param endRow
   *   The bottom edge / row to stop with (exclusive).
   * @param endCol
   *   The right edge / column to stop with (exclusive).
   * @param val
   *   The value to store.
   *
   * @throw IndexOutOfBoundsException
   *   If the rows or columns are inappropriate.
   */
  public void fillRegion(int startRow, int startCol, int endRow, int endCol,
      T val) {
    for (int row = startRow; row < endRow; row++) {
      for (int col = startCol; col < endCol; col++) {
	this.set(row, col, val);
      } // for col
    } // for row
  } // fillRegion(int, int, int, int, T)

  /**
   * Fill a line (horizontal, vertical, diagonal).
   *
   * @param startRow
   *   The row to start with (inclusive).
   * @param startCol
   *   The column to start with (inclusive).
   * @param deltaRow
   *   How much to change the row in each step.
   * @param deltaCol
   *   How much to change the column in each step.
   * @param endRow
   *   The row to stop with (exclusive).
   * @param endCol
   *   The column to stop with (exclusive).
   * @param val
   *   The value to store.
   *
   * @throw IndexOutOfBoundsException
   *   If the rows or columns are inappropriate.
   */
  public void fillLine(int startRow, int startCol, int deltaRow, int deltaCol,
      int endRow, int endCol, T val) {
    for (int row = startRow; row < endRow; row += deltaRow) {
      for (int col = startCol; row < endCol; col += deltaCol) {
	this.set(row, col, val);
      } // for col
    } // for row
  } // fillLine(int, int, int, int, int, int, T)

  /**
   * A make a copy of the matrix. May share references (e.g., if individual
   * elements are mutable, mutating them in one matrix may affect the other
   * matrix) or may not.
   *
   * @return a copy of the matrix.
   */
  public Matrix<T> clone() {
    MatrixV0<T> newMatrix = new MatrixV0<T>(this.width, this.height, this.defaultValue);
    for (int i = 0; i < (this.width * this.height); i++) {
      newMatrix.values[i] = this.values[i];
    } // for
    return newMatrix;
  } // clone()

  /**
   * Determine if this object is equal to another object.
   *
   * @param other
   *   The object to compare.
   *
   * @return true if the other object is a matrix with the same width,
   * height, and equal elements; false otherwise.
   */
  @SuppressWarnings({"unchecked"})
  public boolean equals(Object other) {
    // Whoa, generics are fun
    return (other.getClass().isInstance((this.getClass().getInterfaces()[0]))
	    && this.equals((Matrix<T>) other));
  } // equals(Object)

  public boolean equals(Matrix<T> other) {
    if (other.width() != this.width || other.height() != this.height()) {
      return false;
    } // if
    for (int row = 0; row < this.height; row++) {
      for (int col = 0; col < this.width; col++) {
	if (!this.get(row, col).equals(other.get(row, col))) {
	  return false;
	} // if
      } // for col
    } // for row
    return true;
  } // equals(Matrix<T>)

  /**
   * Compute a hash code for this matrix. Included because any object
   * that implements `equals` is expected to implement `hashCode` and
   * ensure that the hash codes for two equal objects are the same.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int multiplier = 7;
    int code = this.width() + multiplier * this.height();
    for (int row = 0; row < this.height(); row++) {
      for (int col = 0; col < this.width(); col++) {
        T val = this.get(row, col);
        if (val != null) {
          // It's okay if the following computation overflows, since
          // it will overflow uniformly.
          code = code * multiplier + val.hashCode();
        } // if
      } // for col
    } // for row
    return code;
  } // hashCode()
} // class MatrixV0
