import edu.princeton.cs.algs4.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/*
 *
 * The WordNet class.
 *
 * Takes in a file of synsets and a file of hypernyms, and creates a digraph with them.
 * Provides functions that can check if we have a noun, the shortest length between two nouns,
 * the common ancestor for this path and more.
 *
 * Makes use of the SAP class, as well as the algs4 library provided by the online course this was done for.
 *
 * */
public final class WordNet {

    // a treemap with each individual noun (as synsets can contain multiple nouns) as the key
    // the value is a bag of integers which will contain all the instances of all the synsets this specific noun is found in
    private final TreeMap<String, Bag<Integer>> instances;

    // a map with the vertex number as the key, and the synset as the value
    private final Map<Integer, String> vertices;

    // the sap object which will contain the digraph of the wordnet
    private final SAP s;


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        // if either of these are null throw an exception
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("One or more files invalid.");

        // use the In class in algs4 with both files
        In synset = new In(synsets);
        In hypernym = new In(hypernyms);

        // initialize vertices and instances
        vertices = new HashMap<>();
        instances = new TreeMap<>();
        // we don't need to get the index from the file and keep parsing it as it starts at 0 and always goes up by one
        int index = 0;
        while (synset.hasNextLine()) { // while there are synsets left
            // read in the next line, and split it by commas
            // the string at index 1 is our synset
            // put it in the vertices map
            String nextSynset = synset.readLine().split(",")[1];
            vertices.put(index, nextSynset);

            // split the synsets by space to get the nouns that make up this synset
            for (String string : nextSynset.split(" ")) {
                // if the instances already has this noun
                if (instances.containsKey(string)) {
                    // add this new index to the indices that noun is present
                    instances.get(string).add(index);
                }
                else {
                    // otherwise, create a Bag and add the index
                    Bag<Integer> indices = new Bag<>();
                    indices.add(index);
                    // put that in the tree map with the noun as the value
                    instances.put(string, indices);

                }
            }
            index++; // increment index
        }

        // create a new digraph called wordnet of size index as the last index + 1 is the same as the size.
        Digraph wordnet = new Digraph(index);

        // create a variable called notRooted which will count the number of vertices that are rooted
        int rooted = 0;

        while (hypernym.hasNextLine()) {
            String[] components = hypernym.readLine().split(",");
            int vertex = Integer.parseInt(components[0]); // the first index will contain the vertex
            for (int i = 1; i < components.length; i++) {
                // the rest will contain the synset that this vertex points to
                // so add an edge between these
                wordnet.addEdge(vertex, Integer.parseInt(components[i]));
            }
            // vertex is rooted if it points to no vertices
            // if the length of comoponents is 1, this is the case
            if (components.length == 1) rooted++;

        }
        // if there is not exactly 1 root, the provided file does not contain a DAG
        // a DAG refers to a directed acyclic graph
        // there must be a root, and only one root, or else there will be cycles in the graph
        // a cycle is a path that starts and ends at the same vertex
        // this is not allowed
        if (rooted != 1) throw new IllegalArgumentException("Not a DAG");
        // initalize SAP with wordnet
        s = new SAP(wordnet);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        // return the set of keys in keyset
        return instances.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        // if word is null throw an exception
        if (word == null) throw new IllegalArgumentException("Invalid word.");

        // check if instances has a word as a key
        return instances.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("Noun cannot be null.");

        // the sap class has a length method that finds the shortest path between elements in two iterables
        return s.length(instances.get(nounA), instances.get(nounB));
    }


    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("Noun cannot be null.");

        // the sap class has a length method that finds the ancestor shortest path between elements in two iterables
        // take that id, and get the string for it from vertices
        return vertices.get(s.ancestor(instances.get(nounA), instances.get(nounB)));
    }


    public static void main(String[] args) {
        String synset = args[0];
        String hypernym = args[1];
        WordNet wordnet = new WordNet(synset, hypernym);
    }
}
