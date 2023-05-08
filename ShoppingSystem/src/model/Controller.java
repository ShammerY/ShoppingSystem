package model;
import exceptions.*;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

public class Controller {
    private File productData;
    private File orderData;
    private ArrayList<Product> products;
    private ArrayList<Order> orders;
    public Controller(){
        products = new ArrayList<>();
        orders = new ArrayList<>();
    }
    public void loadArchive(){
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

    /**
     * name : setProductSellsAndStock
     * To increase the sells amount and decrease product stock, each time an order is registered
     * @param product : product which will be setted.
     */
    public void setProductSellsAndStock(Product product){
        product.setSells(1);
        product.setStock(-1);
    }

    /**
     * searchProductByName: returns a list of the products that resembles the name entered by the user
     * @param name: name to evaluate the product
     */
    public Product[] searchProductByName(String name){
        return makeProductListByName(name);
    }

    /**
     * makeProductListByName: returns list evaluated products with the name entered;
     * @param name: name to evaluate products;
     */
    private Product[] makeProductListByName(String name){
        Product[] list = new Product[products.size()];
        int len = name.split("").length;
        int cont = 0;
        String startName = "";
        String endName = "";
        for(int i=0;i<products.size();i++){
            String productName = products.get(i).getName();
            if(productName.split("").length>=len){
                startName = productName.substring(0,len);
                endName = productName.substring(productName.split("").length-len,productName.split("").length);
                if(startName.equalsIgnoreCase(name)){
                    list[cont] = products.get(i);
                    cont++;
                }else if(endName.equalsIgnoreCase(name)){
                    list[cont] = products.get(i);
                    cont++;
                }
            }
        }
        if(cont>0){
            list = subStringList(list,0,cont-1);
        }else{
            return null;
        }
        return list;
    }

    /**
     * binarySearchName: returns the exact possition of a product in the list by its name
     * @param name: String name to evaluate the product
     * @param array: list to search the possible product
     */
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

    /**
     * searchProductByPrice: returns a list with the products which corresponds to the range entered
     * @param iLimit : Inferior limir of the range
     * @param sLimit : Superior limit of the range
     */
    public Product[] searchProductByPrice(double iLimit,double sLimit){
        if(iLimit>sLimit){
            return null;
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
            return null;
        }
        list = subStringList(list,infPos,supPos);
        return list;
    }

    /**
     * binarySearchInfPrice: searches for the possition of the product with the least price indicated
     * @param price: value of the inferior price
     * @param array: list to search the product
     */
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
    /**
     * binarySearchSupPrice: searches for the possition of the product with the highest price indicated
     * @param price: value of the Superior price
     * @param array: list to search the product
     */
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

    /**
     * searchProductByStock: returns the list with the products that correspond to the stock range entered
     * @param iLimit :inferior limit of the range
     * @param sLimit : Superior Limit of the range
     */
    public Product[] searchProductByStock(int iLimit,int sLimit){
        if(iLimit>sLimit){
            return null;
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
            return null;
        }
        list = subStringList(list,infPos,supPos);
        return list;
    }

    /**
     *binarySearchInfStock : returns the position of the product with the inferior limit of the range;
     * @param stock: inferior limit of the range
     * @param array : list to search Product
     */
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
    /**
     *binarySearchSupStock : returns the position of the product with the Superior limit of the range;
     * @param stock: Superior limit of the range
     * @param array : list to search Product
     */
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

    /**
     * searchProductByCategory: Returns the list of products that correspond with the category indicated
     * @param category: Category entered
     */
    public Product[] searchProductByCategory(ProductCategory category){
        Product[] list = new Product[products.size()];
        int cont = 0;
        for(Product p:products){
            if(p.getCategory().equals(category)){
                list[cont] = p;
                cont++;
            }
        }
        if(cont>0){
            list = subStringList(list,0,cont-1);
        }else{
            return null;
        }
        return list;
    }

    /**
     * searchProductBySells: returns list pf products that correspond with the sells range entered
     * @param iLimit : inferior limir of the range
     * @param sLimit . Superior limit of the range
     */
    public Product[] searchProductBySells(int iLimit,int sLimit){
        if(iLimit>sLimit){
            return null;
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
            return null;
        }
        list = subStringList(list,infPos,supPos);
        return list;
    }

    /**
     * binarySearchInfSells: return the position of the product with the inferior limit of the range
     * @param sells : inferior limit of the range
     * @param array : list to search product
     */
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
    /**
     * binarySearchSupSells: return the position of the product with the superior limit of the range
     * @param sells : Superior limit of the range
     * @param array : list to search product
     */
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

    /**
     * getOrderByName: returns the list of Orders that correspond to the Customer's name entered
     * @param name : customer's name
     */
    public Order[] getOrderByName(String name){
        Order[] order = new Order[orders.size()];
        int cont = 0;
        for(Order o:orders){
            if(o.getCustomerName().equals(name)){
                order[cont] = o;
                cont++;
            }
        }
        if(cont>0){
            order = subStringOrder(order,0,cont-1);
        }else{
            return null;
        }
        return order;
    }

    /**
     * searchOrderByPrice: returns the order that corresponds to the price indicate
     * @param price : Price entered
     */
    public Order[] searchOrderByPrice(double price){
        Order[] list = getOrders();
        Arrays.sort(list,(a,b) ->{
           if((a.getValue() - b.getValue())>0){
               return 1;
           }else if((a.getValue() - b.getValue())<0){
               return -1;
           }else{
               return 0;
           }
        });
        int begin = 0;
        int end = list.length-1;
        while(begin <= end){
            int half = (int)Math.floor((double)(begin+end)/2);
            if((list[half].getValue() - price)==0){
                Order[] array = {list[half]};
                return array;
            }else if((list[half].getValue() - price)<0){
                begin = half+1;
            }else{
                end = half-1;
            }
        }
        return null;
    }

    /**
     * getOrdersByDate: returns the list of orders that correspond with the date indicated
     * @param date: date entered
     */
    public Order[] getOrdersByDate(String date){
        Order[] list = new Order[orders.size()];
        int cont = 0;
        for(Order o:orders){
            Date dt = o.getDate();
            String orderDate = dt.getDate()+"/"+(dt.getMonth()+1)+"/"+(dt.getYear()+1900);
            if(orderDate.equals(date)){
                list[cont] = o;
                cont++;
            }
        }
        if(cont>0){
            list = subStringOrder(list,0,cont-1);
            return list;
        }else{
            return null;
        }
    }

    /**
     * addProductStock: replenishes a product's stock
     * @param product: product to replenish Stock
     * @param stock : amount of products adding to the stock
     */
    public void addProductStock(Product product, int stock){
        product.setStock(stock);
    }

    /**
     * printList: returns the information of a list of products
     * @param list: list of products
     */
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

    /**
     * printOrder: Prints the information of an order list
     * @param list: list of orderss
     */
    public String printOrder(Order[] list){
        StringBuilder msj = new StringBuilder();
        msj.append(" Customer Name | Total Value | Registration Date");
        for(Order o:list){
            Date date = o.getDate();
            msj.append("\n"+o.getCustomerName()+" : "+o.getValue()+" : "+(date.getDate()+"/"+(date.getMonth()+1)+"/"+(date.getYear()+1900)));
        }
        return msj.toString();
    }

    /**
     * calculateSeparations: calculates the sepparation to arrange the list so it is better looking
     * @param list: list of products to get informarion
     */
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

    /**
     * addSeparation: adds the sepparation to a specific product,
     * @param p: product to print its information
     * @param sep: list of the separations of each Atribute of the product
     */
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

    /**
     * subString: returns a divided list depending with the position range entered
     * @param list: list to divide in sub parts
     * @param inf: inferior position to divide the list
     * @param sup: superior position to divide the list
     */
    private Product[] subStringList(Product[] list,int inf,int sup){
        Product[] array = new Product[(sup-inf)+1];
        int cont = 0;
        for(int i=inf;i<=sup;i++){
            array[cont] = list[i];
            cont++;
        }
        return array;
    }

    /**
     * subStringOrder: The same as the other one but with orders
     * @param list: order List
     * @param inf: inferior limit
     * @param sup : will someone even read this?
     */
    private Order[] subStringOrder(Order[] list,int inf,int sup){
        Order[] array = new Order[(sup-inf)+1];
        int cont = 0;
        for(int i=inf;i<=sup;i++){
            array[cont] = list[i];
            cont++;
        }
        return array;
    }

    /**
     * addproduct: adds a product to the controller ArrayList of Products
     * @param product: product to be added
     */
    public void addProduct(Product product){
        products.add(product);
    }

    /**
     * deleteProduct: Removes a product from the main ArrayList of products
     * @param name: name of the product to be removed;
     */
    public void deleteProduct(String name){
        for(int i=0;i<products.size();i++){
            if((products.get(i).getName().compareToIgnoreCase(name))==0){
                products.remove(i);
            }
        }
    }
    /**
     * addOrder: adds an order to the controller ArrayList of orders
     * @param order: order to be added
     */
    public void addOrder(Order order){
        orders.add(order);
    }
 }
