package me.josellorens.aoc2021.day16;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.toBinaryString;
import static java.lang.String.format;
import static me.josellorens.aoc2021.day16.Literal.Builder.literal;

public class Literal extends Packet {

    private static final int BIT_GROUP_LENGTH = 5;

    List<BitGroup> bitGroups;

    private Literal(Builder builder) {
        super(builder.version, builder.type);
        this.bitGroups = builder.bitGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Literal literal = (Literal) o;
        return bitGroups.equals(literal.bitGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bitGroups);
    }

    @Override
    public int length() {
        return 6 + (bitGroups.size() * 5);
    }

    @Override
    public int versionSum() {
        return version;
    }

    @Override
    public long value() {
        StringBuilder binaryValue = new StringBuilder();
        for (BitGroup group : bitGroups) {
            binaryValue.append(format("%4s", toBinaryString(group.value)).replace(' ', '0'));
        }
        return Long.parseLong(binaryValue.toString(), 2);
    }

    public static final class Builder {

        private int version;
        private int type;
        private List<BitGroup> bitGroups;

        public static Builder literal() {
            return new Builder();
        }

        public Builder version(int version) {
            this.version = version;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder bitGroups(List<BitGroup> bitGroups) {
            this.bitGroups = bitGroups;
            return this;
        }

        public Literal build() {
            return new Literal(this);
        }

        private Builder() {
        }
    }

    public static Literal from(String bitString) {
        final var version = Integer.valueOf(bitString.substring(0, 3), 2);
        final var type = 4;
        final var groups = new ArrayList<BitGroup>();
        var finalGroupFound = false;
        var i = 6;
        while (!finalGroupFound) {
            final var group = BitGroup.from(bitString.substring(i, i + BIT_GROUP_LENGTH));
            finalGroupFound = group.lastGroup;
            groups.add(group);
            i += BIT_GROUP_LENGTH;
        }
        return literal().version(version).type(type).bitGroups(groups).build();
    }
}
