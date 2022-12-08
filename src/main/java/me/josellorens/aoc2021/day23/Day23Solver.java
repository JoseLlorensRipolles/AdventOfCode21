package me.josellorens.aoc2021.day23;

import me.josellorens.aoc2021.DaySolver;

import java.util.*;
import java.util.Map.Entry;

import static java.util.Collections.emptyList;
import static me.josellorens.aoc2021.day23.GameState.gameState;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day23Solver implements DaySolver {

    final List<String> inputLines;
    final Map<Integer, Character> roomByEntries;
    final Map<Character, Integer> entriesByRoom;

    public Day23Solver(List<String> inputLines) {
        this.inputLines = inputLines;
        roomByEntries = Map.of(
            2, 'A',
            4, 'B',
            6, 'C',
            8, 'D'
        );

        entriesByRoom = Map.of(
            'A', 2,
            'B', 4,
            'C', 6,
            'D', 8
        );
    }

    private Map<Character, List<Character>> initialiseRooms(List<String> inputLines, boolean extraLines) {
        final var rooms = new HashMap<Character, List<Character>>();
        for (Entry<Character, Integer> entry : entriesByRoom.entrySet()) {
            final var room = new ArrayList<Character>();
            room.add(inputLines.get(2).charAt(entry.getValue() + 1));
            if (extraLines) {
                room.add(inputLines.get(3).charAt(entry.getValue() + 1));
                room.add(inputLines.get(4).charAt(entry.getValue() + 1));
            }
            room.add(inputLines.get(5).charAt(entry.getValue() + 1));
            rooms.put(entry.getKey(), room);
        }
        return rooms;
    }

    @Override
    public String part1() {
        return solveGame(false);
    }

    @Override
    public String part2() {
        return solveGame(true);
    }

    private String solveGame(boolean part2) {
        final char[] hallway = {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.'};
        final Map<Character, List<Character>> rooms;
        rooms = initialiseRooms(inputLines, part2);
        final var roomsSize = part2 ? 4 : 2;
        final var originalGameState = gameState(hallway, rooms, 0);

        final var statesToVisit = new PriorityQueue<>(this::compare);
        final var positionsVisited = new HashSet<Position>();

        statesToVisit.add(originalGameState);
        while (!statesToVisit.isEmpty()) {
            final var visitingState = statesToVisit.remove();
            final var visitingPosition = visitingState.position;
            if (positionsVisited.contains(visitingPosition)) {
                continue;
            }
            if (winPosition(visitingState.position, roomsSize)) {
                return String.valueOf(visitingState.energySpent);
            }

            positionsVisited.add(visitingPosition);
            statesToVisit.addAll(generateStates(visitingState, positionsVisited, roomsSize));
        }
        throw new IllegalStateException();
    }

    private int compare(GameState a, GameState b) {
        final var heuristicA = heuristic(a);
        final var energySpentA = a.energySpent;
        final var valueA = Long.valueOf(energySpentA + heuristicA);
        final var heuristicB = heuristic(b);
        final var energySpentB = b.energySpent;
        final var valueB = Long.valueOf(energySpentB + heuristicB);

        return valueA.compareTo(valueB);
    }

    private long heuristic(GameState gameState) {
        var result = 0;
        char[] hallway = gameState.position.hallway;
        for (int i = 0; i < hallway.length; i++) {
            char c = hallway[i];
            if (c != '.') {
                var movement = Math.abs(i - entriesByRoom.get(hallway[i])) + 1;
                var energyNeeded = energyIncrease(hallway[i], movement);
                result += energyNeeded;
            }
        }

        for (Entry<Character, List<Character>> room : gameState.position.rooms.entrySet()) {
            var roomName = room.getKey();
            for (Character c : room.getValue()) {
                if (c != roomName) {
                    var movement = Math.abs(entriesByRoom.get(roomName) - entriesByRoom.get(c)) + 1;
                    var energyNeeded = energyIncrease(c, movement);
                    result += energyNeeded;
                }
            }
        }

        return result;
    }

    private List<GameState> generateStates(GameState visitingState, Set<Position> visitedPositions, int roomSize) {
        final var generatedStates = new ArrayList<GameState>();
        final var originalHallway = visitingState.position.hallway;
        for (int i = 0; i < originalHallway.length; i++) {
            if (originalHallway[i] != '.') {
                generatedStates.addAll(moveHallwayAmphipod(visitingState, i, visitedPositions, roomSize));
            }
        }

        generatedStates.addAll(moveRoomAmphipods('A', visitingState, visitedPositions, roomSize));
        generatedStates.addAll(moveRoomAmphipods('B', visitingState, visitedPositions, roomSize));
        generatedStates.addAll(moveRoomAmphipods('C', visitingState, visitedPositions, roomSize));
        generatedStates.addAll(moveRoomAmphipods('D', visitingState, visitedPositions, roomSize));
        return generatedStates;
    }

    private List<GameState> moveRoomAmphipods(char roomName, GameState visitingState, Set<Position> visitedPositions, int roomSize) {

        final var generatedStates = new ArrayList<GameState>();
        final var hallway = visitingState.position.hallway;
        final var rooms = visitingState.position.rooms;
        final var entryOfTheRoom = entriesByRoom.get(roomName);
        final var room = rooms.get(roomName);

        if (room.isEmpty()
            || room.stream().allMatch(it -> it == roomName)) {
            return emptyList();
        }

        final var amphipod = rooms.get(roomName).get(0);
        for (int targetLocation = entryOfTheRoom + 1; targetLocation < hallway.length; targetLocation++) {
            if (hallway[targetLocation] != '.') {
                break;
            }

            if (!roomByEntries.containsKey(targetLocation)) {
                var newHallway = hallway.clone();
                newHallway[targetLocation] = amphipod;

                final var newRooms = cloneRooms(rooms);
                newRooms.get(roomName).remove(0);

                final var movement = Math.abs(targetLocation - entryOfTheRoom) + (roomSize - room.size()) + 1;
                final var energyIncrease = energyIncrease(amphipod, movement);
                final var newEnergy = visitingState.energySpent + energyIncrease;
                final var newState = gameState(newHallway, newRooms, newEnergy);

                if (!visitedPositions.contains(newState.position)) {
                    generatedStates.add(newState);
                }
            }
        }

        for (int targetLocation = entryOfTheRoom - 1; targetLocation >= 0; targetLocation--) {
            if (hallway[targetLocation] != '.') {
                break;
            }

            if (!roomByEntries.containsKey(targetLocation)) {
                var newHallway = hallway.clone();
                newHallway[targetLocation] = amphipod;

                final var newRooms = cloneRooms(rooms);
                newRooms.get(roomName).remove(0);

                final var movement = Math.abs(targetLocation - entryOfTheRoom) + (roomSize - room.size()) + 1;
                final var energyIncrease = energyIncrease(amphipod, movement);
                final var newEnergy = visitingState.energySpent + energyIncrease;
                final var newState = gameState(newHallway, newRooms, newEnergy);

                if (!visitedPositions.contains(newState.position)) {
                    generatedStates.add(newState);
                }
            }
        }

        return generatedStates;
    }

    private static HashMap<Character, List<Character>> cloneRooms(Map<Character, List<Character>> rooms) {
        final var newRooms = new HashMap<Character, List<Character>>();
        for (Entry<Character, List<Character>> room : rooms.entrySet()) {
            newRooms.put(room.getKey(), new ArrayList<>(room.getValue()));
        }
        return newRooms;
    }

    private List<GameState> moveHallwayAmphipod(GameState visitingState, int index, Set<Position> visitedPositions, int roomSize) {
        final var generatedStates = new ArrayList<GameState>();
        final var hallway = visitingState.position.hallway;
        final var rooms = visitingState.position.rooms;
        final var amphipod = visitingState.position.hallway[index];
        for (int targetLocation = index + 1; targetLocation < hallway.length; targetLocation++) {
            if (hallway[targetLocation] != '.') {
                break;
            }

            if (roomByEntries.containsKey(targetLocation)) {
                final var canEnterRoom = canEnterRoom(visitingState, amphipod, targetLocation);
                if (canEnterRoom) {
                    var newHallWay = hallway.clone();
                    newHallWay[index] = '.';
                    var newRooms = cloneRooms(rooms);
                    newRooms.get(amphipod).add(amphipod);
                    var movement = targetLocation - index + (roomSize - rooms.get(amphipod).size());
                    var energyIncrease = energyIncrease(amphipod, movement);
                    var newEnergy = visitingState.energySpent + energyIncrease;
                    var newState = gameState(newHallWay, newRooms, newEnergy);
                    if (!visitedPositions.contains(newState.position)) {
                        generatedStates.add(newState);
                    }
                    return generatedStates;
                }
            }
        }

        for (int targetLocation = index - 1; targetLocation >= 0; targetLocation--) {
            if (hallway[targetLocation] != '.') {
                break;
            }

            if (roomByEntries.containsKey(targetLocation)) {
                final var canEnterRoom = canEnterRoom(visitingState, amphipod, targetLocation);
                if (canEnterRoom) {
                    var newHallWay = hallway.clone();
                    newHallWay[index] = '.';
                    var newRooms = cloneRooms(rooms);
                    newRooms.get(amphipod).add(amphipod);
                    var movement = index - targetLocation + (roomSize - rooms.get(amphipod).size());
                    var energyIncrease = energyIncrease(amphipod, movement);
                    var newEnergy = visitingState.energySpent + energyIncrease;
                    var newState = gameState(newHallWay, newRooms, newEnergy);
                    if (!visitedPositions.contains(newState.position)) {
                        generatedStates.add(newState);
                    }
                    return generatedStates;
                }
            }
        }
        return generatedStates;
    }

    private boolean canEnterRoom(GameState visitingState, char amphipod, int targetLocation) {
        return roomByEntries.get(targetLocation) == amphipod
            && visitingState.position.rooms.get(roomByEntries.get(targetLocation)).stream().allMatch(it -> it.equals(amphipod));
    }

    private long energyIncrease(char c, int delta) {
        switch (c) {
            case 'A':
                return delta;
            case 'B':
                return delta * 10L;
            case 'C':
                return delta * 100L;
            case 'D':
                return delta * 1000L;
            default:
                throw new IllegalStateException();
        }
    }

    private boolean winPosition(Position position, int roomSize) {
        return position.rooms.get('A').stream().allMatch(it -> it == 'A')
            && position.rooms.get('B').stream().allMatch(it -> it == 'B')
            && position.rooms.get('C').stream().allMatch(it -> it == 'C')
            && position.rooms.get('D').stream().allMatch(it -> it == 'D')
            && position.rooms.get('A').size() == roomSize
            && position.rooms.get('B').size() == roomSize
            && position.rooms.get('C').size() == roomSize
            && position.rooms.get('D').size() == roomSize;
    }

    public static void main(String[] args) {
        final var day23Solver = new Day23Solver(inputLinesForDay(23));
        final var day23part1result = timedExecution(day23Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day23part1result.milliseconds, day23part1result.result);
        final var day23part2result = timedExecution(day23Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day23part2result.milliseconds, day23part2result.result);
    }
}
