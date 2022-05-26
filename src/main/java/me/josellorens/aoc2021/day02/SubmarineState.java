package me.josellorens.aoc2021.day02;

import static me.josellorens.aoc2021.day02.SubmarineState.Builder.submarineState;

public class SubmarineState {

    public long horizontal;
    public long depth;
    public long aim;

    public SubmarineState(long horizontal, long depth, long aim) {
        this.horizontal = horizontal;
        this.depth = depth;
        this.aim = aim;
    }

    public SubmarineState add(SubmarineState secondState) {
        return submarineState()
            .horizontal(horizontal + secondState.horizontal)
            .depth(depth + secondState.depth)
            .aim(aim + secondState.aim)
            .build();
    }

    public static final class Builder {
        private long horizontal;
        private long depth;
        private long aim;

        public Builder horizontal(long horizontal) {
            this.horizontal = horizontal;
            return this;
        }

        public Builder depth(long depth) {
            this.depth = depth;
            return this;
        }

        public Builder aim(long aim) {
            this.aim = aim;
            return this;
        }

        public SubmarineState build() {
            return new SubmarineState(horizontal, depth, aim);
        }

        private Builder() {

        }

        public static Builder submarineState() {
            return new Builder();
        }

        public static Builder initialSubmarineState() {
            return new Builder().horizontal(0).depth(0).aim(0);
        }
    }
}
