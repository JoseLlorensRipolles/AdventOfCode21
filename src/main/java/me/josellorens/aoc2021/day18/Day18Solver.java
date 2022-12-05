package me.josellorens.aoc2021.day18;

import me.josellorens.aoc2021.DaySolver;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static me.josellorens.aoc2021.day18.Node.Position.*;
import static me.josellorens.aoc2021.day18.Node.from;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day18Solver implements DaySolver {

    final List<String> inputLines;

    public Day18Solver(List<String> inputLines) {
        this.inputLines = inputLines;
    }

    @Override
    public String part1() {
        final var line = inputLines.get(0);
        var currentNode = reduce(from(line));
        for (int i = 1; i < inputLines.size(); i++) {
            final var newNode = from(inputLines.get(i));
            currentNode = add(currentNode, newNode);
            reduce(currentNode);
        }

        final var magnitude = currentNode.magnitude();
        return String.valueOf(magnitude);
    }

    private Node<Integer> add(Node<Integer> leftNode, Node<Integer> rightNode) {
        leftNode.position = LEFT;
        rightNode.position = RIGHT;
        leftNode.increaseDepth();
        rightNode.increaseDepth();
        final var newHead = new Node.Builder<Integer>()
            .position(HEAD)
            .depth(1)
            .left(leftNode)
            .right(rightNode)
            .build();
        leftNode.father = Optional.of(newHead);
        rightNode.father = Optional.of(newHead);
        return newHead;
    }

    private Node<Integer> reduce(Node<Integer> node) {
        var reduced = false;
        while (!reduced) {
            reduced = true;
            final var nodesToExplode = nodeToExplode(node);
            if (nodesToExplode.isPresent()) {
                reduced = false;
                explode(nodesToExplode.get());
                continue;
            }
            final var nodesToSplit = nodesToSplit(node);
            if (nodesToSplit.isPresent()) {
                reduced = false;
                split(nodesToSplit.get());
            }
        }
        return node;
    }

    public void explode(Node<Integer> nodeToExplode) {
        final var rightValue = nodeToExplode.right.orElseThrow().value.orElseThrow();
        final var leftValue = nodeToExplode.left.orElseThrow().value.orElseThrow();
        explodeLeftUp(nodeToExplode, leftValue);
        explodeRightUp(nodeToExplode, rightValue);
        final var newNode = new Node.Builder<Integer>()
            .value(0)
            .father(nodeToExplode.father)
            .position(nodeToExplode.position)
            .depth(nodeToExplode.depth)
            .build();

        if (nodeToExplode.position == LEFT) {
            nodeToExplode.father.map(it -> it.left = Optional.of(newNode));
        } else {
            nodeToExplode.father.map(it -> it.right = Optional.of(newNode));
        }
    }

    private void split(Node<Integer> node) {
        final var oldValue = (double) node.value.orElseThrow();
        final var leftValue = (int) Math.floor(oldValue / 2);
        final var rightValue = (int) Math.ceil(oldValue / 2);

        final var left = new Node.Builder<Integer>()
            .value(leftValue)
            .father(node)
            .position(LEFT)
            .depth(node.depth + 1)
            .build();

        final var right = new Node.Builder<Integer>()
            .value(rightValue)
            .father(node)
            .position(RIGHT)
            .depth(node.depth + 1)
            .build();

        node.value = empty();
        node.left = Optional.of(left);
        node.right = Optional.of(right);
    }

    private void explodeLeftUp(Node<Integer> node, int valueToAdd) {
        if (node.position == HEAD) {
            return;
        }
        final var father = node.father.orElseThrow();

        switch (node.position) {
            case RIGHT:
                addToRightMost(father.left.orElseThrow(), valueToAdd);
                break;
            case LEFT:
                explodeLeftUp(father, valueToAdd);
                break;

        }
    }

    private void explodeRightUp(Node<Integer> node, int valueToAdd) {
        if (node.position == HEAD) {
            return;
        }
        final var father = node.father.orElseThrow();

        switch (node.position) {
            case RIGHT:
                explodeRightUp(father, valueToAdd);
                break;
            case LEFT:
                addToLeftMost(father.right.orElseThrow(), valueToAdd);
                break;
        }
    }

    private void addToLeftMost(Node<Integer> node, int valueToAdd) {
        if (node.value.isPresent()) {
            node.value = node.value.map(it -> it + valueToAdd);
            return;
        }
        addToLeftMost(node.left.orElseThrow(), valueToAdd);
    }

    private void addToRightMost(Node<Integer> node, int valueToAdd) {
        if (node.value.isPresent()) {
            node.value = node.value.map(it -> it + valueToAdd);
            return;
        }
        addToRightMost(node.right.orElseThrow(), valueToAdd);
    }

    private Optional<Node<Integer>> nodeToExplode(Node<Integer> head) {
        final var nodesToVisit = new LinkedList<Node<Integer>>();
        nodesToVisit.add(head);
        while (!nodesToVisit.isEmpty()) {
            final var nodeVisiting = nodesToVisit.remove();
            if (nodeVisiting.depth >= 5 && nodeVisiting.value.isEmpty()) {
                return Optional.of(nodeVisiting);
            }
            nodeVisiting.right.ifPresent(nodesToVisit::addFirst);
            nodeVisiting.left.ifPresent(nodesToVisit::addFirst);
        }
        return empty();
    }

    private Optional<Node<Integer>> nodesToSplit(Node<Integer> head) {
        final var nodesToVisit = new LinkedList<Node<Integer>>();
        nodesToVisit.add(head);
        while (!nodesToVisit.isEmpty()) {
            final var nodeVisiting = nodesToVisit.remove();
            if (nodeVisiting.value.filter(it -> it >= 10).isPresent()) {
                return Optional.of(nodeVisiting);
            }
            nodeVisiting.right.ifPresent(nodesToVisit::addFirst);
            nodeVisiting.left.ifPresent(nodesToVisit::addFirst);
        }
        return empty();
    }

    @Override
    public String part2() {
        var maxMagnitude = 0;
        for (int i = 0; i < inputLines.size(); i++) {
            for (int j = 0; j < inputLines.size(); j++) {
                if (i == j) {
                    continue;
                }
                final var a = from(inputLines.get(i));
                final var b = from(inputLines.get(j));
                final var ab = add(a, b);
                final var abPrime = reduce(ab);
                final var magnitude = abPrime.magnitude();
                if (magnitude > maxMagnitude) {
                    maxMagnitude = magnitude;
                }
            }
        }
        return String.valueOf(maxMagnitude);
    }

    public static void main(String[] args) {
        final var day18Solver = new Day18Solver(inputLinesForDay(18));
        final var day18part1result = timedExecution(day18Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day18part1result.milliseconds, day18part1result.result);
        final var day18part2result = timedExecution(day18Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day18part2result.milliseconds, day18part2result.result);
    }
}
