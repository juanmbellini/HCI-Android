package hci.tiendapp.backend;

import junit.framework.TestCase;

/**
 * Created by Julian on 11/17/2015.
 */
public class TrieTest extends TestCase {


    Trie trie = new Trie();


    public void testAddProduct() throws Exception {


    }

    public void testHasProduct() throws Exception {

        trie.addProduct("alpargata alpargata arpalgarta");
        trie.addProduct("jorge");
        trie.addProduct("odio");
        trie.addProduct("hci");
        trie.addProduct("abuante");
        trie.addProduct("android");
        trie.addProduct("vieja");
        trie.addProduct("no");
        trie.addProduct("me");
        trie.addProduct("importa");
        trie.addProduct("nada");

        assertTrue(trie.hasProduct("alpargata alpargata arpalgarta"));
        assertTrue(trie.hasProduct("vieja"));
        assertFalse(trie.hasProduct("maquinola"));
        assertFalse(trie.hasProduct("n"));
    }

    public void testGiveMeProducts() throws Exception {

    }
}