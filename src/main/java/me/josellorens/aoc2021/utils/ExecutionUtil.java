package me.josellorens.aoc2021.utils;

import java.util.function.Supplier;

import static java.lang.System.nanoTime;

public class ExecutionUtil {

    public static class TimedExecution {

        public TimedExecution(String result, float milliseconds) {
            this.result = result;
            this.milliseconds = milliseconds;
        }

        public String result;
        public float milliseconds;
    }

    public static TimedExecution timedExecution(Supplier<String> dayPart) {
        final var startTime = nanoTime();
        final var result = dayPart.get();
        final var endTime = nanoTime();
        final var milliseconds = (endTime - startTime) / 1_000_000F;
        return new TimedExecution(result, milliseconds);
    }
}
