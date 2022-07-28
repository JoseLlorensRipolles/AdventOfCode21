package me.josellorens.aoc2021.day07;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.summingInt;
import static me.josellorens.aoc2021.day07.Day07Solver.CostType.BINOMIAL;
import static me.josellorens.aoc2021.day07.Day07Solver.CostType.REGULAR;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day07Solver {

    private final Map<Integer, Integer> crabsByPosition;
    private final int min;
    private final int max;

    private final Map<Integer, Integer> binomialCostByDistance = new HashMap<>();


    public Day07Solver(List<String> inputLines) {
        crabsByPosition = stream(inputLines.get(0).split(","))
            .map(Integer::parseInt)
            .collect(Collectors.groupingBy(identity(), summingInt(it -> 1)));
        min = crabsByPosition.keySet().stream().min(Integer::compareTo).orElseThrow();
        max = crabsByPosition.keySet().stream().max(Integer::compareTo).orElseThrow();
        initialiseBinomialCost();
    }

    private void initialiseBinomialCost() {
        for (var distance = 0; distance <= max - min; distance++) {
            binomialCostByDistance.put(distance, binomialCostByDistance.getOrDefault(distance - 1, 0) + distance);
        }
    }

    public String part1() {
        return minimumFuelToAlignCrabs(REGULAR);
    }

    public String part2() {
        return minimumFuelToAlignCrabs(BINOMIAL);
    }

    private String minimumFuelToAlignCrabs(CostType costType) {
        var fuelCost = IntStream.range(min, max)
            .parallel()
            .map(destination -> fuelToAlignCrabs(costType, destination))
            .min()
            .orElseThrow();
        return String.valueOf(fuelCost);
    }

    private Integer fuelToAlignCrabs(CostType costType, int destination) {
        return crabsByPosition.keySet().parallelStream()
            .map(origin -> fuelToMoveCrabGroup(costType, origin, destination))
            .reduce(Integer::sum)
            .orElseThrow();
    }

    private int fuelToMoveCrabGroup(CostType costType, int origin, int destination) {
        final var distance = abs(origin - destination);
        switch (costType) {
            case REGULAR:
                return distance * crabsByPosition.get(origin);
            case BINOMIAL:
                final var fuelByCrab = binomialCostByDistance.get(distance);
                return fuelByCrab * crabsByPosition.get(origin);
            default:
                throw new IllegalStateException("Cost type not known");
        }
    }

    public enum CostType {
        REGULAR,
        BINOMIAL
    }

    public static void main(String[] args) {
        final var day07Solver = new Day07Solver(inputLinesForDay(7));
        final var day07part1result = timedExecution(day07Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day07part1result.milliseconds, day07part1result.result);
        final var day07part2result = timedExecution(day07Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day07part2result.milliseconds, day07part2result.result);
    }
}
