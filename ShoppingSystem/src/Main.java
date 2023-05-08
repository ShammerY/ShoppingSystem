import exceptions.NotFoundException;
import model.*;

import java.io.IOException;
import java.util.*;

public class Main {
    private Scanner reader;
    private Controller controller;
    public Main(){
        reader = new Scanner(System.in);
        controller = new Controller();
    }
    public static void main(String[] args) throws IOException, NotFoundException {
        Main main = new Main();
        main.executeProgram();
    }
    private void print(Object t){System.out.println(t);}
    public void executeProgram() throws IOException, NotFoundException {
        print(mainMenu());
        switch(reader.next()){
            case "1":
                print(registerProduct());
                controller.saveProductData();
                break;
            case "2":
                registerOrder();
                break;
            case "3":
                print(searchProduct());
                break;
            case "4":
                print(searchOrder());
                break;
            case "5":
                print(replenishStock());
                controller.saveProductData();
                break;
            case "6":
                print(deleteProduct());
                controller.saveProductData();
                break;
            case "0":
                System.exit(0);
            default:
                print("\n Invalid Option");
        }
        //clearScanner();
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
                    "ProductA::5::20";
        }catch(IndexOutOfBoundsException ex){
            return "\n ERROR : NOT ENOUGH INPUT VALUES\n\n Format example:\n" +
                    "ProductA::5::12";
        }
    }
    private void registerOrder() throws IOException, NotFoundException {
        print("\n Enter Customer Name: ");
        reader.nextLine();
        String customerName = reader.nextLine();
        Product[] list = controller.getProducts();
        Arrays.sort(list,(a,b)->{
            return a.getName().compareTo(b.getName());
        });
        ArrayList<Product> customerList = new ArrayList<>();
        String option;
        do {
            print(registerOrderMenu());
            option = reader.next();
            switch(option){
                case "1":
                    customerList = addProductToList(list,customerList);
                    break;
                case "2":
                    print(searchProduct());
                    break;
                case "3":
                    try{
                        controller.addOrder(new Order(customerName,customerList,new Date()));
                        controller.saveOrderData();
                        controller.saveProductData();
                    }catch(NullPointerException ex){
                        ex.printStackTrace();
                        print("\n Order Discarted");
                    }
                    print("Order Registered");
                    break;
                default:
                    print("\n Invalid Option");
            }
        }while(!option.equals("3"));
    }
    private ArrayList<Product> addProductToList(Product[] list, ArrayList<Product> customerList){
        print("Enter Product Name");
        int pos = controller.binarySearchName(reader.next(),list);
        if(pos==-1){
            print("\nProduct Not Found");
            return customerList;
        }else if(list[pos].getStock()<1){
            print("\nProduct is out of stock");
            return customerList;
        }
        customerList.add(list[pos]);
        controller.setProductSellsAndStock(list[pos]);
        print("\n product Added to order");
        return customerList;
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
    private String replenishStock(){
        print("Enter product Name");
        String name = reader.next();
        Product[] products = controller.getProducts();
        Arrays.sort(products,(a,b) ->{
           return a.getName().compareTo(b.getName());
        });
        int pos = controller.binarySearchName(name,products);
        if(pos==-1){
            return "\n Product Not Found ";
        }
        print("\n Enter Stock amount to add :");
        try{
            int cant = reader.nextInt();
            if(cant <1){
                return "\n Invalid amount : Must be greater than 0";
            }
            controller.addProductStock(products[pos],reader.nextInt());
        }catch(InputMismatchException ex){
            return "\n Invalid Amount: must be a round number";
        }
        return "\n Product replenished successfully";
    }
    private String searchProduct(){
        print("\n Search Product by : "+searchProductMenu());
        Product[] list;
        switch(reader.next()){
            case "1":
                list = controller.getProducts();
                break;
            case "2":
                print("\n Enter product NAME :");
                list = controller.searchProductByName(reader.next());
                break;
            case "3":
                list = searchProductByPrice();
                break;
            case "4":
                print("\n Enter product CATEGORY : "+categoryMenu());
                ProductCategory category = validateCategory();
                if(category == null){return "\n Invalid Option";}
                list = controller.searchProductByCategory(category);
                break;
            case "5":
                list = searchProductByStock();
                break;
            case "6":
                list = searchProductBySells();
                break;
            default:
                return "\n Invalid option";
        }
        if(list == null){
            return "\n No Products Found";
        }
        return sortList(list);
    }
    private String sortList(Product[] list){
        print("\n Sort List by :"+sortVariableMenu());
        String value = reader.next();
        print("\n (1) Acendent\n (2) Descendent");
        String ascend = reader.next();
        if(!ascend.equals("1") && !ascend.equals("2")){
            return "\n Invalid ascending option";
        }
        switch(value){
            case "1":
                if(ascend.equals("1")){
                    Arrays.sort(list,(a,b) ->{
                        return a.getName().compareTo(b.getName());
                    });
                }else{
                    Arrays.sort(list,(a,b) ->{
                        return (a.getName().compareTo(b.getName()))*-1;
                    });
                }
                break;
            case "2":
                if(ascend.equals("1")){
                    Arrays.sort(list,(a,b) ->{
                        if((a.getPrice()-b.getPrice())>0){
                            return 1;
                        }else if((a.getPrice()-b.getPrice())<0){
                            return -1;
                        }else{
                            return 0;
                        }
                    });
                }else{
                    Arrays.sort(list,(a,b) ->{
                        if((a.getPrice()-b.getPrice())>0){
                            return -1;
                        }else if((a.getPrice()-b.getPrice())<0){
                            return 1;
                        }else{
                            return 0;
                        }
                    });
                }
                break;
            case "3":
                if(ascend.equals("1")){
                    Arrays.sort(list,(a,b)->{
                       return a.getStock() - b.getStock();
                    });
                }else{
                    Arrays.sort(list,(a,b)->{
                        return (a.getStock() - b.getStock())*-1;
                    });
                }
                break;
            case "4":
                if(ascend.equals("1")){
                    Arrays.sort(list,(a,b)->{
                        return a.getSells() - b.getSells();
                    });
                }else{
                    Arrays.sort(list,(a,b)->{
                        return (a.getSells() - b.getSells())*-1;
                    });
                }
                break;
            default:
                return "\n Invalid Value option";

        }
        return controller.printList(list);
    }
    private Product[] searchProductByPrice(){
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
            print("\n Invalid Price Value\n\nFormat Example:\n 2::10");
            return null;
        }
    }
    private Product[] searchProductByStock(){
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
            print("\n Invalid Stock Value\n\nFormat Example:\n 2::10");
            return null;
        }
    }
    private Product[] searchProductBySells(){
        print("\n Enter Sells : \n\nfor ranged search -> (Inferior Limit)::(Superior Limit)");
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

            return controller.searchProductBySells(limits[0],limits[1]);
        }catch(NumberFormatException ex){
            print("\n Invalid Sells Value\n\nFormat Example:\n 2::10");
            return null;
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
    private String searchOrder(){
        print("\n Search Order by: "+searchOrderMenu());
        Order[] list;
        switch(reader.next()){
            case "1":
                return controller.printOrder(controller.getOrders());
            case "2":
                list = searchOrderByName();
                break;
            case "3":
                list = searchOrderByValue();
                break;
            case "4":
                list = searchOrderByDate();
                break;
            default:
                return "\n Invalid Option";
        }
        if(list==null){
            return "\n No Order Found with this Specification";
        }
        return controller.printOrder(list);
    }
    private Order[] searchOrderByName(){
        print("\n Enter Customer Name");
        String name = reader.next();
        return controller.getOrderByName(name);
    }
    private Order[] searchOrderByValue(){
        print("\n Enter Order Total Price:");
        try{
            double price = reader.nextDouble();
            Order[] order = controller.searchOrderByPrice(price);
            return order;
        }catch(InputMismatchException ex){
            print("\n Invalid price Value");
            return null;
        }
    }
    private Order[] searchOrderByDate(){
        print("\n Enter Date :  day/month/year");
        String date = reader.next();
        return controller.getOrdersByDate(date);
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
    private String searchProductMenu(){
        return  "\n(1) List all products\n"+
                "(2) Name\n"+
                "(3) Price\n"+
                "(4) Category\n"+
                "(5) Available Stock\n"+
                "(6) Sells";
    }
    private String sortVariableMenu(){
        return  "\n(1) Alphabet\n"+
                "(2) Price\n"+
                "(3) Available Stock\n"+
                "(4) Sells";
    }
    private String searchOrderMenu(){
        return  "\n(1) list all orders\n"+
                "(2) Customer name\n"+
                "(3) Total price\n"+
                "(4) Registration Date";

    }
    private String registerOrderMenu(){
        return  "\n ORDER MENU :"+
                "\n(1) Add Product"+
                "\n(2) Search Products"+
                "\n(3) Save Order";
    }
    private String mainMenu(){
        return  "\n OPTIONS : \n"+
                "(1) Register Product \n"+
                "(2) Register Order \n"+
                "(3) Search Product\n"+
                "(4) Search Order\n"+
                "(5) Replenish product Stock\n"+
                "(6) Delete Product\n"+
                "(0) Exit Program";
    }
}