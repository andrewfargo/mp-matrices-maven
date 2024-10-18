package edu.grinnell.csc207.util;

import java.util.Arrays;

/**
 * An implementation of two-dimensional matrices.
 *
 * @author Andrew N. Fargo
 * @author Samuel A. Rebelsky
 *
 * @param <T>
 *   The type of values stored in the matrix.
 */
public class MatrixV0<T> implements Matrix<T> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** The array itself. The lengths are (after operations) exactly
      the dimensions of the array representation. */
  T[][] values;

  /** Default value for array insertions. Notably a reference. */
  private T defaultValue;

  /** Used as an alias for this.values[0].length;
      absolutely necessary since matrix may have 0
      rows validly, where inserting a row should
      create a row of size `cols`. */
  private int cols;

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Repeats an element of type T into a new array.
   * @param val The value to repeat.
   * @param len The length of the generated array.
   * @return [val, val, val, ... val] (len times)
   */
  @SuppressWarnings({"unchecked"})
  private T[] repeat(T val, int len) {
    T[] ret = (T[]) new Object[len];
    for (int i = 0; i < len; i++) {
      ret[i] = val;
    } // for
    return ret;
  } // repeat(T, int)

  /**
   * Shifts each row in the matrix by `amount` spaces.
   *
   * @param row The starting row. Goes until the end.
   * @param amount The number of rows we shift by, negative
   *   is valid and goes in the opposite direction.
   * @pre row + amount is in bounds of this.values.
   */
  private void shiftRows(int row, int amount) {
    T[][] newArr = Arrays.copyOf(this.values, this.values.length);
    for (int r = 0; r < this.height(); r++) {
      newArr[r] = Arrays.copyOf(this.values[r], this.cols);
    } // for
    int startRow = Math.max(row, row - amount);
    int endRow = Math.min(this.height(), this.height() - amount);
    for (int i = startRow; i < endRow; i++) {
      for (int col = 0; col < this.width(); col++) {
        newArr[i + amount][col] = this.values[i][col];
      } // for
    } // for
    this.values = newArr;
  } // shiftRow(int, int)

  /**
   * Shifts each column in the matrix by `amount` spaces.
   *
   * @param col The starting col. Goes until the end.
   * @param amount The number of cols we shift by, negative
   *   is valid and goes in the opposite direction.
   * @pre col + amount is in bounds of this.values[i] for all valid i.
   */
  private void shiftCols(int col, int amount) {
    int startCol = Math.max(col, col - amount);
    int endCol = Math.min(this.width(), this.width() - amount);
    for (int row = 0; row < this.height(); row++) {
      T[] newArr = Arrays.copyOf(this.values[row], this.cols);
      for (int i = startCol; i < endCol; i++) {
        newArr[i + amount] = this.values[row][i];
      } // for
      this.values[row] = newArr;
    } // for
  } // shiftCol(int, int)

  /**
   * Checks if the cell (row, col) is within bounds of the matrix.
   * @param row The row of the cell.
   * @param col The col of the cell.
   * @return true if this.values[row][col] will throw a bounds exception.
   */
  private boolean outOfBounds(int row, int col) {
    return row < 0 || row >= this.height() || col < 0 || col >= this.width();
  } // inBounds(int, int)
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
    this.defaultValue = def;
    this.values = (T[][]) new Object[height][];
    this.cols = width;
    for (int i = 0; i < height; i++) {
      this.values[i] = (T[]) new Object[width];
    } // for

    this.fillRegion(0, 0, this.height(), this.width(), def);
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
    if (this.outOfBounds(row, col)) {
      throw new IndexOutOfBoundsException();
    } // if
    return this.values[row][col];
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
    if (this.outOfBounds(row, col)) {
      throw new IndexOutOfBoundsException();
    } // if
    this.values[row][col] = val;
  } // set(int, int, T)

  /**
   * Determine the number of rows in the matrix.
   *
   * @return the number of rows.
   */
  public int height() {
    return this.values.length;
  } // height()

  /**
   * Determine the number of columns in the matrix.
   *
   * @return the number of columns.
   */
  public int width() {
    return this.cols;
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
      this.insertRow(row, this.repeat(this.defaultValue, this.width()));
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
    if (row < 0 || row > this.height()) {
      throw new IndexOutOfBoundsException("Provided row exceeds height of MatrixV0");
    } // if (throws out)
    if (vals.length != this.width()) {
      throw new ArraySizeException("Inserted row has invalid width");
    } // if (throws out)

    // Reallocate
    this.values = Arrays.copyOf(this.values, this.values.length + 1);
    this.values[this.values.length - 1] = this.repeat(null, this.cols);

    // Shift
    this.shiftRows(row, 1);

    // Place
    this.values[row] = vals;
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
      this.insertCol(col, this.repeat(this.defaultValue, this.height()));
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
    if (col < 0 || col > this.width()) {
      throw new IndexOutOfBoundsException("Provided col exceeds width of MatrixV0");
    } // if throws out
    if (vals.length != this.height()) {
      throw new ArraySizeException("Inserted col has invalid height");
    } // if throws out

    // Reallocate
    for (int row = 0; row < this.height(); row++) {
      this.values[row] = Arrays.copyOf(this.values[row], this.values[row].length);
    } // for
    this.cols++;

    // Shift
    this.shiftCols(col, 1);

    // Place
    for (int i = 0; i < vals.length; i++) {
      this.values[i][col] = vals[i];
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
    if (row < 0 || row >= this.height()) {
      throw new IndexOutOfBoundsException();
    } // if throws out

    // Shift
    this.shiftRows(row, -1);

    // Reallocate
    this.values = Arrays.copyOf(this.values, this.values.length - 1);

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
    if (col < 0 || col >= this.width()) {
      throw new IndexOutOfBoundsException();
    } // if throws out

    // Shift
    this.shiftCols(col, -1);

    // Reallocate
    for (int row = 0; row < this.height(); row++) {
      this.values[row] = Arrays.copyOf(this.values[row],
                                       this.values[row].length - 1);
    } // for

    this.cols--;
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
    if (outOfBounds(startRow, startCol)) {
      throw new IndexOutOfBoundsException();
    } else if (endRow - startRow == 0 || endCol - startCol == 0) {
      return;
    } else if (outOfBounds(endRow - 1, endCol - 1)) {
      throw new IndexOutOfBoundsException();
    } // if/else

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
    if (outOfBounds(startRow, startCol) || outOfBounds(endRow - 1, endCol - 1)) {
      throw new IndexOutOfBoundsException();
    } // if

    for (int row = startRow, col = startCol;
         row < endRow && col < endCol;
         row += deltaRow, col += deltaCol) {
      this.set(row, col, val);
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
    MatrixV0<T> newMatrix = new MatrixV0<T>(this.width(),
                                            this.height(), this.defaultValue);
    for (int row = 0; row < this.height(); row++) {
      for (int col = 0; col < this.height(); col++) {
        newMatrix.set(row, col, this.get(row, col));
      } // for
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
    return (other instanceof Matrix)
            && this.equals((Matrix<T>) other);
  } // equals(Object)

  private boolean equals(Matrix<T> other) {
    if (other.width() != this.width() || other.height() != this.height()) {
      return false;
    } // if
    for (int row = 0; row < this.height(); row++) {
      for (int col = 0; col < this.width(); col++) {
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
