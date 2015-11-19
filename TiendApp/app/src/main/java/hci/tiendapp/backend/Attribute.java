package hci.tiendapp.backend;

/**
 * Created by Julian on 11/18/2015.
 */
public class Attribute {
    private int id;
    private String name;
    private String[] values;

    public Attribute(int id, String name, String[] values) {
        this.id = id;
        this.name = name;
        this.values = values;
    }
}
