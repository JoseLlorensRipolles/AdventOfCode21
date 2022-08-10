package me.josellorens.aoc2021.day10;

import me.josellorens.aoc2021.DaySolver;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day10Solver implements DaySolver {

    private final List<char[]> lines;

    public Day10Solver(List<String> inputLines) {
        lines = inputLines
            .stream()
            .map(String::toCharArray)
            .collect(toList());
    }

    @Override
    public String part1() {
        final var result = lines
            .stream()
            .map(Day10Solver::findCorrupted)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(this::toValue)
            .reduce(Integer::sum)
            .orElseThrow();

        return String.valueOf(result);
    }

    @Override
    public String part2() {
        final var scores = lines
            .stream()
            .filter(it -> findCorrupted(it).isEmpty())
            .map(this::missingString)
            .map(this::toValue)
            .sorted()
            .collect(toList());

        final var result = scores.get(scores.size()/2);
        return String.valueOf(result);
    }

    private Long toValue(String it) {
        var score = 0L;
        for (char c : it.toCharArray()) {
            score = score * 5;
            switch (c) {
                case ']':
                    score += 2;
                    break;
                case ')':
                    score += 1;
                    break;
                case '}':
                    score += 3;
                    break;
                case '>':
                    score += 4;
                    break;
            }
        }
        return score;
    }

    private String missingString(char[] line) {
        var result = "";
        var parenthesis_counter = 0;
        var brackets_counter = 0;
        var arrows_counter = 0;
        var box_counter = 0;
        for (int j = line.length - 1; j >= 0; j--) {
            final var currentChar = line[j];
            switch (currentChar) {
                case '{':
                    brackets_counter++;
                    break;
                case '}':
                    brackets_counter--;
                    break;
                case '[':
                    box_counter++;
                    break;
                case ']':
                    box_counter--;
                    break;
                case '<':
                    arrows_counter++;
                    break;
                case '>':
                    arrows_counter--;
                    break;
                case '(':
                    parenthesis_counter++;
                    break;
                case ')':
                    parenthesis_counter--;
                    break;
            }

            if (parenthesis_counter > 0 || brackets_counter > 0 || arrows_counter > 0 || box_counter > 0) {
                switch (currentChar) {
                    case '{':
                        result += "}";
                        brackets_counter = 0;
                        break;
                    case '[':
                        result += "]";
                        box_counter = 0;
                        break;
                    case '<':
                        result += ">";
                        arrows_counter = 0;
                        break;
                    case '(':
                        result += ")";
                        parenthesis_counter = 0;
                        break;
                    default:
                        throw new IllegalStateException();
                }
            }

        }
        return result;
    }

    private int toValue(Character it) {
        switch (it) {
            case ')':
                return 3;
            case ']':
                return 57;
            case '}':
                return 1197;
            case '>':
                return 25137;
            default:
                throw new IllegalStateException();
        }
    }

    private static Optional<Character> findCorrupted(char[] line) {
        for (int i = 1; i < line.length; i++) {
            final var symbol = line[i];
            if (symbol == ')' || symbol == ']' || symbol == '>' || symbol == '}') {
                if (isCorrupted(line, i, symbol)) {
                    return Optional.of(line[i]);
                }
            }
        }
        return Optional.empty();
    }

    private static boolean isCorrupted(char[] line, int i, char symbol) {
        var parenthesis_counter = 0;
        var brackets_counter = 0;
        var arrows_counter = 0;
        var box_counter = 0;
        for (int j = i - 1; j > 0; j--) {
            final var currentChar = line[j];
            switch (currentChar) {
                case '{':
                    brackets_counter++;
                    break;
                case '}':
                    brackets_counter--;
                    break;
                case '[':
                    box_counter++;
                    break;
                case ']':
                    box_counter--;
                    break;
                case '<':
                    arrows_counter++;
                    break;
                case '>':
                    arrows_counter--;
                    break;
                case '(':
                    parenthesis_counter++;
                    break;
                case ')':
                    parenthesis_counter--;
                    break;
            }

            if (parenthesis_counter > 0 || brackets_counter > 0 || arrows_counter > 0 || box_counter > 0) {
                switch (currentChar) {
                    case '{':
                        return symbol != '}';
                    case '[':
                        return symbol != ']';
                    case '<':
                        return symbol != '>';
                    case '(':
                        return symbol != ')';
                    default:
                        throw new IllegalStateException();
                }
            }

        }
        return false;
    }

    public static void main(String[] args) {
        final var day10Solver = new Day10Solver(inputLinesForDay(10));
        final var day10part1result = timedExecution(day10Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day10part1result.milliseconds, day10part1result.result);
        final var day10part2result = timedExecution(day10Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day10part2result.milliseconds, day10part2result.result);
    }
}
