package me.josellorens.aoc2021.day11;

import me.josellorens.aoc2021.DaySolver;
import me.josellorens.aoc2021.day05.Point;

import java.util.*;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.day05.Point.point;
import static me.josellorens.aoc2021.day11.Octopus.Builder.octopus;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day11Solver implements DaySolver {

    public final List<String> inputLines;
    public Map<Point, Octopus> cavern;
    public final int maxX;
    public final int maxY;

    public Day11Solver(List<String> inputLines) {
        this.inputLines = inputLines;
        final List<List<Octopus>> octopuses = setUpCavern(inputLines);
        maxX = octopuses.get(0).size() -1;
        maxY = octopuses.size() -1;
    }

    private List<List<Octopus>> setUpCavern(List<String> inputLines) {
        final var octopuses = inputLines
            .stream()
            .map(it -> it.chars()
                .map(Character::getNumericValue)
                .boxed()
                .map(energy -> octopus().energy(energy).flashed(false).build())
                .collect(toList()))
            .collect(toList());

        cavern = new HashMap<>();
        for (int y = 0; y < octopuses.size(); y++) {
            for (int x = 0; x < octopuses.get(0).size(); x++) {
                cavern.put(point(x, y), octopuses.get(y).get(x));
            }
        }
        return octopuses;
    }

    @Override
    public String part1() {
        var totalFlashings = 0;
        for (int steps = 0; steps < 100; steps++) {
            final var alreadyPropagated = new HashSet<Point>();
            for (Entry<Point, Octopus> entry: cavern.entrySet()){
                increaseEnergy(entry.getKey(), entry.getValue(), alreadyPropagated);
            }
            for (Octopus octopus: cavern.values()){
                if (octopus.flashed){
                    totalFlashings ++;
                    octopus.resetFlashing();
                }
            }
        }
        return String.valueOf(totalFlashings);
    }

    private void increaseEnergy(Point point, Octopus octopus, Set<Point> alreadyPropagated) {
        octopus.increaseEnergy();
        if (octopus.flashed && ! alreadyPropagated.contains(point)){
            alreadyPropagated.add(point);
            for (Point adjacent: point.adjacentIncludingDiagonals(maxX, maxY)){
                if (!cavern.get(adjacent).flashed){
                increaseEnergy(adjacent, cavern.get(adjacent), alreadyPropagated);
                }
            }
        }
    }

    @Override
    public String part2() {
        setUpCavern(inputLines);
        var step = 0;
        var synchronizedFlashes = false;
        while (!synchronizedFlashes) {
            final var alreadyPropagated = new HashSet<Point>();
            for (Entry<Point, Octopus> entry: cavern.entrySet()){
                increaseEnergy(entry.getKey(), entry.getValue(), alreadyPropagated);
            }
            synchronizedFlashes = cavern.values()
                .stream()
                .allMatch( it -> it.flashed);
            for (Octopus octopus: cavern.values()){
                if (octopus.flashed){
                    octopus.resetFlashing();
                }
            }
            step ++;
        }
        return String.valueOf(step);
    }

    public static void main(String[] args) {
        final var day11Solver = new Day11Solver(inputLinesForDay(11));
        final var day11part1result = timedExecution(day11Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day11part1result.milliseconds, day11part1result.result);
        final var day11part2result = timedExecution(day11Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day11part2result.milliseconds, day11part2result.result);
    }
}
