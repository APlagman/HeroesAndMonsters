package dungeon.character;

import dungeon.character.action.BattleAction;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Alex Plagman on 5/23/2017.
 */
final class ActionComparator implements Serializable, Comparator<BattleAction> {
    @Override
    public int compare(BattleAction o1, BattleAction o2) {
        return o1.toString().compareTo(o2.toString());
    }
}
