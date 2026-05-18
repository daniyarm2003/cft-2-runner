package com.cft.attacks;

public class LightningAttack implements SpecialAttack {
    @Override
    public Type getSpecialAttackType() {
        return Type.LIGHTNING;
    }
}
