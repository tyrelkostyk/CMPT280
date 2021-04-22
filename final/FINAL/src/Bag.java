import lib280.tree.AVLTree280;

/**
 * Tyrel Kostyk
 * tck290
 * 11216033
 *
 * CMPT280
 * Final Exam - Q1
 * April 21 2021
 *
 * Bag280.java
 */


public class Bag<T extends Comparable<? super T>> extends AVLTree280<ComparableArrayedStack<T>> {

    /** The number of duplicates allowed for each unique item */
    protected int duplicatesAllowed;

    /**
     * Constructor for the Bag Class.
     */
    public Bag() {
        super();
        duplicatesAllowed = 10;
    }

    /**
     * Set the internal tree's position to the stack holding this element (or duplicates of it).
     * @param x The element to search for (including duplicates).
     */
    protected void find(T x) {
        // create a temporary stack to search for matching items
        ComparableArrayedStack<T> tempStack = new ComparableArrayedStack<T>(1);
        tempStack.insert(x);

        // search for matching items within the tree
        search(tempStack);
    }

    public int numberIn(T x) {
        // set the cursor at the element holding x (or duplicates of x)
        find(x);

        // if the item exists - return the size of the stack holding all of the duplicate items
        if (itemExists()) {
            return item().count();
        }

        // if the item does not exist, return 0
        return 0;
    }

    public void add(T x) {
        // set the cursor at the element holding x (or duplicates of x)
        find(x);

        // if a stack already exists for this element (and its duplicates), add the duplicate to the stack
        if (itemExists()) {
            item().insert(x);

        // if this is the first of its kind, create a new stack and insert it into the tree
        } else {
            ComparableArrayedStack<T> newStack = new ComparableArrayedStack<T>(duplicatesAllowed);
            newStack.insert(x);
            insert(newStack);
        }
    }

    public void remove(T x) {
        // set the cursor at the element holding x (or duplicates of x)
        find(x);

        // if there is no matching item, do nothing
        if (!itemExists())
            return;

        // if there is only one item of this kind, remove the entire internal stack
        if (item().count() <= 1)
            deleteItem();

        // if there is multiple duplicates of this item, only remove one of the duplicates
        else
            item().deleteItem();
    }

    public static void main(String args[]) {
        // create the test Bag
        Bag<Integer> testBag = new Bag();

        // add some initial elements
        testBag.add(10);
        testBag.add(10);
        testBag.add(10);
        testBag.add(1);
        testBag.add(2);
        testBag.add(5);
        testBag.add(5);

        int expected_01 = 3;
        int results_01 = testBag.numberIn(10);
        if (results_01 != expected_01)
            System.out.println("01: 10 should appear " + expected_01 + " times, but instead appears " + results_01 + " times");

        int expected_02 = 2;
        int results_02 = testBag.numberIn(5);
        if (results_02 != expected_02)
            System.out.println("02: 5 should appear " + expected_02 + " times, but instead appears " + results_02 + " times");

        int expected_03 = 1;
        int results_03 = testBag.numberIn(1);
        if (results_03 != expected_03)
            System.out.println("03: 1 should appear " + expected_03 + " times, but instead appears " + results_03 + " times");

        int expected_04 = 1;
        int results_04 = testBag.numberIn(2);
        if (results_04 != expected_04)
            System.out.println("04: 2 should appear " + expected_04 + " times, but instead appears " + results_04 + " times");

        // add another element
        testBag.add(5);

        int expected_05 = 3;
        int results_05 = testBag.numberIn(5);
        if (results_05 != expected_05)
            System.out.println("05: 5 should appear " + expected_05 + " times, but instead appears " + results_05 + " times");

        // remove an element with multiple duplicates
        testBag.remove(10);

        int expected_06 = 2;
        int results_06 = testBag.numberIn(10);
        if (results_06 != expected_06)
            System.out.println("06: 10 should appear " + expected_06 + " times, but instead appears " + results_06 + " times");

        // remove an element with only 1 remaining copy in the bag
        testBag.remove(1);

        int expected_07 = 0;
        int results_07 = testBag.numberIn(1);
        if (results_07 != expected_07)
            System.out.println("07: 1 should appear " + expected_07 + " times, but instead appears " + results_07 + " times");

        // remove an element with no copies in the bag
        testBag.remove(99);

        int expected_08 = 0;
        int results_08 = testBag.numberIn(99);
        if (results_08 != expected_08)
            System.out.println("08: 99 should appear " + expected_08 + " times, but instead appears " + results_08 + " times");
    }
}
