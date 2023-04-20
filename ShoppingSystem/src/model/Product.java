package model;

public class Product implements Comparable<Product>{

    private String name;
    private double price;
    private String description;
    private int stock;
    private ProductCategory category;
    private int sells;

    public Product(String name, double price,int stock, ProductCategory category ,String description) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.category = category;
        this.sells = 0;
    }
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public int getSells() {
        return sells;
    }

    public void setStock(int cant) {
        stock = stock - cant;
    }

    public void setSells(int cant) {
        sells = sells + cant;
    }

    @Override
    public int compareTo(Product p){
        return this.name.compareTo(p.getName());
    }
}
