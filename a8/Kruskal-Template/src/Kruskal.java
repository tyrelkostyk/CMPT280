import lib280.graph.Vertex280;
import lib280.graph.WeightedEdge280;
import lib280.graph.WeightedGraphAdjListRep280;
import lib280.tree.ArrayedMinHeap280;

public class Kruskal {
	
	public static WeightedGraphAdjListRep280<Vertex280> minSpanningTree(WeightedGraphAdjListRep280<Vertex280> G) {

		// TODO -- Complete this method.

		/* Create and fill a dispenser containing every edge in sorted order */

		ArrayedMinHeap280<WeightedEdge280<Vertex280>> edgeHeap = new ArrayedMinHeap280<WeightedEdge280<Vertex280>>(G.numEdges()*2);

		// iterate through all vertices (start at beginning)
		G.goFirst();
		while (!G.after()) {
			// attempt to add every edge from this vertex (start at beginning)
			G.eGoFirst(G.item());
			while (!G.eAfter()) {
				// only add edge if (adjacent index > current node index); otherwise we've already added it
				if (G.eItemAdjacentIndex() > G.itemIndex())
					edgeHeap.insert(G.eItem());
				G.eGoForth();
			}
			G.goForth();
		}

		/* Create and fill a sub-graph of G that is the Minimum Spanning Tree of G */

		// initialize the Minimum Spanning Tree
		WeightedGraphAdjListRep280<Vertex280> minST = new WeightedGraphAdjListRep280<Vertex280>(G.capacity(), G.directed());
		minST.ensureVertices(G.capacity());
		// initialize the Union-Find data structure
		UnionFind280 UF = new UnionFind280(G.capacity());

		// iterate through each edge in the heap, from smallest to largest
		while (edgeHeap.itemExists()) {
			WeightedEdge280<Vertex280> edge = edgeHeap.item();

			// if the two vertices are in different sets, add the edge between them
			if (UF.find(edge.firstItem().index()) != UF.find(edge.secondItem().index())) {
				minST.addEdge(edge.firstItem(), edge.secondItem());
				minST.setEdgeWeight(edge.firstItem(), edge.secondItem(),
						G.getEdgeWeight(edge.firstItem(), edge.secondItem()));
				UF.union(edge.firstItem().index(), edge.secondItem().index());
			}

			// delete this edge and move on to the next one
			edgeHeap.deleteItem();
		}

		return minST;
	}
	
	
	public static void main(String args[]) {
		WeightedGraphAdjListRep280<Vertex280> G = new WeightedGraphAdjListRep280<Vertex280>(1, false);
		G.initGraphFromFile("../../../git-repos/CMPT280/a8/Kruskal-template/mst.graph");
		System.out.println("Example Output Graph:");
		System.out.println(G);
		
		WeightedGraphAdjListRep280<Vertex280> minST = minSpanningTree(G);

		System.out.println("\nMinimum Spanning Tree Graph:");
		System.out.println(minST);
	}
}


