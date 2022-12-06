package me.josellorens.aoc2021.day19;

import me.josellorens.aoc2021.DaySolver;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.String.valueOf;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.day19.Point3D.point3D;
import static me.josellorens.aoc2021.day19.Scanner.Builder.scanner;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day19Solver implements DaySolver {

    final List<String> inputLines;

    public Day19Solver(List<String> inputLines) {
        this.inputLines = inputLines;
    }

    @Override
    public String part1() {
        final var scanners = parse(inputLines);
        var mainScanner = scanners.poll();
        while (!scanners.isEmpty()) {
            for (int i = 0; i < scanners.size(); i++) {
                Scanner currentScanner = scanners.get(i);
                for (Scanner orientedCurrentScanner : orientations(currentScanner)) {
                    final var offset = findOffset(mainScanner, orientedCurrentScanner);
                    if (offset.isPresent()) {
                        mainScanner = addToMain(mainScanner, orientedCurrentScanner, offset.get());
                        scanners.remove(i);
                        break;
                    }
                }
            }
        }
        final var beaconsNumber = mainScanner.points.size();
        return valueOf(beaconsNumber);
    }

    private List<Scanner> orientations(Scanner currentScanner) {
        final var orientations = new ArrayList<Scanner>();
        for (int i = 0; i < 24; i++) {
            int finalI = i;
            final var rotatedPoints = currentScanner.points
                .stream()
                .map(it -> rotatePoint(it, finalI))
                .collect(toList());
            final var rotatedScanner = scanner()
                .points(rotatedPoints)
                .build();
            orientations.add(rotatedScanner);
        }
        return orientations;
    }

    private Point3D rotatePoint(Point3D point, int rotationIndex) {
        final Map<Integer, Function<Point3D, Point3D>> rotations = new HashMap<>();
        rotations.put(0, it -> point3D(it.x, it.y, it.z));
        rotations.put(1, it -> point3D(it.x, -it.y, -it.z));
        rotations.put(2, it -> point3D(it.x, it.z, -it.y));
        rotations.put(3, it -> point3D(it.x, -it.z, it.y));
        rotations.put(4, it -> point3D(-it.x, it.y, -it.z));
        rotations.put(5, it -> point3D(-it.x, -it.y, it.z));
        rotations.put(6, it -> point3D(-it.x, it.z, it.y));
        rotations.put(7, it -> point3D(-it.x, -it.z, -it.y));
        rotations.put(8, it -> point3D(it.y, it.x, -it.z));
        rotations.put(9, it -> point3D(it.y, -it.x, it.z));
        rotations.put(10, it -> point3D(it.y, it.z, it.x));
        rotations.put(11, it -> point3D(it.y, -it.z, -it.x));
        rotations.put(12, it -> point3D(-it.y, it.x, it.z));
        rotations.put(13, it -> point3D(-it.y, -it.x, -it.z));
        rotations.put(14, it -> point3D(-it.y, it.z, -it.x));
        rotations.put(15, it -> point3D(-it.y, -it.z, it.x));
        rotations.put(16, it -> point3D(it.z, it.y, -it.x));
        rotations.put(17, it -> point3D(it.z, -it.y, it.x));
        rotations.put(18, it -> point3D(it.z, it.x, it.y));
        rotations.put(19, it -> point3D(it.z, -it.x, -it.y));
        rotations.put(20, it -> point3D(-it.z, it.x, -it.y));
        rotations.put(21, it -> point3D(-it.z, -it.x, it.y));
        rotations.put(22, it -> point3D(-it.z, it.y, it.x));
        rotations.put(23, it -> point3D(-it.z, -it.y, -it.x));

        return rotations.get(rotationIndex).apply(point);
    }

    private Scanner addToMain(Scanner mainScanner, Scanner mergingScanner, Point3D offset) {
        final var newPoints = mergingScanner.points
            .stream()
            .map(it -> it.add(offset))
            .collect(toList());

        final var mainPoints = Stream.of(mainScanner.points, newPoints)
            .flatMap(Collection::stream)
            .distinct()
            .collect(toList());

        return scanner()
            .points(mainPoints)
            .build();
    }

    private Optional<Point3D> findOffset(Scanner mainScanner, Scanner currentScanner) {
        final var mainScannerPoints = new HashSet<>(mainScanner.points);
        for (Point3D mainScannerReference : mainScanner.points) {
            for (Point3D currentScannerReference : currentScanner.points) {
                var matches = 0;
                final var offset = mainScannerReference.minus(currentScannerReference);
                for (Point3D visitingPoint : currentScanner.points) {
                    final var offsetPoint = visitingPoint.add(offset);
                    if (mainScannerPoints.contains(offsetPoint)) {
                        matches++;
                    }
                }
                if (matches > 11) {
                    return Optional.of(offset);
                }
            }
        }
        return empty();
    }

    private LinkedList<Scanner> parse(List<String> inputLines) {
        final var scanners = new LinkedList<Scanner>();
        var currentPoints = new ArrayList<Point3D>();
        for (int i = 1; i < inputLines.size(); i++) {
            final var line = inputLines.get(i);
            if (line.isBlank()) {
                continue;
            }
            if (line.charAt(1) == '-') {
                scanners.add(scanner()
                    .points(currentPoints)
                    .build());
                currentPoints = new ArrayList<>();
            } else {
                final var numbers = Arrays.stream(line.split(",")).map(Integer::parseInt).collect(toList());
                final var newPoint = point3D(numbers.get(0), numbers.get(1), numbers.get(2));
                currentPoints.add(newPoint);
            }
        }
        scanners.add(scanner()
            .points(currentPoints)
            .build());
        return scanners;
    }

    @Override
    public String part2() {
        final var scanners = parse(inputLines);
        var mainScanner = scanners.poll();
        final var offsets = new ArrayList<Point3D>();
        while (!scanners.isEmpty()) {
            for (int i = 0; i < scanners.size(); i++) {
                Scanner currentScanner = scanners.get(i);
                for (Scanner orientedCurrentScanner : orientations(currentScanner)) {
                    final var offset = findOffset(mainScanner, orientedCurrentScanner);
                    if (offset.isPresent()) {
                        mainScanner = addToMain(mainScanner, orientedCurrentScanner, offset.get());
                        scanners.remove(i);
                        offsets.add(offset.get());
                        break;
                    }
                }
            }
        }
        var maxDistance = 0;
        for (Point3D offset1 : offsets) {
            for (Point3D offset2 : offsets) {
                final var distance = manhattan(offset1, offset2);
                if (distance > maxDistance) {
                    maxDistance = distance;
                }
            }
        }
        return valueOf(maxDistance);
    }

    private int manhattan(Point3D offset1, Point3D offset2) {
        return abs(offset1.x - offset2.x)
            + abs(offset1.y - offset2.y)
            + abs(offset1.z - offset2.z);
    }

    public static void main(String[] args) {
        final var day19Solver = new Day19Solver(inputLinesForDay(19));
        final var day19part1result = timedExecution(day19Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day19part1result.milliseconds, day19part1result.result);
        final var day19part2result = timedExecution(day19Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day19part2result.milliseconds, day19part2result.result);
    }
}
