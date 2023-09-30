/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public final class SAP {

    private final Digraph digraph;
    private final boolean[] marked1;
    private final boolean[] marked2;
    private final int[] dist1;
    private final int[] dist2;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("Digraph cannot be null.");
        digraph = new Digraph(G);
        int size = digraph.V();
        // we want two arrays for marked and dist as we will be calculating ancestors/lengths for two separate starting points
        marked1 = new boolean[size];
        marked2 = new boolean[size];
        dist1 = new int[size];
        dist2 = new int[size];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        // check that vertices are not out of our range of indices
        validate(v);
        validate(w);

        // if we get two identical ids, return 0
        if (v == w) return 0;

        // pass those with true as we want the length
        // the helper function takes in an iterable so we can use the same method for one vertex or a set of vertices
        // use Collections.singleton to turn this into an iterable
        return helper(Collections.singleton(v), Collections.singleton(w), true);

    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        // check that vertices are in range
        validate(v);
        validate(w);

        if (v == w) return v; // if they are the same we can return either one

        // pass those into out helper with false as we want the ancestor
        return helper(Collections.singleton(v), Collections.singleton(w), false);
    }


    private int helper(Iterable<Integer> set1, Iterable<Integer> set2, boolean length) {
        // bfs to find shortest path so use queues
        Queue<Integer> set1Queue = new Queue<>();
        Queue<Integer> set2Queue = new Queue<>();

        // enqueue each value marking it as true and the distance as 0
        for (int val : set1) {
            marked1[val] = true;
            dist1[val] = 0;
            set1Queue.enqueue(val);
        }
        for (int val : set2) {
            marked2[val] = true;
            dist2[val] = 0;
            set2Queue.enqueue(val);
        }

        // the minimum length is Integer.MAX_VALUE to start with
        int min = Integer.MAX_VALUE;
        int ancestor = -1; // ancestor is -1

        // while one of the queues still has values
        while (!(set1Queue.isEmpty() && set2Queue.isEmpty())) {

            // if the first one has values
            if (!set1Queue.isEmpty()) {
                int v = set1Queue.dequeue();
                // dequeue an element
                // if the other set has a path to this, this is an ancestor
                // so if it marked and the sum of the distances is less than min
                if (marked2[v] && dist1[v] + dist2[v] < min) {
                    // update shortest ancestor and minimum length
                    ancestor = v;
                    min = dist1[v] + dist2[v];
                }

                for (int adj : digraph.adj(v)) {
                    if (!marked1[adj]) { // if it isn't marked
                        // mark it and give it the distance
                        dist1[adj] = dist1[v] + 1;
                        marked1[adj] = true;
                        // do the same thing for the adjacent vertices that we did earlier
                        if (marked2[adj] && dist1[adj] + dist2[adj] < min) {
                            // if the other set has a pth and it is shorter, update ancestor and minimum length
                            ancestor = adj;
                            min = dist1[adj] + dist2[adj];
                        }
                        // if dist1 is at or past min, it won't be coming down, so we don't need to enqueue this node
                        else if (dist1[adj] >= min) {
                            continue;
                        }
                        set1Queue.enqueue(adj);
                    }
                }
            }

            // do the same thing for this queue
            if (!set2Queue.isEmpty()) {
                int w = set2Queue.dequeue();

                if (marked1[w] && dist1[w] + dist2[w] < min) {
                    ancestor = w;
                    min = dist1[w] + dist2[w];
                }

                for (int adj : digraph.adj(w)) {
                    if (!marked2[adj]) {
                        dist2[adj] = dist2[w] + 1;
                        marked2[adj] = true;
                        if (marked1[adj] && dist1[adj] + dist2[adj] < min) {
                            ancestor = adj;
                            min = dist1[adj] + dist2[adj];
                        }
                        else if (dist2[adj] >= min) {
                            continue;
                        }
                        set2Queue.enqueue(adj);
                    }
                }
            }
        }
        // reset all values in both marked and distance arrays
        reset();

        // if we want the length and that length isn't equal to Integer.MAX_VALUE, we found a length so return ir
        if (length && min != Integer.MAX_VALUE) return min;

        // if we don't want length, we want ancestor so return it
        // if want length but didn't find a path so min is still Integer.MAX_VALUE, we also didn't find an ancestor so it is still -1
        // so we can still return ancestor
        return ancestor;
    }

    private void reset() {
        Arrays.fill(marked1, false);
        Arrays.fill(marked2, false);
        Arrays.fill(dist1, 0);
        Arrays.fill(dist2, 0);
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Invalid value(s). Iterable cannot be null.");

        // get the iterator for both
        Iterator<Integer> v1 = v.iterator();
        Iterator<Integer> w1 = w.iterator();

        // if they are empty return -1
        if (!v1.hasNext() || !w1.hasNext()) return -1;

        // validate values in both iterators
        validateIterator(v1);
        validateIterator(w1);


        // an advantage of making the helper: we can use it here as well as the process won't change based off of whether
        // we are doing it from a vertex or a set of vertices

        // use true as we want the length
        return helper(v, w, true);
    }

    private void validateIterator(Iterator<Integer> iter) {

        // check all values to make sure there is no null and that they are in range.
        while (iter.hasNext()) {
            Integer next = iter.next();
            if (next == null)
                throw new IllegalArgumentException("Iterable cannot contain null");
            validate(next);

        }
    }


    /*
     * Validate the vertex is within the set of vertices in the digraph
     * */
    private void validate(int v) {
        if (v < 0 || v >= digraph.V()) throw new IllegalArgumentException("Vertex out of range.");
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();

        // get the iterator for both
        Iterator<Integer> v1 = v.iterator();
        Iterator<Integer> w1 = w.iterator();

        // if they are empty return -1
        if (!v1.hasNext() || !w1.hasNext()) return -1;

        // validate values in both iterators
        validateIterator(v1);
        validateIterator(w1);
        // we are doing it from a vertex or a set of vertices
        // use false as we want the ancestor
        return helper(v, w, false);
    }
}
