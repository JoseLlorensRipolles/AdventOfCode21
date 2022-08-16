package me.josellorens.aoc2021.day05;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point point(int x, int y) {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Point add(Point point){
        return point(x + point.x, y + point.y);
    }

    public Point clip(int maxX, int maxY){
        if (this.x < 0) this.x = 0;
        if (this.y < 0) this.y = 0;
        if (this.x > maxX) this.x = maxX;
        if (this.y > maxY) this.y = maxY;
        return this;
    }

    public Set<Point> adjacentIncludingDiagonals(int maxX, int maxY){
        final var diagonalOffset = Set.of(
            point(-1, -1),
            point(0, -1),
            point(1, -1),
            point(-1, 0),
            point(1, 0),
            point(-1, 1),
            point(0, 1),
            point(1, 1)
        );

        return diagonalOffset
            .stream()
            .map(this::add)
            .filter(it -> it.x >= 0)
            .filter(it -> it.x <= maxX)
            .filter(it -> it.y >= 0)
            .filter(it -> it.y <= maxY)
            .collect(toSet());
    }
}
