package model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.io.*;
public class Controller {
    private ArrayList<Product> products;
    public Controller(){
        products = new ArrayList<Product>();
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
        StringBuilder ms = new StringBuilder();
        for(Product p:products){
            if(p.getName().equals(name)){
                ms.append("\n"+p.getName()+" : $"+p.getPrice()+" : "+p.getStock()+" : "+p.getCategory()+" : "+p.getSells());
            }
        }
        return ms.toString();
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
    public void addProduct(Product product){
        products.add(product);
    }

}
