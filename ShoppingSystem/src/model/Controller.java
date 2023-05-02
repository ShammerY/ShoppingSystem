package model;
import exceptions.*;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;

public class Controller {
    private File productData;
    private File orderData;
    private ArrayList<Product> products;
    public Controller(){
        products = new ArrayList<>();
        createDataFile();
        loadProductData();
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
    private int binarySearchName(String name, Product[] array){
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
    public String searchProductByPrice(double price){
        StringBuilder msj = new StringBuilder();
        Product[] list = new Product[products.size()];
        list = products.toArray(list);
        Arrays.sort(list,(a,b) ->{
            if((a.getPrice()-b.getPrice())>0){
                return 1;
            }else if((a.getPrice()-b.getPrice())<0){
                return -1;
            }else{
                return 0;
            }
        });
        msj.append("\n NAME | PRICE | STOCK | CATEGORY |\n");
        try{
            int pos = binarySearchPrice(price,list);
            msj.append("\n"+list[pos].getName()+" : $"+list[pos].getPrice()+" : "+list[pos].getStock()+" : "+list[pos].getCategory()+" : "+list[pos].getSells());
        }catch(IndexOutOfBoundsException ex){
            return "Product Not Found";
        }
        return msj.toString();
    }
    private int binarySearchPrice(double price, Product[] array){
        int begin = 0;
        int end = array.length-1;
        while(begin <= end){
            int half = (int)Math.floor((double)(begin+end)/2);
            if((array[half].getPrice() - price)==0){
                return half;
            }else if((array[half].getPrice() - price)<0){
                begin = half+1;
            }else{
                end = half-1;
            }
        }
        return -1;
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
    public String searchProductBySells(int sells){
        StringBuilder msj = new StringBuilder();
        Product[] list = new Product[products.size()];
        list = products.toArray(list);
        Arrays.sort(list,(a,b) -> {
            if((a.getSells()-b.getSells())>0){
                return 1;
            }else if((a.getSells()-b.getSells())<0){
                return -1;
            }else{
                return 0;
            }
        });
        msj.append("NAME | PRICE | STOCK | CATEGORY | Sells |");
        try{
            int pos = binarySearchSells(sells,list);
            msj.append("\n"+list[pos].getName()+" : $"+list[pos].getPrice()+" : "+list[pos].getStock()+" : "+list[pos].getCategory()+" : "+list[pos].getSells());
        }catch(IndexOutOfBoundsException ex){
            return "Product Not Found";
        }
        return msj.toString();
    }
    private int binarySearchSells(int sells,Product[] array){
        int begin = 0;
        int end = array.length-1;
        while(begin <= end){
            int half = (int)Math.floor((double)(begin+end)/2);
            if((array[half].getSells() - sells)==0){
                return half;
            }else if((array[half].getSells() - sells)<0){
                begin = half+1;
            }else{
                end = half-1;
            }
        }
        return -1;
    }
    public String printList(){
        StringBuilder msj = new StringBuilder();
        msj.append("NAME | PRICE | STOCK | CATEGORY | Sells |");
        for(Product p : products){
            msj.append("\n"+p.getName()+" : $"+p.getPrice()+" : "+p.getStock()+" : "+p.getCategory()+" : "+p.getSells());
        }
        return msj.toString();
    }
    public void addProduct(Product product){
        products.add(product);
    }

}
