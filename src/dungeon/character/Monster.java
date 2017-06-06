package dungeon.character;

import dungeon.Utils;
import dungeon.character.action.BattleAction;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public final class Monster extends DungeonCharacter implements Serializable {
    private final ProbabilityCheck healCheck;
    private final Range healRange;
    private final Set<BattleAction<Monster>> actions;

    Monster(String name, int hitPoints, int attackSpeed, double chanceToHit,
            Range damageRange, String basicAttackDescription, double chanceToHeal,
            Range healRange, Set<BattleAction<Monster>> actions) {
        super(name, hitPoints, attackSpeed, chanceToHit, damageRange, basicAttackDescription);
        if (healRange == null || actions == null || actions.isEmpty()) {
            throw new IllegalArgumentException("Invalid Monster properties.");
        }
        this.healCheck = new ProbabilityCheck(chanceToHeal);
        this.healRange = healRange;
        this.actions = actions;
    }

    @Override
    public Optional<Effect> respondToAttack(DungeonCharacter attacker) {
        if (isAlive()) {
            Optional<HealEffect> effect = attemptToHeal();
            return effect.flatMap(Optional::ofNullable);
        }
        return Optional.empty();
    }

    private Optional<HealEffect> attemptToHeal() {
        if (healCheck.pass()) {
            int healPoints = healRange.getRandom();
            System.out.println(getName() + " healed itself for " + healPoints + " points.");
            Optional<HealEffect> effect = Optional.ofNullable(addHitPoints(healPoints, false));
            displayStatus();
            return effect;
        }
        return Optional.empty();
    }

    public Optional<BattleAction<Monster>> getRandomAction() {
        return Utils.getRandomElement(actions);
    }
}