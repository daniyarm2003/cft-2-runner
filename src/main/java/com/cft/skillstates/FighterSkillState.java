package com.cft.skillstates;

import com.cft.fighters.Fighter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FighterNeutralSkillState.class, name = "NEUTRAL_STATE"),
        @JsonSubTypes.Type(value = FighterTrendSkillState.class, name = "TREND_STATE")
})
public interface FighterSkillState {
    double getWeight(Fighter fighter, List<Fighter> otherFighters);
    double getModifiedSkillIncrease(Fighter fighter, double baseIncrease);
}
