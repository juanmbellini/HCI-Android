package hci.tiendapp.backend;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Trie {
    private Map<Character, LetterNode> root = new HashMap<>();

    public Trie() {
    }

    /**
     * Evaluates if the dictionary is empty
     *
     * @return <code>true</code> if the dictionary is empty, or <code>false</code> if not
     */
    public boolean isEmpty() {
        return root.isEmpty();
    }


    /**
     * Adds the specified word to the dictionary
     *
     * @param word The word to be added
     * @throw IllegalArgumentException When trying to add a word with more than 7 letters
     */
    public void addProduct(String word) {

        if (word.length() > 128) {
            throw new IllegalArgumentException("Product must have a mixumum length of 128 letters");
        }

        char [] aux = word.toUpperCase().toCharArray();
        addProduct(aux, 0, root);
    }

    private void addProduct(char[] product, int i, Map<Character, LetterNode> nodes) {

        if (i == product.length) {
            if(nodes.get('#') == null) { // This means that the word was already added
                nodes.put('#', new LetterNode('#'));
            }
            return;
        }

        LetterNode next = nodes.get(product[i]);
        if(next == null) {
            next = new LetterNode(product[i]);
            nodes.put(product[i], next);
        }
        addProduct(product, i + 1, next.next);
    }

    /**
     * Evaluates if the specified word is contained in the dictionary
     *
     * @param word The word to be evaluated
     * @return <code>true</code> if the dictionary contained the word, or <code>false</code> if not
     */
    public boolean hasProduct(String word) {

        char [] aux = word.toUpperCase().toCharArray();
        return hasWord(aux, 0, root);
    }

    private boolean hasWord(char[] word, int i, Map<Character, LetterNode> nodes) {

        if (i == word.length) {
            return nodes.containsKey('#');
        }

        LetterNode aux = nodes.get(word[i]);
        return aux != null && hasWord(word, i + 1, aux.next);
    }



    /**
     * Returns a set of words that satisfy the letters positions conditions. It also contains those words that are shorter than the i-th position
     * but satisfied all the 0, 1, ... , i - 1 conditions.
     * <P>For example, if your dictionary has the words
     * "HELADO", "HORA", "HORARIO", "HOLA", "ARBOL", "AVION", "HOY", "HORAS" and you want words that has in their first position an 'H',
     * in the fourth position an 'A', and in the fifth position a 'D', this method returns a set with the words("HORA", "HOLA", "HELADO", "HOY")</p>
     *
     * @param input The string that the words must start with
     * @return A Set of words that satisfy the conditions
     */
    public Set<String> giveMeProducts(char[] input) {

        ConditionsQueue queue = new ConditionsQueue();

        for(int i = 0; i < input.length; i++){
            try {
                queue.enqueueCondition(new WordCondition(i,input[i])); // Exception is thrown in case there are two conditions for the same position
            }
            catch(AlreadyInCollectionException e) {
                //Skip this
            }
        }

        Set<String> result = new HashSet<>();
        giveMeWords(queue, result, 0, new char[128], root); // Seven is the maximum word length
        return result;
    }



    private void giveMeWords(ConditionsQueue queue, Set<String> results, int currentPosition,
                             char[] word, Map<Character, LetterNode> nodes) {

        WordCondition currentCondition = queue.peekCondition();

        if (currentCondition == null || currentPosition != currentCondition.getPosition()) {

            // If currentCondition is null, there are no more conditions to be satisfied, so I must continue traveling through the trie searching
            // for words. If current condition is not null, but there is no condition for the current position, I also have to travel through
            // the trie, after I get a condition for the current position

            for (Map.Entry<Character, LetterNode> each : nodes.entrySet()) {
                char auxLetter = each.getKey();
                if (auxLetter != '#') {
                    // The possible words continue, so I must check if the next letters of the possible words
                    // satisfies the conditions
                    word[currentPosition] = auxLetter;
                    giveMeWords(queue, results, currentPosition + 1, word, each.getValue().next);
                } else {
                    // If a found an existing word, I must add it to the set even though it doesn't satisfy. It can be used
                    // in a possible move
                    String addingWord = String.valueOf(word, 0, currentPosition);
                    results.add(addingWord);

                }
            }
            return;
        }
        // If a get here, it means that there was a condition for the current position
        // If there is a word that finishes before this condition, it must be added

        if (nodes.get('#') != null) {
            String addingWord = String.valueOf(word, 0, currentPosition);
            results.add(addingWord);
        }

        LetterNode aux = nodes.get(currentCondition.getLetter());
        if (aux == null) {
            return; // There is no word that satisfies the current letter condition
        }
        word[currentPosition] = aux.elem;
        currentCondition = queue.dequeueCondition();
        giveMeWords(queue, results, currentPosition + 1, word, aux.next);
        queue.returnConditionToTheQueue(currentCondition);




    }
    public class WordCondition {

        private int position;
        private char letter;

        public WordCondition(int position, char letter) {
            this.position = position;
            this.letter = letter;
        }

        public int getPosition() {
            return position;
        }

        public char getLetter() {
            return letter;
        }


    }

    public static class ConditionsQueue {


        Node first;
        Node last;

        public ConditionsQueue() {
            first = null;
            last = null;
        }

        /**
         * Evaluates if the queue is empty
         *
         * @return <code>true</code> if the queue is empty, or <code>false</code> if not
         */
        public boolean isEmpty() {
            return first == null;
        }

        /**
         * Enqueues the specified condition
         *
         * @param condition The condition to be enqueued
         * @throw AllreadyInCollectionException When adding a condition to an already contained position
         */
        public void enqueueCondition(WordCondition condition) throws AlreadyInCollectionException {

            Node current = first;
            Node prev = null;

            while (current != null && current.condition.getPosition() < condition.getPosition()) {
                prev = current;
                current = current.next;
            }

            if (current != null && current.condition.getPosition() == condition.getPosition()) {
                throw new AlreadyInCollectionException("You must specify only one letter per position");
            }

            Node aux = new Node(condition, current);

            if (prev == null) {
                first = aux;
            } else {
                prev.next = aux;
            }
        }


        public void returnConditionToTheQueue(WordCondition condition) {

            first = new Node(condition, first);
            if (last == null) {
                last = first;
            }

        }


        /**
         * Dequeues the first condition in the queue
         *
         * @return The first condition in the queue, or <code>null</code> if the queue was empty
         */
        public WordCondition dequeueCondition() {

            if (first == null) {
                return null;
            }

            WordCondition aux = first.condition;
            first = first.next;
            if (first == null) {
                last = null; // If after moving forward in the queue there is no more elements, there is no more last one
            }
            return aux;
        }


        public WordCondition peekCondition() {

            if (first == null) {
                return null;
            }
            return first.condition;
        }


        private static class Node {

            WordCondition condition;
            Node next;

            public Node(WordCondition condition, Node next) {
                this.condition = condition;
                this.next = next;
            }
        }

    }


        private static class LetterNode {
        char elem;
        Map<Character, LetterNode> next;

        public LetterNode(char c) {
            elem = c;
            next = (c == '#') ? null : new HashMap<Character, LetterNode>(); // Avoids creating a list in a finishing word node
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            LetterNode other = (LetterNode) obj;
            return (other.elem == this.elem); // Equality of nodes is defined by its element
        }
    }
}