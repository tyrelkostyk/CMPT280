import lib280.base.Dispenser280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.tree.LinkedSimpleTree280;
import lib280.tree.OrderedSimpleTree280;

/**
 * CMPT 280
 * Assignment 4, Question 2
 *
 * Tyrel Kostyk
 * tck290
 * 11216033
 */


public class AVLTree280<I extends Comparable<I>> extends OrderedSimpleTree280<I> implements Dispenser280<I>
{

    protected int leftHeight = 0;
    protected int rightHeight = 0;

    /**
     * Insert x into the AVL Tree.
     * @param x item to be inserted into the tree
     */
    public void insert(I x)
    {
        // can't insert if the tree is full
        if (this.isFull())
            throw new ContainerFull280Exception("Can't insert; tree is full.");

        // tree was empty; create new root node and call it a day
        if (isEmpty())
            rootNode = createNewNode(x);

        // new node is less than current node; insert in left subtree
        else if (x.compareTo(rootItem()) < 0)
        {
            OrderedSimpleTree280<I> leftTree = rootLeftSubtree();
            leftTree.insert(x);
            setRootLeftSubtree(leftTree);
            // recalculate height of the left subtree
            leftHeight = height(leftTree);
        }

        // new node is greater than or equal to current node; insert in right subtree
        else
        {
            OrderedSimpleTree280<I> rightTree = rootRightSubtree();
            rightTree.insert(x);
            setRootRightSubtree(rightTree);
            // recalculate height of the right subtree
            rightHeight = height(rightTree);
        }

        // check if this is a critical node
        if (!hasAVLProperty()) {
            // TODO: restore AVL property, if necessary
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

    }


    protected int height(LinkedSimpleTree280<I> x)
    {
        // return 0 if this tree does not exist
        if (x == null)
            return 0;

        // otherwise, return the height of the tallest subtree. plus 1 for the root
        int leftHeight = height(x.rootLeftSubtree());
        int rightHeight = height(x.rootRightSubtree());
        return 1 + Math.max(leftHeight, rightHeight);
    }


    private boolean hasAVLProperty()
    {
        return (Math.abs(leftHeight - rightHeight) >= 2);
    }



}
