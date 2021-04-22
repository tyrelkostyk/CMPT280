import lib280.dispenser.ArrayedStack280;

/**
 * Tyrel Kostyk
 * tck290
 * 11216033
 *
 * CMPT280
 * Final Exam - Q1
 * April 21 2021
 *
 * ComparableArrayedStack.java
 */

/**
 * This class provides a thin wrapper around the ArrayedStack280 class, such
 * that a stack may be compared to another stack, based on their first elements.
 * @param <I> The (generic) element data type within the Stack.
 */
public class ComparableArrayedStack<I extends Comparable<? super I>> extends ArrayedStack280<I> implements Comparable<ComparableArrayedStack<I>>
{
    /**
     * Constructor for the ComparableArrayedStack.
     * @param capacity The max number of items that can be fit within the Stack.
     */
    public ComparableArrayedStack(int capacity) {
        super(capacity);
    }

    /**
     * Compare the first item of this Stack with the first item of another Stack.
     * @param other The other stack, being compared against.
     * @return Below zero if other is larger; 0 if match; above 0 if this stack is larger.
     */
    public int compareTo(ComparableArrayedStack<I> other) {
        return item().compareTo(other.item());
    }
}
