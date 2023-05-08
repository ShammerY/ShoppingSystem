package model;
import jdk.jfr.StackTrace;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
        controller.addProduct(new Product("ProductA",5,10,ProductCategory.BOOK,"description"));
        controller.addProduct(new Product("ProductB",2,10,ProductCategory.SPORT,"description"));
        controller.addProduct(new Product("ProductC",3,10,ProductCategory.BEAUTY,"description"));
        return controller;
    }
    public Controller setup4(){
        Controller controller = new Controller();
        controller.addProduct(new Product("ProductA",5,10,ProductCategory.BOOK,"description"));
        controller.addProduct(new Product("ProductB",2,3,ProductCategory.SPORT,"description"));
        controller.addProduct(new Product("AObject",1,8,ProductCategory.BEAUTY,"description"));
        controller.addProduct(new Product("BObject",15,4,ProductCategory.SPORT,"description"));
        controller.addProduct(new Product("ProductC",9,1,ProductCategory.ELECTRONIC,"description"));
        return controller;
    }
    public Controller setup5(){
        Controller controller = new Controller();
        ArrayList<Product> list = setup2();
        controller.addOrder(new Order("OrderA",list,new Date()));
        controller.addOrder(new Order("OrderB",list,new Date()));
        controller.addOrder(new Order("OrderA",list,new Date()));
        controller.addOrder(new Order("OrderD",list,new Date()));

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
    public void setSellsAndStocktest(){
        boolean flag = false;
        Controller controller = setup3();
        Product[] list = controller.getProducts();
        controller.setProductSellsAndStock(list[0]);
        if(list[0].getSells()==1 && list[0].getStock()==9){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void getListByName(){
        boolean flag = false;
        Controller controller = setup4();
        Product[] list = controller.searchProductByName("A");
        if(list[0].getName().equals("ProductA") && list[1].getName().equals("AObject")){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void binarySearchNameTest(){
        boolean flag = false;
        Controller controller = setup4();
        Product[] list = controller.getProducts();
        Arrays.sort(list,(a,b)->{
            return a.getName().compareTo(b.getName());
        });
        int pos = controller.binarySearchName("AObject",list);
        if(list[pos].getName().equals("AObject")){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void searchProductByPriceTest(){
        boolean flag = false;
        Controller controller = setup4();
        Product[] list = controller.searchProductByPrice(2,10);
        if(list[0].getName().equals("ProductB") && list[1].getName().equals("ProductA") && list[2].getName().equals("ProductC")){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void searchProductByStockTest(){
        boolean flag = false;
        Controller controller = setup4();
        Product[] list = controller.searchProductByStock(2,10);
        if(list[0].getName().equals("ProductB") && list[1].getName().equals("BObject") && list[2].getName().equals("AObject")){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void searchProductByCategoryTest(){
        boolean flag = false;
        Controller controller = setup4();
        Product[] list = controller.searchProductByCategory(ProductCategory.SPORT);
        if(list[0].getName().equals("ProductB") && list[1].getName().equals("BObject")){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void searchProductBySellsTest(){
        boolean flag = false;
        Controller controller = setup4();
        controller.getProducts()[0].setSells(3);
        controller.getProducts()[1].setSells(2);
        controller.getProducts()[3].setSells(5);
        Product[] list = controller.searchProductBySells(2,5);
        if(list[0].getName().equals("ProductB") && list[1].getName().equals("ProductA")&& list[2].getName().equals("BObject")){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void searchOrderByNameTest(){
        boolean flag = false;
        Controller controller = setup5();
        Order[] list = controller.getOrderByName("OrderA");
        if(list.length==2){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void searchOrderByPriceTest(){
        boolean flag = false;
        Controller controller = setup5();
        Order[] list = controller.searchOrderByPrice(10);
        if(list[0].getValue()==10){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void addproductStockTest(){
        boolean flag = false;
        Controller controller = setup3();
        Product product = controller.getProducts()[0];
        controller.addProductStock(product,5);
        if(product.getStock()==15){
            flag = true;
        }
        assertTrue(flag);
    }
    @Test
    public void deleteProductTest(){
        boolean flag = false;
        Controller controller = setup4();
        controller.deleteProduct("ProductB");
        if(controller.getProducts().length==4){
            flag = true;
        }
        assertTrue(flag);
    }
}
