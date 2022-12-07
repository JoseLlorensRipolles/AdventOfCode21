package me.josellorens.aoc2021.day21;

import me.josellorens.aoc2021.DaySolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import static java.lang.Math.max;
import static java.lang.String.valueOf;
import static java.util.Map.entry;
import static me.josellorens.aoc2021.day21.GameState.Builder.gameState;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day21Solver implements DaySolver {

    final List<String> inputLines;
    final int originalPlayer1Position;
    final int originalPlayer2Position;

    public Day21Solver(List<String> inputLines) {
        this.inputLines = inputLines;
        originalPlayer1Position = Integer.parseInt(inputLines.get(0).split(": ")[1]);
        originalPlayer2Position = Integer.parseInt(inputLines.get(1).split(": ")[1]);
    }

    @Override
    public String part1() {
        var dieState = 1;

        var rolls = 0;
        var player1Score = 0;
        var player2Score = 0;

        var player1Position = originalPlayer1Position;
        var player2Position = originalPlayer2Position;

        while (player2Score < 1000) {
            var movement = 0;
            for (int i = 0; i < 3; i++) {
                movement += dieState;
                dieState = increaseDieState(dieState);
                rolls++;
            }
            player1Position = increasePosition(player1Position, movement);
            player1Score += player1Position;
            if (player1Score >= 1000) {
                break;
            }

            movement = 0;
            for (int i = 0; i < 3; i++) {
                movement += dieState;
                dieState = increaseDieState(dieState);
                rolls++;
            }
            player2Position = increasePosition(player2Position, movement);
            player2Score += player2Position;
        }

        final var result = result(player1Score, player2Score, rolls);
        return valueOf(result);
    }

    private int result(int player1Score, int player2Score, int rolls) {
        if (player1Score >= 1000) {
            return player2Score * rolls;
        } else {
            return player1Score * rolls;
        }
    }

    private int increaseDieState(int dieState) {
        return (dieState + 1) % 100 == 0 ? 100 : (dieState + 1) % 100;
    }

    @Override
    public String part2() {
        final Map<GameState, Long> timesReachedByState = new HashMap<>();
        final PriorityQueue<GameState> statesToVisit = new PriorityQueue<>(GameState::compareTo);

        var player1Victories = 0L;
        var player2Victories = 0L;

        final var initialState = gameState()
            .player1Position(originalPlayer1Position)
            .player2Position(originalPlayer2Position)
            .player1Score(0)
            .player2Score(0)
            .player1Turn(true)
            .build();

        timesReachedByState.put(initialState, 1L);
        statesToVisit.add(initialState);

        while (!statesToVisit.isEmpty()) {
            final var stateVisiting = statesToVisit.remove();
            if (stateVisiting.player1Score >= 21) {
                player1Victories += timesReachedByState.get(stateVisiting);
                continue;
            }

            if (stateVisiting.player2Score >= 21) {
                player2Victories += timesReachedByState.get(stateVisiting);
                continue;
            }

            final var timesReached = timesReachedByState.get(stateVisiting);
            final List<Entry<Integer, Integer>> movements = List.of(
                entry(3, 1),
                entry(4, 3),
                entry(5, 6),
                entry(6, 7),
                entry(7, 6),
                entry(8, 3),
                entry(9, 1));

            for (Entry<Integer, Integer> movement : movements) {
                var newPosition = getNewPosition(stateVisiting, movement);
                var newState = getNewState(stateVisiting, newPosition);
                if (!timesReachedByState.containsKey(newState)) {
                    statesToVisit.add(newState);
                }
                timesReachedByState.put(newState, timesReachedByState.getOrDefault(newState, 0L) + (timesReached * movement.getValue()));

            }
        }
        final var result = max(player1Victories, player2Victories);
        return String.valueOf(result);
    }

    private int getNewPosition(GameState stateVisiting, Entry<Integer, Integer> movement) {
        if (stateVisiting.player1Turn) {
            return increasePosition(stateVisiting.player1Position, movement.getKey());
        } else {
            return increasePosition(stateVisiting.player2Position, movement.getKey());
        }
    }

    private static GameState getNewState(GameState stateVisiting, int newPosition) {
        if (stateVisiting.player1Turn) {
            return gameState()
                .player1Position(newPosition)
                .player2Position(stateVisiting.player2Position)
                .player1Score(stateVisiting.player1Score + newPosition)
                .player2Score(stateVisiting.player2Score)
                .player1Turn(false)
                .build();
        } else {
            return gameState()
                .player1Position(stateVisiting.player1Position)
                .player2Position(newPosition)
                .player1Score(stateVisiting.player1Score)
                .player2Score(stateVisiting.player2Score + newPosition)
                .player1Turn(true)
                .build();
        }
    }

    private int increasePosition(int position, int movement) {
        return (position + movement) % 10 == 0 ? 10 : (position + movement) % 10;
    }

    public static void main(String[] args) {
        final var day21Solver = new Day21Solver(inputLinesForDay(21));
        final var day21part1result = timedExecution(day21Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day21part1result.milliseconds, day21part1result.result);
        final var day21part2result = timedExecution(day21Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day21part2result.milliseconds, day21part2result.result);
    }
}
