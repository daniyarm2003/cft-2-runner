package com.cft.fighters;

public interface FighterFactory {
    Fighter createFighter(String fighterName, FighterHealthClass healthClass);
    void setCurrentId(int id);
}
