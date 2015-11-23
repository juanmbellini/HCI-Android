package hci.tiendapp.backend;

import com.google.gson.annotations.Expose;

/**
 * Created by JuanMarcos on 22/11/15.
 */
public class Filter {

    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String[] values;

    public Filter(int id, String name, String[] values) {
        this.id = id;
        this.name = name;
        this.values = values;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getValues() {
        return values;
    }
}
