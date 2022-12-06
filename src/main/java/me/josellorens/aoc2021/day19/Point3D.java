package me.josellorens.aoc2021.day19;

import java.util.Objects;

public class Point3D {
    public int x;
    public int y;
    public int z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Point3D point3D(int x, int y, int z) {
        return new Point3D(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point = (Point3D) o;
        return x == point.x && y == point.y && z == point.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public Point3D add(Point3D point) {
        return point3D(x + point.x, y + point.y, z + point.z);
    }

    public Point3D minus(Point3D point) {
        return point3D(x - point.x, y - point.y, z - point.z);
    }
}
