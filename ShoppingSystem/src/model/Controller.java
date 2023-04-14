package model;

import java.util.ArrayList;
import java.io.*;
public class Controller {
    private ArrayList<Product> products;
    public void saveData() throws IOException{
        String path = "data.json";
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));



    }

}
