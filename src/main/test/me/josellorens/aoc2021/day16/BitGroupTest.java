package me.josellorens.aoc2021.day16;

import org.junit.jupiter.api.Test;

import static me.josellorens.aoc2021.day16.BitGroup.Builder.bitGroup;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BitGroupTest {

    @Test
    void should_create_a_bit_group_from_a_binary_stream() {
        // given
        final var binString = "01101";

        // and
        final var expectedBitGroup = bitGroup().lastGroup(true).value(13).build();

        // when
        final var bitGroup = BitGroup.from(binString);

        // then
        assertEquals(expectedBitGroup, bitGroup);
    }
}