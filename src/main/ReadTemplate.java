package main;

import java.io.File;
import java.util.Scanner;

public class ReadTemplate {

    String location;
    ReadTemplate(String location){
        this.location=location;
    }

    public String  getTemplate(){
        String data=null;
        try{
            File file=new File(location);
            Scanner scanner=new Scanner(file);
            data=scanner.useDelimiter("//A").next();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
