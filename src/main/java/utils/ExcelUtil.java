/*
@ Demo test for Medium
@ Author : Hoi
@ aug 2019
 */
package utils;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

public class ExcelUtil  {
    private static XSSFWorkbook ExcelWBook;
    private static XSSFSheet ExcelWSheet;
    public static XSSFSheet readExcel(String Path, String SheetName) {
        try{
            //open excel file
            FileInputStream ExcelFile = new FileInputStream(Path);
            //Access the required test data sheet
            ExcelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
        } catch (Exception e) {
            System.out.println(e);
        }
        return ExcelWSheet;
    }
}
