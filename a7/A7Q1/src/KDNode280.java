import lib280.base.NDPoint280;

public class KDNode280 implements Comparable<KDNode280> {
    /** Contents of the node */
    protected NDPoint280 point;

    /** The left node */
    protected KDNode280 leftChild;

    /** The right node */
    protected KDNode280 rightChild;


    public KDNode280(int dim) {
        point = new NDPoint280(dim);
    }

    public KDNode280(Double pt[]) {
        point = new NDPoint280(pt);
    }

    public KDNode280(double pt[]) {
        point = new NDPoint280(pt);
    }

    public NDPoint280 point() {
        return this.point;
    }

    public int compareTo(KDNode280 other) {
        return this.point().compareTo(other.point());
    }

    public KDNode280 leftChild() {
        return this.leftChild;
    }

    public KDNode280 rightChild() {
        return this.rightChild;
    }

    public void setLeftChild(KDNode280 node) {
        this.leftChild = node;
    }

    public void setRightChild(KDNode280 node) {
        this.rightChild = node;
    }

    public String toStringByLevel(int depth) {
        String result = "";
        String blanks = "";
        for (int i=0; i<depth; i++) {
            blanks += "    ";
        }

        // print the right subtree
        if (this.rightChild != null) result += this.rightChild.toStringByLevel(depth+1);

        // print the root node and current depth
        result += "\n" + blanks + depth + ": " + point().toString();

        // print the right subtree
        if (this.rightChild != null) result += this.leftChild.toStringByLevel(depth+1);

        // return result
        return result;
    }
}
