package com.cft.fighters;

import com.cft.skillstates.FighterSkillStateManager;
import com.cft.skillupdaters.FighterSkillUpdater;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Fighter {
    private final int id;

    @JsonIgnore
    private final Random random;

    private final String name;

    private final List<FighterSkill> skills;

    private final int primeEvent;

    private final List<FighterSkillUpdater> skillUpdaters;

    @JsonProperty
    private final FighterSkillStateManager skillStateManager;

    private int fightCount = 0;

    private boolean deleted = false;

    public Fighter(int id, String name, Random random, double initialSkill, int primeEvent, List<FighterSkillUpdater> skillUpdaters, FighterSkillStateManager skillStateManager) {
        this.id = id;
        this.name = name;

        this.random = random;
        this.primeEvent = primeEvent;
        this.skills = new ArrayList<>();

        for(FighterSkill.SkillType skillType : FighterSkill.SkillType.values()) {
            this.skills.add(new FighterSkill(skillType, 0.0));
        }

        this.updateSkills(initialSkill);

        this.skillUpdaters = List.copyOf(skillUpdaters);
        this.skillStateManager = skillStateManager;
    }

    @JsonCreator
    public Fighter(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("primeEvent") int primeEvent,
                   @JsonProperty("skills") List<FighterSkill> skills, @JsonProperty("skillUpdaters") List<FighterSkillUpdater> skillUpdaters,
                   @JsonProperty("skillStateManager") FighterSkillStateManager skillStateManager, @JsonProperty("fightCount") int fightCount,
                   @JsonProperty("deleted") boolean deleted) {

        this.id = id;
        this.name = name;

        this.random = new Random();
        this.primeEvent = primeEvent;
        this.skills = List.copyOf(skills);

        this.skillUpdaters = List.copyOf(skillUpdaters);
        this.skillStateManager = skillStateManager;

        this.fightCount = fightCount;
        this.deleted = deleted;
    }

    public void updateSkills(double skillDelta) {
        double[] intervalEnds = this.random.doubles(this.skills.size() - 1).sorted().toArray();

        for(int i = 0; i < intervalEnds.length + 1; i++) {
            double intervalStart = i > 0 ? intervalEnds[i - 1] : 0.0;
            double intervalEnd = i < intervalEnds.length ? intervalEnds[i] : 1.0;

            double intervalSize = intervalEnd - intervalStart;
            double delta = skillDelta * intervalSize;

            FighterSkill skill = this.skills.get(i);
            double newSkillLevel = skill.getSkillLevel() + delta;

            if(newSkillLevel < 0.0) {
                newSkillLevel -= 2.0 * delta;
            }

            skill.setSkillLevel(newSkillLevel);
        }
    }

    public double getTotalSkillLevel() {
        return this.skills.stream().mapToDouble(FighterSkill::getSkillLevel).sum();
    }

    public int getPrimeEvent() {
        return this.primeEvent;
    }

    public int getFightCount() {
        return this.fightCount;
    }

    public int getId() {
        return this.id;
    }

    public Iterator<FighterSkill> getSkillData() {
        return this.skills.iterator();
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void update(List<Fighter> otherFighters) {
        double runningTotal = 0.0;
        double netSkillIncrease = 0.0;

        for(FighterSkillUpdater skillUpdater : this.skillUpdaters) {
            double weight = skillUpdater.getWeight();

            runningTotal += weight;
            netSkillIncrease += weight * skillUpdater.getSkillIncrease(this, otherFighters, this.random);
        }

        netSkillIncrease /= runningTotal;

        this.skillStateManager.updateState(this, otherFighters, this.random);
        netSkillIncrease = this.skillStateManager.getModifiedSkillIncrease(this, netSkillIncrease);

        this.updateSkills(netSkillIncrease);

        this.fightCount++;
    }
}
