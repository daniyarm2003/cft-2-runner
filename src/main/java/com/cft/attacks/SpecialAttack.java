package com.cft.attacks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BallAttack.class, name = "BALL"),
        @JsonSubTypes.Type(value = SmallProjectileAttack.class, name = "SMALL_PROJECTILE"),
        @JsonSubTypes.Type(value = LightningAttack.class, name = "LIGHTNING")
})
public interface SpecialAttack {
    @JsonIgnore
    Type getSpecialAttackType();

    enum Type {
        BALL, SMALL_PROJECTILE, LIGHTNING;
    }
}
