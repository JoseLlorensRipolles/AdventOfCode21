package me.josellorens.aoc2021.day22;

import java.util.Objects;

public class Cuboid {

    public int minX;
    public int maxX;
    public int minY;
    public int maxY;
    public int minZ;
    public int maxZ;

    public Cuboid(Builder builder) {
        this.minX = builder.minX;
        this.maxX = builder.maxX;
        this.minY = builder.minY;
        this.maxY = builder.maxY;
        this.minZ = builder.minZ;
        this.maxZ = builder.maxZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuboid cuboid = (Cuboid) o;
        return minX == cuboid.minX
            && maxX == cuboid.maxX
            && minY == cuboid.minY
            && maxY == cuboid.maxY
            && minZ == cuboid.minZ
            && maxZ == cuboid.maxZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, maxX, minY, maxY, minZ, maxZ);
    }

    public long size() {
        return (maxX - minX + 1L) * (maxY - minY + 1L) * (maxZ - minZ + 1L);
    }

    public static final class Builder {

        private int minX;
        private int maxX;
        private int minY;
        private int maxY;
        private int minZ;
        private int maxZ;

        public Builder minX(int minX) {
            this.minX = minX;
            return this;
        }

        public Builder maxX(int maxX) {
            this.maxX = maxX;
            return this;
        }

        public Builder minY(int minY) {
            this.minY = minY;
            return this;
        }

        public Builder maxY(int maxY) {
            this.maxY = maxY;
            return this;
        }

        public Builder minZ(int minZ) {
            this.minZ = minZ;
            return this;
        }

        public Builder maxZ(int maxZ) {
            this.maxZ = maxZ;
            return this;
        }

        public static Builder cuboid() {
            return new Builder();
        }

        public Cuboid build() {
            return new Cuboid(this);
        }

        private Builder() {
        }
    }
}
