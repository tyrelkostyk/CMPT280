package lib280.tree;

import lib280.exception.ContainerEmpty280Exception;

public class IterableArrayedHeap280<I extends Comparable<? super I>> extends ArrayedHeap280<I> {

	/**
	 * Create an iterable heap with a given capacity.
	 * @param cap The maximum number of elements that can be in the heap.
	 */
	public IterableArrayedHeap280(int cap) {
		super(cap);
	}

	/**
	 * Create and return a new ArrayedBinaryTreeIterator280 object.
	 * @return A new ArrayedBinaryTreeIterator280 object.
	 */
	public ArrayedBinaryTreeIterator280<I> iterator() {
		return new ArrayedBinaryTreeIterator280<>(this);
	}


	/**
	 * Delete the item at the specified position.
	 * @param iter An ArrayedBinaryTreeIterator280 object, pointing to the item to be deleted.
	 * @precond !isEmpty(). The cursor position must be valid.
	 * @throws ContainerEmpty280Exception if the heap is empty.
	 */
	public void deleteAtPosition(ArrayedBinaryTreeIterator280<I> iter) throws ContainerEmpty280Exception {
		if ( this.isEmpty() )
			throw new ContainerEmpty280Exception("Cannot delete an item from an empty heap.");

		// Delete the current item by moving in the last item.
		// If there is more than one item, and we aren't deleting the last item,
		// copy the last item in the array to the current position.

		if ( this.count > 1 ) {
			/* This is functional difference #1 from ArrayedHeap280's deleteItem() function */
			this.currentNode = iter.currentNode;
			this.items[currentNode] = this.items[count];
		}
		this.count--;

		// If we deleted the last remaining item, make the the current item invalid, and we're done.
		if ( this.count == 0 ) {
			this.currentNode = 0;
			return;
		}

		/* This is functional difference #2 from ArrayedHeap280's deleteItem() function */
		// Propagate the new root down the lib280.tree.
		int n = this.currentNode;

		// While offset n has a left child...
		while ( findLeftChild(n) <= this.count ) {
			// Select the left child.
			int child = findLeftChild(n);

			// If the right child exists and is larger, select it instead.
			if( child + 1 <= count && items[child].compareTo(items[child+1]) < 0 )
				child++;

			// If the parent is smaller than the root...
			if( items[n].compareTo(items[child]) < 0 ) {
				// Swap them.
				I temp = items[n];
				items[n] = items[child];
				items[child] = temp;
				n = child;
			}
			else return;

		}
	}
}
