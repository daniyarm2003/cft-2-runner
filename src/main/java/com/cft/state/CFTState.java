package com.cft.state;

import com.cft.fighters.Fighter;
import com.cft.fighters.FighterFactory;
import com.cft.fighters.FighterHeartClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CFTState {
    private List<Fighter> fighters;
    private int cftEventsPassed = 0;

    private final FighterFactory fighterFactory;
    private final Saver stateSaver;

    public CFTState(FighterFactory fighterFactory, Saver stateSaver) {
        this.fighters = new ArrayList<>();

        this.fighterFactory = fighterFactory;
        this.stateSaver = stateSaver;
    }

    public Fighter addFighter(String fighterName, FighterHeartClass healthClass) {
        Fighter fighter = this.fighterFactory.createFighter(fighterName, healthClass);
        this.fighters.add(fighter);

        return fighter;
    }

    public void runEvent() {
        List<Fighter> activeFighters = this.fighters.stream()
                .filter(fighter -> !fighter.isDeleted())
                .toList();

        for(Fighter fighter : activeFighters) {
            List<Fighter> otherFighters = activeFighters.stream()
                    .filter(other -> other.getId() != fighter.getId())
                    .toList();

            fighter.update(otherFighters);
        }

        this.cftEventsPassed++;
    }

    public int getNumCftEventsPassed() {
        return this.cftEventsPassed;
    }

    private Fighter getFighterById(int id) throws FighterNotFoundException {
        return this.fighters.stream().filter(f -> f.getId() == id).findFirst()
                .orElseThrow(() -> new FighterNotFoundException(id));
    }

    public Fighter changeFighterHeartClass(int fighterId, FighterHeartClass healthClass, Random random) throws FighterNotFoundException {
        Fighter fighter = this.getFighterById(fighterId);

        FighterHeartClass curHeartClass = fighter.getHeartClass();
        double skew = curHeartClass.getSkew();

        if(curHeartClass != healthClass) {
            skew = healthClass.getMinHp() < curHeartClass.getMinHp() ? 0.8 : 0.2;
        }

        fighter.setHealth(healthClass.generateHealthValue(random, skew));

        return fighter;
    }

    public Fighter deleteFighter(int fighterId) throws FighterNotFoundException {
        Fighter fighter = this.getFighterById(fighterId);
        fighter.setDeleted(true);

        return fighter;
    }

    public void loadState() throws IOException {
        if(this.stateSaver.isSaved()) {
            SaveContext saveContext = this.stateSaver.load();

            this.fighters = new ArrayList<>(saveContext.fighters());
            this.fighterFactory.setCurrentId(this.fighters.stream().mapToInt(Fighter::getId).max().orElse(-1) + 1);
        }
    }

    public void saveState() throws IOException {
        this.stateSaver.save(this.getSaveContext());
    }

    private SaveContext getSaveContext() {
        return new SaveContext(List.copyOf(this.fighters), this.cftEventsPassed);
    }

    public record SaveContext(List<Fighter> fighters, int cftEventsPassed) {}

    public interface Saver {
        void save(SaveContext context) throws IOException;
        boolean isSaved();
        SaveContext load() throws IOException;
    }

    public static class FighterNotFoundException extends RuntimeException {
        private final int fighterId;

        public FighterNotFoundException(int fighterId) {
            super("Fighter with ID %d not found".formatted(fighterId));
            this.fighterId = fighterId;
        }

        public int getFighterId() {
            return this.fighterId;
        }
    }
}
