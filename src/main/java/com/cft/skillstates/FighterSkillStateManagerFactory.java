package com.cft.skillstates;

import java.util.Random;

public interface FighterSkillStateManagerFactory {
    FighterSkillStateManager createSkillStateManager(Random random);
}
