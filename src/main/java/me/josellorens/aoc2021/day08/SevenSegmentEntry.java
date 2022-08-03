package me.josellorens.aoc2021.day08;

import java.util.List;

public class SevenSegmentEntry {

    public final List<SevenSegmentPattern> uniquePatterns;
    public final List<SevenSegmentPattern> outputPatterns;

    private SevenSegmentEntry(Builder builder) {
        this.uniquePatterns = builder.uniquePatterns;
        this.outputPatterns = builder.outputPatterns;
    }

    public static final class Builder {

        private List<SevenSegmentPattern> uniquePatterns;
        private List<SevenSegmentPattern> outputPatterns;

        private Builder() {
        }

        public static Builder sevenSegmentEntry() {
            return new Builder();
        }

        public Builder uniquePatterns(List<SevenSegmentPattern> uniquePatterns) {
            this.uniquePatterns = uniquePatterns;
            return this;
        }

        public Builder outputPatterns(List<SevenSegmentPattern> outputPatterns) {
            this.outputPatterns = outputPatterns;
            return this;
        }

        public SevenSegmentEntry build() {
            return new SevenSegmentEntry(this);
        }
    }
}
