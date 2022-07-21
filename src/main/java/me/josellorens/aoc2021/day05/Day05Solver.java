package me.josellorens.aoc2021.day05;

import me.josellorens.aoc2021.DaySolver;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.lang.Math.abs;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day05Solver implements DaySolver {
    private final Pattern INPUT_PATTERN = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");
    private final List<Line> lines;

    public Day05Solver(List<String> inputLines) {
        this.lines = inputLines.stream()
            .map(this::toLine)
            .collect(toList());
    }

    private Line toLine(String it) {
        final var matcher = INPUT_PATTERN.matcher(it);
        if (matcher.find()) {
            final var x1 = Integer.parseInt(matcher.group(1));
            final var y1 = Integer.parseInt(matcher.group(2));
            final var x2 = Integer.parseInt(matcher.group(3));
            final var y2 = Integer.parseInt(matcher.group(4));

            final var start = new Point(x1, y1);
            final var end = new Point(x2, y2);

            return new Line(start, end);
        } else {
            throw new IllegalStateException("The input line could not be parsed.");
        }
    }

    @Override
    public String part1() {
        Map<Point, Integer> pointCounter = new ConcurrentHashMap<>();
        lines.parallelStream()
            .filter(this::vertical)
            .forEach(line -> countPointsInVerticalLines(line, pointCounter));

        lines.parallelStream()
            .filter(this::horizontal)
            .forEach(line -> countPointsInHorizontalLines(line, pointCounter));

        final var result = pointCounter.values().stream().filter(it -> it > 1).count();
        return String.valueOf(result);
    }

    @Override
    public String part2() {
        Map<Point, Integer> pointCounter = new ConcurrentHashMap<>();
        ExecutorService executor = newFixedThreadPool(3);
        executor.submit(() -> lines.parallelStream()
            .filter(this::vertical)
            .forEach(line -> countPointsInVerticalLines(line, pointCounter)));

        executor.submit(() -> lines.parallelStream()
            .filter(this::horizontal)
            .forEach(line -> countPointsInHorizontalLines(line, pointCounter)));

        executor.submit(() -> lines.parallelStream()
            .filter(this::diagonal)
            .forEach(line -> countPointsInDiagonalLines(line, pointCounter)));

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                throw new IllegalStateException("Executor did not finish successfully");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final var result = pointCounter.values().stream().filter(it -> it > 1).count();
        return String.valueOf(result);
    }

    private boolean vertical(Line line) {
        return line.firstPoint.x == line.secondPoint.x;
    }

    private boolean horizontal(Line line) {
        return line.firstPoint.y == line.secondPoint.y;
    }

    private boolean diagonal(Line line) {
        return abs(line.firstPoint.x - line.secondPoint.x) == abs(line.firstPoint.y - line.secondPoint.y);
    }

    private void countPointsInVerticalLines(Line line, Map<Point, Integer> pointCounter) {
        final var startPoint = line.firstPoint.y < line.secondPoint.y ? line.firstPoint : line.secondPoint;
        final var endPoint = line.firstPoint.y < line.secondPoint.y ? line.secondPoint : line.firstPoint;
        final var x = startPoint.x;
        for (int y = startPoint.y; y <= endPoint.y; y++) {
            final var currentPoint = new Point(x, y);
            final var currentCount = pointCounter.getOrDefault(currentPoint, 0);
            pointCounter.put(currentPoint, currentCount + 1);
        }
    }

    private void countPointsInHorizontalLines(Line line, Map<Point, Integer> pointCounter) {
        final var startPoint = line.firstPoint.x < line.secondPoint.x ? line.firstPoint : line.secondPoint;
        final var endPoint = line.firstPoint.x < line.secondPoint.x ? line.secondPoint : line.firstPoint;
        final var y = startPoint.y;
        for (int x = startPoint.x; x <= endPoint.x; x++) {
            final var currentPoint = new Point(x, y);
            final var currentCount = pointCounter.getOrDefault(currentPoint, 0);
            pointCounter.put(currentPoint, currentCount + 1);
        }
    }

    private void countPointsInDiagonalLines(Line line, Map<Point, Integer> pointCounter) {
        final var startPoint = line.firstPoint;
        final var endPoint = line.secondPoint;

        final var x_direction = startPoint.x < endPoint.x ? 1 : -1;
        final var y_direction = startPoint.y < endPoint.y ? 1 : -1;
        final var limitPoint = new Point(endPoint.x + x_direction, endPoint.y + y_direction);
        for (int x = startPoint.x, y = startPoint.y;
             !new Point(x, y).equals(limitPoint);
             x += x_direction, y += y_direction) {
            final var currentPoint = new Point(x, y);
            final var currentCount = pointCounter.getOrDefault(currentPoint, 0);
            pointCounter.put(currentPoint, currentCount + 1);
        }
    }

    public static void main(String[] args) {
        final var day05Solver = new Day05Solver(inputLinesForDay(5));
        final var day05part1result = timedExecution(day05Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day05part1result.milliseconds, day05part1result.result);
        final var day05part2result = timedExecution(day05Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day05part2result.milliseconds, day05part2result.result);
    }
}
