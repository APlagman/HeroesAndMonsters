package dungeon;

import dungeon.character.DungeonCharacter;
import dungeon.character.Hero;
import dungeon.character.Monster;
import dungeon.character.ActionLog;
import dungeon.character.Effect;

import java.io.*;
import java.util.*;

/**
 * Created by Alex Plagman on 5/23/2017.
 */
class Battle implements Serializable {
    static final String SAVE_FILE_NAME = '.' + File.separator + "saveGame.ser";

    private final ActionLog actionLog;
    private final Stack<Round> roundsFinished;
    private final int heroCount;
    private final int monsterCount;
    private final List<Hero> heroes;
    private final List<Monster> monsters;

    Battle(int heroCount, int monsterCount) {
        actionLog = new ActionLog();
        this.heroCount = heroCount;
        this.monsterCount = monsterCount;
        this.heroes = new ArrayList<>(heroCount);
        this.monsters = new ArrayList<>(monsterCount);
        this.roundsFinished = new Stack<>();
    }

    void init(Dungeon dungeon) {
        actionLog.clear();
        roundsFinished.clear();
        heroes.clear();
        monsters.clear();
        System.out.println("Creating battle with " + heroCount + ((heroCount == 1) ? " hero" : " heroes") + " against " + monsterCount + " monsters.");
        for (int i = 0; i < heroCount; ++i) {
            heroes.add(dungeon.chooseHero());
            System.out.println();
        }
        for (int i = 0; i < monsterCount; ++i) {
            monsters.add(dungeon.createRandomMonster());
        }
    }

    void begin() {
        if (roundsFinished.isEmpty()) {
            System.out.println("A new battle begins!");
        }

        Round nextRound = new Round(this);
        do {
            Round round = nextRound;
            nextRound = determineNextRound(round.play(), round);
        } while (nextRound != null);

        if (getLivingHeroes().isEmpty()) {
            System.out.println("The heroes were defeated :-(");
        } else if (getLivingMonsters().isEmpty()) {
            System.out.println("The heroes were victorious!");
        } else {
            System.out.println("Quitters never win ;-)");
        }
    }

    private Round determineNextRound(Round.Result result, Round old) {
        char input = ' ';
        switch (result) {
            case UNDONE:
                if (!roundsFinished.isEmpty()) {
                    Round previous = roundsFinished.pop();
                    System.out.println(System.lineSeparator() + "Returning to previous round..." + System.lineSeparator() + "----------------------------------");
                    previous.undo();
                    return previous;
                } else {
                    undoLastAction(); // Ensures all actions are undone when playing a game from a save file.
                    System.out.println(System.lineSeparator() + "You are at the start of the battle!");
                    return new Round(this);
                }
            case NORMAL:
                roundsFinished.push(old);
                do {
                    System.out.print("Enter 'Q' to quit, 'S' to save, or any other character to continue: ");
                    input = Keyboard.readChar();
                    if (input == 's' || input == 'S') {
                        saveGame();
                    }
                } while (input == 's' || input == 'S');
            default:
                if (input == 'q' || input == 'Q') {
                    return null;
                }
        }
        if (result == Round.Result.NORMAL) {
            return new Round(this);
        }
        return null;
    }

    List<Hero> getLivingHeroes() {
        return getLiving(heroes);
    }

    List<Monster> getLivingMonsters() {
        return getLiving(monsters);
    }

    void undoLastAction() {
        actionLog.undoLastAction();
    }

    private void saveGame() {
        try (FileOutputStream oFile = new FileOutputStream(SAVE_FILE_NAME);
             ObjectOutputStream out = new ObjectOutputStream(oFile)) {
            out.writeObject(this);
            System.out.println("Saved data to " + SAVE_FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving game.");
            e.printStackTrace();
        }
    }

    private static <E extends DungeonCharacter> List<E> getLiving(List<E> list) {
        List<E> living = new ArrayList<>();
        list.forEach((character) -> {
            if (character.isAlive())
                living.add(character);
        });
        return living;
    }

    void displayBattleStatus() {
        System.out.println("Rounds finished: " + roundsFinished.size() +
                System.lineSeparator() + "Heroes remaining: " + getLivingHeroes().size() +
                System.lineSeparator() + "Monsters remaining: " + getLivingMonsters().size());
    }

    void logAction(Collection<Effect> effects) {
        actionLog.logAction(effects);
    }

    Optional<Hero> getRandomLivingHero() {
        return Utils.getRandomElement(getLivingHeroes());
    }

    Optional<Monster> promptForMonster() {
        System.out.println("Please choose a monster:");
        return Utils.promptForElement(getLivingMonsters());
    }
}
