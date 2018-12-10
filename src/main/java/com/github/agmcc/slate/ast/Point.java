package com.github.agmcc.slate.ast;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Point implements Comparable<Point> {

  private final int line;

  private final int column;

  @Override
  public int compareTo(Point other) {
    if (line < other.line || (line == other.line) && column < other.column) {
      return -1;
    } else if (column == other.column && line == other.line) {
      return 0;
    } else {
      return 1;
    }
  }

  @Override
  public String toString() {
    return "[" + line + "," + column + "]";
  }
}
