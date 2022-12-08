package me.josellorens.aoc2021.day22;

import me.josellorens.aoc2021.DaySolver;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.day22.Cuboid.Builder.cuboid;
import static me.josellorens.aoc2021.day22.CuboidInstruction.Builder.cuboidInstruction;
import static me.josellorens.aoc2021.day22.CuboidInstruction.Instruction.OFF;
import static me.josellorens.aoc2021.day22.CuboidInstruction.Instruction.ON;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day22Solver implements DaySolver {

    final List<CuboidInstruction> cuboidInstructions;

    public Day22Solver(List<String> inputLines) {
        cuboidInstructions = parseInput(inputLines);
    }

    private List<CuboidInstruction> parseInput(List<String> inputLines) {
        return inputLines.stream()
            .map(this::parseLine)
            .collect(toList());
    }

    private CuboidInstruction parseLine(String line) {
        Pattern pattern = Pattern.compile("(on|off) x=(.*)\\.\\.(.*),y=(.*)\\.\\.(.*),z=(.*)\\.\\.(.*)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            final var instruction = matcher.group(1).equals("on") ? ON : OFF;
            final var minX = Integer.parseInt(matcher.group(2));
            final var maxX = Integer.parseInt(matcher.group(3));
            final var minY = Integer.parseInt(matcher.group(4));
            final var maxY = Integer.parseInt(matcher.group(5));
            final var minZ = Integer.parseInt(matcher.group(6));
            final var maxZ = Integer.parseInt(matcher.group(7));

            final var cuboid = cuboid()
                .minX(minX)
                .maxX(maxX)
                .minY(minY)
                .maxY(maxY)
                .minZ(minZ)
                .maxZ(maxZ)
                .build();

            return cuboidInstruction()
                .cuboid(cuboid)
                .instruction(instruction)
                .build();
        }
        throw new IllegalStateException("Could not parse input line");
    }


    @Override
    public String part1() {
        final var cuboidInstructionsPart1 = cuboidInstructions.subList(0, 20);
        final var onPoints = numberOfOnPoints(cuboidInstructionsPart1);
        return String.valueOf(onPoints);
    }

    @Override
    public String part2() {
        final var onPoints = numberOfOnPoints(cuboidInstructions);
        return String.valueOf(onPoints);
    }

    private static Long numberOfOnPoints(List<CuboidInstruction> cuboidInstructionsPart1) {
        return bootSystem(cuboidInstructionsPart1)
            .stream()
            .map(Cuboid::size)
            .reduce(Long::sum)
            .orElseThrow();
    }

    private static Set<Cuboid> bootSystem(List<CuboidInstruction> cuboidInstructions) {
        final var cuboidsOn = new HashSet<Cuboid>();

        final var instructionsToProcess = new LinkedList<>(cuboidInstructions);
        while (!instructionsToProcess.isEmpty()) {
            final var cuboidInstruction = instructionsToProcess.remove();
            final var candidateCuboid = cuboidInstruction.cuboid;
            if (cuboidInstruction.instruction == ON) {
                var broken = false;
                for (Cuboid reference : cuboidsOn) {
                    final var overlap = overlap(reference, candidateCuboid);
                    if (overlap.isPresent()) {
                        intersection(candidateCuboid, overlap.get())
                            .stream()
                            .map(it -> cuboidInstruction().instruction(ON).cuboid(it).build())
                            .forEach(instructionsToProcess::push);
                        broken = true;
                        break;
                    }
                }
                if (!broken) {
                    cuboidsOn.add(candidateCuboid);
                }
            } else {
                var broken = true;
                while (broken) {
                    broken = false;
                    for (Cuboid reference : cuboidsOn) {
                        final var overlap = overlap(reference, candidateCuboid);
                        if (overlap.isPresent()) {
                            cuboidsOn.remove(reference);
                            cuboidsOn.addAll(intersection(reference, overlap.get()));
                            broken = true;
                            break;
                        }
                    }
                }
            }
        }
        return cuboidsOn;
    }

    public static Optional<Cuboid> overlap(Cuboid first, Cuboid second) {
        final var minX = max(first.minX, second.minX);
        final var maxX = min(first.maxX, second.maxX);
        final var minY = max(first.minY, second.minY);
        final var maxY = min(first.maxY, second.maxY);
        final var minZ = max(first.minZ, second.minZ);
        final var maxZ = min(first.maxZ, second.maxZ);

        final var cuboid = cuboid()
            .minX(minX)
            .maxX(maxX)
            .minY(minY)
            .maxY(maxY)
            .minZ(minZ)
            .maxZ(maxZ)
            .build();

        if (isInverted(cuboid)) {
            return empty();
        } else {
            return Optional.of(cuboid);
        }
    }

    private static boolean isInverted(Cuboid cuboid) {
        return cuboid.maxX < cuboid.minX || cuboid.maxY < cuboid.minY || cuboid.maxZ < cuboid.minZ;
    }

    public static List<Cuboid> intersection(Cuboid reference, Cuboid toRemove) {
        final var cuboid1 = cuboid()
            .minX(reference.minX)
            .maxX(reference.maxX)
            .minY(reference.minY)
            .maxY(reference.maxY)
            .minZ(reference.minZ)
            .maxZ(toRemove.minZ - 1)
            .build();

        final var cuboid2 = cuboid()
            .minX(reference.minX)
            .maxX(reference.maxX)
            .minY(reference.minY)
            .maxY(reference.maxY)
            .minZ(toRemove.maxZ + 1)
            .maxZ(reference.maxZ)
            .build();

        final var cuboid3 = cuboid()
            .minX(reference.minX)
            .maxX(toRemove.minX - 1)
            .minY(reference.minY)
            .maxY(reference.maxY)
            .minZ(toRemove.minZ)
            .maxZ(toRemove.maxZ)
            .build();

        final var cuboid4 = cuboid()
            .minX(toRemove.maxX + 1)
            .maxX(reference.maxX)
            .minY(reference.minY)
            .maxY(reference.maxY)
            .minZ(toRemove.minZ)
            .maxZ(toRemove.maxZ)
            .build();

        final var cuboid5 = cuboid()
            .minX(toRemove.minX)
            .maxX(toRemove.maxX)
            .minY(reference.minY)
            .maxY(toRemove.minY - 1)
            .minZ(toRemove.minZ)
            .maxZ(toRemove.maxZ)
            .build();

        final var cuboid6 = cuboid()
            .minX(toRemove.minX)
            .maxX(toRemove.maxX)
            .minY(toRemove.maxY + 1)
            .maxY(reference.maxY)
            .minZ(toRemove.minZ)
            .maxZ(toRemove.maxZ)
            .build();

        return Stream.of(cuboid1, cuboid2, cuboid3, cuboid4, cuboid5, cuboid6)
            .filter(it -> !isInverted(it))
            .collect(toList());
    }

    public static void main(String[] args) {
        final var day22Solver = new Day22Solver(inputLinesForDay(22));
        final var day22part1result = timedExecution(day22Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day22part1result.milliseconds, day22part1result.result);
        final var day22part2result = timedExecution(day22Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day22part2result.milliseconds, day22part2result.result);
    }
}
