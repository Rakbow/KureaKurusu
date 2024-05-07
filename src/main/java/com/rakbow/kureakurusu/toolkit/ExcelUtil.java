package com.rakbow.kureakurusu.toolkit;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;
import java.util.stream.StreamSupport;

/**
 * @author Rakbow
 * @since 2024/3/23 0:39
 */
public class ExcelUtil {

    @SneakyThrows
    public static <T> List<T> getDataFromExcel(InputStream stream, Class<T> clazz) {
        List<T> res = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(stream);
        Sheet sheet = workbook.getSheetAt(0);
        Row row;
        // 获取最大行数
        int maxRowNum = sheet.getLastRowNum();
        // 获取第一行
        row = sheet.getRow(0);
        // 获取最大列数
        int maxColNum = row.getLastCellNum();
        Iterable<Cell> cells = row::cellIterator;
        List<String> keySet =  StreamSupport.stream(cells.spliterator(), false)
                .map(cell -> (cell == null) ? "" : cell.toString()).toList();
        // 循环遍历excel表格，把每条数据封装成 map集合，再放入list集合中
        for (int i = 1; i <= maxRowNum; i++) {
            Map<String, String> dic = new HashMap<>();
            row = sheet.getRow(i);
            if(row == null) continue;
            for (int j = 0; j < maxColNum; j++) {
                String cellData = (String) getCellFormatValue(row.getCell(j));
                dic.put(keySet.get(j), cellData); // map 封装
            }
            res.add(JsonUtil.to(dic, clazz));
        }
        return res;
    }


    // 用于获取表格中的数据方法
    private static Object getCellFormatValue(Cell cell){
        Object cellValue;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }

}
