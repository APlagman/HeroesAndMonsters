package dungeon.character.action;

import dungeon.Round;
import dungeon.character.DungeonCharacter;
import dungeon.character.Effect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Alex Plagman on 5/12/2017.
 */
public final class BasicAttack<C extends DungeonCharacter> implements BattleAction<C>, Serializable {
    BasicAttack() {
    }

    @Override
    public Collection<Effect> perform(Round round, C source, Collection<DungeonCharacter> targets) {
        ActionUtils.validateAction(getTargetType(), source, targets);

        Collection<Effect> effects = new ArrayList<>();
        targets.forEach((target) -> {
            System.out.println(source.getName() + ' ' + source.getBasicAttackDescription() + ' ' + target.getName() + '.');
            effects.addAll(ActionUtils.attemptAttack(source, target));
        });
        return effects;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.SINGULAR;
    }

    @Override
    public String toString() {
        return getStaticName();
    }

    public static String getStaticName() {
        return "Basic Attack";
    }
}
