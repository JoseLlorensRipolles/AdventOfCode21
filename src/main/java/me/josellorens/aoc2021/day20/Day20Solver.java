package me.josellorens.aoc2021.day20;

import me.josellorens.aoc2021.DaySolver;
import me.josellorens.aoc2021.day05.Point;

import java.util.*;

import static java.lang.String.valueOf;
import static me.josellorens.aoc2021.day05.Point.point;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day20Solver implements DaySolver {

    final List<String> inputLines;
    final Map<Integer, Boolean> algorithm;
    final Set<Point> originalImage;
    final int imageSideLength;

    public Day20Solver(List<String> inputLines) {
        this.inputLines = inputLines;
        this.algorithm = readAlgorithm(inputLines);
        this.originalImage = readImage(inputLines);
        this.imageSideLength = imageSideLength(inputLines);
    }

    private int imageSideLength(List<String> inputLines) {
        return inputLines.get(inputLines.size() - 1).length();
    }


    @Override
    public String part1() {
        var image = new HashSet<>(originalImage);
        image = enhance(image);
        image = enhance(image);
        final var pixelCount = pixelCount(image, 2);
        return valueOf(pixelCount);
    }

    @Override
    public String part2() {
        var image = new HashSet<>(originalImage);
        for (int i = 0; i < 50; i++) {
            image = enhance(image);
        }
        final var pixelCount = pixelCount(image, 50);
        return valueOf(pixelCount);
    }

    private long pixelCount(HashSet<Point> image, int iterations) {
        return image.stream()
            .filter(it -> it.x >= -iterations)
            .filter(it -> it.x <= imageSideLength + iterations)
            .filter(it -> it.y >= -iterations)
            .filter(it -> it.y <= imageSideLength + iterations)
            .count();
    }

    private HashSet<Point> enhance(HashSet<Point> image) {
        final int minX = -100;
        final int maxX = imageSideLength + 100;
        final int minY = -100;
        final int maxY = imageSideLength + 100;

        final var newImage = new HashSet<Point>();
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                if (enhancesToLightPixel(image, i, j)) {
                    newImage.add(point(i, j));
                }
            }
        }
        return newImage;
    }

    private boolean enhancesToLightPixel(HashSet<Point> image, int x, int y) {
        var index = 0;
        for (Point adjacent : point(x, y).orderedAdjacentIncludingItself()) {
            index = index << 1;
            if (image.contains(point(adjacent.x, adjacent.y))) {
                index += 1;
            }
        }
        return algorithm.get(index);
    }

    private Set<Point> readImage(List<String> inputLines) {
        final var image = new HashSet<Point>();
        var indexStart = -1;
        for (int i = 0; i < inputLines.size(); i++) {
            if (inputLines.get(i).isBlank()) {
                indexStart = i + 1;
                break;
            }
        }

        for (int i = indexStart; i < inputLines.size(); i++) {
            final var line = inputLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    image.add(point(j, inputLines.size() - i - 1));
                }
            }
        }
        return image;
    }

    private Map<Integer, Boolean> readAlgorithm(List<String> inputLines) {
        final var algorithm = new HashMap<Integer, Boolean>();
        char[] charArray = inputLines.get(0).toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '#') {
                algorithm.put(i, true);
            } else {
                algorithm.put(i, false);
            }
        }
        return algorithm;
    }

    public static void main(String[] args) {
        final var day20Solver = new Day20Solver(inputLinesForDay(20));
        final var day20part1result = timedExecution(day20Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day20part1result.milliseconds, day20part1result.result);
        final var day20part2result = timedExecution(day20Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day20part2result.milliseconds, day20part2result.result);
    }
}
