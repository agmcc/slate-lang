package com.github.agmcc.slate.ast;

import lombok.Data;

@Data
public class Position implements Comparable<Position> {

  private final Point start;

  private final Point end;

  public static Position of(int startLine, int startCol, int endLine, int endCol) {
    return new Position(new Point(startLine, startCol), new Point(endLine, endCol));
  }

  @Override
  public int compareTo(Position other) {
    return start.compareTo(other.start);
  }
}
