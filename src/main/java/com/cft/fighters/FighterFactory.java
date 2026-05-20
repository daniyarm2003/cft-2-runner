package com.cft.fighters;

import com.cft.attacks.SpecialAttack;

public interface FighterFactory {
    Fighter createFighter(String fighterName, FighterHeartClass healthClass, SpecialAttack.Type attackType);
    void setCurrentId(int id);
}
