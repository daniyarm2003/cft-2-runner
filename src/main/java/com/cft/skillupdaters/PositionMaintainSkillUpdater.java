package com.cft.skillupdaters;

import com.cft.fighters.Fighter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Random;

public class PositionMaintainSkillUpdater implements FighterSkillUpdater {
    private final double weight;

    @JsonCreator
    public PositionMaintainSkillUpdater(@JsonProperty("weight") double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public double getSkillIncrease(Fighter fighter, List<Fighter> otherFighters, Random random) {
        return otherFighters.stream()
                .mapToInt(other -> other.getTotalSkillLevel() < fighter.getTotalSkillLevel() ? 1 : 0)
                .average()
                .orElse(0.0);
    }
}
