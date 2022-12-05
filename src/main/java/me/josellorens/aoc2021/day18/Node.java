package me.josellorens.aoc2021.day18;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static me.josellorens.aoc2021.day18.Node.Position.*;

public class Node<T> {
    public int depth;
    public Position position;
    public Optional<T> value;
    public Optional<Node<T>> father;
    public Optional<Node<T>> left;
    public Optional<Node<T>> right;

    public Node(Builder<T> builder) {
        this.depth = builder.depth;
        this.position = builder.position;
        this.value = requireNonNull(builder.value, "value");
        this.father = requireNonNull(builder.father, "father");
        this.left = requireNonNull(builder.left, "left");
        this.right = requireNonNull(builder.right, "right");
    }

    @Override
    public String toString() {
        if (value.isPresent()) {
            return value.get().toString();
        } else {
            return "[" + left.orElseThrow() + "," + right.orElseThrow() + "]";
        }
    }

    public static Node<Integer> from(String line) {
        return from(line, 0, null, HEAD);
    }

    private static Node<Integer> from(String line, int depth, Node<Integer> parent, Position position) {
        final var currentNode = new Node.Builder<Integer>()
            .father(Optional.ofNullable(parent))
            .value(empty())
            .right(empty())
            .left(empty())
            .depth(depth + 1)
            .position(position)
            .build();
        if (line.matches("\\w+")) {
            currentNode.value = Optional.of(Integer.valueOf(line));
            return currentNode;
        }
        final var splits = split(line);
        currentNode.left = Optional.of(from(splits.get(0), depth + 1, currentNode, LEFT));
        currentNode.right = Optional.of(from(splits.get(1), depth + 1, currentNode, RIGHT));
        return currentNode;
    }

    private static List<String> split(String line) {
        var depthLevel = 0;
        for (int i = 0; i < line.length(); i++) {
            var currentChar = line.charAt(i);
            switch (currentChar) {
                case '[':
                    depthLevel -= 1;
                    break;
                case ']':
                    depthLevel += 1;
                    break;
            }
            if (depthLevel == -1 && currentChar == ',') {
                return List.of(line.substring(1, i), line.substring(i + 1, line.length() - 1));
            }
        }
        throw new IllegalStateException("Could not split the line");
    }

    public void increaseDepth() {
        this.depth++;
        this.left.ifPresent(Node::increaseDepth);
        this.right.ifPresent(Node::increaseDepth);
    }

    public int magnitude() {
        if (value.isPresent()) {
            return (int) value.orElseThrow();
        }

        return 3 * left.orElseThrow().magnitude() + 2 * right.orElseThrow().magnitude();
    }

    public static final class Builder<T> {

        private int depth;
        private Position position;
        private Optional<T> value = empty();
        private Optional<Node<T>> father = empty();
        private Optional<Node<T>> left = empty();
        private Optional<Node<T>> right = empty();

        public Builder<T> depth(int depth) {
            this.depth = depth;
            return this;
        }

        public Builder<T> position(Position position) {
            this.position = position;
            return this;
        }

        public Builder<T> value(T value) {
            return value(Optional.of(value));
        }

        public Builder<T> value(Optional<T> value) {
            this.value = value;
            return this;
        }

        public Builder<T> father(Node<T> father) {
            return father(Optional.of(father));
        }

        public Builder<T> father(Optional<Node<T>> father) {
            this.father = father;
            return this;
        }

        public Builder<T> left(Node<T> left) {
            return left(Optional.of(left));
        }

        public Builder<T> left(Optional<Node<T>> left) {
            this.left = left;
            return this;
        }

        public Builder<T> right(Node<T> right) {
            return right(Optional.of(right));
        }

        public Builder<T> right(Optional<Node<T>> right) {
            this.right = right;
            return this;
        }

        public Node<T> build() {
            return new Node<>(this);
        }
    }

    public enum Position {
        HEAD,
        LEFT,
        RIGHT
    }
}
