package dungeon.character;

import dungeon.character.action.*;

import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * Created by Alex Plagman on 5/22/2017.
 */
@SuppressWarnings("MagicNumber")
public final class HeroFactory {
    private final Hashtable<HeroClass, Function<String, Hero>> registry;

    public HeroFactory() {
        registry = new Hashtable<>();
        populateHeroes();
    }

    private void populateHeroes() {
        ActionFactory actionFactory = ActionFactory.getInstance();

        Set<BattleAction<Hero>> warriorActions = new TreeSet<>(new ActionComparator());
        warriorActions.add(actionFactory.getHeroAttack(BasicAttack.getStaticName()));
        warriorActions.add(actionFactory.getHeroAttack(CrushingAttack.getStaticName()));

        registry.put(HeroClass.WARRIOR, (String name) ->
                new Hero(name, 125, 4, 0.8, new Range(35, 60),
                        "swings a mighty sword at", 0.2, warriorActions));

        Set<BattleAction<Hero>> sorceressActions = new TreeSet<>(new ActionComparator());
        sorceressActions.add(actionFactory.getHeroAttack(BasicAttack.getStaticName()));
        sorceressActions.add(actionFactory.getHeroAttack(BoostHPSpell.getStaticName()));

        registry.put(HeroClass.SORCERESS, (String name) ->
                new Hero(name, 75, 5, 0.7, new Range(25, 50),
                        "casts a spell of fireball at", 0.3, sorceressActions));

        Set<BattleAction<Hero>> thiefActions = new TreeSet<>(new ActionComparator());
        thiefActions.add(actionFactory.getHeroAttack(BasicAttack.getStaticName()));
        thiefActions.add(actionFactory.getHeroAttack(SurpriseAttack.getStaticName()));

        registry.put(HeroClass.THIEF, (String name) ->
                new Hero(name, 100, 6, 0.8, new Range(20, 40),
                        "thrusts his dagger toward", 0.5, thiefActions));

        Set<BattleAction<Hero>> mageActions = new TreeSet<>(new ActionComparator());
        mageActions.add(actionFactory.getHeroAttack(BasicAttack.getStaticName()));
        mageActions.add(actionFactory.getHeroAttack(FireBlastSpell.getStaticName()));

        registry.put(HeroClass.MAGE, (String name) ->
                new Hero(name, 50, 5, 0.6, new Range(5, 20),
                        "swings his staff at", 0.1, mageActions));

        Set<BattleAction<Hero>> archerActions = new TreeSet<>(new ActionComparator());
        archerActions.add(actionFactory.getHeroAttack(BasicAttack.getStaticName()));
        archerActions.add(actionFactory.getHeroAttack(MultiShotAttack.getStaticName()));

        registry.put(HeroClass.ARCHER, (String name) ->
                new Hero(name, 100, 6, 0.7, new Range(30, 55),
                        "shoots an arrow at", 0.2, archerActions));
    }

    public Hero createHero(HeroClass heroClass, String name) {
        if (name == null || name.isEmpty()) {
            return registry.get(heroClass).apply("Zote the Mighty");
        }
        return registry.get(heroClass).apply(name);
    }

    public enum HeroClass {
        WARRIOR, SORCERESS, THIEF, MAGE, ARCHER
    }
}
