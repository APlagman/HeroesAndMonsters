package dungeon.character;

/**
 * Created by Alex Plagman on 5/23/2017.
 */
public final class DamageEffect implements Effect {
    private final DungeonCharacter target;
    private final int damage;

    public DamageEffect(DungeonCharacter target, int damage) {
        this.target = target;
        this.damage = damage;
    }

    @Override
    public void undo() {
        target.addHitPoints(damage, false);
    }
}
