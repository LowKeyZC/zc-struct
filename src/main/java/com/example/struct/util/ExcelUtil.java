package com.example.struct.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
//fixme
public class ExcelUtil {

  public static void getExcelContent(File file) {
    try {
      Workbook workbook = WorkbookFactory.create(file);
      int sheetsNum = workbook.getNumberOfSheets();
      for (int i = 0; i < sheetsNum; i++) {
        Sheet sheet = workbook.getSheetAt(i);
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        for (int j = firstRowNum; j < lastRowNum + 1; j++) {
          Row row = sheet.getRow(j);
          int firstCellNum = row.getFirstCellNum();
          int lastCellNum = row.getLastCellNum();
          for (int k = firstCellNum; k < lastCellNum; k++) {
            System.out.println(row.getCell(k).toString());
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException {
    getExcelContent(new ClassPathResource("files/aaa.xls").getFile());
  }

 /* public static void main(String[] args) throws Exception {

    List<Integer> exclude = Arrays.asList(9,10,18,20,23,26,44,53,69,70,71,78,81,90,98,105,106,110,112,114,116,117,118,121,123,129,133,140,149,158,168,183,189,196);
    StringBuffer sb = new StringBuffer();

    File xlsFile = new File("D:\\work\\3 云信运费贷\\开卡银行名称映射关系-云信和司机端(3).xlsx");

    int a = 0;

    // 工作表
    Workbook workbook = WorkbookFactory.create(xlsFile);

    // 表个数。
    int numberOfSheets = workbook.getNumberOfSheets();

    // 遍历表
    for (int i = 0; i < numberOfSheets; i++) {
      Sheet sheet = workbook.getSheetAt(i);

      // 行数。
//      int rowNumbers = sheet.getLastRowNum() + 1;

      // Excel第一行。
      Row temp = sheet.getRow(0);
      if (temp. == null) {
        continue;
      }

//      int cells = temp.getPhysicalNumberOfCells();

      // 读数据。
      for (int row = 0; row < 220; row++) {
        if (exclude.contains(row)) {
          continue;
        }
        Row r = sheet.getRow(row);

        String str = "INSERT INTO fuyou_bank_map(fuyouBankName,thirdBankName,thirdType,thirdCode)VALUES(";

        String fyBankName = r.getCell(2).toString().trim();
        String thirdBankName = r.getCell(1).toString().trim();
        String thirdCode = r.getCell(0).toString().trim();

        str += "'" + fyBankName + "','" + thirdBankName + "'," + 1 + ",'" + thirdCode + "');";
        a++;
        System.out.println(str);



        *//*for (int col = 0; col < 3; col++) {
          System.out.print(r.getCell(col).toString() + " ");
        }

        // 换行。
        System.out.println();*//*
      }
    }
    System.out.println(a);
  }*/
}
