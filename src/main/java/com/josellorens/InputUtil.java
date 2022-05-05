package com.josellorens;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.newBufferedReader;
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

    public static List<Integer> inputLinesAsIntegersForDay(int dayNumber) {
        final var inputFileName = String.format("input%02d.txt", dayNumber);
        final var fileUrl = requireNonNull(InputUtil.class.getClassLoader().getResource(inputFileName));
        try (final var reader = newBufferedReader(Paths.get(fileUrl.toURI()))) {
            return reader.lines()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
