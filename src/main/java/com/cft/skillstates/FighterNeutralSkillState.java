package com.cft.skillstates;

import com.cft.fighters.Fighter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FighterNeutralSkillState implements FighterSkillState {
    private final double weight;

    @JsonCreator
    public FighterNeutralSkillState(@JsonProperty("weight") double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight(Fighter fighter, List<Fighter> otherFighters) {
        return this.weight;
    }

    @Override
    public double getModifiedSkillIncrease(Fighter fighter, double baseIncrease) {
        return baseIncrease;
    }
}
