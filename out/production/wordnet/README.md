# wordnet
The wordnet assignment from part 2 of the algorithms course on Coursera.

Here is the specification: https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php

The following is a quick summary of the specification.

This project is made up of 3 classes.

The wordnet class puts synonyms into groups called synsets. Sysnets can be connected to hypernyms which is a synset that is more general than this synset.

This results in a directed graph (digraph).

The following image is a part of a synset provided by the assignment.

![Wordnet Example](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/wordnet-event.png)  

The purpose of this class is to be able to perform operations on graphs like this. These operations include checking if a word is in the graph, the shortest path between two words, the common ancestor of this path etc.

The second class is the Shortest Ancestral Path or SAP.

This is a generalization of wordnet in that it works for all digraphs and is able to perform the length and ancestor operations for both indvidual vertices and sets of vertices.

The third class is Outcast.

This class takes a list of words in a wordnet graph, and finds the outcast, namely the word that is the furthest distance from all the other words. 
