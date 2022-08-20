package me.josellorens.aoc2021.day13;

import me.josellorens.aoc2021.DaySolver;
import me.josellorens.aoc2021.day05.Point;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.day05.Point.point;
import static me.josellorens.aoc2021.day13.FoldOperation.Builder.foldOperation;
import static me.josellorens.aoc2021.day13.FoldOperation.Direction.HORIZONTAL;
import static me.josellorens.aoc2021.day13.FoldOperation.Direction.VERTICAL;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day13Solver implements DaySolver {

    boolean[][] paper;
    List<FoldOperation> folds;
    List<Point> points;

    public Day13Solver(List<String> inputLines) {
        points = inputLines.stream()
            .filter(Day13Solver::pointLine)
            .map(it -> Arrays.stream(it.split(",")).map(Integer::valueOf).collect(toList()))
            .map(it -> point(it.get(0), it.get(1)))
            .collect(toList());

        folds = inputLines.stream()
            .map(Day13Solver::foldLine)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(toList());
    }

    private void initialisePaper(List<Point> points) {
        final var maxX = points.stream()
            .map(it -> it.x)
            .max(Integer::compareTo)
            .map(it -> it + 1)
            .orElseThrow();

        final var maxY = points.stream()
            .map(it -> it.y)
            .max(Integer::compareTo)
            .map(it -> it + 1)
            .orElseThrow();
        paper = new boolean[maxX][maxY+1];
        points.forEach(it -> paper[it.x][it.y] = true);
    }

    private static Optional<FoldOperation> foldLine(String line) {
        final var pointPattern = Pattern.compile("fold along ([a-zA-Z])=(\\d+)");
        final var matcher = pointPattern.matcher(line);

        if (matcher.find()) {
            final var foldDirection = matcher.group(1).equals("y") ? VERTICAL : HORIZONTAL;
            final var lineNumber = Integer.parseInt(matcher.group(2));
            return Optional.of(foldOperation().foldDirection(foldDirection).lineNumber(lineNumber).build());
        } else {
            return empty();
        }
    }

    private static boolean pointLine(String it) {
        final var pointPattern = Pattern.compile("\\d+,\\d+");
        final var matcher = pointPattern.matcher(it);
        return matcher.find();
    }


    @Override
    public String part1() {
        initialisePaper(points);
        var maxX = paper.length;
        var maxY = paper[0].length;
        final var fold = folds.get(0);

        if (fold.direction == VERTICAL) {
            for (int y = fold.lineNumber + 1; y < maxY; y++) {
                final var foldedY = maxY - y - 1;
                for (int x = 0; x < maxX; x++) {
                    paper[x][foldedY] = paper[x][foldedY] || paper[x][y];
                }
            }

            maxY = fold.lineNumber;
        } else {
            for (int x = fold.lineNumber + 1; x < maxX; x++) {
                final var foldedX = maxX - x - 1;
                for (int y = 0; y < maxY; y++) {
                    paper[foldedX][y] = paper[foldedX][y] || paper[x][y];
                }
            }
            maxX = fold.lineNumber;
        }

        var count = 0;
        for (int i = 0; i < maxX; i++) {
            for (int j = 0; j < maxY; j++) {
                if (paper[i][j]) {
                    count++;
                }
            }

        }

        return String.valueOf(count);
    }

    @Override
    public String part2() {
        initialisePaper(points);
        var maxX = paper.length;
        var maxY = paper[0].length;
        for (FoldOperation fold : folds) {
            int lineNumber = fold.lineNumber;
            if (fold.direction == VERTICAL) {
                for (int y = lineNumber +1; y < maxY; y++) {
                    final var foldedY = maxY - y - 1;
                    for (int x = 0; x < maxX; x++) {
                        paper[x][foldedY] = (paper[x][foldedY] || paper[x][y]);
                    }
                }
                maxY = lineNumber;
            } else {
                for (int x = lineNumber +1; x < maxX; x++) {
                    final var foldedX = maxX - x - 1;
                    for (int y = 0; y < maxY; y++) {
                        paper[foldedX][y] = (paper[foldedX][y] || paper[x][y]);
                    }
                }
                maxX = lineNumber;
            }
        }
        return paperVisualization(maxX, maxY);
    }

    private String paperVisualization(int maxX, int maxY) {
        StringBuilder string = new StringBuilder();
        for (int j = 0; j < maxY; j++) {
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < maxX; i++) {
                if (paper[i][j]) {
                    row.append("X");
                } else {
                    row.append(" ");
                }
            }
            string.append(row).append("\n");
        }
        return string.toString();
    }

    public static void main(String[] args) {
        final var day13Solver = new Day13Solver(inputLinesForDay(13));
        final var day13part1result = timedExecution(day13Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day13part1result.milliseconds, day13part1result.result);
        final var day13part2result = timedExecution(day13Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day13part2result.milliseconds, day13part2result.result);
    }
}
