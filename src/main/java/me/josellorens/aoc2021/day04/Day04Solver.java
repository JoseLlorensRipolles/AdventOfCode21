package me.josellorens.aoc2021.day04;

import me.josellorens.aoc2021.DaySolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.day04.Board.BOARD_SIZE;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day04Solver implements DaySolver {

    List<Integer> draftNumbers;
    List<Board> boards = new ArrayList<>();

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
            boards.add(new Board(newBoard));
        }
    }

    public String part1() {
        final var draftedNumbers = new HashSet<Integer>();
        for (Integer number : draftNumbers) {
            draftedNumbers.add(number);
            final var result = boards.parallelStream()
                .filter(board -> board.winner(draftedNumbers))
                .findAny()
                .map(board -> board.unmarkedSum(draftedNumbers) * number);
            if (result.isPresent()) {
                return String.valueOf(result.get());
            }
        }
        throw new IllegalStateException("No winning bingos were found");
    }

    public String part2() {
        final var winningBoards = new CopyOnWriteArraySet<Board>();
        final var draftedNumbers = new HashSet<Integer>();
        for (Integer number : draftNumbers) {
            draftedNumbers.add(number);
            final var result = boards.parallelStream()
                .filter(board -> !winningBoards.contains(board))
                .filter(board -> board.winner(draftedNumbers))
                .peek(winningBoards::add)
                .filter(__ -> winningBoards.size() == boards.size())
                .findAny()
                .map(board -> board.unmarkedSum(draftedNumbers) * number);

            if (result.isPresent()) {
                return String.valueOf(result.get());
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
