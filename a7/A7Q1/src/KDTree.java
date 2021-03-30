

public class KDTree {
    KDNode280 rootNode;

    public void KDTree(int dim) {
        rootNode = new KDNode280(dim);
    }

    public void KDTree(Double pts[]) {
        rootNode = new KDNode280(pts);
    }

    public void KDTree(double pts[]) {
        rootNode = new KDNode280(pts);
    }

    public static void main(String args[]) {

    }
}
