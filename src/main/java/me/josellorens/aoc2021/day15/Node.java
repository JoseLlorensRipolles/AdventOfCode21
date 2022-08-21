package me.josellorens.aoc2021.day15;

import me.josellorens.aoc2021.day05.Point;

public class Node{

    public Point coordinates;
    public int costToReach;

    public Node(Point coordinates, int costToReach) {
        this.coordinates = coordinates;
        this.costToReach = costToReach;
    }

    public static final class Builder {

        private Point coordinates;
        private int costToReach;

        public static Builder node() {
            return new Builder();
        }

        public Builder coordinates(Point coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder costToReach(int costToReach) {
            this.costToReach = costToReach;
            return this;
        }

        public Node build() {
            return new Node(coordinates, costToReach);
        }

        private Builder() {
        }
    }
}
