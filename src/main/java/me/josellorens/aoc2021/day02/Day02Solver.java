package me.josellorens.aoc2021.day02;

import me.josellorens.aoc2021.DaySolver;

import java.util.List;
import java.util.regex.Pattern;

public class Day02Solver implements DaySolver {

    final List<String> inputLines;

    public Day02Solver(List<String> inputLines) {
        this.inputLines = inputLines;
    }

    public String part1() {
        var hor = 0;
        var depth = 0;
        final var pattern = Pattern.compile("(\\w+) (\\d+)");
        for (final var line : inputLines) {
            final var matcher = pattern.matcher(line);
            matcher.find();
            final var instruction = matcher.group(1);
            final var value = Integer.parseInt(matcher.group(2));

            switch (instruction) {
                case "forward":
                    hor += value;
                    break;
                case "down":
                    depth += value;
                    break;
                case "up":
                    depth -= value;
                    break;
            }
        }
        return String.format("%d", hor * depth);
    }

    public String part2() {
        var hor = 0;
        var depth = 0;
        var aim = 0;
        final var pattern = Pattern.compile("(\\w+) (\\d+)");
        for (final var line : inputLines) {
            final var matcher = pattern.matcher(line);
            matcher.find();
            final var instruction = matcher.group(1);
            final var value = Integer.parseInt(matcher.group(2));

            switch (instruction) {
                case "forward":
                    hor += value;
                    depth += aim * value;
                    break;
                case "down":
                    aim += value;
                    break;
                case "up":
                    aim -= value;
                    break;
            }
        }
        return String.format("%d", hor * depth);
    }
}
