package model;
import exceptions.*;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class Controller {
    private File productData;
    private File orderData;
    private ArrayList<Product> products;
    private ArrayList<Order> orders;
    public Controller(){
        products = new ArrayList<>();
        orders = new ArrayList<>();
        createDataFile();
        loadProductData();
        loadOrderData();
    }
    public Product[] getProducts(){
        Product[] list = new Product[products.size()];
        list = products.toArray(list);
        return list;
    }
    public Order[] getOrders(){
        Order[] list = new Order[orders.size()];
        list = orders.toArray(list);
        return list;
    }
    public void saveProductData() throws IOException{
        FileOutputStream fos = new FileOutputStream(productData);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

        Gson gson = new Gson();
        String data = gson.toJson(products);

        writer.write(data);
        writer.flush();
        fos.close();

    }
    public void saveOrderData() throws IOException{
        FileOutputStream fos = new FileOutputStream(orderData);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

        Gson gson = new Gson();
        String data = gson.toJson(orders);

        writer.write(data);
        writer.flush();
        fos.close();
    }
    private void createDataFile(){
        File path = new File(System.getProperty("user.dir")+"/data");
        if(!path.exists()){
            path.mkdir();
        }
        productData = new File(path.getPath(),"productData.txt");
        orderData = new File(path.getPath(),"orderData.txt");
    }
    public void loadProductData(){
        try{
            FileInputStream fis = new FileInputStream(productData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            StringBuilder content = new StringBuilder();
            while((line = reader.readLine())!=null){
                content.append(line);
            }
            Gson gson = new Gson();
            Product[] list = gson.fromJson(content.toString(),Product[].class);
            fis.close();
            for(Product p:list){
                products.add(p);
            }
        }catch(Exception ex){}
    }
    public void loadOrderData(){
        try{
            FileInputStream fis = new FileInputStream(orderData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            StringBuilder content = new StringBuilder();
            while((line = reader.readLine())!=null){
                content.append(line);
            }
            Gson gson = new Gson();
            Order[] list = gson.fromJson(content.toString(),Order[].class);
            fis.close();
            for(Order p:list){
                orders.add(p);
            }
        }catch(Exception ex){}
    }
    public boolean validateAvailableName(String name){
        Product[] list = new Product[products.size()];
        list = products.toArray(list);
        Arrays.sort(list,(a, b) -> {
            return a.getName().compareTo(b.getName());
        });
        if(binarySearchName(name, list)==-1){
            return true;
        }else{
            return false;
        }
    }
    public void setProductSellsAndStock(Product product){
        product.setSells(1);
        product.setStock(1);
    }
    public String searchProductByName(String name){
        StringBuilder msj = new StringBuilder();
        Product[] list = new Product[products.size()];
        list = products.toArray(list);
        msj.append("\n NAME | PRICE | STOCK | CATEGORY |\n");
        Arrays.sort(list,(a, b) -> {
            int criteria = a.getName().compareTo(b.getName());
            return criteria;
        });
        try{
            int pos = binarySearchName(name,list);
            msj.append("\n"+list[pos].getName()+" : $"+list[pos].getPrice()+" : "+list[pos].getStock()+" : "+list[pos].getCategory()+" : "+list[pos].getSells());
            return msj.toString();
        }catch(IndexOutOfBoundsException ex){
            return "Product Not Found";
        }

    }
    public int binarySearchName(String name, Product[] array){
        int begin = 0;
        int end = array.length-1;
        while(begin <= end){
            int half = (int)Math.floor((double)(begin+end)/2);
            if(array[half].getName().compareToIgnoreCase(name)==0){
                return half;
            }else if(array[half].getName().compareToIgnoreCase(name)<0){
                begin = half+1;
            }else{
                end = half-1;
            }
        }
        return -1;
    }
    public String searchProductByPrice(double iLimit,double sLimit){
        if(iLimit>sLimit){
            return "\n Invalid input order";
        }
        Product[] list = getProducts();
        Arrays.sort(list,(a,b) ->{
            if((a.getPrice()-b.getPrice())>0){
                return 1;
            }else if((a.getPrice()-b.getPrice())<0){
                return -1;
            }else{
                return 0;
            }
        });
        int infPos=-1;
        int supPos=-1;
        if(iLimit<list[0].getPrice()){
            infPos = 0;
        }else{
            infPos = binarySearchInfPrice(iLimit,list);
        }
        if(sLimit>list[list.length-1].getPrice()){
            supPos = list.length-1;
        }else{
            supPos = binarySearchSupPrice(sLimit,list);
        }
        if((infPos==-1 || supPos==-1) || (infPos>supPos)){
            return "No Product Found...";
        }
        list = subString(list,infPos,supPos);
        return printList(list);
    }
    private int binarySearchInfPrice(double price, Product[] array){
        int begin = 0;
        int end = array.length-1;
        if(price>array[array.length-1].getPrice()){
            return -1;
        }
        while(begin <= end){
            int half = (int)Math.floor((double)(begin+end)/2);
            if((array[half].getPrice() - price)==0){
                if(half==0 || (array[half-1].getPrice()!=array[half].getPrice())){
                    return half;
                }else if(array[half-1].getPrice()==array[half].getPrice()){
                    end = half-1;
                }
            }else if((array[half].getPrice() - price)<0){
                if(array[half+1].getPrice()>price){
                    return half+1;
                }else{
                    begin = half+1;
                }
            }else if((array[half].getPrice() - price)>0){
                if(array[half-1].getPrice()<price){
                    return half;
                }else{
                    end = half-1;
                }
            }
        }
        return -2;
    }
    private int binarySearchSupPrice(double price, Product[] array){
        int begin = 0;
        int end = array.length-1;
        if(price<array[0].getPrice()){
            return -1;
        }
        while(begin <= end) {
            int half = (int)Math.floor((double)(begin+end)/2);
            if((array[half].getPrice() - price)==0){
                if(half==(array.length-1) || (array[half+1].getPrice()!=array[half].getPrice())){
                    return half;
                }else if(array[half+1].getPrice()==array[half].getPrice()){
                    begin = half+1;
                }
            }else if((array[half].getPrice() - price)<0){
                if(array[half+1].getPrice()>price){
                    return half;
                }else{
                    begin = half+1;
                }
            }else if((array[half].getPrice() - price)>0){
                if(array[half-1].getPrice()<price){
                    return half-1;
                }else{
                    end = half-1;
                }
            }
        }
        return -2;
    }
    public String searchProductByStock(int iLimit,int sLimit){
        if(iLimit>sLimit){
            return "\n Invalid input order";
        }
        Product[] list = getProducts();
        Arrays.sort(list,(a,b) ->{
            if((a.getStock()-b.getStock())>0){
                return 1;
            }else if((a.getStock()-b.getStock())<0){
                return -1;
            }else{
                return 0;
            }
        });
        int infPos=-1;
        int supPos=-1;
        if(iLimit<list[0].getStock()){
            infPos = 0;
        }else{
            infPos = binarySearchInfStock(iLimit,list);
        }
        if(sLimit>list[list.length-1].getStock()){
            supPos = list.length-1;
        }else{
            supPos = binarySearchSupStock(sLimit,list);
        }
        if((infPos==-1 || supPos==-1) || (infPos>supPos)){
            return "\n No products Found...";
        }
        list = subString(list,infPos,supPos);
        return printList(list);
    }
    private int binarySearchInfStock(int stock, Product[] array){
        int begin = 0;
        int end = array.length-1;
        if(stock>array[array.length-1].getStock()){
            return -1;
        }
        while(begin <= end){
            int half = (int)Math.floor((double)(begin+end)/2);
            if((array[half].getStock() - stock)==0){
                if(half==0 || (array[half-1].getStock()!=array[half].getStock())){
                    return half;
                }else if(array[half-1].getStock()==array[half].getStock()){
                    end = half-1;
                }
            }else if((array[half].getStock() - stock)<0){
                if(array[half+1].getStock()>stock){
                    return half+1;
                }else{
                    begin = half+1;
                }
            }else if((array[half].getStock() - stock)>0){
                if(array[half-1].getStock()<stock){
                    return half;
                }else{
                    end = half-1;
                }
            }
        }
        return -2;
    }
    private int binarySearchSupStock(int stock, Product[] array){
        int begin = 0;
        int end = array.length-1;
        if(stock<array[0].getStock()){
            return -1;
        }
        while(begin <= end) {
            int half = (int)Math.floor((double)(begin+end)/2);
            if((array[half].getStock() - stock)==0){
                if(half==(array.length-1) || (array[half+1].getStock()!=array[half].getStock())){
                    return half;
                }else if(array[half+1].getStock()==array[half].getStock()){
                    begin = half+1;
                }
            }else if((array[half].getStock() - stock)<0){
                if(array[half+1].getStock()>stock){
                    return half;
                }else{
                    begin = half+1;
                }
            }else if((array[half].getStock() - stock)>0){
                if(array[half-1].getStock()<stock){
                    return half-1;
                }else{
                    end = half-1;
                }
            }
        }
        return -2;
    }
    public String searchProductByCategory(ProductCategory category){
        StringBuilder msj = new StringBuilder();
        for(Product p:products){
            if(p.getCategory().equals(category)){
                msj.append("\n"+p.getName()+" : $"+p.getPrice()+" : "+p.getStock()+" : "+p.getCategory()+" : "+p.getSells());
            }
        }
        return msj.toString();
    }
    public String searchProductBySells(int iLimit,int sLimit){
        if(iLimit>sLimit){
            return "\n Invalid input order";
        }
        Product[] list = getProducts();
        Arrays.sort(list,(a,b) ->{
            if((a.getSells()-b.getSells())>0){
                return 1;
            }else if((a.getSells()-b.getSells())<0){
                return -1;
            }else{
                return 0;
            }
        });
        int infPos=-1;
        int supPos=-1;
        if(iLimit<list[0].getSells()){
            infPos = 0;
        }else{
            infPos = binarySearchInfSells(iLimit,list);
        }
        if(sLimit>list[list.length-1].getSells()){
            supPos = list.length-1;
        }else{
            supPos = binarySearchSupSells(sLimit,list);
        }
        if((infPos==-1 || supPos==-1) || (infPos>supPos)){
            return "\n No products Found...";
        }
        list = subString(list,infPos,supPos);
        return printList(list);
    }
    private int binarySearchInfSells(int sells, Product[] array){
        int begin = 0;
        int end = array.length-1;
        if(sells>array[array.length-1].getSells()){
            return -1;
        }
        while(begin <= end){
            int half = (int)Math.floor((double)(begin+end)/2);
            if((array[half].getSells() - sells)==0){
                if(half==0 || (array[half-1].getSells()!=array[half].getSells())){
                    return half;
                }else if(array[half-1].getSells()==array[half].getSells()){
                    end = half-1;
                }
            }else if((array[half].getSells() - sells)<0){
                if(array[half+1].getSells()>sells){
                    return half+1;
                }else{
                    begin = half+1;
                }
            }else if((array[half].getSells() - sells)>0){
                if(array[half-1].getSells()<sells){
                    return half;
                }else{
                    end = half-1;
                }
            }
        }
        return -2;
    }
    private int binarySearchSupSells(int sells, Product[] array){
        int begin = 0;
        int end = array.length-1;
        if(sells<array[0].getSells()){
            return -1;
        }
        while(begin <= end) {
            int half = (int)Math.floor((double)(begin+end)/2);
            if((array[half].getSells() - sells)==0){
                if(half==(array.length-1) || (array[half+1].getSells()!=array[half].getSells())){
                    return half;
                }else if(array[half+1].getSells()==array[half].getSells()){
                    begin = half+1;
                }
            }else if((array[half].getSells() - sells)<0){
                if(array[half+1].getSells()>sells){
                    return half;
                }else{
                    begin = half+1;
                }
            }else if((array[half].getSells() - sells)>0){
                if(array[half-1].getSells()<sells){
                    return half-1;
                }else{
                    end = half-1;
                }
            }
        }
        return -2;
    }
    public String printList(Product[] list){
        StringBuilder msj = new StringBuilder();
        int[] sep = calculateSeparations(list);
        String[] inf = new String[5];
        msj.append("\n|   NAME   |  PRICE  |  STOCK  |  CATEGORY  |  Sells  |\n");
        for(Product p : list){
            inf = addSeparation(p,sep);
            msj.append("\n"+inf[0]+" : $"+inf[1]+" : "+inf[2]+" : "+inf[3]+" : "+inf[4]);
        }
        msj.append("\n\n FOUND "+list.length+" ITEMS");
        return msj.toString();
    }
    private int[] calculateSeparations(Product[] list){
        int[] sep = new int[5];
        int maxName = 0;
        int maxPrice = 0;
        int maxStock = 0;
        int maxSells = 0;
        int maxCategory = 0;
        for(Product p:list){
            int length = p.getName().split("").length;
            if(maxName<length){maxName = length;}
            String value = ""+p.getPrice();
            length = value.split("").length;
            if(maxPrice<length){maxPrice = length;}
            value = ""+p.getStock();
            length = value.split("").length;
            if(maxStock<length){maxStock = length;}
            value = ""+p.getSells();
            length = value.split("").length;
            if(maxSells<length){maxSells = length;}
            value = ""+p.getCategory();
            length = value.split("").length;
            if(maxCategory<length){maxCategory = length;}
        }
        sep[0] = maxName;
        sep[1] = maxPrice;
        sep[2] = maxStock;
        sep[4] = maxSells;
        sep[3] = maxCategory;
        return sep;
    }
    private String[] addSeparation(Product p, int[] sep){
        String[] inf = new String[5];
        inf[0] = p.getName();
        inf[1] = ""+p.getPrice();
        inf[2] = ""+p.getStock();
        inf[3] = ""+p.getCategory();
        inf[4] = ""+p.getSells();
        int len = inf[0].split("").length;
        int separation = sep[0]-len;
        for(int i=0;i<separation;i++){
            inf[0] += " ";
        }
        len = inf[1].split("").length;
        separation = sep[1] -len;
        for(int i=0;i<separation;i++){
            inf[1] += " ";
        }
        len = inf[2].split("").length;
        separation = sep[2] -len;
        for(int i=0;i<separation;i++){
            inf[2] += " ";
        }
        len = inf[3].split("").length;
        separation = sep[3] -len;
        for(int i=0;i<separation;i++){
            inf[3] += " ";
        }
        len = inf[4].split("").length;
        separation = sep[4] -len;
        for(int i=0;i<separation;i++){
            inf[4] += " ";
        }
        return inf;
    }
    private Product[] subString(Product[] list,int inf,int sup){
        Product[] array = new Product[(sup-inf)+1];
        int cont = 0;
        for(int i=inf;i<=sup;i++){
            array[cont] = list[i];
            cont++;
        }
        return array;
    }
    public void addProduct(Product product){
        products.add(product);
    }
    public void deleteProduct(String name){
        for(int i=0;i<products.size();i++){
            if((products.get(i).getName().compareToIgnoreCase(name))==0){
                products.remove(i);
            }
        }
    }
    public void addOrder(Order order){
        orders.add(order);
    }
 }
