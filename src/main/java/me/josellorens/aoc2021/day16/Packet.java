package me.josellorens.aoc2021.day16;

import java.util.Objects;

import static java.lang.Character.getNumericValue;
import static java.lang.Integer.toBinaryString;
import static java.lang.String.format;

public abstract class Packet {

    public final int version;
    public final int type;

    public Packet(int version, int type) {
        this.version = version;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return version == packet.version && type == packet.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(version, type);
    }

    public abstract int length();

    public abstract int versionSum();

    public abstract long value();

    public static String binaryString(String hexString) {
        StringBuilder res = new StringBuilder();
        for (char c : hexString.toCharArray()) {
            res.append(format("%4s", toBinaryString(getNumericValue(c))).replace(' ', '0'));
        }
        return res.toString();
    }
}
