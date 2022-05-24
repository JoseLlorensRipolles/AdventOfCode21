package me.josellorens.aoc2021.day02;

import me.josellorens.aoc2021.DaySolver;

import java.util.List;

public class Day02Solver implements DaySolver {

    final List<String> inputLines;

    public Day02Solver(List<String> inputLines) {
        this.inputLines = inputLines;
    }

    public String part1() {
        final var state = new SubmarineState(0, 0, 0);

        for (final var line : inputLines) {
            final var instruction = SubmarineInstruction.from(line);
            switch (instruction.direction) {
                case FORWARD:
                    state.horizontal += instruction.value;
                    break;
                case DOWN:
                    state.depth += instruction.value;
                    break;
                case UP:
                    state.depth -= instruction.value;
                    break;
            }
        }
        return String.format("%d", state.horizontal * state.depth);
    }

    public String part2() {
        final var state = new SubmarineState(0, 0, 0);
        for (final var line : inputLines) {
            final var instruction = SubmarineInstruction.from(line);
            switch (instruction.direction) {
                case FORWARD:
                    state.horizontal += instruction.value;
                    state.depth += state.aim * instruction.value;
                    break;
                case DOWN:
                    state.aim += instruction.value;
                    break;
                case UP:
                    state.aim -= instruction.value;
                    break;
            }
        }
        return String.format("%d", state.horizontal * state.depth);
    }
}
