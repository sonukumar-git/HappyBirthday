package main;

import java.io.File;
import java.util.Scanner;

public class ReadTemplate {

    String location;

    public String  getTemplate(String location){
        this.location=location;
        String data=null;
        try{
            File file=new File(location);
            Scanner scanner=new Scanner(file);
            data=scanner.useDelimiter("//A").next();

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
