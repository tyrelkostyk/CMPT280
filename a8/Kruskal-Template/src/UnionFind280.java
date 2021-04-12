

import lib280.graph.Edge280;
import lib280.graph.GraphAdjListRep280;
import lib280.graph.Vertex280;


public class UnionFind280 {
	GraphAdjListRep280<Vertex280, Edge280<Vertex280>> G;
	
	/**
	 * Create a new union-find structure.
	 * 
	 * @param numElements Number of elements (numbered 1 through numElements, inclusive) in the set.
	 * @postcond The structure is initialized such that each element is in its own subset.
	 */
	public UnionFind280(int numElements) {
		G = new GraphAdjListRep280<Vertex280, Edge280<Vertex280>>(numElements, true);
		G.ensureVertices(numElements);		
	}
	
	/**
	 * Return the representative element (equivalence class) of a given element.
	 * @param id The elements whose equivalence class we wish to find.
	 * @return The representative element (equivalence class) of the element 'id'.
	 */
	public int find(int id) {
		// save the initial index
		int cachedIndex = G.itemIndex();

		// store the initial index
		int representativeIndex = id;

		// start at the vertex represented by the index given
		G.goIndex(id);
		// start at the first edge of this vertex
		G.eGoFirst(G.item());

		// continue iterating while there are still edges to iterate over
		while (!G.eAfter()) {
			// update stored index
			representativeIndex = G.eItemAdjacentIndex();

			// go to adjacent vertex referred to by this edge
			G.goIndex(representativeIndex);
			// start at the first edge of this new vertex
			G.eGoFirst(G.item());
		}

		// restore the initial index
		G.goIndex(cachedIndex);

		// return the final index
		return representativeIndex;
	}
	
	/**
	 * Merge the subsets containing two items, id1 and id2, making them, and all of the other elemnets in both sets, "equivalent".
	 * @param id1 First element.
	 * @param id2 Second element.
	 */
	public void union(int id1, int id2) {
		// find the roots of the two indices given
		int rootOne = find(id1);
		int rootTwo = find(id2);

		// if the two indices don't share a common root, merge them by adding an edge between their roots
		if (rootOne != rootTwo)
			G.addEdge(rootOne, rootTwo);
	}

	public static void main(String args[]) {
		int sizeOfGraph = 6;
		UnionFind280 UF = new UnionFind280(sizeOfGraph);

		if (UF.find(1) != 1)
			System.out.println("01: Find operation expected 1 but got " + UF.find(1));

		if (UF.find(3) != 3)
			System.out.println("02: Find operation expected 3 but got " + UF.find(3));

		if (UF.find(6) != 6)
			System.out.println("03: Find operation expected 6 but got " + UF.find(6));

		UF.union(1, 6);	// sets: {1,6} {2} {3} {4} {5}
		if (UF.find(1) != UF.find(6))
			System.out.println("04: Find operation expected 1 and 6 to be the same but got " + UF.find(1) + " and " + UF.find(6));

		UF.union(1, 2);	// sets: {1,2,6} {3} {4} {5}
		if (UF.find(2) != UF.find(6))
			System.out.println("05: Find operation expected 2 and 6 to be the same but got " + UF.find(2) + " and " + UF.find(6));

		if (UF.find(2) == UF.find(3))
			System.out.println("06: Find operation expected 2 and 3 to be different but got " + UF.find(2));

		UF.union(3, 4);	// sets: {1,2,6} {3,4} {5}
		UF.union(3, 5);	// sets: {1,2,6} {3,4,5}
		if (UF.find(3) != UF.find(4))
			System.out.println("07: Find operation expected 3 and 4 to be the same but got " + UF.find(3) + " and " + UF.find(4));
		if (UF.find(3) != UF.find(5))
			System.out.println("08: Find operation expected 3 and 5 to be the same but got " + UF.find(3) + " and " + UF.find(5));
		if (UF.find(4) != UF.find(5))
			System.out.println("09: Find operation expected 4 and 5 to be the same but got " + UF.find(4) + " and " + UF.find(5));

		UF.union(6, 5);	// sets: {1,2,6,3,4,5}
		if (UF.find(1) != UF.find(4))
			System.out.println("10: Find operation expected 1 and 4 to be the same but got " + UF.find(1) + " and " + UF.find(4));

		System.out.println("Testing UnionFind280 Complete.");
	}
}
