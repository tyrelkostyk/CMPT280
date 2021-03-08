package lib280.dispenser;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.tree.ArrayedBinaryTreeIterator280;
import lib280.tree.IterableArrayedHeap280;

public class PriorityQueue280<I extends Comparable<? super I>> {
	
	// This is the heap that we are restricting.
	// Items in the priority queue get stored in the heap.
	protected IterableArrayedHeap280<I> items;
	
	
	/**
	 * Create a new priority queue with a given capacity.
	 * @param cap The maximum number of items that can be in the queue.
	 */
	public PriorityQueue280(int cap) {
		items = new IterableArrayedHeap280<I>(cap);
	}
	
	public String toString() {
		return items.toString();	
	}

	// TODO
	// Add Priority Queue ADT methods (from the specification) here.

	/**
	 * Insert item I into Priority Queue.
	 * @precond !isFull()
	 * @throws ContainerFull280Exception if the Priority Queue is full.
	 * @param item the item to be inserted.
	 */
	public void insert(I item) throws ContainerFull280Exception {
		if ( isFull() )
			throw new ContainerFull280Exception("Cannot add item to a Priority Queue that is full.");

		items.insert(item);
	}


	/**
	 * Confirm if the Priority Queue is full.
	 * @return true if the Priority Queue is full.
	 */
	public boolean isFull() {
		return items.isFull();
	}


	/**
	 * Confirm if the Priority Queue is empty.
	 * @return true if the Priority Queue is empty.
	 */
	public boolean isEmpty() {
		return items.isEmpty();
	}


	/**
	 * Get the number of items currently in the Priority Queue.
	 * @return the number of items currently in the Priority Queue.
	 */
	public int count() {
		return items.count();
	}


	/**
	 * Get the maximum priority item from the Priority Queue.
	 * @precond !isEmpty()
	 * @throws ContainerEmpty280Exception if the queue is empty.
	 * @return The maximum priority item from the Priority Queue.
	 */
	public I maxItem() throws ContainerEmpty280Exception {
		if ( isEmpty() )
			throw new ContainerEmpty280Exception("Cannot return max priority item of an empty Priority Queue.");

		// create and use an iterator to go to the first item in the Priority Queue
		ArrayedBinaryTreeIterator280<I> iterator = items.iterator();
		iterator.goFirst();

		return iterator.item();
	}


	/**
	 * Get the minimum priority item from the Priority Queue.
	 * @precond !isEmpty()
	 * @throws ContainerEmpty280Exception if the queue is empty.
	 * @return The minimum priority item from the Priority Queue.
	 */
	public I minItem() throws ContainerEmpty280Exception {
		if ( isEmpty() )
			throw new ContainerEmpty280Exception("Cannot return min priority item of an empty Priority Queue.");

		// create and use an iterator to go to the last item in the Priority Queue
		ArrayedBinaryTreeIterator280<I> iterator = items.iterator();

		// grab the first item in the Priority Queue
		iterator.goFirst();
		I minItem = iterator.item();

		// loop through all items in the Priority Queue
		while ( !iterator.after() ) {
			// grab the current item temporarily
			I tempItem = iterator.item();

			// if the temp item is smaller, then use it as the current min item
			if ( minItem.compareTo(tempItem) > 0 )
				minItem = tempItem;

			// continue to the next item
			iterator.goForth();
		}

		// return the smallest item
		return minItem;
	}


	/**
	 * Delete the maximum priority item from the Priority Queue.
	 * @precond !isEmpty()
	 * @throws ContainerEmpty280Exception if the queue is empty.
	 */
	public void deleteMax() throws ContainerEmpty280Exception {
		if ( isEmpty() )
			throw new ContainerEmpty280Exception("Cannot delete max priority item of an empty Priority Queue.");

		// create and use an iterator to go to the first item in the Priority Queue
		ArrayedBinaryTreeIterator280<I> iterator = items.iterator();
		iterator.goFirst();

		items.deleteAtPosition(iterator);
	}


	/**
	 * Delete the minimum priority item from the Priority Queue.
	 * @precond !isEmpty()
	 * @throws ContainerEmpty280Exception if the queue is empty.
	 */
	public void deleteMin() throws ContainerEmpty280Exception {
		if ( isEmpty() )
			throw new ContainerEmpty280Exception("Cannot delete minimum priority item of an empty Priority Queue.");

		// create and use two iterators to find the min priority item in the Priority Queue
		ArrayedBinaryTreeIterator280<I> iterator = items.iterator();
		ArrayedBinaryTreeIterator280<I> minLocation;
		// start at the first item in the Priority Queue
		iterator.goFirst();
		minLocation = iterator;

		// loop through all items in the Priority Queue
		while ( !iterator.after() ) {
			// if the temp iterator's item is smaller, then update the current min iterator
			if ( minLocation.item().compareTo(iterator.item()) > 0 )
				minLocation = iterator;

			// go to the next item
			iterator.goForth();
		}

		items.deleteAtPosition(minLocation);
	}


	/**
	 * Delete all occurrences of the highest priority item from the Priority Queue.
	 * @precond !isEmpty()
	 * @throws ContainerEmpty280Exception if the queue is empty.
	 */
	public void deleteAllMax() throws ContainerEmpty280Exception {
		if ( isEmpty() )
			throw new ContainerEmpty280Exception("Cannot delete max priority items of an empty Priority Queue.");

		// create and use an iterator to go to the first item in the Priority Queue
		ArrayedBinaryTreeIterator280<I> iterator = items.iterator();
		iterator.goFirst();

		// create a copy of the original highest priority item
		I maxPriorityItem = iterator.item();

		// continue deleting the first (aka highest priority item) while it is equal to the original first item
		while (iterator.item().compareTo(maxPriorityItem) == 0) {
			// delete the item
			items.deleteAtPosition(iterator);

			// if the Priority Queue is now empty, then exit
			if ( isEmpty() )
				return;

			// otherwise, continue on to the next highest priority item
			iterator.goFirst();
		}
	}


	public static void main(String args[]) {
		class PriorityItem<I> implements Comparable<PriorityItem<I>> {
			I item;
			Double priority;
						
			public PriorityItem(I item, Double priority) {
				super();
				this.item = item;
				this.priority = priority;
			}

			public int compareTo(PriorityItem<I> o) {
				return this.priority.compareTo(o.priority);
			}
			
			public String toString() {
				return this.item + ":" + this.priority;
			}
		}
		
		PriorityQueue280<PriorityItem<String>> Q = new PriorityQueue280<PriorityItem<String>>(5);
		
		// Test isEmpty()
		if( !Q.isEmpty()) 
			System.out.println("Error (1): Queue is empty, but isEmpty() says it isn't.");
		
		// Test insert() and maxItem()
		Q.insert(new PriorityItem<String>("Sing", 5.0));
		if( Q.maxItem().item.compareTo("Sing") != 0) {
			System.out.println("Error (2): Front of queue should be 'Sing' but it's not. It is: " + Q.maxItem().item);
		}
		
		// Test isEmpty() when queue not empty
		if( Q.isEmpty()) 
			System.out.println("Error (3): Queue is not empty, but isEmpty() says it is.");
		
		// test count()
		if( Q.count() != 1 ) {
			System.out.println("Error (4): Count should be 1 but it's not.");
		}

		// test minItem() with one element
		if( Q.minItem().item.compareTo("Sing")!=0) {
			System.out.println("Error (5): min priority item should be 'Sing' but it's not.");
		}	
		
		// insert more items
		Q.insert(new PriorityItem<String>("Fly", 5.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Error (6): Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Dance", 3.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Error (7): Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Jump", 7.0));
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Error (8): Front of queue should be 'Jump' but it's not.");
		
		if(Q.minItem().item.compareTo("Dance") != 0) System.out.println("Error (9): minItem() should be 'Dance' but it's not. Priority: " + Q.minItem().priority);
		
		if( Q.count() != 4 ) {
			System.out.println("Error (10): Count should be 4 but it's not.");
		}
		
		// Test isFull() when not full
		if( Q.isFull()) 
			System.out.println("Error (11): Queue is not full, but isFull() says it is.");
		
		Q.insert(new PriorityItem<String>("Eat", 10.0));
		if( Q.maxItem().item.compareTo("Eat")!=0) System.out.println("Error (12): Front of queue should be 'Eat' but it's not.");

		if( !Q.isFull()) 
			System.out.println("Error (13): Queue is full, but isFull() says it isn't.");

		// Test insertion on full queue
		try {
			Q.insert(new PriorityItem<String>("Sleep", 15.0));
			System.out.println("Error (14): Expected ContainerFull280Exception inserting to full queue but got none.");
		}
		catch(ContainerFull280Exception e) {
			// Expected exception
		}
		catch(Exception e) {
			System.out.println("Error (15): Expected ContainerFull280Exception inserting to full queue but got a different exception.");
			e.printStackTrace();
		}
		
		// test deleteMin
		Q.deleteMin();
		if(Q.minItem().item.compareTo("Sing") != 0) System.out.println("Error (16): Min item should be 'Sing', but it isn't. Priority: " + Q.minItem().priority);
		
		Q.insert(new PriorityItem<String>("Dig", 1.0));
		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("Error (17): minItem() should be 'Dig' but it's not. Priority: " + Q.minItem().priority);

		// Test deleteMax
		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Error (18): Front of queue should be 'Jump' but it's not.");

		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Error (19): Front of queue should be 'Fly' but it's not.");

		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("Error (20): minItem() should be 'Dig' but it's not. Priority: " + Q.minItem().priority);

		Q.deleteMin();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Error (21): Front of queue should be 'Fly' but it's not. Priority: " + Q.minItem().priority);

		Q.insert(new PriorityItem<String>("Scream", 2.0));
		Q.insert(new PriorityItem<String>("Run", 2.0));

		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Error (22): Front of queue should be 'Fly' but it's not.");
		
		// test deleteAllMax()
		Q.deleteAllMax();
		if( Q.maxItem().item.compareTo("Scream")!=0) System.out.println("Error (23): Front of queue should be 'Scream' but it's not.");
		if( Q.minItem().item.compareTo("Scream") != 0) System.out.println("Error (24): minItem() should be 'Scream' but it's not.");
		Q.deleteAllMax();

		// Queue should now be empty again.
		if( !Q.isEmpty()) 
			System.out.println("Error (25): Queue is empty, but isEmpty() says it isn't.");

		System.out.println("Regression test complete.");
	}

}
