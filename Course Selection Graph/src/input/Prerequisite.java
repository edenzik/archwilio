package input;

import java.util.ArrayList;

/**
 * Prerequisite graph
 * Created by zhan on 7/28/15.
 */
public class Prerequisite {

    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;  // total number of vertices
    private int E;  // total number of edges
    private ArrayList<Integer>[] adj;  // adjacent vertices of each vertex


    /**
     * Initialize a directed graph from input
     * @param input
     */
    public Prerequisite(String input) {
        String[] lines = input.split("\\s+");
        this.V = Integer.parseInt(lines[0]);
        if (V < 0) throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        adj = (ArrayList<Integer>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
        int E = Integer.parseInt(lines[1]);
        if (E < 0) throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");
        for (int i = 2; i < lines.length; i+=2) {
            int v = Integer.parseInt(lines[i]);
            int w = Integer.parseInt(lines[i+1]);
            addEdge(v, w);
        }
    }


    /**
     * Returns the number of vertices in the digraph.
     *
     * @return the number of vertices in the digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in the digraph.
     *
     * @return the number of edges in the digraph
     */
    public int E() {
        return E;
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    /**
     * Adds the directed edge v->w to the digraph.
     *
     * @param v the tail vertex
     * @param w the head vertex
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= v <= V and 0 <= w <= V
     */
    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        E++;
    }

    /**
     * Returns the vertices adjacent from vertex v in the digraph.
     *
     * @param v the vertex
     * @return the vertices adjacent from vertex v in the digraph
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= v <= V
     */
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex <tt>v</tt>.
     * This is known as the <em>outdegree</em> of vertex <tt>v</tt>.
     *
     * @param v the vertex
     * @return the outdegree of vertex <tt>v</tt>
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= v < V
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    /**
     * Returns a string representation of the graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(String.format("%s: ", Global.idx_to_course.get(v)));
            for (int w : adj[v]) {
                s.append(String.format("%s ", Global.idx_to_course.get(w)));
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }



}
