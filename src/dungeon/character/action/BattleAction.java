package dungeon.character.action;

import dungeon.Round;
import dungeon.character.DungeonCharacter;
import dungeon.character.Effect;

import java.util.Collection;

/**
 * Created by Alex Plagman on 5/12/2017.
 */
public interface BattleAction<C extends DungeonCharacter> {
    Collection<Effect> perform(Round round, C source, Collection<DungeonCharacter> targets);

    TargetType getTargetType();

    enum TargetType {
        ALL(-1), SELF(0), SINGULAR(1), MULTIPLE(2);

        private int count;

        TargetType(int count) {
            this.count = count;
        }

        public final int getCount() {
            return count;
        }

        void setCount(int count) {
            this.count = count;
        }
    }

    enum Failure {
        FAIL, MISS, EVADE
    }
}

