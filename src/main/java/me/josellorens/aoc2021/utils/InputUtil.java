package me.josellorens.aoc2021.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class InputUtil {

    public static List<String> inputLinesForDay(int dayNumber) {
        try {
            final var inputFileName = String.format("input%02d.txt", dayNumber);
            final var fileURI = requireNonNull(InputUtil.class.getClassLoader().getResource(inputFileName)).toURI();
            return Files.readAllLines(Paths.get(fileURI), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
