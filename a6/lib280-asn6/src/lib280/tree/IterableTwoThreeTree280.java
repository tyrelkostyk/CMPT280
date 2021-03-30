package lib280.tree;

import lib280.base.CursorPosition280;
import lib280.base.Keyed280;
import lib280.base.Pair280;
import lib280.dictionary.KeyedDict280;
import lib280.exception.*;

public class IterableTwoThreeTree280<K extends Comparable<? super K>, I extends Keyed280<K>> extends TwoThreeTree280<K, I> implements KeyedDict280<K,I> {

	// References to the leaf nodes with the smallest and largest keys.
	LinkedLeafTwoThreeNode280<K,I> smallest, largest;
	
	// These next two variables represent the cursor which
	// the methods inherited from KeyedLinearIterator280 will
	// manipulate.  The cursor may only be positioned at leaf
	// nodes, never at internal nodes.
	
	// Reference to the leaf node at which the cursor is positioned.
	LinkedLeafTwoThreeNode280<K,I> cursor;
	
	// Reference to the predecessor of the node referred to by 'cursor' 
	// (or null if no such node exists).
	LinkedLeafTwoThreeNode280<K,I> prev;
	
	
	protected LinkedLeafTwoThreeNode280<K,I> createNewLeafNode(I newItem) {
		return new LinkedLeafTwoThreeNode280<K,I>(newItem);
	}


	@Override
	public void insert(I newItem) {

		if( this.has(newItem.key()) ) 
			throw new DuplicateItems280Exception("Key already exists in the tree.");

		// If the tree is empty, just make a leaf node. 
		if( this.isEmpty() ) {
			this.rootNode = createNewLeafNode(newItem);
			// Set the smallest and largest nodes to be the one leaf node in the tree.
			this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
			this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
		}
		// If the tree has one node, make an internal node, and make it the parent
		// of both the existing leaf node and the new leaf node.
		else if( !this.rootNode.isInternal() ) {
			LinkedLeafTwoThreeNode280<K,I> newLeaf = createNewLeafNode(newItem);
			LinkedLeafTwoThreeNode280<K,I> oldRoot = (LinkedLeafTwoThreeNode280<K,I>)rootNode;
			InternalTwoThreeNode280<K,I> newRoot;
			if( newItem.key().compareTo(oldRoot.getKey1()) < 0) {
				// New item's key is smaller than the existing item's key...
				newRoot = createNewInternalNode(newLeaf, oldRoot.getKey1(), oldRoot, null, null);	
				newLeaf.setNext(oldRoot);
				oldRoot.setPrev(newLeaf);
				
				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = newLeaf;
				this.largest = oldRoot;
			}
			else {
				// New item's key is larger than the existing item's key. 
				newRoot = createNewInternalNode(oldRoot, newItem.key(), newLeaf, null, null);
				oldRoot.setNext(newLeaf);
				newLeaf.setPrev(oldRoot);
				
				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = oldRoot;
				this.largest = newLeaf;
			}
			this.rootNode = newRoot;
		}
		else {
			Pair280<TwoThreeNode280<K,I>, K> extra = this.insert((InternalTwoThreeNode280<K,I>)this.rootNode, newItem);

			// If extra returns non-null, then the root was split and we need
			// to make a new root.
			if( extra != null ) {
				InternalTwoThreeNode280<K,I> oldRoot = (InternalTwoThreeNode280<K,I>)rootNode;

				// extra always contains larger keys than its sibling.
				this.rootNode = createNewInternalNode(oldRoot, extra.secondItem(), extra.firstItem(), null, null);				
			}
		}
	}


	/**
	 * Recursive helper for the public insert() method.
	 * @param root Root of the (sub)tree into which we are inserting.
	 * @param newItem The item to be inserted.
	 */
	protected Pair280<TwoThreeNode280<K,I>, K> insert(TwoThreeNode280<K,I> root,
                                                      I newItem) {

		if( !root.isInternal() ) {
			// If root is a leaf node, then it's time to create a new
			// leaf node for our new element and return it so it gets linked
			// into root's parent.
			Pair280<TwoThreeNode280<K,I>, K> extraNode;
			LinkedLeafTwoThreeNode280<K,I> oldLeaf = (LinkedLeafTwoThreeNode280<K, I>) root;

			// If the new element is smaller than root, copy root's element to
			// a new leaf node, put new element in existing leaf node, and
			// return new leaf node.
			if( newItem.key().compareTo(root.getKey1()) < 0) {
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(root.getData()), root.getKey1());
				((LeafTwoThreeNode280<K,I>)root).setData(newItem);
			}
			else {
				// Otherwise, just put the new element in a new leaf node
				// and return it.
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(newItem), newItem.key());
			}
			
			LinkedLeafTwoThreeNode280<K,I> newLeaf = (LinkedLeafTwoThreeNode280<K, I>) extraNode.firstItem();
		
			// No matter what happens above, the node 'newLeaf' is a new leaf node that is 
			// immediately to the right of the node 'oldLeaf'.

			/** Student code BEGIN */

			// connect newLeaf
			newLeaf.setNext(oldLeaf.next());
			newLeaf.setPrev(oldLeaf);

			// update the leaf node that used to be to the right of oldLeaf
			if (oldLeaf != this.largest)
				oldLeaf.next().setPrev(newLeaf);

			// update oldLead
			oldLeaf.setNext(newLeaf);

			// Also adjust this.largest if necessary.

			this.largest = newLeaf;

			/** Student code END */

			// (this.smallest will never need adjustment because if a new
			//  smallest element is inserted, it gets put in the existing 
			//  leaf node, and the old smallest element is copied to a  
			//  new node -- this is "true" case for the previous if/else.)
		
			return extraNode;
		}
		else { // Otherwise, recurse! 
			Pair280<TwoThreeNode280<K,I>, K> extra;
			TwoThreeNode280<K,I> insertSubtree;

			if( newItem.key().compareTo(root.getKey1()) < 0 ) {
				// decide to recurse left
				insertSubtree = root.getLeftSubtree();
			}
			else if(!root.isRightChild() || newItem.key().compareTo(root.getKey2()) < 0 ) {
				// decide to recurse middle
				insertSubtree = root.getMiddleSubtree();
			}
			else {
				// decide to recurse right
				insertSubtree = root.getRightSubtree();
			}

			// Actually recurse where we decided to go.
			extra = insert(insertSubtree, newItem);

			// If recursion resulted in a new node needs to be linked in as a child
			// of root ...
			if( extra != null ) {
				// Otherwise, extra.firstItem() is an internal node... 
				if( !root.isRightChild() ) {
					// if root has only two children.  
					if( insertSubtree == root.getLeftSubtree() ) {
						// if we inserted in the left subtree...
						root.setRightSubtree(root.getMiddleSubtree());
						root.setMiddleSubtree(extra.firstItem());
						root.setKey2(root.getKey1());
						root.setKey1(extra.secondItem());
						return null;
					}
					else {
						// if we inserted in the right subtree...
						root.setRightSubtree(extra.firstItem());
						root.setKey2(extra.secondItem());
						return null;
					}
				}
				else {
					// otherwise root has three children
					TwoThreeNode280<K, I> extraNode;
					if( insertSubtree == root.getLeftSubtree()) {
						// if we inserted in the left subtree
						extraNode = createNewInternalNode(root.getMiddleSubtree(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setMiddleSubtree(extra.firstItem());
						root.setRightSubtree(null);
						K k1 = root.getKey1();
						root.setKey1(extra.secondItem());
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k1);
					}
					else if( insertSubtree == root.getMiddleSubtree()) {
						// if we inserted in the middle subtree
						extraNode = createNewInternalNode(extra.firstItem(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, extra.secondItem());
					}
					else {
						// we inserted in the right subtree
						extraNode = createNewInternalNode(root.getRightSubtree(), extra.secondItem(), extra.firstItem(), null, null);
						K k2 = root.getKey2();
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k2);
					}
				}
			}
			// Otherwise no new node was returned, so there is nothing extra to link in.
			else return null;
		}		
	}


	@Override
	public void delete(K keyToDelete) {
		if( this.isEmpty() ) return;

		if( !this.rootNode.isInternal()) {
			if( this.rootNode.getKey1() == keyToDelete ) {
				this.rootNode = null;
				this.smallest = null;
				this.largest = null;
			}
		}
		else {
			delete(this.rootNode, keyToDelete);	
			// If the root only has one child, replace the root with its
			// child.
			if( this.rootNode.getMiddleSubtree() == null) {
				this.rootNode = this.rootNode.getLeftSubtree();
				if( !this.rootNode.isInternal() ) {
					this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
					this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
				}
			}
		}
	}


	/**
	 * Given a key, delete the corresponding key-item pair from the tree.
	 * @param root root of the current tree
	 * @param keyToDelete The key to be deleted, if it exists.
	 */
	protected void delete(TwoThreeNode280<K, I> root, K keyToDelete ) {
		if( root.getLeftSubtree().isInternal() ) {
			// root is internal, so recurse.
			TwoThreeNode280<K,I> deletionSubtree;
			if( keyToDelete.compareTo(root.getKey1()) < 0){
				// recurse left
				deletionSubtree = root.getLeftSubtree();
			}
			else if( root.getRightSubtree() == null || keyToDelete.compareTo(root.getKey2()) < 0 ){
				// recurse middle
				deletionSubtree = root.getMiddleSubtree();
			}
			else {
				// recurse right
				deletionSubtree = root.getRightSubtree();
			}

			delete(deletionSubtree, keyToDelete);

			// Do the first possible of:
			// steal left, steal right, merge left, merge right
			if( deletionSubtree.getMiddleSubtree() == null)  
				if(!stealLeft(root, deletionSubtree))
					if(!stealRight(root, deletionSubtree))
						if(!giveLeft(root, deletionSubtree))
							if(!giveRight(root, deletionSubtree))
								throw new InvalidState280Exception("This should never happen!");

		}
		else {
			// children of root are leaf nodes
			if( root.getLeftSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on left

				/** Student code BEGIN */

				// grab the leaf to delete
				LinkedLeafTwoThreeNode280<K,I> leafToDelete = (LinkedLeafTwoThreeNode280<K, I>) root.getLeftSubtree();

				// update smallest value if necessary
				if (leafToDelete.getKey1().compareTo(this.smallest.getKey1()) == 0)
					this.smallest = leafToDelete.next();

				// update largest value if necessary
				if (leafToDelete.getKey1().compareTo(this.largest.getKey1()) == 0)
					this.largest = leafToDelete.prev();

				// update the linear successor
				leafToDelete.next().setPrev(leafToDelete.prev());

				// update the linear predecessor
				leafToDelete.prev().setNext(leafToDelete.next());

				/** Student code END */

				// Proceed with deletion of leaf from the 2-3 tree.
				root.setLeftSubtree(root.getMiddleSubtree());
				root.setMiddleSubtree(root.getRightSubtree());
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else 
					root.setKey1(root.getKey2());
				if( root.getRightSubtree() != null) root.setKey2(null);
				root.setRightSubtree(null);					
			}
			else if( root.getMiddleSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is in middle

				/** Student code BEGIN */

				// grab the leaf to delete
				LinkedLeafTwoThreeNode280<K,I> leafToDelete = (LinkedLeafTwoThreeNode280<K, I>) root.getMiddleSubtree();

				// update smallest value if necessary
				if (leafToDelete.getKey1().compareTo(this.smallest.getKey1()) == 0)
					this.smallest = leafToDelete.next();

				// update largest value if necessary
				if (leafToDelete.getKey1().compareTo(this.largest.getKey1()) == 0)
					this.largest = leafToDelete.prev();

				// update the linear successor
				leafToDelete.next().setPrev(leafToDelete.prev());

				// update the linear predecessor
				leafToDelete.prev().setNext(leafToDelete.next());

				/** Student code END */

				// Proceed with deletion from the 2-3 tree.
				root.setMiddleSubtree(root.getRightSubtree());				
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else 
					root.setKey1(root.getKey2());

				if( root.getRightSubtree() != null) {
					root.setKey2(null);
					root.setRightSubtree(null);
				}
			}
			else if( root.getRightSubtree() != null && root.getRightSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on the right

				/** Student code BEGIN */

				// grab the leaf to delete
				LinkedLeafTwoThreeNode280<K,I> leafToDelete = (LinkedLeafTwoThreeNode280<K, I>) root.getRightSubtree();

				// update smallest value if necessary
				if (leafToDelete.getKey1().compareTo(this.smallest.getKey1()) == 0)
					this.smallest = leafToDelete.next();

				// update largest value if necessary
				if (leafToDelete.getKey1().compareTo(this.largest.getKey1()) == 0)
					this.largest = leafToDelete.prev();

				// update the linear successor
				leafToDelete.next().setPrev(leafToDelete.prev());

				// update the linear predecessor
				leafToDelete.prev().setNext(leafToDelete.next());

				/** Student code END */

				// Proceed with deletion of the node from the 2-3 tree.
				root.setKey2(null);
				root.setRightSubtree(null);
			}
			else {
				// key to delete does not exist in tree.
			}
		}		
	}	
	
	
	@Override
	public K itemKey() throws NoCurrentItem280Exception {
		/** Student code BEGIN */

		if( !itemExists() )
			throw new NoCurrentItem280Exception("There is no current item from which to obtain its key.");

		return this.cursor.getKey1();

		/** Student code END */
	}


	@Override
	public Pair280<K, I> keyItemPair() throws NoCurrentItem280Exception {
		// Return a pair consisting of the key of the item
		// at which the cursor is positioned, and the entire
		// item in the node at which the cursor is positioned.
		if( !itemExists() ) 
			throw new NoCurrentItem280Exception("There is no current item from which to obtain its key.");
		return new Pair280<K, I>(this.itemKey(), this.item());
	}


	@Override
	public I item() throws NoCurrentItem280Exception {
		/** Student code BEGIN */

		if( !itemExists() )
			throw new NoCurrentItem280Exception("There is no current item to obtain.");

		return this.cursor.getData();

		/** Student code END */
	}


	@Override
	public boolean itemExists() {
		return this.cursor != null;
	}


	@Override
	public boolean before() {
		return this.cursor == null && this.prev == null;
	}


	@Override
	public boolean after() {
		return this.cursor == null && this.prev != null || this.isEmpty();
	}


	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if( this.after() ) throw new AfterTheEnd280Exception("Cannot advance the cursor past the end.");
		if( this.before() ) this.goFirst();
		else {
			this.prev = this.cursor;
			this.cursor = this.cursor.next();
		}
	}


	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if(this.isEmpty()) throw new ContainerEmpty280Exception("Attempted to move linear iterator to first element of an empty tree.");
		this.prev = null;
		this.cursor = this.smallest;
	}


	@Override
	public void goBefore() {
		this.prev = null;
		this.cursor = null;
	}


	@Override
	public void goAfter() {
		this.prev = this.largest;
		this.cursor = null;
	}


	@Override
	public CursorPosition280 currentPosition() {
		return new TwoThreeTreePosition280<K,I>(this.cursor, this.prev);
	}


	@SuppressWarnings("unchecked")
	@Override
	public void goPosition(CursorPosition280 c) {
		if(c instanceof TwoThreeTreePosition280 ) {
			this.cursor = ((TwoThreeTreePosition280<K,I>) c).cursor;
			this.prev = ((TwoThreeTreePosition280<K,I>) c).prev;		
		}
		else {
			throw new InvalidArgument280Exception("The provided position was not a TwoThreeTreePosition280 object.");
		}
	}


	public void search(K k) {
		/** Student code BEGIN
		 *
		 * NOTE: This code was somewhat copied from the find(k) function in TwoThreeTree280.
		 */

		TwoThreeNode280<K,I> itemNode = find(this.rootNode, k);

		// if item is null, just place cursor after the end of the data structure
		if( itemNode == null ) {
			goAfter();

		// otherwise, update the cursor pointers to point at the found item
		} else {
			this.prev = cursor;
			this.cursor = (LinkedLeafTwoThreeNode280<K, I>) itemNode;
		}

		/** Student code END */
	}


	@Override
	public void searchCeilingOf(K k) {
		// Position the cursor at the smallest item that
		// has key at least as large as 'k', if such an
		// item exists.  If no such item exists, leave 
		// the cursor in the after position.
		
		// This one is easier to do with a linear search.
		// Could make it potentially faster but the solution is
		// not obvious -- just use linear search via the cursor.
		
		// If it's empty, do nothing; itemExists() will be false.
		if( this.isEmpty() ) 
			return;
		
		// Find first item item >= k.  If there is no such item,
		// cursor will end up in after position, and that's fine
		// since itemExists() will be false.
		this.goFirst();
		while(this.itemExists() && this.itemKey().compareTo(k) < 0) {
			this.goForth();
		}
		
	}


	@Override
	public void setItem(I x) throws NoCurrentItem280Exception,
            InvalidArgument280Exception {
		/** Student code BEGIN */

		if( !itemExists() )
			throw new NoCurrentItem280Exception("There is no current item to set.");

		else if( this.cursor.getKey1() != x.key() )
			throw new InvalidArgument280Exception("Can't set item with unmatched key.");

		this.cursor.setData(x);

		/** Student code END */
	}


	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		/** Student code BEGIN */

		// grab the successor of the leaf to delete
		LinkedLeafTwoThreeNode280<K,I> successor = (LinkedLeafTwoThreeNode280<K, I>) this.cursor.next();

		// delete the leaf
		delete(this.cursor.getKey1());

		// update the cursor position
		this.prev = this.cursor;
		this.cursor = successor;

		/** Student code END */
	}


    @Override
    public String toStringByLevel() {
        String s = super.toStringByLevel();
        
        s += "\nThe Linear Ordering is: ";
        CursorPosition280 savedPos = this.currentPosition();
        this.goFirst();
        while(this.itemExists()) {
            s += this.itemKey() + ", ";
            this.goForth();
        }
        this.goPosition(savedPos);
        
        if( smallest != null)
            s += "\nSmallest: " + this.smallest.getKey1();
        if( largest != null ) {
            s += "\nLargest: " + this.largest.getKey1();
        }
        return s;
    }

	public static void main(String args[]) {

		// A class for an item that is compatible with our 2-3 Tree class.  It has to implement Keyed280
		// as required by the class header of the 2-3 tree.  Keyed280 just requires that the item have a method
		// called key() that returns its key.  You *must* test your tree using Loot objects.

		class Loot implements Keyed280<String> {
			protected int goldValue;
			protected String key;

			@Override
			public String key() {
				return key;
			}
			
			@SuppressWarnings("unused")
			public int itemValue() {
				return this.goldValue;
			}

			Loot(String key, int i) {
				this.goldValue = i;
				this.key = key;
			}

		}
		
		// Create a tree to test with. 
		IterableTwoThreeTree280<String, Loot> T = new IterableTwoThreeTree280<String, Loot>();

		/** Student code BEGIN */

		Loot sampleOne = new Loot("AAA", 1000);
		T.insert(sampleOne);

		Loot sampleTwo = new Loot("BBB", 5000);
		T.insert(sampleTwo);

		Loot sampleThree = new Loot("QQQ", 200);
		T.insert(sampleThree);

		Loot sampleFour = new Loot("ZZZ", 2000);
		T.insert(sampleFour);

		if ( !T.before() )
			System.out.println("01: Should be before(), but we're not");

		T.goFirst();
		if ( !T.item().key().equals(sampleOne.key()) )
			System.out.println("02: Expected item to be " + sampleOne.key() + " but got " + T.item().key());

		T.goForth();
		if ( !T.item().key().equals(sampleTwo.key()) )
			System.out.println("03: Expected item to be " + sampleTwo.key() + " but got " + T.item().key());

		T.goForth();
		if ( !T.item().key().equals(sampleThree.key()) )
			System.out.println("04: Expected item to be " + sampleThree.key() + " but got " + T.item().key());

		T.goForth();
		if ( !T.item().key().equals(sampleFour.key()) )
			System.out.println("05: Expected item to be " + sampleFour.key() + " but got " + T.item().key());

		T.goForth();
		if ( !T.after() )
			System.out.println("06: Should be after(), but we're not");

		if ( T.itemExists() )
			System.out.println("07: Says item exists after tree, but it shouldn't");

		T.goBefore();
		if ( T.itemExists() )
			System.out.println("08: Says item exists before tree, but it shouldn't");

		if ( !T.before() )
			System.out.println("09: Should be before(), but we're not");

		T.goAfter();
		if ( T.itemExists() )
			System.out.println("10: Says item exists after tree, but it shouldn't");

		T.goFirst();
		if ( !T.itemKey().equals(sampleOne.key()) )
			System.out.println("11: Expected item to be " + sampleOne.key() + " but got " + T.itemKey());

		T.goForth();
		if ( !T.keyItemPair().firstItem().equals(sampleTwo.key()) )
			System.out.println("12: Expected item to be " + sampleTwo.key() + " but got " + T.keyItemPair().firstItem());

		T.deleteItem();
		if ( !T.itemKey().equals(sampleThree.key()) )
			System.out.println("13: Expected item to be " + sampleThree.key() + " but got " + T.itemKey());

		T.search(sampleTwo.key());
		if ( !T.after() )
			System.out.println("14: Should be after(), but we're not");

		T.search(sampleOne.key());
		if ( !T.itemKey().equals(sampleOne.key()) )
			System.out.println("15: Expected item to be " + sampleOne.key() + " but got " + T.itemKey());

		Loot sampleOneUpdate = new Loot("AAA", 1000000);

		T.setItem(sampleOneUpdate);
		if ( !T.itemKey().equals(sampleOneUpdate.key()) )
			System.out.println("16: Expected item to be " + sampleOneUpdate.key() + " but got " + T.itemKey());

		T.searchCeilingOf(sampleOneUpdate.key());
		if ( !T.itemKey().equals(sampleOneUpdate.key()) )
			System.out.println("17: Expected item to be " + sampleOneUpdate.key() + " but got " + T.itemKey());

		System.out.println("Regression Test Completed.");

		/** Student code END */
	}


	
}
