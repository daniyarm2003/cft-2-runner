package com.cft.attacks;

public interface SpecialAttackFactory {
    SpecialAttack createSpecialAttack(SpecialAttack.Type attackType);
}
