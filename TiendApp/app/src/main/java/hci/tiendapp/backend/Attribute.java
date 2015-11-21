package hci.tiendapp.backend;

import com.google.gson.annotations.Expose;

/**
 * Created by Julian on 11/18/2015.
 */
public class Attribute {

    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String[] values;

    public Attribute(int id, String name, String[] values) {
        this.id = id;
        this.name = name;
        this.values = values;
    }
}
