package me.josellorens.aoc2021;

import me.josellorens.aoc2021.day16.Literal;
import me.josellorens.aoc2021.day16.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.josellorens.aoc2021.Operator.Builder.operator;
import static me.josellorens.aoc2021.Operator.LengthType.PACKETS_SIZE;
import static me.josellorens.aoc2021.Operator.LengthType.TOTAL_LENGTH;

public class Operator extends Packet {

    private final LengthType lengthType;
    private final List<Packet> packets;

    public Operator(int version, int type, LengthType lengthType, List<Packet> packets) {
        super(version, type);
        this.lengthType = lengthType;
        this.packets = packets;
    }

    @Override
    public int length() {
        final var lengthLength = lengthType == TOTAL_LENGTH ? 15 : 11;
        return 7 + lengthLength + (packets.stream().map(Packet::length).reduce(Integer::sum).orElseThrow());
    }

    @Override
    public int versionSum() {
        return version + packets.stream().map(Packet::versionSum).reduce(Integer::sum).orElseThrow();
    }

    @Override
    public long value() {
        switch (type) {
            case 0:
                return packets.stream().map(Packet::value).reduce(Long::sum).orElseThrow();
            case 1:
                return packets.stream().map(Packet::value).reduce((a, b) -> a * b).orElseThrow();
            case 2:
                return packets.stream().map(Packet::value).min(Long::compareTo).orElseThrow();
            case 3:
                return packets.stream().map(Packet::value).max(Long::compareTo).orElseThrow();
            case 5:
                return packets.get(0).value() > packets.get(1).value() ? 1 : 0;
            case 6:
                return packets.get(0).value() < packets.get(1).value() ? 1 : 0;
            case 7:
                return packets.get(0).value() == packets.get(1).value() ? 1 : 0;
        }
        throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Operator operator = (Operator) o;
        return lengthType == operator.lengthType && Objects.equals(packets, operator.packets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lengthType, packets);
    }

    public enum LengthType {
        TOTAL_LENGTH,
        PACKETS_SIZE
    }

    public static final class Builder {

        private int version;
        private int type;
        private LengthType lengthType;
        private List<Packet> packets;

        public static Builder operator() {
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

        public Builder lengthType(LengthType lengthType) {
            this.lengthType = lengthType;
            return this;
        }

        public Builder packets(List<Packet> packets) {
            this.packets = packets;
            return this;
        }

        public Operator build() {
            return new Operator(version, type, lengthType, packets);
        }

        private Builder() {
        }
    }

    public static Operator from(String bitString) {
        final var version = Integer.valueOf(bitString.substring(0, 3), 2);
        final var type = Integer.valueOf(bitString.substring(3, 6), 2);
        final var lengthTypeRaw = Integer.valueOf(bitString.substring(6, 7), 2);
        final var lengthType = lengthTypeRaw == 0 ? TOTAL_LENGTH : PACKETS_SIZE;
        final var lengthValue = lengthValue(bitString, lengthType);

        var packets = new ArrayList<Packet>();
        if (lengthType == TOTAL_LENGTH) {
            packets = extractPacketsIn(bitString.substring(22, 22 + lengthValue));
        } else {
            packets = extractNextPackets(bitString.substring(18), lengthValue);
        }

        return operator()
            .version(version)
            .type(type)
            .lengthType(lengthType)
            .packets(packets)
            .build();
    }

    private static ArrayList<Packet> extractNextPackets(String bitString, int packetsToExtract) {
        final var packets = new ArrayList<Packet>();
        var offset = 0;
        for (int i = 0; i < packetsToExtract; i++) {
            var isLiteral = isLiteral(bitString, offset);
            if (isLiteral) {
                final var packet = Literal.from(bitString.substring(offset));
                packets.add(packet);
                offset += packet.length();
            } else {
                final var packet = Operator.from(bitString.substring(offset));
                packets.add(packet);
                offset += packet.length();
            }
        }
        return packets;
    }

    private static int trailingZeroes(String bitString) {
        var result = 0;
        for (int i = bitString.length() - 1; i >= 0; i--) {
            if (bitString.charAt(i) == '1') {
                return result;
            }
            result++;
        }
        return result;
    }

    private static boolean isLiteral(String bitString, int offset) {
        return Integer.valueOf(bitString.substring(offset + 3, offset + 6), 2) == 4;
    }

    private static ArrayList<Packet> extractPacketsIn(String bitString) {
        final var packets = new ArrayList<Packet>();
        var offset = 0;
        final var trailingZeroes = trailingZeroes(bitString);
        while (offset < bitString.length() - trailingZeroes) {
            var isLiteral = isLiteral(bitString, offset);
            if (isLiteral) {
                final var packet = Literal.from(bitString.substring(offset));
                packets.add(packet);
                offset += packet.length();
            } else {
                final var packet = Operator.from(bitString.substring(offset));
                packets.add(packet);
                offset += packet.length();
            }
        }
        return packets;
    }

    private static int lengthValue(String bitString, LengthType lengthType) {
        if (lengthType == TOTAL_LENGTH) {
            return Integer.valueOf(bitString.substring(7, 22), 2);
        }
        if (lengthType == PACKETS_SIZE) {
            return Integer.valueOf(bitString.substring(7, 18), 2);
        }

        throw new IllegalStateException("Unknown length type");
    }
}
