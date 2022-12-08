package me.josellorens.aoc2021.day23;

import java.util.List;
import java.util.Map;

import static me.josellorens.aoc2021.day23.Position.position;

public class GameState {

    public final Position position;
    public final Long energySpent;

    private GameState(char[] hallway, Map<Character, List<Character>> rooms, long energySpent) {
        this.position = position(hallway, rooms);
        this.energySpent = energySpent;
    }

    public static GameState gameState(char[] hallway, Map<Character, List<Character>> rooms, long energySpent) {
        return new GameState(hallway, rooms, energySpent);
    }
}
