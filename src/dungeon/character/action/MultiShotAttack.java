package dungeon.character.action;

import dungeon.Round;
import dungeon.character.DungeonCharacter;
import dungeon.character.Effect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Alex Plagman on 5/22/2017.
 */
public final class MultiShotAttack<C extends DungeonCharacter> implements BattleAction<C>, Serializable {
    MultiShotAttack() {
    }

    @Override
    public Collection<Effect> perform(Round round, C source, Collection<DungeonCharacter> targets) {
        ActionUtils.validateAction(getTargetType(), source, targets);

        Collection<Effect> effects = new ArrayList<>();
        System.out.println(source.getName() + " launches a volley of arrows!");
        targets.forEach((target) -> {
            if (target.isAlive()) {
                effects.addAll(ActionUtils.attemptAttack(source, target));
            }
        });
        return effects;
    }

    @Override
    public TargetType getTargetType() {
        TargetType t = TargetType.MULTIPLE;
        t.setCount(2);
        return t;
    }

    @Override
    public String toString() {
        return getStaticName();
    }

    public static String getStaticName() {
        return "Multi-Shot";
    }
}
