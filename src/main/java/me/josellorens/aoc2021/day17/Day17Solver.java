package me.josellorens.aoc2021.day17;

import me.josellorens.aoc2021.DaySolver;
import me.josellorens.aoc2021.day05.Point;

import java.util.List;
import java.util.regex.Pattern;

import static java.lang.Math.abs;
import static java.util.stream.IntStream.range;
import static me.josellorens.aoc2021.day05.Point.point;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day17Solver implements DaySolver {

    final int xMin;
    final int xMax;
    final int yMin;
    final int yMax;

    public Day17Solver(List<String> inputLines) {
        final var pattern = Pattern.compile("target area: x=(.+)\\.\\.(.+), y=(.+)\\.\\.(.+)");
        final var matcher = pattern.matcher(inputLines.get(0));
        if (matcher.find()) {
            xMin = Integer.parseInt(matcher.group(1));
            xMax = Integer.parseInt(matcher.group(2));
            yMin = Integer.parseInt(matcher.group(3));
            yMax = Integer.parseInt(matcher.group(4));
        } else {
            throw new IllegalStateException("Match not found");
        }
    }

    @Override
    public String part1() {
        final var maxYReached = range(0, abs(yMin)).sum();
        return String.valueOf(maxYReached);
    }

    private int getMinXVel() {
        for (int initialVel = 0; initialVel < 1_000; initialVel++) {
            var position = 0;
            for (int i = initialVel; i > 0; i--) {
                position += initialVel;
                if (position <= xMax && position >= xMin) {
                    return initialVel;
                }
            }
        }
        throw new IllegalStateException("Minimum X initialVel exceeds 1000");
    }

    @Override
    public String part2() {
        final var minXVelocity = getMinXVel();
        final var maxYVelocity = abs(yMin) - 1;

        var validVelocities = 0;
        for (int xVel = minXVelocity; xVel <= xMax; xVel++) {
            for (int yVel = yMin; yVel <= maxYVelocity; yVel++) {
                if (reachesTarget(xVel, yVel)) {
                    validVelocities++;
                }
            }
        }
        return String.valueOf(validVelocities);
    }

    private boolean reachesTarget(int xVel, int yVel) {
        var velocity = point(xVel, yVel);
        var position = point(0, 0);

        while (!outsideRange(position)) {
            position = position.add(velocity);
            if (inTargetArea(position)) {
                return true;
            }
            var newXVel = velocity.x == 0 ? 0 : velocity.x - 1;
            var newYVel = velocity.y - 1;
            velocity = point(newXVel, newYVel);
        }
        return false;
    }

    private boolean inTargetArea(Point position) {
        return position.x >= xMin
            && position.x <= xMax
            && position.y >= yMin
            && position.y <= yMax;
    }

    private boolean outsideRange(Point position) {
        return position.x > xMax || position.y < yMin;
    }

    public static void main(String[] args) {
        final var day17Solver = new Day17Solver(inputLinesForDay(17));
        final var day17part1result = timedExecution(day17Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day17part1result.milliseconds, day17part1result.result);
        final var day17part2result = timedExecution(day17Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day17part2result.milliseconds, day17part2result.result);
    }
}
