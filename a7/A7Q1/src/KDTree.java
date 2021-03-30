import lib280.base.NDPoint280;
import lib280.tree.BinaryNode280;

public class KDTree {
    /** The node at the root of this tree */
    protected KDNode280 rootNode;

    public KDTree(KDNode280[] points, int left, int right, int depth) {
        if (points.length == 0)
            return;

        if (depth > points[0].point().dim())
            return;

        int k = points[0].point().dim();
        int dimToEval = (depth % k);
        int medianOffset = (left + right) / 2;

        jSmallest(points, left, right, medianOffset, dimToEval);

//        System.out.println("Adding Node; Depth: " + depth);
//        System.out.println("Adding Node; Value: " + points[medianOffset].point().toString());
//        System.out.println();
        this.rootNode = points[medianOffset];

//        System.out.println("Adding Left Node");
        KDTree leftNode = new KDTree(points, left, medianOffset-1, depth+1);
        this.rootNode.setLeftChild(leftNode.rootNode);

//        System.out.println("Adding Right Node");
        KDTree rightNode = new KDTree(points, medianOffset+1, right, depth+1);
        this.rootNode.setRightChild(rightNode.rootNode);
    }

    public int dim() {
        if (rootNode == null)
            return 0;
        return this.rootNode.point().dim();
    }

    private void jSmallest(KDNode280[] points, int left, int right, int j, int dim)
    {
        if (right <= left)
            return;

        int pivotIndex = partition(points, left, right, dim);

        if (j == pivotIndex)
            return;

        if (j < pivotIndex)
            jSmallest(points, left, pivotIndex-1, j, dim);
        else if (j > pivotIndex)
            jSmallest(points, pivotIndex+1, right, j, dim);
    }

    private int partition(KDNode280[] points, int left, int right, int dim) {
        KDNode280 pivot = points[right];
        int swapOffset = left;

        for (int i=left; i < right; i++) {
            if (points[i].point().compareByDim(dim, pivot.point()) <= 0) {
                // swap current point with point at swapOffset
                KDNode280 tempNode = points[i];
                points[i] = points[swapOffset];
                points[swapOffset] = tempNode;

                swapOffset++;
            }
        }

        // swap pivot point with point at swapOffset
        KDNode280 tempNode = points[right];
        points[right] = points[swapOffset];
        points[swapOffset] = tempNode;

        return swapOffset;
    }

    public KDNode280 rootNode() {
        return this.rootNode;
    }

    @Override
    public String toString() {
        return this.rootNode.toStringByLevel(0);
    }


    public static void main(String args[]) {
        /* TEST ONE */

        Double[] pointOne = {5.0, 2.0};
        KDNode280 nodeOne = new KDNode280(pointOne);
        Double[] pointTwo = {9.0, 10.0};
        KDNode280 nodeTwo = new KDNode280(pointTwo);
        Double[] pointThree = {11.0, 1.0};
        KDNode280 nodeThree = new KDNode280(pointThree);
        Double[] pointFour = {4.0, 3.0};
        KDNode280 nodeFour = new KDNode280(pointFour);
        Double[] pointFive = {2.0, 12.0};
        KDNode280 nodeFive = new KDNode280(pointFive);
        Double[] pointSix = {3.0, 7.0};
        KDNode280 nodeSix = new KDNode280(pointSix);
        Double[] pointSeven = {1.0, 5.0};
        KDNode280 nodeSeven = new KDNode280(pointSeven);
        KDNode280[] nodes = {nodeOne, nodeTwo, nodeThree, nodeFour, nodeFive, nodeSix, nodeSeven};

        System.out.println("UNIT TEST ONE of KDTree begin.");

        System.out.println("Input 2D nodes:");
        for (KDNode280 node : nodes) {
            System.out.println(node.point().toString());
        }

        // create tree
        KDTree testTree = new KDTree(nodes, 0, nodes.length-1, 0);

        // test dimensions of new tree
        if (testTree.dim() != pointOne.length)
            System.out.println("01: dim should be " + pointOne.length + ", but instead it is " + testTree.dim());

        System.out.println("\n" + testTree);

        System.out.println("UNIT TEST ONE of KDTree complete.");

//        /* TEST TWO */
//        System.out.println("UNIT TEST TWO of KDTree begin.");
//        System.out.println("UNIT TEST TWO of KDTree complete.");
    }
}
