package com.cft.fighters;

public interface FighterFactory {
    Fighter createFighter(String fighterName, FighterHeartClass healthClass);
    void setCurrentId(int id);
}
