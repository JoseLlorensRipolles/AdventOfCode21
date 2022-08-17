package me.josellorens.aoc2021.day12;

import java.util.Set;

public class Path {

    public final Set<String> visitedSmallCaves;
    public final String cave;
    public final boolean smallCaveTwice;

    private Path(Builder builder) {
        this.visitedSmallCaves = builder.visitedSmallCaves;
        this.cave = builder.cave;
        this.smallCaveTwice = builder.smallCaveTwice;
    }

    public static final class Builder {

        private Set<String> visitedSmallCaves;
        private String cave;
        private boolean smallCaveTwice;

        public Builder visitedSmallCaves(Set<String> visitedSmallCaves) {
            this.visitedSmallCaves = visitedSmallCaves;
            return this;
        }

        public Builder cave(String cave) {
            this.cave = cave;
            return this;
        }

        public Builder smallCaveTwice(boolean smallCaveTwice) {
            this.smallCaveTwice = smallCaveTwice;
            return this;
        }

        public Path build() {
            return new Path(this);
        }

        private Builder() {
        }

        public static Builder path() {
            return new Builder();
        }
    }
}
