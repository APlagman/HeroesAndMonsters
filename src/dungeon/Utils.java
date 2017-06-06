package dungeon;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Alex Plagman on 5/23/2017.
 */
public final class Utils {
    private Utils() {
    }

    static <E> Optional<E> promptForElement(Collection<E> collection) {
        int index = 0;
        for (E element : collection) {
            ++index;
            System.out.println(index + ". " + element.toString());
        }

        int choice = Keyboard.readInt();

        index = 0;
        for (E element : collection) {
            ++index;
            if (index == choice) {
                return Optional.ofNullable(element);
            }
        }

        System.out.println("Invalid choice; selecting randomly.");
        return getRandomElement(collection);
    }

    public static <E> Optional<E> getRandomElement(Collection<E> collection) {
        return collection.stream().skip((long) (collection.size() * Math.random())).findFirst();
    }

    public static <E, T> Optional<E> promptForElementWithAlternative(Collection<E> collection, T alternative) {
        if (alternative == null) {
            throw new IllegalArgumentException("Alternative option cannot be null!");
        }

        int index = 0;
        for (E element : collection) {
            ++index;
            System.out.println(index + ". " + element.toString());
        }
        System.out.println(++index + ". " + alternative.toString());

        int choice = Keyboard.readInt();

        index = 0;
        for (E element : collection) {
            ++index;
            if (index == choice) {
                return Optional.ofNullable(element);
            }
        }
        if (++index == choice) {
            return Optional.empty();
        }

        System.out.println("Invalid choice; selecting randomly.");
        return getRandomElement(collection);
    }

    public static String toCamelCase(String str) {
        return str.charAt(0) + str.toLowerCase().substring(1);
    }
}
