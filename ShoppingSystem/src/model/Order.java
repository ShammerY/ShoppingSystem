package model;
import java.util.ArrayList;
import java.util.Date;

public class Order {
    private String customerName;
    private ArrayList<Product> products;
    private Date date;
    private double value;
    public Order(String name,ArrayList<Product> products,Date date){
        this.customerName = name;
        this.products = products;
        this.value = calculateValue();
        this.date = date;
    }
    public String getCustomerName(){
        return this.customerName;
    }
    public Date getDate(){
        return this.date;
    }
    public void setOrder(ArrayList<Product> list){
        this.products = list;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public double getValue() {
        return value;
    }
    private double calculateValue(){
        double total = 0;
        for(Product p:products){
            total += p.getPrice();
        }
        return total;
    }
}
