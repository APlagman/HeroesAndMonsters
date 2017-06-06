package dungeon.character.action;

import dungeon.Round;
import dungeon.character.DungeonCharacter;
import dungeon.character.Effect;
import dungeon.character.Hero;
import dungeon.character.ProbabilityCheck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Alex Plagman on 5/12/2017.
 */
public final class SurpriseAttack implements BattleAction<Hero>, Serializable {
    private final static ProbabilityCheck SURPRISE_CHECK = new ProbabilityCheck(0.4);
    private final static ProbabilityCheck FAIL_CHECK = new ProbabilityCheck(0.1);
    private final BasicAttack<Hero> attack;

    SurpriseAttack(BasicAttack<Hero> attack) {
        if (attack == null) {
            throw new IllegalArgumentException("Null attack!");
        }
        this.attack = attack;
    }

    @Override
    public Collection<Effect> perform(Round round, Hero source, Collection<DungeonCharacter> targets) {
        ActionUtils.validateAction(getTargetType(), source, targets);

        Collection<Effect> effects = new ArrayList<>();
        targets.forEach((target) -> {
            switch (calculateEffectiveness()) {
                case SUCCESS:
                    System.out.println(source.getName() + " scores a surprise attack!");
                    round.addTurn();
                case NORMAL:
                    effects.addAll(attack.perform(round, source, targets));
                    break;
                default:
                    System.out.println("Uh oh! " + target.getName() + " saw " + source.getName() + " and blocked their attack!");
                    break;
            }
        });
        return effects;
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.SINGULAR;
    }

    private static Effectiveness calculateEffectiveness() {
        if (FAIL_CHECK.pass()) {
            return Effectiveness.FAILURE;
        } else if (SURPRISE_CHECK.pass()) {
            return Effectiveness.SUCCESS;
        } else {
            return Effectiveness.NORMAL;
        }
    }

    @Override
    public String toString() {
        return getStaticName();
    }

    public static String getStaticName() {
        return "Surprise Attack";
    }

    private enum Effectiveness {
        SUCCESS, NORMAL, FAILURE
    }
}
