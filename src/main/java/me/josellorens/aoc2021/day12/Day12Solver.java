package me.josellorens.aoc2021.day12;

import me.josellorens.aoc2021.DaySolver;

import java.util.*;

import static me.josellorens.aoc2021.day12.Path.Builder.path;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day12Solver implements DaySolver {

    private final Map<String, Set<String>> edges = new HashMap<>();

    public Day12Solver(List<String> inputLines) {
        inputLines
            .stream()
            .map(it -> it.split("-"))
            .forEach(it -> {
                if (!edges.containsKey(it[0])) {
                    edges.put(it[0], new HashSet<>());
                }
                if (!edges.containsKey(it[1])) {
                    edges.put(it[1], new HashSet<>());
                }
                edges.get(it[0]).add(it[1]);
                edges.get(it[1]).add(it[0]);

            });
    }

    @Override
    public String part1() {
        final var paths = new LinkedList<Path>();
        paths.add(path().cave("start").visitedCaves(new HashSet<>()).build());

        var numberOfPaths = 0;
        while (!paths.isEmpty()) {
            final var currentPath = paths.pop();
            final var currentCave = currentPath.cave;
            final var currentVisited = currentPath.visitedSmallCaves;

            if (currentCave.equals("end")) {
                numberOfPaths++;
                continue;
            }

            if (smallCave(currentCave)) {
                currentVisited.add(currentCave);
            }
            edges.get(currentCave)
                .stream()
                .filter(toVisit -> !currentVisited.contains(toVisit))
                .forEach(toVisit -> paths.add(
                    path()
                        .cave(toVisit)
                        .visitedCaves(new HashSet<>(currentVisited))
                        .build()));
        }

        return String.valueOf(numberOfPaths);
    }

    @Override
    public String part2() {
        final var paths = new LinkedList<Path>();
        paths.add(path().cave("start").visitedCaves(new HashSet<>()).build());

        var numberOfPaths = 0;
        while (!paths.isEmpty()) {
            final var currentPath = paths.pop();
            final var currentCave = currentPath.cave;
            final var currentVisited = currentPath.visitedSmallCaves;
            var visitedTwice = currentPath.smallCaveTwice;

            if (currentCave.equals("end")) {
                numberOfPaths++;
                continue;
            }

            if (smallCave(currentCave)) {
                if (!currentVisited.contains(currentCave)) {
                    currentVisited.add(currentCave);
                } else {
                    visitedTwice = true;
                }
            }
            final var finalVisitedTwice = visitedTwice;
            edges.get(currentCave)
                .stream()
                .filter(toVisit -> !currentVisited.contains(toVisit) || !finalVisitedTwice)
                .filter(toVisit -> !toVisit.equals("start"))
                .forEach(toVisit -> paths.add(
                    path()
                        .cave(toVisit)
                        .visitedCaves(new HashSet<>(currentVisited))
                        .smallCaveTwice(finalVisitedTwice)
                        .build()));
        }

        return String.valueOf(numberOfPaths);
    }

    private boolean smallCave(String cave) {
        return !cave.toUpperCase().equals(cave);
    }

    public static void main(String[] args) {
        final var day12Solver = new Day12Solver(inputLinesForDay(12));
        final var day12part1result = timedExecution(day12Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day12part1result.milliseconds, day12part1result.result);
        final var day12part2result = timedExecution(day12Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day12part2result.milliseconds, day12part2result.result);
    }
}
