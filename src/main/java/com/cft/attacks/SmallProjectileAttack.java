package com.cft.attacks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SmallProjectileAttack implements SpecialAttack {

    private final Type projectileType;

    @JsonCreator
    public SmallProjectileAttack(@JsonProperty("projectileType") Type projectileType) {
        this.projectileType = projectileType;
    }

    public Type getProjectileType() {
        return this.projectileType;
    }

    @Override
    public SpecialAttack.Type getSpecialAttackType() {
        return SpecialAttack.Type.SMALL_PROJECTILE;
    }

    public enum Type {
        @JsonProperty("shuriken")
        SHURIKEN,

        @JsonProperty("knife")
        KNIFE,

        @JsonProperty("bullet")
        BULLET,

        @JsonProperty("baseball")
        BASEBALL;
    }
}
