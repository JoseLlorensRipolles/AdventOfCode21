package me.josellorens.aoc2021.day16;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PacketTest {

    @ParameterizedTest
    @MethodSource("inputs_for_hexadecimal_parsing")
    void test_hexadecimal_parsing(String input, String expectedOutput) {
        // when
        var result = Packet.binaryString(input);

        // then
        assertEquals(expectedOutput, result);
    }

    public static Stream<Arguments> inputs_for_hexadecimal_parsing() {
        return Stream.of(
            arguments("D2FE28", "110100101111111000101000"),
            arguments("EE00D40C823060", "11101110000000001101010000001100100000100011000001100000"),
            arguments("38006F45291200", "00111000000000000110111101000101001010010001001000000000")
        );
    }
}