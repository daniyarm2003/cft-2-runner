package com.cft.skillupdaters;

import com.cft.fighters.Fighter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Random;

public class FollowAverageSkillUpdater implements FighterSkillUpdater {
    private final double weight;

    @JsonCreator
    public FollowAverageSkillUpdater(@JsonProperty("weight") double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public double getSkillIncrease(Fighter fighter, List<Fighter> otherFighters, Random random) {
        double avgPos = otherFighters.stream()
                .mapToDouble(Fighter::getTotalSkillLevel)
                .average()
                .orElse(fighter.getTotalSkillLevel());

        return avgPos - fighter.getTotalSkillLevel();
    }
}
