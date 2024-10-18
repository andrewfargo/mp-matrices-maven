# Matrices (two-dimensional arrays)

A project for CSC-207.

Authors:

* Andrew N. Fargo
* Samuel A. Rebelsky (starter code)

---

Overview:

This package provides an implementation of a 2-dimensional matrix of generic values.
In addition to providing an interface for using these matrices, namely `Matrix<T>`,
an implementation is provided for this interface, `MatrixV0<T>`.

`MatrixV0<T>` relies upon Java's ability to define two-dimensional arrays,
but other implementations are possible and may be more efficient.

`Matrix<T>` defines (or overrides) the following public methods:
- `public static <T> void print(PrintWriter, Matrix<T>[, boolean])`
  prints a generic matrix [with or without labels].
- `public T get(int, int)`
  gets an element from the matrix.
- `public void set(int, int, T)`
  sets an element to the matrix.
- `public int height()`
  returns the height of the matrix.
- `public int width()`
  returns the width of the matrix.
- `public void insertRow(int, [T[] vals])`
  inserts a row at a position [with values].
- `public void insertCol(int, [T[] vals])`
  inserts a column at a position [with values].
- `public void deleteRow(int)` deletes a row.
- `public void deleteCol(int)` deletes a column.
- `public void fillRegion(int, int, int, int, T)` fills a region of the matrix.
- `public void fillLine(int, int, int, int, int, int, T)`
  fills a line of the matrix.
- `public Matrix<T> clone()` satisfies `Cloneable` and clones the matrix
  (maintaining references to objects inside.)
- `public boolean equals(Object)` overrides the `Object` method and compares
  values within the matrix for equality.

---

Citations:

- Heavy guidance for this project was obtained from the assignment specification written by Samuel A. Rebelsky.
  The specification may be found at <https://rebelsky.cs.grinnell.edu/Courses/CSC207/2024Fa/mps/mp06.html>
- Information about Java's Arrays class was found on the Java 17 Oracle docs.
  You may read the specific page referenced here: <https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Arrays.html>

---

This code may be found at <https://github.com/andrewfargo/mp-matrices-maven>. 

The original code may be found at <https://github.com/Grinnell-CSC207/mp-matrices-maven>.
