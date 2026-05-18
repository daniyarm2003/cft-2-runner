package com.cft.attacks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BallAttack implements SpecialAttack {

    private final int color;

    @JsonCreator
    public BallAttack(@JsonProperty("color") int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    @Override
    public Type getSpecialAttackType() {
        return Type.BALL;
    }
}
