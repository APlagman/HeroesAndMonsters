package dungeon.character;

import java.io.Serializable;

/**
 * Created by Alex Plagman on 5/23/2017.
 */
public class ProbabilityCheck implements Serializable {
    private double value;

    public ProbabilityCheck(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Invalid probability check!");
        }
        this.value = value;
    }

    public boolean pass() {
        return Math.random() < value;
    }
}
