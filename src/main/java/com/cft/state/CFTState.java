package com.cft.state;

import com.cft.fighters.Fighter;
import com.cft.fighters.FighterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CFTState {
    private List<Fighter> fighters;

    private final FighterFactory fighterFactory;
    private final Saver stateSaver;

    public CFTState(FighterFactory fighterFactory, Saver stateSaver) {
        this.fighters = new ArrayList<>();

        this.fighterFactory = fighterFactory;
        this.stateSaver = stateSaver;
    }

    public void addFighter(String fighterName) {
        this.fighters.add(this.fighterFactory.createFighter(fighterName));
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
        return new SaveContext(List.copyOf(this.fighters));
    }

    public record SaveContext(List<Fighter> fighters) {}

    public interface Saver {
        void save(SaveContext context) throws IOException;
        boolean isSaved();
        SaveContext load() throws IOException;
    }
}
