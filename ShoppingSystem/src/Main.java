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
    private void clearScanner(){
        if(reader.nextLine()!=null){
            return;
        }
    }
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
                print(controller.printList(controller.getProducts()));
                break;
            case "4":
                print(searchProduct());
                break;
            case "5":
                print(deleteProduct());
                controller.saveProductData();
                break;
            case "0":
                System.exit(0);
            default:
                print("\n Invalid Option");
        }
        clearScanner();
        executeProgram();
    }
    private String registerProduct(){
        try{
            print("\n Enter : NAME::PRICE::STOCK");
            reader.nextLine();
            String inf = reader.nextLine();
            String[] info = inf.split("::");
            if(!controller.validateAvailableName(info[0])){
                return "Product has already been registered";
            }
            print("\n Enter Product Category :"+categoryMenu());
            ProductCategory category = validateCategory();
            print("\n Enter Description :");
            reader.nextLine();
            String description = reader.nextLine();
            if(category == null){
                return "\n ERROR : Invalid Category Value";
            }
            controller.addProduct(new Product(info[0],Double.parseDouble(info[1]),Integer.parseInt(info[2]),category,description));
            return "\n Product Registered Successfully";
        }catch(NumberFormatException ex){
            return "\n ERROR : INVALID INPUT VALUE\n\n Format example:\n" +
                    "Product::5::20";
        }catch(IndexOutOfBoundsException ex){
            return "\n ERROR : NOT ENOUGH INPUT VALUES\n\n Format example:\n" +
                    "Product::5::12";
        }
    }
    private String deleteProduct(){
        print("\n Enter NAME :");
        String name = reader.next();
        if(controller.validateAvailableName(name)){
            return "\n Product Not Found";
        }
        controller.deleteProduct(name);
        return "\n Product Deleted Succesfuly";
    }
    private String searchProduct(){
        print("\n Search Product by : "+searchMenu());
        switch(reader.next()){
            case "1":
                print("\n Enter product NAME :");
                return controller.searchProductByName(reader.next());
            case "2":
                return searchProductByPrice();
            case "3":
                print("\n Enter product CATEGORY : "+categoryMenu());
                ProductCategory category = validateCategory();
                if(category == null){return "\n Invalid Option";}
                return controller.searchProductByCategory(category);
            case "4":
                return searchProductByStock();
            case "5":
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
    private String searchProductByPrice(){
        print("\n Enter price : \n\nfor ranged search -> (Inferior Limit)::(Superior Limit)");
        String[] input = reader.next().split("::");
        double[] limits = new double[2];
        try{
            if(input.length==1){
                limits[0] = Double.parseDouble(input[0]);
                limits[1] = limits[0];
            }else{
                limits[0] = Double.parseDouble(input[0]);
                limits[1] = Double.parseDouble(input[1]);
            }

            return controller.searchProductByPrice(limits[0],limits[1]);
        }catch(NumberFormatException ex){
            return "\n Invalid Price Value\n\nFormat Example:\n 2::10";
        }
    }
    private String searchProductByStock(){
        print("\n Enter Stock : \n\nfor ranged search -> (Inferior Limit)::(Superior Limit)");
        String[] input = reader.next().split("::");
        int[] limits = new int[2];
        try{
            if(input.length==1){
                limits[0] = Integer.parseInt(input[0]);
                limits[1] = limits[0];
            }else{
                limits[0] = Integer.parseInt(input[0]);
                limits[1] = Integer.parseInt(input[1]);
            }

            return controller.searchProductByStock(limits[0],limits[1]);
        }catch(NumberFormatException ex){
            return "\n Invalid Stock Value\n\nFormat Example:\n 2::10";
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
                "(4) Available Stock\n"+
                "(5) Sells";
    }
    private String mainMenu(){
        return  "\n OPTIONS : \n"+
                "(1) Register Product \n"+
                "(2) Register Order \n"+
                "(3) List All Products\n"+
                "(4) Search Product\n"+
                "(5) Delete Product\n"+
                "(0) Exit Program";
    }
}