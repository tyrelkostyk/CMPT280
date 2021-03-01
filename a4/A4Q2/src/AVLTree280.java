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


public class AVLTree280<I extends Comparable<I>> extends OrderedSimpleTree280<I> implements Dispenser280<I> {

    /**
     * Insert x into the AVL Tree.
     *
     * @param x item to be inserted into the tree
     */
    @Override
    public void insert(I x) {
        // can't insert if the tree is full
        if (this.isFull())
            throw new ContainerFull280Exception("Can't insert; tree is full.");

        cur = rootNode;

        // tree was empty; create new root node and call it a day
        if (isEmpty())
            rootNode = createNewNode(x);

            // new node is less than current node; insert in left subtree
        else if (x.compareTo(item()) < 0) {
            OrderedSimpleTree280<I> leftTree = rootLeftSubtree();
            leftTree.insert(x);
            setRootLeftSubtree(leftTree);
        }

        // new node is greater than or equal to current node; insert in right subtree
        else {
            OrderedSimpleTree280<I> rightTree = rootRightSubtree();
            rightTree.insert(x);
            setRootRightSubtree(rightTree);
        }

        restoreAVLProperty(this);
    }

    /**
     * Delete current item from the data structure.
     *
     * @throws NoCurrentItem280Exception if the cursor is not currently positioned at a valid item.
     * @precond itemExists()
     */
    public void deleteItem() {
        // current item must exist and be valid
        if (!this.itemExists())
            throw new NoCurrentItem280Exception("Can't delete; current item is not valid.");

    }


    protected int height(LinkedSimpleTree280<I> x) {
        // return 0 if this tree does not exist
        if (x == null || x.isEmpty())
            return 0;

        // otherwise, return the height of the tallest subtree. plus 1 for the root
        int leftSubtreeHeight = height(x.rootLeftSubtree());
        int rightSubtreeHeight = height(x.rootRightSubtree());
        return 1 + Math.max(leftSubtreeHeight, rightSubtreeHeight);
    }


    private int signedImbalance(LinkedSimpleTree280<I> x) {
        if (x.isEmpty())
            return 0;

        return height(x.rootLeftSubtree()) - height(x.rootRightSubtree());
    }


    private void restoreAVLProperty(LinkedSimpleTree280<I> x) {
        int imbalance = signedImbalance(x);

        // no need to restore AVL property if its not violated
        if (Math.abs(imbalance) <= 1)
            return;

        // x is left heavy
        if (imbalance == 2) {
            // LR imbalance: do an extra left rotation on left child
            if (signedImbalance(x.rootLeftSubtree()) < 0)
                leftRotation(x.rootLeftSubtree());
            // LR or LL imbalance: do right rotation on root node
            rightRotation(x);

            // x is right heavy
        } else {
            // RL imbalance: do an extra right rotation on right child
            if (signedImbalance(x.rootRightSubtree()) > 0)
                rightRotation(x.rootRightSubtree());
            // RL or RR imbalance: do left rotation on root node
            leftRotation(x);
        }

    }


    /**
     * Right rotation. Fixes an LL imbalance.
     * <p>
     * This node (A) becomes the right child of its left child (B), and the previous
     * right child (E) of B becomes A's left child. E is now left child to A, who is
     * now right child of B. A and B need to update their heights.
     */
    private void rightRotation(LinkedSimpleTree280<I> x) {
        // make a copy of the left child of A (B)
        OrderedSimpleTree280<I> B = (OrderedSimpleTree280<I>) x.rootLeftSubtree();

        // make a copy of the right child of B (E)
        OrderedSimpleTree280<I> E = (OrderedSimpleTree280<I>) B.rootRightSubtree();

        // set E to be the left child of the root
        x.setRootLeftSubtree(E);

        // set the root as the right child of B
        B.setRootRightSubtree(x);
    }


    /**
     * Left rotation. Fixes an RR imbalance.
     * <p>
     * This node (A) becomes the left child of its right child (C), and the previous
     * left child (D) of C becomes A's right child. D is now right child to A, who is
     * now left child of C. A and C need to update their heights.
     */
    private void leftRotation(LinkedSimpleTree280<I> x) {
        // make a copy of the right child of A (C)
        OrderedSimpleTree280<I> C = (OrderedSimpleTree280<I>) x.rootRightSubtree();

        // make a copy of the left child of C (D)
        OrderedSimpleTree280<I> D = (OrderedSimpleTree280<I>) C.rootLeftSubtree();

        // set D to be the right child of the root
        x.setRootRightSubtree(D);

        // set the copy of A as the left child of C
        C.setRootLeftSubtree(x);
    }


    public static void main(String[] args) {

        AVLTree280<Integer> H = new AVLTree280<Integer>();

        for (int i = 1; i <= 10; i++) {
            H.insert(i);
        }

        System.out.println("Done testing insert(); see AVL Tree:");
        System.out.println(H.toString());

    }
}
