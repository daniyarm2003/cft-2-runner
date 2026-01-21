package com.cft.skillstates;

import com.cft.fighters.Fighter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FighterTrendSkillState implements FighterSkillState {

    private final double weight;
    private final double skillTrend;

    @JsonCreator
    public FighterTrendSkillState(@JsonProperty("weight") double weight, @JsonProperty("skillTrend") double skillTrend) {
        this.weight = weight;
        this.skillTrend = skillTrend;
    }

    @Override
    public double getWeight(Fighter fighter, List<Fighter> otherFighters) {
        double weightScale = 1.0 / (1.0 + Math.exp((fighter.getFightCount() - fighter.getPrimeEvent()) * Math.signum(this.skillTrend)));

        if(this.skillTrend < 0.0 && fighter.getFightCount() > 2 * fighter.getPrimeEvent()) {
            weightScale *= 1.0 + fighter.getFightCount() - 2 * fighter.getPrimeEvent();
        }

        return this.weight * weightScale;
    }

    @Override
    public double getModifiedSkillIncrease(Fighter fighter, double baseIncrease) {
        return this.skillTrend * Math.abs(baseIncrease);
    }
}
