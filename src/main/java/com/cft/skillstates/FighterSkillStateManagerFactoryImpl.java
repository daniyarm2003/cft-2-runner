package com.cft.skillstates;

import java.util.List;
import java.util.Random;

public class FighterSkillStateManagerFactoryImpl implements FighterSkillStateManagerFactory {
    private final double stateDurationMean, stateDurationStdDev;

    public FighterSkillStateManagerFactoryImpl(double stateDurationMean, double stateDurationStdDev) {
        this.stateDurationMean = stateDurationMean;
        this.stateDurationStdDev = stateDurationStdDev;
    }

    @Override
    public FighterSkillStateManager createSkillStateManager(Random random) {
        List<FighterSkillState> states = List.of(
                new FighterNeutralSkillState(1.2),
                new FighterTrendSkillState(0.5, 5.0),
                new FighterTrendSkillState(0.5, -5.0)
        );

        return new FighterSkillStateManager(states, this.stateDurationMean, this.stateDurationStdDev);
    }
}
