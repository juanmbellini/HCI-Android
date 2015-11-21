package hci.tiendapp.backend;

/**
 * Created by Julian on 11/18/2015.
 */
public class Product {

    private int id;
    private String name;
    private int price;
    private String[] imageUrl;
    private Category category;
    private SubCategory subcategory;
    private Attribute[] attributes;

    public Product(int id, String name, int price, String[] imageUrl, Category category, SubCategory subcategory, Attribute[] attributes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.subcategory = subcategory;
        this.attributes = attributes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String[] getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public SubCategory getSubcategory() {
        return subcategory;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }
}
