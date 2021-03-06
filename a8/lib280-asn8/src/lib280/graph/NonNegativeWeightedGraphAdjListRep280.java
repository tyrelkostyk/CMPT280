 package lib280.graph;

//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;

 import lib280.base.Pair280;
 import lib280.exception.InvalidArgument280Exception;

 import java.util.Arrays;
 import java.util.InputMismatchException;
 import java.util.Scanner;


 public class NonNegativeWeightedGraphAdjListRep280<V extends Vertex280> extends
         WeightedGraphAdjListRep280<V> {

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d,
                                                  String vertexTypeName) {
         super(cap, d, vertexTypeName);
     }

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d) {
         super(cap, d);
     }


     @Override
     public void setEdgeWeight(V v1, V v2, double weight) {
         // Overriding this method to throw an exception if a weight is negative will cause
         // super.initGraphFromFile to throw an exception when it tries to set a weight to
         // something negative.

         // Verify that the weight is non-negative
         if(weight < 0) throw new InvalidArgument280Exception("Specified weight is negative.");

         // If it is, then just set the edge weight using the superclass method.
         super.setEdgeWeight(v1, v2, weight);
     }

     @Override
     public void setEdgeWeight(int srcIdx, int dstIdx, double weight) {
         // Get the vetex objects associated with each index and pass off to the
         // version of setEdgeWEight that accepts vertex objects.
         this.setEdgeWeight(this.vertex(srcIdx), this.vertex(dstIdx), weight);
     }


     /**
      * Implementation of Dijkstra's algorithm.
      * @param startVertex Start vertex for the single-source shortest paths.
      * @return An array of size G.numVertices()+1 in which offset k contains the shortest
      *         path from startVertex to k.  Offset 0 is unused since vertex indices start
      *         at 1.
      */
     public Pair280<double[], int[]> shortestPathDijkstra(int startVertex) {
         // initialize arrays for tracking states and values of each node
         double tentativeDistance[] = new double[capacity()+1];
         boolean visited[] = new boolean[capacity()+1];
         int predecessorNode[] = new int[capacity()+1];

         // set default values for the internal arrays
         Arrays.fill(tentativeDistance, Double.POSITIVE_INFINITY);
         Arrays.fill(visited, false);
         Arrays.fill(predecessorNode, 0);

         // set initial distance for starter node
         tentativeDistance[startVertex] = 0;

         // set initial loop variables for tracking nodes
         int currentNode = startVertex;
         int adjacentNode = 0;

         // continue until all nodes have been visited
         this.goIndex(currentNode);
         while (this.itemExists()) {
             visited[currentNode] = true;

             // evaluate each edge that connects to a non-visited node
             this.eGoFirst(this.item());
             while (!this.eAfter()) {
                 adjacentNode = this.eItemAdjacentIndex();
                 double newDistance = tentativeDistance[currentNode] + this.getEdgeWeight(adjacentNode, currentNode);
                 // update the adjacent node's values if it has not been visited and the new distance is shorter
                 if ((!visited[adjacentNode]) && (tentativeDistance[adjacentNode] > (newDistance))) {
                    tentativeDistance[adjacentNode] = newDistance;
                    predecessorNode[adjacentNode] = currentNode;
                 }
                 // continue to the next edge
                 this.eGoForth();
             }

             // find the next node to process
             int nextNode = 0;
             for (int i=1; i<tentativeDistance.length; i++) {
                 if ((!visited[i]) && (tentativeDistance[i] < tentativeDistance[nextNode]))
                     nextNode = i;
             }

             // continue to the next node
             currentNode = nextNode;
             this.goIndex(currentNode);
         }

         // Remove this return statement when you're ready -- it's a placeholder to prevent a compiler error.
         return new Pair280<double[], int[]>(tentativeDistance, predecessorNode);
     }

     // Given a predecessors array output from this.shortestPathDijkatra, return a string
     // that represents a path from the start node to the given destination vertex 'destVertex'.
     private static String extractPath(int[] predecessors, int destVertex) {
         if (predecessors[destVertex] == 0)
             return "Not Reachable";

         // initialize string
         String reversePath = ", " + destVertex;

         // iterate and add each step along the way
         int node = predecessors[destVertex];
         while (predecessors[node] != 0) {
             reversePath = ", " + node + reversePath;
             node = predecessors[node];
         }
         // add the final (aka, the initial) node in the path
         reversePath = node + reversePath;

         return "The path to " + destVertex + " is: " + reversePath;
     }

     // Regression Test
     public static void main(String args[]) {
         NonNegativeWeightedGraphAdjListRep280<Vertex280> G = new NonNegativeWeightedGraphAdjListRep280<Vertex280>(1, false);

         if( args.length == 0)
             G.initGraphFromFile("../../../git-repos/CMPT280/a8/lib280-asn8/src/lib280/graph/weightedtestgraph.gra");
         else
             G.initGraphFromFile(args[0]);

         System.out.println("Enter the number of the start vertex: ");
         Scanner in = new Scanner(System.in);
         int startVertex;
         try {
             startVertex = in.nextInt();
         }
         catch(InputMismatchException e) {
             in.close();
             System.out.println("That's not an integer!");
             return;
         }

         if( startVertex < 1 || startVertex > G.numVertices() ) {
             in.close();
             System.out.println("That's not a valid vertex number for this graph.");
             return;
         }
         in.close();


         Pair280<double[], int[]> dijkstraResult = G.shortestPathDijkstra(startVertex);
         double[] finalDistances = dijkstraResult.firstItem();
         //double correctDistances[] = {-1, 0.0, 1.0, 3.0, 23.0, 7.0, 16.0, 42.0, 31.0, 36.0};
         int[] predecessors = dijkstraResult.secondItem();

         for(int i=1; i < G.numVertices() +1; i++) {
             System.out.println("The length of the shortest path from vertex " + startVertex + " to vertex " + i + " is: " + finalDistances[i]);
 //			if( correctDistances[i] != finalDistances[i] )
 //				System.out.println("Length of path from to vertex " + i + " is incorrect; should be " + correctDistances[i] + ".");
 //			else {
                 System.out.println(extractPath(predecessors, i));
 //			}
         }
     }

 }
