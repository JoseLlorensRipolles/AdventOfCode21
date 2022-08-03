package me.josellorens.aoc2021.day08;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class Input08Line {

    public final List<Set<Character>> uniquePatterns;
    public final List<Set<Character>> outputPatterns;

    private Input08Line(Builder builder) {
        this.uniquePatterns = builder.uniquePatterns;
        this.outputPatterns = builder.outputPatterns;
    }

    public static final class Builder {

        private List<Set<Character>> uniquePatterns;
        private List<Set<Character>> outputPatterns;

        private Builder() {
        }

        public static Builder input08Line() {
            return new Builder();
        }

        public Builder uniquePatterns(List<String> uniquePatternsStrings) {
            this.uniquePatterns = uniquePatternsStrings.stream()
                .map(it -> it.chars().mapToObj(e -> (char) e).collect(toSet()))
                .collect(Collectors.toList());
            return this;
        }

        public Builder outputPatterns(List<String> outputPatternsStrings) {
            this.outputPatterns = outputPatternsStrings.stream()
                .map(it -> it.chars().mapToObj(e -> (char) e).collect(toSet()))
                .collect(Collectors.toList());;
            return this;
        }

        public Input08Line build() {
            return new Input08Line(this);
        }
    }
}
