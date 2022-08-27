package me.josellorens.aoc2021.day16;

import me.josellorens.aoc2021.DaySolver;
import me.josellorens.aoc2021.Operator;

import java.util.List;

import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day16Solver implements DaySolver {

    private final String hexTransmission;

    public Day16Solver(List<String> inputLines) {
        hexTransmission = inputLines.get(0);
    }

    @Override
    public String part1() {
        final var binaryString = Packet.binaryString(hexTransmission);
        final var instruction = Operator.from(binaryString);
        return String.valueOf(instruction.versionSum());
    }

    @Override
    public String part2() {
        final var binaryString = Packet.binaryString(hexTransmission);
        final var instruction = Operator.from(binaryString);
        return String.valueOf(instruction.value());
    }

    public static void main(String[] args) {
        final var day16Solver = new Day16Solver(inputLinesForDay(16));
        final var day16part1result = timedExecution(day16Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day16part1result.milliseconds, day16part1result.result);
        final var day16part2result = timedExecution(day16Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day16part2result.milliseconds, day16part2result.result);
    }
}
