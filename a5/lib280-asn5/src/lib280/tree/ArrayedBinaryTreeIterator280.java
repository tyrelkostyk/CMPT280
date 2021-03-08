package lib280.tree;

import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class ArrayedBinaryTreeIterator280<I> extends ArrayedBinaryTreePosition280 implements LinearIterator280<I> {

	// This is a reference to the tree that created this iterator object.
	ArrayedBinaryTree280<I> tree;
	
	// An integer that represents the cursor position is inherited from
	// ArrayedBinaryTreePosition280.
	
	/**
	 * Create a new iterator from a given heap.
	 * @param t The heap for which to create a new iterator.
	 */
	public ArrayedBinaryTreeIterator280(ArrayedBinaryTree280<I> t) {
		super(t.currentNode);
		this.tree = t;
	}


	/**
	 * Confirm if the cursor is currently located before the data structure.
	 * @return true if the cursor is located before the data structure.
	 */
	@Override
	public boolean before() {
		return this.currentNode == 0;
	}


	/**
	 * Confirm if the cursor is currently located after the data structure.
	 * @return true if the cursor is located after the data structure.
	 */
	@Override
	public boolean after() {
		return this.currentNode > tree.count || tree.isEmpty();
	}


	/**
	 * Move the cursor ahead one step.
	 * @precond !after()
	 * @throws AfterTheEnd280Exception if the cursor is after the final item in the tree.
	 */
	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if ( this.after() )
			throw new AfterTheEnd280Exception("Cannot advance cursor in the after position.");

		this.currentNode++;
	}


	/**
	 * Move the cursor to the first item in the data structure.
	 * @precond !tree.isEmpty()
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 */
	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if ( tree.isEmpty() )
			throw new ContainerEmpty280Exception("Cannot move to first item of an empty tree.");

		this.currentNode = 1;
	}


	/**
	 * Move the cursor to before the data structure.
	 */
	@Override
	public void goBefore() {
		this.currentNode = 0;
	}

	/**
	 * Move the cursor to after the data structure.
	 */
	@Override
	public void goAfter() {
		if ( tree.isEmpty() )
			this.currentNode = 0;
		else
			this.currentNode = tree.count + 1;
	}


	/**
	 * Return the item currently being pointed to.
	 * @precond tree.itemExists()
	 * @return The item if it exists.
	 * @throws NoCurrentItem280Exception
	 */
	@Override
	public I item() throws NoCurrentItem280Exception {
		// store previous node, then update the trees node to that of the iterator
		int previousNode = tree.currentNode;
		tree.currentNode = this.currentNode;

		// confirm that the item at iterator's position exists
		if ( !tree.itemExists() )
			throw new NoCurrentItem280Exception("Cannot return an item that does not exist.");

		// grab the item at iterator's position
		I item = tree.item();

		// restore the tree's original position
		tree.currentNode = previousNode;

		return item;
	}


	/**
	 * Confirm if the item currently being pointed to exists.
	 * @return true if the item exists.
	 */
	@Override
	public boolean itemExists() {
		// store previous node, then update the trees node to that of the iterator
		int previousNode = tree.currentNode;
		tree.currentNode = this.currentNode;

		// confirm that the item at iterator's position exists
		boolean itemExists = tree.itemExists();

		// restore the tree's original position
		tree.currentNode = previousNode;

		return itemExists;
	}


}
