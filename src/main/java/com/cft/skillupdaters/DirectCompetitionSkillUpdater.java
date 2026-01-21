package com.cft.skillupdaters;

import com.cft.fighters.Fighter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DirectCompetitionSkillUpdater implements FighterSkillUpdater {
    private final double weight;

    @JsonCreator
    public DirectCompetitionSkillUpdater(@JsonProperty("weight") double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public double getSkillIncrease(Fighter fighter, List<Fighter> otherFighters, Random random) {
        Fighter closest = otherFighters.stream()
                .min(Comparator.comparingDouble(other -> Math.abs(other.getTotalSkillLevel() - fighter.getTotalSkillLevel())))
                .orElse(fighter);

        return Math.max(closest.getTotalSkillLevel() - fighter.getTotalSkillLevel(), 0.0);
    }
}
