package hci.tiendapp.backend;

import com.google.gson.annotations.Expose;

/**
 * Created by Julian on 11/18/2015.
 */
public class SubCategory {

    @Expose
    private int id;
    @Expose
    private String name;

    @Expose
    private Category category;

    public SubCategory(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
