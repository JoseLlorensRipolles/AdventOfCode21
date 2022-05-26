package me.josellorens.aoc2021.day02;

import me.josellorens.aoc2021.DaySolver;

import java.util.List;

import static me.josellorens.aoc2021.day02.SubmarineState.Builder.initialSubmarineState;

public class Day02Solver implements DaySolver {

    final List<String> inputLines;

    public Day02Solver(List<String> inputLines) {
        this.inputLines = inputLines;
    }

    public String part1() {
        // This parallelization is just for fun, as it is slower than sequential execution due to the small list and overhead
        final var state = inputLines.parallelStream()
            .map(SubmarineInstruction::from)
            .map(this::executePart1Instruction)
            .reduce(initialSubmarineState().build(), SubmarineState::add);
        return String.format("%d", state.horizontal * state.depth);
    }

    private SubmarineState executePart1Instruction(SubmarineInstruction instruction) {
        final var state = initialSubmarineState().build();
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
        return state;
    }

    public String part2() {
        final var state = initialSubmarineState().build();

        inputLines.stream()
            .map(SubmarineInstruction::from)
            .forEach(instruction -> executePart2Instruction(state, instruction));
        return String.format("%d", state.horizontal * state.depth);
    }

    private void executePart2Instruction(SubmarineState state, SubmarineInstruction instruction) {
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
}
