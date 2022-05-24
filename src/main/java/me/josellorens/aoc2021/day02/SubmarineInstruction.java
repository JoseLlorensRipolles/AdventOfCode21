package me.josellorens.aoc2021.day02;

import java.util.regex.Pattern;

public class SubmarineInstruction {

    public enum Direction {
        FORWARD,
        DOWN,
        UP
    }

    private final static Pattern PATTERN = Pattern.compile("(\\w+) (\\d+)");

    public final Direction direction;
    public final long value;

    private SubmarineInstruction(Direction direction, long value) {
        this.direction = direction;
        this.value = value;
    }

    public static SubmarineInstruction from(String instructionLine) {
        final var matcher = PATTERN.matcher(instructionLine);
        if (matcher.find()) {
            final var direction = Direction.valueOf(matcher.group(1).toUpperCase());
            final var value = Long.parseLong(matcher.group(2));
            return new SubmarineInstruction(direction, value);
        } else {
            throw new IllegalStateException("The input line could not be parsed.");
        }
    }
}
