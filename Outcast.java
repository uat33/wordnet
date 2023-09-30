/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.HashMap;
import java.util.Objects;

public final class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;


    }


    // static class as we don't need access to outer class fields
    // make this class, so we can easily get the hash code for each pair of nouns
    private static final class Nouns {

        private final String nounA;
        private final String nounB;

        public Nouns(String a, String b) {
            if (a == null || b == null) throw new IllegalArgumentException("Nouns cannot be null.");
            this.nounA = a;
            this.nounB = b;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj == this) return true;
            if (obj.getClass() != getClass()) return false;
            Nouns nouns = (Nouns) obj;
            // if the nounA's match and the nounB's match, or the a's match with the b's, they are the same pair
            return (nouns.nounA.equals(this.nounA) && nouns.nounB.equals(this.nounB)) ||
                    (nouns.nounA.equals(this.nounB) && nouns.nounB.equals(this.nounA));
        }

        @Override
        public int hashCode() {
            // return the sum of the hashcodes of the two nouns
            return Objects.hashCode(nounA) + Objects.hashCode(nounB);
        }
    }

    // given an array of WordNet nouns, return an outcast
    /*
     * An outcast is defined as the string with the furthest distance from all the other strings.
     * */
    public String outcast(String[] nouns) {

        String max = ""; // where we'll save outcast
        int maxVal = 0; // the max distance we want to compare to
        // cache the values so we don't have to recalculate (j, i) if we've already done (i, j)
        HashMap<Nouns, Integer> distanceCache = new HashMap<>();
        for (int i = 0; i < nouns.length; i++) {
            int sum = 0; // the current nouns distance

            // go through all the nouns
            // because in order to get the value of each noun we have to get the distance to every other noun
            // this requires two loops
            // cache the values so at least we don't have to calculate for the same pair twice
            for (int j = 0; j < nouns.length; j++) {
                Nouns current = new Nouns(nouns[i], nouns[j]);
                if (distanceCache.containsKey(current)) {
                    sum += distanceCache.get(current);
                }
                else {
                    int distance = wordnet.distance(nouns[i], nouns[j]);
                    distanceCache.put(current, distance);
                    sum += distance;
                }
            }
            if (sum > maxVal) {
                maxVal = sum;
                max = nouns[i];
            }
        }
        return max;
    }
}
