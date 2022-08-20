package me.josellorens.aoc2021.day13;

public class FoldOperation {

    public Direction direction;
    public int lineNumber;

    public FoldOperation(Builder builder) {
        this.direction = builder.direction;
        this.lineNumber = builder.lineNumber;
    }

    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    public static final class Builder{

        private Direction direction;
        private int lineNumber;

        public static Builder foldOperation(){
            return new Builder();
        }

        public Builder foldDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Builder lineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public FoldOperation build() {
            return new FoldOperation(this);
        }
    }
}
