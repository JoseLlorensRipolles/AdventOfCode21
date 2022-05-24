package me.josellorens.aoc2021.day02;

public class SubmarineState {

    public long horizontal;
    public long depth;
    public long aim;

    public SubmarineState(long horizontal, long depth, long aim){
        this.horizontal = horizontal;
        this.depth = depth;
        this.aim = aim;
    }

    public static final class Builder{
        private long horizontal;
        private long depth;
        private long aim;

        public Builder horizontal(long horizontal){
            this.horizontal = horizontal;
            return this;
        }

        public Builder depth(long depth){
            this.depth = depth;
            return this;
        }

        public Builder aim(long aim){
            this.aim = aim;
            return this;
        }

        public SubmarineState build(){
            return new SubmarineState(horizontal, depth, aim);
        }

        private Builder(){

        }

        public static Builder submarineState(){
            return new Builder();
        }
    }
}
