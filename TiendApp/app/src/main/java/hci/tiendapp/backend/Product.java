package hci.tiendapp.backend;

import com.google.gson.annotations.Expose;

/**
 * Created by Julian on 11/18/2015.
 */
public class Product {

    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private double price;
    @Expose
    private String[] imageUrl;
    @Expose
    private Category category;
    @Expose
    private SubCategory subcategory;
    @Expose
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

    public double getPrice() {
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
