package model;
import exceptions.*;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;

public class Controller {
    private ArrayList<Product> products;
    public Controller(){
        products = new ArrayList<>();
    }
    public void saveData() throws IOException{
        File file = new File("data.json");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

        Gson gson = new Gson();
        String data = gson.toJson(products);

        writer.write(data);
        writer.flush();
        fos.close();

    }
    public String loadData() throws IOException{
        try{
            File file = new File("data.json");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            String content = "";
            while((line = reader.readLine())!=null){
                content += line;
            }
            Gson gson = new Gson();
            Product[] list = gson.fromJson(content,Product[].class);
            fis.close();
            for(int i=0;i<list.length;i++){
                products.add(list[i]);
            }
            return "Data Recovered";
        }catch(FileNotFoundException ex){
            return "";
        }
    }
    public String searchProductByName(String name){
        try{
            StringBuilder msj = new StringBuilder();

            Product[] list = new Product[products.size()];
            for(int i=0;i<products.size();i++){list[i]=products.get(i);}
            Arrays.sort(list);
            System.out.println(printList2(list));
            int pos = binarySearchName(name,list);
            System.out.println(pos);
            msj.append("\n"+list[pos].getName()+" : $"+list[pos].getPrice()+" : "+list[pos].getStock()+" : "+list[pos].getCategory()+" : "+list[pos].getSells());

            return msj.toString();
        }catch(IndexOutOfBoundsException ex){
            return "Not Found";
        }

    }
    private int binarySearchName(String name, Product[] array){
        int begin = 0;
        int end = array.length-1;
        while(begin <= end){
            int half = (int)Math.floor((double)(begin+end)/2);
            if(array[half].getName().compareTo(name)==0){
                return half;
            }else if(array[half].getName().compareTo(name)<0){
                begin = half+1;
            }else{
                end = half-1;
            }
        }
        return -1;
    }
    public String searchProductByPrice(Double price){
        StringBuilder msj = new StringBuilder();
        for(Product p:products){
            if(p.getPrice() == price){
                msj.append("\n"+p.getName()+" : $"+p.getPrice()+" : "+p.getStock()+" : "+p.getCategory()+" : "+p.getSells());
            }
        }
        return msj.toString();
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
        for(Product p:products){
            if(p.getSells() == sells){
                msj.append("\n"+p.getName()+" : $"+p.getPrice()+" : "+p.getStock()+" : "+p.getCategory()+" : "+p.getSells());
            }
        }
        return msj.toString();
    }
    public String printList(){
        String msj = "NAME | PRICE | STOCK | CATEGORY | Sells |";
        for(Product p : products){
            msj += "\n"+p.getName()+" : $"+p.getPrice()+" : "+p.getStock()+" : "+p.getCategory()+" : "+p.getSells();
        }
        return msj;
    }
    public String printList2(Product[] list){
        String msj = "NAME | PRICE | STOCK | CATEGORY | Sells |";
        for(Product p : list){
            msj += "\n"+p.getName()+" : $"+p.getPrice()+" : "+p.getStock()+" : "+p.getCategory()+" : "+p.getSells();
        }
        return msj;
    }
    public void addProduct(Product product){
        products.add(product);
    }

}
