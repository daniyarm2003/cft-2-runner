package com.cft.fighters;

public interface FighterFactory {
    Fighter createFighter(String fighterName);
    void setCurrentId(int id);
}
