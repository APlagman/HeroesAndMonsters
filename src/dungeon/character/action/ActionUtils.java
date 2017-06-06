package dungeon.character.action;

import dungeon.character.DungeonCharacter;
import dungeon.character.Effect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Alex Plagman on 5/23/2017.
 */
final class ActionUtils implements Serializable {
    enum ActionNoun {
        ATTACK, SPELL
    }

    private ActionUtils() {
    }

    static Collection<Effect> attemptAttack(DungeonCharacter source, DungeonCharacter target) {
        if (source.hitSucceeded()) {
            return damage(source, target, source.rollDamage());
        } else {
            displayFailure(source, target, BattleAction.Failure.MISS, ActionNoun.ATTACK);
            return Collections.emptyList();
        }
    }

    static Collection<Effect> damage(DungeonCharacter source, DungeonCharacter target, int damage) {
        Collection<Effect> effects = new ArrayList<>();
        effects.add(target.subtractHitPoints(damage));
        target.displayStatus();
        target.respondToAttack(source).ifPresent(effects::add);
        return effects;
    }

    static void displayFailure(DungeonCharacter source, DungeonCharacter target, BattleAction.Failure type, ActionNoun actionNoun) {
        displayFailure(source, target, type, actionNoun.name().toLowerCase());
    }

    static void displayFailure(DungeonCharacter source, DungeonCharacter target, BattleAction.Failure type, String actionNoun) {
        switch (type) {
            case FAIL:
                System.out.println(source.getName() + "'s " + actionNoun + " failed!");
                break;
            case MISS:
                System.out.println(source.getName() + "'s " + actionNoun + " missed " + target.getName() + '!');
                break;
            case EVADE:
                System.out.println(target.getName() + " evaded " + source.getName() + "'s " + actionNoun + '!');
                break;
        }
    }

    static void validateAction(BattleAction.TargetType targetType, DungeonCharacter source, Collection<DungeonCharacter> targets) {
        validateSource(source);
        if (targetType.getCount() > 0) {
            validateTargets(targets, targetType.getCount());
        }
    }

    private static void validateSource(DungeonCharacter source) {
        if (source == null || source.isDead()) {
            throw new IllegalArgumentException("Null or dead source!");
        }
    }

    private static void validateTargets(Collection<DungeonCharacter> targets, int maxTargets) {
        if (targets == null || targets.isEmpty()) {
            throw new IllegalArgumentException("Attempting to act without a target!");
        }
        if (targets.size() > maxTargets) {
            throw new IllegalArgumentException("Attempting to act upon too many targets!");
        }
        targets.forEach(ActionUtils::validateTarget);
    }

    private static void validateTarget(DungeonCharacter target) {
        if (target == null || target.isDead()) {
            throw new IllegalArgumentException("Null or dead target!");
        }
    }
}
