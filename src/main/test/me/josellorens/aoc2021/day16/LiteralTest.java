package me.josellorens.aoc2021.day16;


import org.junit.jupiter.api.Test;

import java.util.List;

import static me.josellorens.aoc2021.day16.BitGroup.Builder.bitGroup;
import static me.josellorens.aoc2021.day16.Literal.Builder.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LiteralTest {

    @Test
    void should_create_a_literal() {
        // given
        final var binString = "110100101111111000101000";

        // and
        final var expectedVersion = 6;
        final var expectedType = 4;
        final var firstBitGroup = bitGroup().lastGroup(false).value(7).build();
        final var secondBitGroup = bitGroup().lastGroup(false).value(14).build();
        final var thirdBitGroup = bitGroup().lastGroup(true).value(5).build();
        final var expectedLiteral = literal()
            .version(expectedVersion)
            .type(expectedType)
            .bitGroups(List.of(firstBitGroup, secondBitGroup, thirdBitGroup))
            .build();

        // and
        final var expectedValue = 2021;

        // when
        final var literal = Literal.from(binString);

        // then
        assertEquals(literal, expectedLiteral);
        assertEquals(literal.value(), expectedValue);
    }

}