package dungeon.character;

import java.io.Serializable;
import java.util.Optional;

public abstract class DungeonCharacter implements Serializable {
    private final int attackSpeed;
    private final ProbabilityCheck hitCheck;
    private final Range damageRange;
    private final String basicAttackDescription;
    private String name;
    private int hitpoints;

    DungeonCharacter(String name, int hitpoints, int attackSpeed,
                     double chanceToHit, Range damageRange, String basicAttackDescription) {
        if (name == null || name.isEmpty() || hitpoints < 0 || attackSpeed <= 0
                || damageRange == null || basicAttackDescription == null || basicAttackDescription.isEmpty()) {
            throw new IllegalArgumentException("Invalid character properties!");
        }
        this.name = name;
        this.hitpoints = hitpoints;
        this.attackSpeed = attackSpeed;
        this.hitCheck = new ProbabilityCheck(chanceToHit);
        this.damageRange = damageRange;
        this.basicAttackDescription = basicAttackDescription;
    }

    public final int getAttackSpeed() {
        return attackSpeed;
    }

    public final String getBasicAttackDescription() {
        return basicAttackDescription;
    }

    @Override
    public final String toString() {
        return getName();
    }

    public final String getName() {
        return name;
    }

    public final boolean isDead() {
        return !isAlive();
    }

    public final boolean isAlive() {
        return (hitpoints > 0);
    }

    public final boolean hitSucceeded() {
        return hitCheck.pass();
    }

    public final int rollDamage() {
        return damageRange.getRandom();
    }

    public HealEffect addHitPoints(int points) {
        return addHitPoints(points, true);
    }

    public HealEffect addHitPoints(int hitPoints, boolean displayGain) {
        if (hitPoints <= 0)
            throw new IllegalArgumentException("Hitpoint amount must be positive.");
        else {
            int prevHP = this.hitpoints;
            this.hitpoints += hitPoints;
            if (displayGain) {
                System.out.println(getName() + " gained " + hitPoints + " hitpoints.");
            }
            if (this.hitpoints > Integer.MIN_VALUE && this.hitpoints < 0) {
                this.hitpoints = Integer.MAX_VALUE;
            }
            return new HealEffect(this, this.hitpoints - prevHP);
        }
    }

    public DamageEffect subtractHitPoints(int points) {
        return subtractHitPoints(points, true);
    }

    public DamageEffect subtractHitPoints(int hitPoints, boolean displayDamage) {
        if (hitPoints < 0) {
            throw new IllegalArgumentException("Hitpoint amount must be positive.");
        } else {
            int prevHP = this.hitpoints;
            this.hitpoints -= hitPoints;
            if (displayDamage) {
                System.out.println(getName() + " took " + hitPoints + " points of damage.");
            }
            if (this.hitpoints < 0) {
                this.hitpoints = 0;
            }
            return new DamageEffect(this, prevHP - this.hitpoints);
        }
    }

    public Optional<Effect> respondToAttack(DungeonCharacter attacker) {
        // By default, do nothing special after defending.
        // Allows for Monsters to heal, and could be used as a hook for retaliation.
        return Optional.empty();
    }

    public void displayStatus() {
        if (hitpoints > 0) {
            System.out.println(getName() + " now has " + hitpoints + " hitpoints remaining.");
        } else {
            System.out.println(getName() + " has been killed.");
        }
    }
}