package dungeon.character.action;

import dungeon.Round;
import dungeon.character.DungeonCharacter;
import dungeon.character.Effect;
import dungeon.character.ProbabilityCheck;
import dungeon.character.Range;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Alex Plagman on 5/12/2017.
 */
public final class CrushingAttack<C extends DungeonCharacter> implements BattleAction<C>, Serializable {
    private static final ProbabilityCheck ATTACK_HIT_CHECK = new ProbabilityCheck(0.4);
    private static final Range DAMAGE_RANGE = new Range(100, 176);

    CrushingAttack() {
    }

    @Override
    public Collection<Effect> perform(Round round, C source, Collection<DungeonCharacter> targets) {
        ActionUtils.validateAction(getTargetType(), source, targets);

        Collection<Effect> effects = new ArrayList<>();
        targets.forEach((target) -> {
            if (ATTACK_HIT_CHECK.pass()) {
                System.out.println(source.getName() + " lands a CRUSHING BLOW!");
                effects.addAll(ActionUtils.damage(source, target, DAMAGE_RANGE.getRandom()));
            } else {
                ActionUtils.displayFailure(source, target, BattleAction.Failure.FAIL, "crushing blow");
            }
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
        return "Crushing Attack";
    }
}
