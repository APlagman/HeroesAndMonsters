package dungeon.character.action;

import dungeon.character.Hero;
import dungeon.character.Monster;

import java.util.Hashtable;

/**
 * Created by Alex Plagman on 5/22/2017.
 */
public class ActionFactory {
    private static ActionFactory instance;
    private final Hashtable<String, BattleAction<Hero>> heroActions;
    private final Hashtable<String, BattleAction<Monster>> monsterActions;

    private ActionFactory() {
        heroActions = new Hashtable<>();
        BasicAttack<Hero> basic = new BasicAttack<>();
        heroActions.put(BasicAttack.getStaticName(), basic);
        heroActions.put(CrushingAttack.getStaticName(), new CrushingAttack<>());
        heroActions.put(SurpriseAttack.getStaticName(), new SurpriseAttack(basic));
        heroActions.put(BoostHPSpell.getStaticName(), new BoostHPSpell<>());
        heroActions.put(FireBlastSpell.getStaticName(), new FireBlastSpell<>());
        heroActions.put(MultiShotAttack.getStaticName(), new MultiShotAttack<>());

        monsterActions = new Hashtable<>();
        monsterActions.put(BasicAttack.getStaticName(), new BasicAttack<>());
    }

    public static ActionFactory getInstance() {
        if (instance == null) {
            instance = new ActionFactory();
        }

        return instance;
    }

    public BattleAction<Hero> getHeroAttack(String type) {
        return heroActions.get(type);
    }

    public BattleAction<Monster> getMonsterAttack(String type) {
        return monsterActions.get(type);
    }
}
