# wordnet
The wordnet assignment from part 2 of the algorithms course on Coursera.

Here is the specification: https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php

The following is a quick summary of the specification.

This project is made up of 3 classes.

The wordnet class puts synonyms into groups called synsets. Sysnets can be connected to hypernyms which is a synset that is more general than this synset.

This results in a directed graph (digraph).

The following image is a part of a synset provided by the assignment.

![Wordnet Example](https://coursera.cs.princeton.edu/algs4/assignments/wordnet/wordnet-event.png)  

To use:

  Run the WordNet program passing in the synsets filename as the first argument and the hypernym file name as the second argument. 
  Add any operations you want to complete to the main method.

The purpose of this class is to be able to perform operations on graphs like this. These operations include checking if a word is in the graph, the shortest path between two words, the common ancestor of this path etc.

The second class is the Shortest Ancestral Path or SAP.

This is a generalization of wordnet in that it works for all digraphs and is able to perform the length and ancestor operations for both indvidual vertices and sets of vertices.

To use:
  Run the SAP program with the digraph file name as the first argument.
  Keep entering in integers two at a time on the same line and it will find the shortest length and common ancestor for these vertices.

The third class is Outcast.

This class takes a list of words in a wordnet graph, and finds the outcast, namely the word that is the furthest distance from all the other words. 

To use:

  Run the Outcast program passing in the synsets filename as the first argument and the hypernym file name as the second argument. 
  The remaining arguments can be any number of files you want, where each file contains a set of words from the synsets.
  The program will find and output the outcast in each of these files.

