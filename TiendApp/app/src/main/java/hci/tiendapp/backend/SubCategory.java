package hci.tiendapp.backend;

/**
 * Created by Julian on 11/18/2015.
 */
public class SubCategory {
    private int id;
    private String name;

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
}
