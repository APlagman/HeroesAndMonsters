package dungeon.character;

import java.io.Serializable;

/**
 * Created by Alex Plagman on 5/23/2017.
 */
public class Range implements Serializable {
    private final int lowerBound;
    private final int upperBound;

    public Range(int lowerBound, int upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Lower bound must be <= upper bound!");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int getRandom() {
        return (int) (Math.random() * upperBound + lowerBound);
    }
}
