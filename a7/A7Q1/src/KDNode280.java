import lib280.base.NDPoint280;

public class KDNode280 extends NDPoint280 {


    /**
     * Create a new N-dimensional Node at the origin (0,0,..., 0).
     * @param dim Dimensionality of the point.
     */
    public KDNode280(int dim) {
        super(dim);
    }

    /**
     * Create a point from a coordinate in an array of Double.  The dimensionality of
     * the point is equal to the length of pt.
     *
     * @param pt Coordinates of the new point.
     */
    public KDNode280(Double pt[]) {
        super(pt);
    }

    /**
     * Create a point from a coordinate in array of double.  The dimensionality of
     * the point is equal to the length of pt.
     *
     * @param pt Coordinates of the new point.
     */
    public KDNode280(double pt[]) {
        super(pt);
    }

}
