package main;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.SendingEmailApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class DobMatch {
    static ArrayList<ArrayList<String>> data = new ArrayList<>();
    public static void main(String[] args) {
        DobMatch dobMatch=new DobMatch();

        boolean flag = false;

        String template;

        System.out.println("Opening file..");

        try {
            String fileLocation = "src\\resources\\emp_det.xls";
            File file = new File(fileLocation);   //creating a new file instance

            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            if (fileLocation.endsWith(".xlsx")) {
                System.out.println("File extension is .xlsx");
                XSSFWorkbook wb = new XSSFWorkbook(fis);
                XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
                //iterating over excel file
                for (Row row : sheet) {
                    data.add(dobMatch.readRow(row));
                }
            } else if (fileLocation.endsWith(".xls")) {
                System.out.println("File extension is .xls");
                //creating workbook instance that refers to .xls file
                HSSFWorkbook wb = new HSSFWorkbook(fis);
                //creating a Sheet object to retrieve the object
                HSSFSheet sheet = wb.getSheetAt(0);
                //evaluating cell type
                for (Row row : sheet)     //iteration over row using for each loop
                {
                    data.add(dobMatch.readRow(row));
                }
            } else {
                System.out.println("Unsupported File Format [only support .xlsx/.xls]");

            }

            fis.close();

            SimpleDateFormat formatter = new SimpleDateFormat("d-MMM");
            Date date = new Date();
            String cDate = formatter.format(date).toLowerCase();
            System.out.println("Today is " + cDate);

            ReadTemplate readTemplateFile = new ReadTemplate();
            SendingEmailApp sendHBMail = new SendingEmailApp();
            try {
                if (data != null) {
                    data.remove(0);
                    for (ArrayList<String> arr : data) {

                        if ((arr.size() > 2) && arr.get(4) != null && arr.get(5) != null) {
                            if (cDate.equals(arr.get(4).trim().toLowerCase())) {
                                flag = true;
                                System.out.println(arr.get(0) + "’s Birthday is today");
                                System.out.println("Sending Happy birthday mail to " + arr.get(1));
                                template = readTemplateFile.getTemplate(arr.get(5));
                                sendHBMail.sendMail(arr.get(1).trim(),
                                        arr.get(2).trim(),
                                        "Wishing you a very Happy Birthday!",
                                        "",
                                        template,
                                        "text/html");

                            } else {
                                System.out.println(arr.get(0) + "’s birthday  is not today");
                            }

                        }
                    }

                } else
                    System.out.println("No data Found!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("The index you have entered is invalid");
                System.out.println("[Array Index Out Of Bounds Exception]");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("other problem");
            }

            System.out.println("closing file..");

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found Try Again");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!flag)
            System.out.println("No Mail sent");
    }

    public ArrayList<String> readRow(Row row){
        ArrayList<String> rowData = new ArrayList<>();
        for (Cell cell : row)    //iteration over cell using for each loop
        {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:    //field that represents string cell type
                    //getting the value of the cell as a string
                    rowData.add(cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC: //field that represents numeric cell type
                    //getting the value of the cell as a number

                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                        rowData.add(dateFormat.format(cell.getDateCellValue()));

                    } else {
                        rowData.add(String.valueOf(cell.getNumericCellValue()));
                    }
                    break;
            }
        }
        return rowData;

    }
}
