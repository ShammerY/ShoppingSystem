import model.*;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
public class Main {
    private Scanner reader;
    private Controller controller;
    public Main(){
        reader = new Scanner(System.in);
        controller = new Controller();
    }
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.executeProgram();
    }
    private void print(Object t){System.out.println(t);}
    public void executeProgram() throws IOException {
        print(mainMenu());
        switch(reader.next()){
            case "1":
                print(registerProduct());
                controller.saveProductData();
                break;
            case "2":
                break;
            case "3":
                print(controller.printList());
                break;
            case "4":
                print(searchProduct());
                break;
            case "0":
                System.exit(0);
            default:
                print("\n Invalid Option");
        }
        executeProgram();
    }
    private String registerProduct(){
        try{
            print("\n Enter : NAME PRICE STOCK");
            reader.nextLine();
            String inf = reader.nextLine();
            String[] info = inf.split(" ");
            if(!controller.validateAvailableName(info[0])){
                return "Product has already been registered";
            }
            print("\n Enter Product Category :"+categoryMenu());
            ProductCategory category = validateCategory();
            print("\n Enter Description :");
            reader.nextLine();
            String description = reader.nextLine();
            if(category == null){
                return "\n ERROR : Invalid Input Value";
            }
            controller.addProduct(new Product(info[0],Double.parseDouble(info[1]),Integer.parseInt(info[2]),category,description));
            return "\n Product Registered Successfully";
        }catch(NumberFormatException ex){
            return "\n ERROR : INVALID INPUT VALUE";
        }catch(IndexOutOfBoundsException ex){
            return "\n ERROR : NOT ENOUGH INPUT VALUES";
        }
    }
    private String searchProduct(){
        print("\n Search Product by : "+searchMenu());
        switch(reader.next()){
            case "1":
                print("\n Enter product NAME :");
                return controller.searchProductByName(reader.next());
            case "2":
                print("\n Enter product Price :");
                try{
                    return controller.searchProductByPrice(reader.nextDouble());
                }catch(InputMismatchException ex){
                    reader.next();
                    return "\n Invalid Price Value";
                }
            case "3":
                print("\n Enter product CATEGORY : "+categoryMenu());
                ProductCategory category = validateCategory();
                if(category == null){return "\n Invalid Option";}
                return controller.searchProductByCategory(category);
            case "4":
                print("\n Enter product SELLS :");
                try{
                    return controller.searchProductBySells(reader.nextInt());
                }catch(InputMismatchException ex){
                    reader.next();
                    return "\n Invalid Sells Value";
                }
            default:
                return "\n Invalid option";

        }
    }


    private ProductCategory validateCategory(){
        ProductCategory category = null;
        switch(reader.next()){
            case "1":
                category = ProductCategory.BOOK;
                break;
            case "2":
                category = ProductCategory.ELECTRONIC;
                break;
            case "3":
                category = ProductCategory.ACCESSORY;
                break;
            case "4":
                category = ProductCategory.FOOD;
                break;
            case "5":
                category = ProductCategory.STATIONERY;
                break;
            case "6":
                category = ProductCategory.SPORT;
                break;
            case "7":
                category = ProductCategory.BEAUTY;
                break;
            case "8":
                category = ProductCategory.GAME;
                break;
        }
        return category;
    }
    private String categoryMenu(){
        return  "\n"+
                "(1) Books\n"+
                "(2) Electronics\n"+
                "(3) Clothes & Accessories\n"+
                "(4) Foods & Beverages\n"+
                "(5) Paper related Things\n"+
                "(6) Sports\n"+
                "(7) Beauty & Self Care\n"+
                "(8) Games & Toys";
    }
    private String searchMenu(){
        return  "\n(1) Name\n"+
                "(2) Price\n"+
                "(3) Category\n"+
                "(4) Sells";
    }
    private String mainMenu(){
        return  "\n OPTIONS : \n"+
                "(1) Register Product \n"+
                "(2) Register Order \n"+
                "(3) List All Products\n"+
                "(4) Search Product\n"+
                "(0) Exit Program";
    }
}