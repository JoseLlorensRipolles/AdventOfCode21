package me.josellorens.aoc2021.day16;

import java.util.Objects;

import static me.josellorens.aoc2021.day16.BitGroup.Builder.bitGroup;

public class BitGroup {

    public final boolean lastGroup;
    public final int value;

    public BitGroup(Builder builder) {
        this.lastGroup = builder.lastGroup;
        this.value = builder.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BitGroup bitGroup = (BitGroup) o;
        return lastGroup == bitGroup.lastGroup && value == bitGroup.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastGroup, value);
    }

    public static final class Builder {

        private boolean lastGroup;
        private int value;

        public static Builder bitGroup() {
            return new Builder();
        }

        public Builder lastGroup(boolean lastGroup) {
            this.lastGroup = lastGroup;
            return this;
        }

        public Builder value(int value) {
            this.value = value;
            return this;
        }

        public BitGroup build() {
            return new BitGroup(this);
        }

        private Builder() {
        }
    }

    public static BitGroup from(String bitString) {
        final var lastGroup = bitString.charAt(0) == '0';
        final var value = Integer.valueOf(bitString.substring(1), 2);

        return bitGroup().lastGroup(lastGroup).value(value).build();
    }
}
