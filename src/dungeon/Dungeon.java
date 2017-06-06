package dungeon;

import dungeon.character.Hero;
import dungeon.character.HeroFactory;
import dungeon.character.Monster;
import dungeon.character.MonsterFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static dungeon.character.HeroFactory.HeroClass;

class Dungeon {
    private static final int DEFAULT_HERO_COUNT = 3, DEFAULT_MONSTER_COUNT = 3;

    private final HeroFactory heroFactory;
    private final MonsterFactory monsterFactory;

    private Dungeon() {
        this.heroFactory = new HeroFactory();
        this.monsterFactory = new MonsterFactory();
    }

    public static void main(String[] args) {
        Dungeon dungeon = new Dungeon();
        Battle battle;
        if (promptYes("Load existing save?")) {
            battle = dungeon.loadGame();
        } else {
            battle = new Battle(promptForHeroCount(), DEFAULT_MONSTER_COUNT);
            battle.init(dungeon);
        }
        battle.begin();
        while (promptYes("Play again?")) {
            battle = new Battle(promptForHeroCount(), DEFAULT_MONSTER_COUNT);
            battle.init(dungeon);
            battle.begin();
        }
    }

    private static boolean promptYes(String message) {
        System.out.println(message + " (Y/N)");
        char again = Keyboard.readChar();

        return (again == 'Y' || again == 'y');
    }

    private Battle loadGame() {
        Battle battle;
        try (FileInputStream iFile = new FileInputStream(Battle.SAVE_FILE_NAME);
             ObjectInputStream in = new ObjectInputStream(iFile)) {
            battle = (Battle) in.readObject();
            battle.displayBattleStatus();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading save; creating new game.");
            int heroCount = promptForHeroCount();
            battle = new Battle(heroCount, DEFAULT_MONSTER_COUNT);
            battle.init(this);
        }
        return battle;
    }

    private static int promptForHeroCount() {
        System.out.println("Please enter a number of heroes (1+):");
        int heroCount = Keyboard.readInt();
        if (heroCount < 1) {
            heroCount = DEFAULT_HERO_COUNT;
            System.out.println("Invalid number of heroes. Using default (" + DEFAULT_HERO_COUNT + ").");
        }
        return heroCount;
    }

    Hero chooseHero() {
        System.out.println("Choose a hero:");
        for (int c = 0; c < HeroClass.values().length; ++c) {
            System.out.println((c + 1) + ". " + Utils.toCamelCase(HeroClass.values()[c].name()));
        }
        int choice = Keyboard.readInt();
        System.out.print("Enter character name: ");
        String name = Keyboard.readString();

        if (choice > 0 && choice < HeroClass.values().length + 1) {
            return heroFactory.createHero(HeroClass.values()[choice - 1], name);
        } else {
            System.out.println("Invalid choice, returning default character.");
            return heroFactory.createHero(HeroClass.WARRIOR, name);
        }
    }

    Monster createRandomMonster() {
        return monsterFactory.createRandomMonster();
    }
}