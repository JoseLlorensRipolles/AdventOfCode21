package me.josellorens.aoc2021.day04;

import me.josellorens.aoc2021.DaySolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day04Solver implements DaySolver {

    Integer BOARD_SIZE = 5;
    List<Integer> draftNumbers;
    List<List<List<Integer>>> boards = new ArrayList<>();

    public Day04Solver(List<String> inputLines) {
        final var inputSize = inputLines.size();

        this.draftNumbers = Arrays.stream(inputLines.get(0).split(","))
            .map(Integer::parseInt)
            .collect(toList());

        for (int i = 2; i <= inputSize - BOARD_SIZE + 1; i += 6) {
            var newBoard = new ArrayList<List<Integer>>();
            for (int j = 0; j < BOARD_SIZE; j++) {
                var newLine = Arrays.stream(inputLines.get(i + j).trim().split(" +"))
                    .map(Integer::parseInt)
                    .collect(toList());
                newBoard.add(newLine);
            }
            boards.add(newBoard);
        }
    }

    public String part1() {
        final var draftedNumbers = new HashSet<Integer>();
        for (Integer number : draftNumbers) {
            draftedNumbers.add(number);
            for (List<List<Integer>> board : boards) {
                if (checkWinner(board, draftedNumbers)) {
                    return String.valueOf(sumOfUnmarked(board, draftedNumbers) * number);
                }
            }
        }
        throw new IllegalStateException("No winning bingos were found");
    }

    private Integer sumOfUnmarked(List<List<Integer>> board, HashSet<Integer> draftedNumbers) {
        return board
            .stream()
            .flatMap(List::stream)
            .filter(it -> !draftedNumbers.contains(it))
            .reduce(0, Integer::sum);
    }

    private boolean checkWinner(List<List<Integer>> board, HashSet<Integer> draftedNumbers) {
        for (List<Integer> row : board) {
            var isWinner = true;
            for (Integer number : row) {
                if (!draftedNumbers.contains(number)) {
                    isWinner = false;
                    break;
                }
            }
            if (isWinner) {
                return true;
            }
        }

        for (int colNumber = 0; colNumber < BOARD_SIZE; colNumber++) {
            int finalColNumber = colNumber;
            final var col = board.stream().map(row -> row.get(finalColNumber)).collect(toList());
            var isWinner = true;
            for (Integer number : col) {
                if (!draftedNumbers.contains(number)) {
                    isWinner = false;
                    break;
                }
            }
            if (isWinner) {
                return true;
            }
        }

        return false;
    }

    public String part2() {
        final var winningBoards = new HashSet<List<List<Integer>>>();
        final var draftedNumbers = new HashSet<Integer>();
        for (Integer number : draftNumbers) {
            draftedNumbers.add(number);
            for (List<List<Integer>> board : boards) {
                if (winningBoards.contains(board)) {
                    continue;
                }
                if (checkWinner(board, draftedNumbers)) {
                    winningBoards.add(board);
                    if (winningBoards.size() == boards.size()) {
                        return String.valueOf(sumOfUnmarked(board, draftedNumbers) * number);
                    }
                }
            }
        }
        throw new IllegalStateException("No winning bingos were found");
    }

    public static void main(String[] args) {
        final var day04Solver = new Day04Solver(inputLinesForDay(4));
        final var day04part1result = timedExecution(day04Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day04part1result.milliseconds, day04part1result.result);
        final var day04part2result = timedExecution(day04Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day04part2result.milliseconds, day04part2result.result);
    }
}
