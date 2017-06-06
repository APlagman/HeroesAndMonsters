package dungeon.character;

import dungeon.Utils;
import dungeon.character.action.BattleAction;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public final class Hero extends DungeonCharacter implements Serializable {
    private final ProbabilityCheck blockCheck;
    private final Set<BattleAction<Hero>> actions;

    Hero(String name, int hitPoints, int attackSpeed, double chanceToHit, Range damageRange,
         String basicAttackDescription, double chanceToBlock, Set<BattleAction<Hero>> actions) {
        super(name, hitPoints, attackSpeed, chanceToHit, damageRange, basicAttackDescription);
        if (actions == null || actions.isEmpty()) {
            throw new IllegalArgumentException("Invalid Hero properties.");
        }
        this.blockCheck = new ProbabilityCheck(chanceToBlock);
        this.actions = actions;
    }

    public Optional<BattleAction<Hero>> promptForAction() {
        System.out.println("Please choose an action:");
        return Utils.promptForElementWithAlternative(actions, "Undo Last Action");
    }

    @Override
    public final DamageEffect subtractHitPoints(int points) {
        if (blockCheck.pass()) {
            System.out.println(getName() + " BLOCKED the attack!");
            return null;
        } else {
            return super.subtractHitPoints(points);
        }
    }
}