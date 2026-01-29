package com.cft.fighters;

import com.cft.skillstates.FighterSkillStateManager;
import com.cft.skillstates.FighterSkillStateManagerFactory;
import com.cft.skillupdaters.DirectCompetitionSkillUpdater;
import com.cft.skillupdaters.GaussianRandomSkillUpdater;
import com.cft.skillupdaters.PositionMaintainSkillUpdater;

import java.util.List;
import java.util.Random;

public class FighterFactoryImpl implements FighterFactory {
    private final Random random;

    private final FighterSkillStateManagerFactory skillStateManagerFactory;

    private int curFighterId = 0;

    public FighterFactoryImpl(Random random, FighterSkillStateManagerFactory skillStateManagerFactory) {
        this.random = random;
        this.skillStateManagerFactory = skillStateManagerFactory;
    }

    @Override
    public Fighter createFighter(String fighterName) {
        double initialSkillMean = 3.5;
        double initialSkillStdDev = 1.4;

        double primeEventMean = 30.0;
        double primeEventStdDev = 5.0;

        double initialSkill = random.nextGaussian(initialSkillMean, initialSkillStdDev);
        int primeEvent = (int)random.nextGaussian(primeEventMean, primeEventStdDev);

        FighterSkillStateManager skillStateManager = skillStateManagerFactory.createSkillStateManager(this.random);

        return new Fighter(this.curFighterId++, fighterName, random, initialSkill, primeEvent, List.of(
                new GaussianRandomSkillUpdater(0.8, 0.0, 0.28),
                new DirectCompetitionSkillUpdater(0.25),
                new PositionMaintainSkillUpdater(1.5)
        ), skillStateManager);
    }

    @Override
    public void setCurrentId(int id) {
        this.curFighterId = id;
    }
}
