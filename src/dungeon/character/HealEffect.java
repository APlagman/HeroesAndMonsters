package dungeon.character;

/**
 * Created by Alex Plagman on 5/23/2017.
 */
public final class HealEffect implements Effect {
    private final DungeonCharacter target;
    private final int amount;

    public HealEffect(DungeonCharacter target, int amount) {
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void undo() {
        target.subtractHitPoints(amount, false);
    }
}
