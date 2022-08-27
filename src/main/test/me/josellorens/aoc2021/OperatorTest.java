package me.josellorens.aoc2021;

import org.junit.jupiter.api.Test;

import java.util.List;

import static me.josellorens.aoc2021.Operator.Builder.operator;
import static me.josellorens.aoc2021.Operator.LengthType.PACKETS_SIZE;
import static me.josellorens.aoc2021.Operator.LengthType.TOTAL_LENGTH;
import static me.josellorens.aoc2021.day16.BitGroup.Builder.bitGroup;
import static me.josellorens.aoc2021.day16.Literal.Builder.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OperatorTest {

    @Test
    void should_create_an_operator_of_total_length_type_from_a_string() {
        // given
        final var binString = "00111000000000000110111101000101001010010001001000000000";

        // and
        final var expectedVersion = 1;
        final var expectedType = 6;
        final var firstPacket = literal()
            .version(6)
            .type(4)
            .bitGroups(List.of(
                bitGroup()
                    .value(10)
                    .lastGroup(true)
                    .build()))
            .build();

        final var secondPacket = literal()
            .version(2)
            .type(4)
            .bitGroups(List.of(
                bitGroup()
                    .value(1)
                    .lastGroup(false)
                    .build(),
                bitGroup()
                    .value(4)
                    .lastGroup(true)
                    .build()))
            .build();

        final var expectedOperator = operator()
            .version(expectedVersion)
            .type(expectedType)
            .lengthType(TOTAL_LENGTH)
            .packets(List.of(firstPacket, secondPacket))
            .build();

        // when
        final var operator = Operator.from(binString);

        // then
        assertEquals(operator, expectedOperator);
    }

    @Test
    void should_create_an_operator_of_packets_number_type_from_a_string() {
        // given
        final var binString = "11101110000000001101010000001100100000100011000001100000";

        // and
        final var expectedVersion = 7;
        final var expectedType = 3;

        // and
        final var firstPacket = literal()
            .version(2)
            .type(4)
            .bitGroups(List.of(
                bitGroup()
                    .value(1)
                    .lastGroup(true)
                    .build()))
            .build();

        final var secondPacket = literal()
            .version(4)
            .type(4)
            .bitGroups(List.of(
                bitGroup()
                    .value(2)
                    .lastGroup(true)
                    .build()))
            .build();

        final var thirdPacket = literal()
            .version(1)
            .type(4)
            .bitGroups(List.of(
                bitGroup()
                    .value(3)
                    .lastGroup(true)
                    .build()))
            .build();

        final var expectedOperator = operator()
            .version(expectedVersion)
            .type(expectedType)
            .lengthType(PACKETS_SIZE)
            .packets(List.of(firstPacket, secondPacket, thirdPacket))
            .build();

        // when
        final var operator = Operator.from(binString);

        // then
        assertEquals(operator, expectedOperator);
    }
}