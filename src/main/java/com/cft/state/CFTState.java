package com.cft.state;

import com.cft.attacks.SpecialAttack;
import com.cft.attacks.SpecialAttackFactory;
import com.cft.fighters.Fighter;
import com.cft.fighters.FighterFactory;
import com.cft.fighters.FighterHeartClass;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CFTState {
    private final SpecialAttackFactory specialAttackFactory;
    private List<Fighter> fighters;
    private int cftEventsPassed = 0;

    private final FighterFactory fighterFactory;
    private final Saver stateSaver;

    public CFTState(FighterFactory fighterFactory, Saver stateSaver, SpecialAttackFactory specialAttackFactory) {
        this.specialAttackFactory = specialAttackFactory;
        this.fighters = new ArrayList<>();

        this.fighterFactory = fighterFactory;
        this.stateSaver = stateSaver;
    }

    public Fighter addFighter(String fighterName, FighterHeartClass healthClass, SpecialAttack.Type attackType) {
        Fighter fighter = this.fighterFactory.createFighter(fighterName, healthClass, attackType);
        this.fighters.add(fighter);

        return fighter;
    }

    public void runEvent() {
        List<Fighter> activeFighters = this.fighters.stream()
                .filter(fighter -> !fighter.isDeleted())
                .toList();

        for(Fighter fighter : activeFighters) {
            List<Fighter> otherFighters = activeFighters.stream()
                    .filter(other -> other.getId() != fighter.getId() && other.getHeartClass() == fighter.getHeartClass())
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

    public Fighter changeFighter(int fighterId, FighterHeartClass healthClass, SpecialAttack.Type attackType, Random random) throws FighterNotFoundException {
        Fighter fighter = this.getFighterById(fighterId);

        if(healthClass != null) {
            FighterHeartClass curHeartClass = fighter.getHeartClass();
            double skew = curHeartClass.getSkew();

            if(curHeartClass != healthClass) {
                skew = healthClass.getMinHp() < curHeartClass.getMinHp() ? 0.8 : 0.2;
            }

            fighter.setHealth(healthClass.generateHealthValue(random, skew));
        }

        if(attackType != null) {
            fighter.setSpecialAttack(this.specialAttackFactory.createSpecialAttack(attackType));
        }

        return fighter;
    }

    public Fighter deleteFighter(int fighterId) throws FighterNotFoundException {
        Fighter fighter = this.getFighterById(fighterId);
        fighter.setDeleted(true);

        return fighter;
    }

    public void dumpBasicInfo(OutputStream stream) throws IOException {
        try(OutputStreamWriter writer = new OutputStreamWriter(stream); BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write("CFT Events Passed: %d\n".formatted(this.getNumCftEventsPassed()));

            bufferedWriter.write("Fighter Info:\n");
            for(Fighter fighter : this.fighters) {
                bufferedWriter.write("ID: %d, Name: %s, Heart Class: %s (%s), Special Attack Type: %s, Deleted: %s\n".formatted(fighter.getId(), fighter.getName(),
                        fighter.getHeartClass().getHeartClassName(), fighter.getHeartClass().getShortName(), fighter.getSpecialAttack().getSpecialAttackType().getReadableName(),
                        fighter.isDeleted() ? "Yes" : "No"));
            }
        }
    }

    public void loadState() throws IOException {
        if(this.stateSaver.isSaved()) {
            SaveContext saveContext = this.stateSaver.load();

            this.fighters = new ArrayList<>(saveContext.fighters());
            this.fighterFactory.setCurrentId(this.fighters.stream().mapToInt(Fighter::getId).max().orElse(-1) + 1);

            this.cftEventsPassed = saveContext.cftEventsPassed();
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
