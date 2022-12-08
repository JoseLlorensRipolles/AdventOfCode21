package me.josellorens.aoc2021.day22;

import org.junit.jupiter.api.Test;

import static me.josellorens.aoc2021.day22.Cuboid.Builder.cuboid;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Day22SolverTest {

    @Test
    void should_return_overlap() {
        // given
        var first = cuboid()
            .minX(0)
            .maxX(10)
            .minY(-5)
            .maxY(20)
            .minZ(5)
            .maxZ(15)
            .build();

        var second = cuboid()
            .minX(2)
            .maxX(5)
            .minY(0)
            .maxY(10)
            .minZ(10)
            .maxZ(20)
            .build();

        var expected = cuboid()
            .minX(2)
            .maxX(5)
            .minY(0)
            .maxY(10)
            .minZ(10)
            .maxZ(15)
            .build();

        // when
        var result = Day22Solver.overlap(first, second);
        var result2 = Day22Solver.overlap(first, second);

        // then
        assertTrue(result.isPresent());
        assertTrue(result2.isPresent());
        assertEquals(expected, result.get());
        assertEquals(result.get(), result2.get());
    }

    @Test
    void should_return_empty_if_there_is_no_overlap() {
        // given
        var first = cuboid()
            .minX(0)
            .maxX(10)
            .minY(-5)
            .maxY(20)
            .minZ(5)
            .maxZ(15)
            .build();

        var second = cuboid()
            .minX(2)
            .maxX(5)
            .minY(0)
            .maxY(10)
            .minZ(16)
            .maxZ(20)
            .build();

        // when
        var result = Day22Solver.overlap(first, second);

        // then
        assertTrue(result.isEmpty());
    }
}