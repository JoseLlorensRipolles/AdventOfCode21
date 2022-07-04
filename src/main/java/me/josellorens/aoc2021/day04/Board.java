package me.josellorens.aoc2021.day04;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class Board {

    public static Integer BOARD_SIZE = 5;
    private final List<List<Integer>> board;

    public Board(List<List<Integer>> board){
        this.board = board;
    }

    public boolean winner(Set<Integer> draftedNumbers){
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

    public Integer unmarkedSum(HashSet<Integer> draftedNumbers) {
        return board
            .stream()
            .flatMap(List::stream)
            .filter(it -> !draftedNumbers.contains(it))
            .reduce(0, Integer::sum);
    }

}
