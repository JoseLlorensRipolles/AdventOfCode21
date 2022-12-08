package me.josellorens.aoc2021.day23;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Position {

    public final char[] hallway;
    public final Map<Character, List<Character>> rooms;

    private Position(char[] hallway, Map<Character, List<Character>> rooms) {
        this.hallway = hallway;
        this.rooms = rooms;
    }

    public static Position position(char[] hallway, Map<Character, List<Character>> rooms) {
        return new Position(hallway, rooms);
    }

    public String toString() {
        return Arrays.toString(hallway) + "\n" +
            rooms.get('A') + "\n" +
            rooms.get('B') + "\n" +
            rooms.get('C') + "\n" +
            rooms.get('D') + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Arrays.equals(hallway, position.hallway) && Objects.equals(rooms, position.rooms);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(rooms);
        result = 31 * result + Arrays.hashCode(hallway);
        return result;
    }
}
