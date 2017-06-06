package dungeon.character;

import dungeon.character.action.ActionFactory;
import dungeon.character.action.BasicAttack;
import dungeon.character.action.BattleAction;

import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * Created by Alex Plagman on 5/22/2017.
 */
@SuppressWarnings("MagicNumber")
public final class MonsterFactory {
    private final Hashtable<MonsterType, Supplier<Monster>> registry;

    public MonsterFactory() {
        registry = new Hashtable<>();
        populateMonsters();
    }

    private void populateMonsters() {
        ActionFactory actionFactory = ActionFactory.getInstance();
        Set<BattleAction<Monster>> monsterActions = new TreeSet<>(new ActionComparator());
        monsterActions.add(actionFactory.getMonsterAttack(BasicAttack.getStaticName()));

        registry.put(MonsterType.GREMLIN, () ->
                new Monster("Gnarltooth the Gremlin", 70, 5, 0.8,
                        new Range(15, 30), "jabs its kris at",
                        0.4, new Range(20, 40), monsterActions));
        registry.put(MonsterType.OGRE, () ->
                new Monster("Oscar the Ogre", 200, 2, 0.6,
                        new Range(30, 60), "slowly swings a club toward",
                        0.1, new Range(30, 60), monsterActions));
        registry.put(MonsterType.SKELETON, () ->
                new Monster("Skeletor the Skeleton", 100, 3, 0.8,
                        new Range(30, 50), "slices its rusty blade at",
                        0.3, new Range(30, 50), monsterActions));
        registry.put(MonsterType.SHADE, () ->
                new Monster("Sargath the Shade", 125, 4, 1.0,
                        new Range(10, 40), "extends shadowy tendrils toward",
                        0.2, new Range(20, 30), monsterActions));
        registry.put(MonsterType.SLIME, () ->
                new Monster("Glob the Slime", 50, 3, 0.5,
                        new Range(5, 10), "launches itself at",
                        0.9, new Range(10, 20), monsterActions));
    }

    public Monster createRandomMonster() {
        int choice = (int) (Math.random() * MonsterType.values().length);
        return registry.get(MonsterType.values()[choice]).get();
    }

    private enum MonsterType {
        GREMLIN, OGRE, SKELETON, SHADE, SLIME
    }
}
