package com.cft.skillupdaters;

import com.cft.fighters.Fighter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Random;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectCompetitionSkillUpdater.class, name = "DIRECT_COMPETITION"),
        @JsonSubTypes.Type(value = FollowAverageSkillUpdater.class, name = "FOLLOW_AVERAGE"),
        @JsonSubTypes.Type(value = GaussianRandomSkillUpdater.class, name = "GAUSSIAN"),
        @JsonSubTypes.Type(value = PositionMaintainSkillUpdater.class, name = "POSITION_MAINTAIN")
})
public interface FighterSkillUpdater {
    double getWeight();
    double getSkillIncrease(Fighter fighter, List<Fighter> otherFighters, Random random);
}
