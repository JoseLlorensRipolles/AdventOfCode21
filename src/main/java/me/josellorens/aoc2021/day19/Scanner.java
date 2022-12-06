package me.josellorens.aoc2021.day19;

import java.util.List;

public class Scanner {
    public List<Point3D> points;

    private Scanner(Builder builder) {
        this.points = builder.points;
    }

    public static final class Builder {
        private List<Point3D> points;

        public Builder points(List<Point3D> points) {
            this.points = points;
            return this;
        }

        public static Builder scanner() {
            return new Builder();
        }

        public Scanner build() {
            return new Scanner(this);
        }

        private Builder() {
        }
    }
}
