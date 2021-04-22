import lib280.dispenser.ArrayedStack280;

/**
 * This class provides a thin wrapper around the ArrayedStack280 class, such
 * that a stack may be compared to another stack, based on its first element.
 * @param <I> The element type within the Stack.
 */
public class ComparableArrayedStack<I extends Comparable<? super I>> extends ArrayedStack280<I> implements Comparable<ComparableArrayedStack<I>>
{
    public ComparableArrayedStack(int capacity) {
        super(capacity);
    }

    public int compareTo(ComparableArrayedStack<I> other) {
        return item().compareTo(other.item());
    }
}
