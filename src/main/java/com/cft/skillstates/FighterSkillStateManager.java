package com.cft.skillstates;

import com.cft.fighters.Fighter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Random;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FighterSkillStateManager {
    private final List<FighterSkillState> skillStates;
    private final double stateDurationMean, stateDurationStdDev;

    private FighterSkillState currentState = null;
    private int currentStateLifespan = 0;

    @JsonCreator
    public FighterSkillStateManager(@JsonProperty("skillStates") List<FighterSkillState> skillStates, @JsonProperty("stateDurationMean") double stateDurationMean,
                                     @JsonProperty("stateDurationStdDev") double stateDurationStdDev, @JsonProperty("currentState") FighterSkillState currentState,
                                     @JsonProperty("currentStateLifespan") int currentStateLifespan) {

        this(skillStates, stateDurationMean, stateDurationStdDev);

        this.currentState = currentState;
        this.currentStateLifespan = currentStateLifespan;
    }

    public FighterSkillStateManager(List<FighterSkillState> skillStates, double stateDurationMean, double stateDurationStdDev) {
        if(skillStates.isEmpty()) {
            throw new IllegalArgumentException("Skill state list cannot be empty");
        }

        this.skillStates = List.copyOf(skillStates);
        this.stateDurationMean = stateDurationMean;
        this.stateDurationStdDev = stateDurationStdDev;
    }

    private FighterSkillState getRandomSkillState(Fighter fighter, List<Fighter> otherFighters, Random random) {
        double totalWeight = this.skillStates.stream()
                .mapToDouble(state -> state.getWeight(fighter, otherFighters))
                .sum();

        double randomSelection = totalWeight * random.nextDouble();
        double runningTotal = 0.0;

        for(FighterSkillState state : this.skillStates) {
            runningTotal += state.getWeight(fighter, otherFighters);

            if(runningTotal >= randomSelection) {
                return state;
            }
        }

        return this.skillStates.getLast();
    }

    public void updateState(Fighter fighter, List<Fighter> otherFighters, Random random) {
        if(this.currentStateLifespan > 0) {
            this.currentStateLifespan--;
            return;
        }

        this.currentState = this.getRandomSkillState(fighter, otherFighters, random);
        this.currentStateLifespan = Math.max((int)random.nextGaussian(this.stateDurationMean, this.stateDurationStdDev), 2);
    }

    public double getModifiedSkillIncrease(Fighter fighter, double baseIncrease) {
        return this.currentState.getModifiedSkillIncrease(fighter, baseIncrease);
    }
}
