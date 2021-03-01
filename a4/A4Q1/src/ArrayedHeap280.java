import lib280.base.Dispenser280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.tree.ArrayedBinaryTree280;

/**
 * CMPT 280
 * Assignment 4, Question 1
 *
 * Tyrel Kostyk
 * tck290
 * 11216033
 */


public class ArrayedHeap280<I extends Comparable<I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I>
{

    /**
     * Constructor.
     * @param cap Maximum number of elements that can be in the lib280.tree.
     */
    public ArrayedHeap280(int cap) {
        super(cap);

        // define items as a list of objects that implement the Comparable type
        items = (I[]) new Comparable[capacity+1];
    }

    /**	Insert x into the data structure.
     * @precond !isFull() and !has(x)
     * @param x item to be inserted into the data structure
     * @throws ContainerFull280Exception if the dispenser is already full.
     */
    public void insert(I x)
    {
        // can't insert if the heap is full
        if (this.isFull())
            throw new ContainerFull280Exception("Can't insert; heap is full.");

        // increase count, assign new value to new node
        count++;
        items[count] = x;

        // current item is always the root
        currentNode = 1;

        // if this was the root node, then exit now - the rest is just re-arranging
        if (count == 1)
            return;

        int newElementIndex = count;
        int parentIndex = findParent(newElementIndex);

        // continue swapping with the parent node until it is not smaller than this new element
        while (items[parentIndex].compareTo(items[newElementIndex]) < 0) {
            // save the new element value
            I temporaryHolder = items[newElementIndex];
            // replace new element with its parent node
            items[newElementIndex] = items[parentIndex];
            // replace parent with (saved) new element value
            items[parentIndex] = temporaryHolder;

            // update indices
            newElementIndex = parentIndex;
            parentIndex = findParent(newElementIndex);

            // if the root has been assessed; then exit
            if (parentIndex == 0)
                return;
        }
    }

    /**	Delete current item from the data structure.
     * @precond	itemExists()
     * @throws NoCurrentItem280Exception if the cursor is not currently positioned at a valid item.
     */
    public void deleteItem()
    {
        // current item must exist and be valid
        if (!this.itemExists())
            throw new NoCurrentItem280Exception("Can't delete; current item is not valid.");

        // replace root node value with last item in tree
        items[currentNode] = items[count];
        count--;

        // if this was the root node, then exit now - the rest is just re-arranging
        if (count == 0) {
            currentNode = 0;
            return;
        }

        int movingElementIndex = currentNode;
        int rightChildIndex = findRightChild(movingElementIndex);
        int leftChildIndex = findLeftChild(movingElementIndex);

        // continue swapping while there are children node
        while (rightChildIndex <= count && leftChildIndex <= count)
        {
            // save the new element value
            I temporaryHolder = items[movingElementIndex];

            // replace new element with the largest child node
            if (items[rightChildIndex].compareTo(items[leftChildIndex]) > 0) {
                items[movingElementIndex] = items[rightChildIndex];
                items[rightChildIndex] = temporaryHolder;
                movingElementIndex = rightChildIndex;
            } else {
                items[movingElementIndex] = items[leftChildIndex];
                items[leftChildIndex] = temporaryHolder;
                movingElementIndex = leftChildIndex;
            }

            // update indices
            rightChildIndex = findRightChild(movingElementIndex);
            leftChildIndex = findLeftChild(movingElementIndex);
        }
    }


    /**
     * START OF TEST CODE PROVIDED BY heaptests.txt
     */

    /**
     * Helper for the regression test.  Verifies the heap property for all nodes.
     */
    private boolean hasHeapProperty()
    {
        for (int i=1; i <= count; i++) {
            if (findRightChild(i) <= count) {  // if i Has two children...
                // ... and i is smaller than either of them, , then the heap property is violated.
                if ( items[i].compareTo(items[findRightChild(i)]) < 0 ) return false;
                if ( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
            }
            else if ( findLeftChild(i) <= count ) {  // if n has one child...
                // ... and i is smaller than it, then the heap property is violated.
                if ( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
            }
            else break;  // Neither child exists.  So we're done.
        }
        return true;
    }

    /**
     * Regression test
     */
    public static void main(String[] args) {

        ArrayedHeap280<Integer> H = new ArrayedHeap280<Integer>(10);

        // Empty heap should have the heap property.
        if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");

        // Insert items 1 through 10, checking after each insertion that
        // the heap property is retained, and that the top of the heap is correctly i.
        for(int i = 1; i <= 10; i++) {
            H.insert(i);
            if(H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
            if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
        }

        System.out.println("Done testing insert(); see arrayed heap:");
        System.out.println(H.toString());
        System.out.println("\nNow testing deleteItem()");

        // Remove the elements 10 through 1 from the heap, checking
        // after each deletion that the heap property is retained and that
        // the correct item is at the top of the heap.
        for(int i = 10; i >= 1; i--) {
            // Remove the element i.
            H.deleteItem();
            // If we've removed item 1, the heap should be empty.
            if(i==1) {
                if( !H.isEmpty() ) System.out.println("Expected the heap to be empty, but it wasn't.");
            }
            else {
                // Otherwise, the item left at the top of the heap should be equal to i-1.
                if(H.item() != i-1) System.out.println("Expected current item to be " + i + ", got " + H.item());
                if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
            }
        }

        System.out.println("\nDone testing deleteItem()");
        System.out.println("\nRegression Test Complete.");

        /**
         * END OF TEST CODE PROVIDED BY heaptests.txt
         */
    }

}
