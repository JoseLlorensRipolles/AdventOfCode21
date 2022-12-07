package me.josellorens.aoc2021.day21;

import java.util.Objects;

public class GameState implements Comparable<GameState> {
    public int player1Score;
    public int player2Score;
    public int player1Position;
    public int player2Position;
    public boolean player1Turn;

    public GameState(Builder builder) {
        this.player1Score = builder.player1Score;
        this.player2Score = builder.player2Score;
        this.player1Position = builder.player1Position;
        this.player2Position = builder.player2Position;
        this.player1Turn = builder.player1Turn;
    }

    @Override
    public int compareTo(GameState o) {
        if (player1Score == o.player1Score) {
            return Integer.compare(player2Score, o.player2Score);
        } else {
            return Integer.compare(player1Score, o.player1Score);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameState gameState = (GameState) o;
        return player1Score == gameState.player1Score
            && player2Score == gameState.player2Score
            && player1Position == gameState.player1Position
            && player2Position == gameState.player2Position
            && player1Turn == gameState.player1Turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(player1Score, player2Score, player1Position, player2Position, player1Turn);
    }

    public static final class Builder {

        private int player1Score;
        private int player2Score;
        private int player1Position;
        private int player2Position;
        private boolean player1Turn;

        public Builder player1Score(int player1Score) {
            this.player1Score = player1Score;
            return this;
        }

        public Builder player2Score(int player2Score) {
            this.player2Score = player2Score;
            return this;
        }

        public Builder player1Position(int player1Position) {
            this.player1Position = player1Position;
            return this;
        }

        public Builder player2Position(int player2Position) {
            this.player2Position = player2Position;
            return this;
        }

        public Builder player1Turn(boolean player1Turn) {
            this.player1Turn = player1Turn;
            return this;
        }

        public GameState build() {
            return new GameState(this);
        }

        public static Builder gameState() {
            return new Builder();
        }

        private Builder() {
        }
    }
}
