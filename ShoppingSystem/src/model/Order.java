package model;
import java.util.ArrayList;

public class Order {
    private String customerName;
    private Product[] products;
    private String date;
    private double value;
    public Order(String name,Product[] products,String date,double value){
        this.customerName = name;
        this.products = products;
        this.value = value;
        this.date = date;
    }
    public String getCustomerName(){
        return this.customerName;
    }
    public String getDate(){
        return this.date;
    }
    public Product[] getProducts() {
        return products;
    }
    public double getValue() {
        return value;
    }
}
