package me.josellorens.aoc2021.day22;

public class CuboidInstruction {

    public Cuboid cuboid;
    public Instruction instruction;

    public CuboidInstruction(Cuboid cuboid, Instruction instruction) {
        this.cuboid = cuboid;
        this.instruction = instruction;
    }

    public static final class Builder {

        private Cuboid cuboid;
        private Instruction instruction;

        public Builder cuboid(Cuboid cuboid) {
            this.cuboid = cuboid;
            return this;
        }

        public Builder instruction(Instruction instruction) {
            this.instruction = instruction;
            return this;
        }

        public static Builder cuboidInstruction() {
            return new Builder();
        }

        public CuboidInstruction build() {
            return new CuboidInstruction(cuboid, instruction);
        }

        private Builder() {
        }
    }

    public enum Instruction {
        ON,
        OFF
    }
}
