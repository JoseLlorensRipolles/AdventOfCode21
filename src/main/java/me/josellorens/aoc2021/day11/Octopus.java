package me.josellorens.aoc2021.day11;

public class Octopus {
    public int energy;
    public boolean flashed;

    public Octopus(Builder builder) {
        this.energy = builder.energy;
        this.flashed = builder.flashed;
    }

    public static final class Builder {
        private int energy;
        private boolean flashed;

        private Builder() {
        }

        public static Builder octopus() {
            return new Builder();
        }

        public Builder energy(int energy) {
            this.energy = energy;
            return this;
        }

        public Builder flashed(boolean flashed) {
            this.flashed = flashed;
            return this;
        }

        public Octopus build() {
            return new Octopus(this);
        }
    }

    public void increaseEnergy() {
        if (!flashed) {
            energy++;
        }

        if (energy > 9) {
            flashed = true;
        }
    }

    public void resetFlashing() {
        energy = 0;
        flashed = false;
    }
}
