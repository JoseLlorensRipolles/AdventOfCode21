package me.josellorens.aoc2021.day09;

import me.josellorens.aoc2021.DaySolver;
import me.josellorens.aoc2021.day05.Point;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.day05.Point.point;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day09Solver implements DaySolver {

    private Map<Point, Integer> heightMap = new HashMap<>();
    private final int mapWidth;
    private final int mapHeight;

    public Day09Solver(List<String> inputLines) {
        mapHeight = inputLines.size();
        mapWidth = inputLines.get(0).length();
        for (int y = 0; y < inputLines.size(); y++) {
            final var line = inputLines.get(y);
            for (int x = 0; x < line.length(); x++) {
                final var point = new Point(x, y);
                final var height = Character.getNumericValue(line.charAt(x));
                heightMap.put(point, height);
            }

        }
    }

    @Override
    public String part1() {
        var result = heightMap.keySet()
            .stream()
            .filter(this::localMinimum)
            .map(it -> heightMap.get(it))
            .map(it -> it +1)
            .reduce(Integer::sum)
            .orElseThrow();
        return String.valueOf(result);
    }

    @Override
    public String part2() {
        final var localMinimums = heightMap.keySet()
            .stream()
            .filter(this::localMinimum)
            .map(this::basin)
            .map(Set::size)
            .sorted(reverseOrder())
            .collect(toList());

        final var result = localMinimums.get(0) * localMinimums.get(1) * localMinimums.get(2);
        return String.valueOf(result);
    }

    private Set<Point> basin(Point localMinimum) {
        Set<Point> visitedPoints = new HashSet<>();
        Deque<Point> pointsToVisit = new LinkedList<>();
        pointsToVisit.add(localMinimum);

        while (!pointsToVisit.isEmpty()){
            final var currentPoint = pointsToVisit.pop();
            if (visitedPoints.contains(currentPoint)){
                continue;
            }else{
                visitedPoints.add(currentPoint);
            }

            if (currentPoint.x > 0) {
                Point potentialPoint = point(currentPoint.x - 1, currentPoint.y);
                if(heightMap.get(potentialPoint) < 9) {
                    pointsToVisit.add(potentialPoint);
                }
            }

            if (currentPoint.x < mapWidth -1) {
                final var potentialPoint = point(currentPoint.x + 1, currentPoint.y);
                if(heightMap.get(potentialPoint) < 9) {
                    pointsToVisit.add(potentialPoint);
                }
            }

            if (currentPoint.y > 0) {
                final var potentialPoint = point(currentPoint.x , currentPoint.y -1);
                if(heightMap.get(potentialPoint) < 9) {
                    pointsToVisit.add(potentialPoint);
                }
            }

            if (currentPoint.y < mapHeight - 1) {
                final var potentialPoint = point(currentPoint.x , currentPoint.y +1);
                if(heightMap.get(potentialPoint) < 9) {
                    pointsToVisit.add(potentialPoint);
                }
            }
            
        }
        return visitedPoints;
    }

    private boolean localMinimum(Point currentPoint) {
        var currentValue = heightMap.get(currentPoint);
        var localMinimum = true;
        if (currentPoint.x > 0) {
            localMinimum = heightMap.get(point(currentPoint.x - 1, currentPoint.y)) > currentValue;
        }

        if (currentPoint.x < mapWidth -1) {
            final var aux = heightMap.get(point(currentPoint.x + 1, currentPoint.y)) > currentValue;
            localMinimum = localMinimum && aux;
        }

        if (currentPoint.y > 0) {
            final var aux = heightMap.get(point(currentPoint.x , currentPoint.y -1)) > currentValue;
            localMinimum = localMinimum && aux;
        }

        if (currentPoint.y < mapHeight - 1) {
            final var aux = heightMap.get(point(currentPoint.x , currentPoint.y +1)) > currentValue;
            localMinimum = localMinimum && aux;
        }

        return localMinimum;
    }

    public static void main(String[] args) {
        final var day09Solver = new Day09Solver(inputLinesForDay(9));
        final var day09part1result = timedExecution(day09Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day09part1result.milliseconds, day09part1result.result);
        final var day09part2result = timedExecution(day09Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day09part2result.milliseconds, day09part2result.result);
    }
}
