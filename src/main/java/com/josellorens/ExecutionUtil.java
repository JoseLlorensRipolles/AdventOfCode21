package com.josellorens;

import java.util.function.Function;

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

    public static <E> TimedExecution timedExecution(Function<E, String> function, E input) {
        final var startTime = nanoTime();
        final var result = function.apply(input);
        final var endTime = nanoTime();
        final var duration = (endTime - startTime) / 1_000_000F;
        return new TimedExecution(result, duration);
    }


}
