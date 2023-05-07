package model;
import jdk.jfr.StackTrace;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class ControllerTest {

    public Controller setup1(){
        return new Controller();
    }
    public ArrayList<Product> setup2(){
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("ProductA",5,10,ProductCategory.BOOK,"description"));
        products.add(new Product("ProductB",2,10,ProductCategory.SPORT,"description"));
        products.add(new Product("ProductC",3,10,ProductCategory.BEAUTY,"description"));
        return products;
    }
    public Controller setup3(){
        Controller controller = new Controller();
        ArrayList<Product> products = setup2();
        controller.addProduct(new Product("ProductA",5,10,ProductCategory.BOOK,"description"));
        controller.addProduct(new Product("ProductB",2,10,ProductCategory.SPORT,"description"));
        controller.addProduct(new Product("ProductC",3,10,ProductCategory.BEAUTY,"description"));
        return controller;
    }
    @Test
    public void addProductTest(){
        boolean flag = false;
        Controller controller = setup1();
        Product product = new Product("ProductA",5,10,ProductCategory.BOOK,"description");
        controller.addProduct(product);
        if(controller.getProducts()[0].equals(product)){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void addOrderTest(){
        boolean flag = false;
        Controller controller = setup1();
        ArrayList<Product> products = setup2();
        Order order = new Order("NameA",products,new Date());
        controller.addOrder(order);
        if(controller.getOrders()[0].equals(order)){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void productNameIsAvailableNameTest(){
        boolean flag = false;
        Controller controller = setup3();
        if(controller.validateAvailableName("New Name")){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void productNameIsNotAvailableNameTest(){
        boolean flag = true;
        Controller controller = setup3();
        if(!controller.validateAvailableName("ProductB")){
            flag = false;
        }
        assertFalse(flag);
    }
    @Test
    public void test(){
        String a = "hola";
        Integer b = 33;
        Boolean c = false;
        Product product = new Product("P",1,1,ProductCategory.BOOK,"");
        System.out.println(a.getClass());
        System.out.println(b.getClass());
        System.out.println(c.getClass());
        System.out.println(product.getClass());
    }
}
