package main;

import utils.SendingEmailApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class DobMatch {
    public static void main(String[] args) {
        boolean flag=false;
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        String template;
        Scanner scan;

        System.out.println("Opening file..");
        try {
            File file = new File("src\\resources\\emp_det.txt");
            scan = new Scanner(file);
            System.out.println("Reading File..");


            while (scan.hasNextLine()) {
                data.add(new ArrayList<>(
                        Arrays.asList(scan.nextLine().replaceAll("\\s+$", "").split(","))
                ));
            }

        SimpleDateFormat formatter=new SimpleDateFormat("d-MMM");
        Date date=new Date();
        String cDate=formatter.format(date).toLowerCase();
        System.out.println("Today is "+cDate);

        ReadTemplate templateFile=new ReadTemplate("src\\resources\\template\\template.html");
        template=templateFile.getTemplate();

        SendingEmailApp sendHBMail=new SendingEmailApp();

        for(ArrayList<String> arr:data){
            if(arr.size()>2){
                if(cDate.equals(arr.get(3).trim().toLowerCase())) {
                    flag=true;
                    System.out.println(arr.get(0) + "’s Birthday is today");
                    System.out.println("Sending Happy birthday mail to "+arr.get(1));
                    sendHBMail.sendMail(arr.get(1).trim(),
                            "Wishing you a very Happy Birthday!",
                            "",
                            template,
                            "text/html");

                }
                else{
                    System.out.println(arr.get(0)+"’s birthday  is not today");
                                    }

            }

        }

        System.out.println("closing file..");
        scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!flag)
        System.out.println("No Mail sent");


    }
}
