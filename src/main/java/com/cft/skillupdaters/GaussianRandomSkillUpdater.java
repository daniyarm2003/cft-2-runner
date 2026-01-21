package com.cft.skillupdaters;

import com.cft.fighters.Fighter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Random;

public class GaussianRandomSkillUpdater implements FighterSkillUpdater {
    private final double weight;

    @JsonProperty
    private final double mean;

    @JsonProperty
    private final double stdDev;

    @JsonCreator
    public GaussianRandomSkillUpdater(@JsonProperty("weight") double weight, @JsonProperty("mean") double mean, @JsonProperty("stdDev") double stdDev) {
        this.weight = weight;
        this.mean = mean;
        this.stdDev = stdDev;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public double getSkillIncrease(Fighter fighter, List<Fighter> otherFighters, Random random) {
        return random.nextGaussian(this.mean, this.stdDev);
    }
}
