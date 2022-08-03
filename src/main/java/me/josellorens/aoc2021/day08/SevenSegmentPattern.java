package me.josellorens.aoc2021.day08;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class SevenSegmentPattern {
    public final String pattern;

    private SevenSegmentPattern(String pattern) {
        this.pattern = pattern;
    }

    public Set<Character> asCharacterSet() {
        return pattern.chars().mapToObj(e -> (char) e).collect(toSet());

    }

    public static SevenSegmentPattern from(String pattern) {
        return new SevenSegmentPattern(pattern);
    }
}
