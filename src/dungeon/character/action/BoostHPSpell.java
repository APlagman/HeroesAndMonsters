package dungeon.character.action;

import dungeon.Round;
import dungeon.character.DungeonCharacter;
import dungeon.character.Effect;
import dungeon.character.Range;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Alex Plagman on 5/22/2017.
 */
public final class BoostHPSpell<C extends DungeonCharacter> implements BattleAction<C>, Serializable {
    private static final Range HEAL_RANGE = new Range(25, 50);

    BoostHPSpell() {
    }

    @Override
    public Collection<Effect> perform(Round round, C source, Collection<DungeonCharacter> targets) {
        ActionUtils.validateAction(getTargetType(), source, targets);

        Collection<Effect> effects = new ArrayList<>();
        int amountToAdd = HEAL_RANGE.getRandom();
        effects.add(source.addHitPoints(amountToAdd));
        source.displayStatus();
        return effects;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.SELF;
    }

    @Override
    public String toString() {
        return getStaticName();
    }

    public static String getStaticName() {
        return "Boost HP";
    }
}
