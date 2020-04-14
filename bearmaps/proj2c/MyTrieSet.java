package bearmaps.proj2c;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyTrieSet implements TrieSet61B {

    private static final int R = 256;        // extended ASCII
    private Node root;      // root of trie

    // R-way trie node
    private class Node {
        private char ch;
        private boolean isKey;
        private DataIndexedCharMap next;
        private Node(char c, boolean b, int R) {
            ch = c;
            isKey = b;
            next = new DataIndexedCharMap<Node>(R);
        }
    }

    public class DataIndexedCharMap<V> {
        private HashMap<V, Node>items;
        public DataIndexedCharMap(int R) {
            items = new HashMap<>(R);
        }
    }

    /**
     * Initializes an empty string symbol table.
     */
    public MyTrieSet() {
        root = new Node ('i', false, 256);
    }


    /**
     * Clears all items out of Trie
     */
    @Override
    public void clear() {
        root = new Node ('i', false, 256);
    }

    /**
     * Returns true if the Trie contains KEY, false otherwise
     */
    @Override
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return get(root, key, 0) != null;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d); //WHAT IS THE LETTER IN THE WORD(KEY) D POSITION
        return get((Node)x.next.items.get(c), key, d + 1);
    }

//
    /**
     * Inserts string KEY into Trie
     */
    // CHAR X = ASCII_VALUE_OF(C);
    //IF CURR.NEXT[X] DOes NOT HAS A NODE IN IT:
    //THEN INSERT A NODE IN THAT POSITION
//    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!curr.next.items.containsKey(c)) {
                curr.next.items.put(c, new Node(c, false, 256));
            }
            curr = (Node)curr.next.items.get(c);
        }
        curr.isKey = true;
    }

    /** Returns a list of all words that start with PREFIX */
    @Override
    public List<String> keysWithPrefix(String prefix) {
        List<String> results = new ArrayList<>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, List<String> results) {
        if (x == null) {
            return;
        }
        if (x.isKey == true) {
            results.add(prefix.toString());
        }
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect((Node)x.next.items.get(c), prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }


        /** Returns the longest prefix of KEY that exists in the Trie
         * Not required for Lab 9. If you don't implement this, throw an
         * UnsupportedOperationException.
         */
    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }
}
