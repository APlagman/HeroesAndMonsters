package dungeon.character;

import java.io.Serializable;

/**
 * Created by Alex Plagman on 5/23/2017.
 */
public interface Effect extends Serializable {
    void undo();
}
