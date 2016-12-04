package util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import util.annotation.ExcelColumn;
import util.annotation.ExcelHeader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsq on 16/12/4.
 */
public class ExcelAnnocationUtils {

    public static HSSFWorkbook exportExcelData(Class<?> clazz, List<?> appDatas) {
        Map<String, Object> results = new HashMap<String, Object>() ;
        Field[] fields = clazz.getDeclaredFields() ;
        if(clazz.isAnnotationPresent(ExcelHeader.class)) {
            /**
             * 获取用@ExcelHeader注解的entity类名 为excelHeader
             */
            ExcelHeader excelHeader = clazz.getAnnotation(ExcelHeader.class) ;
            results.put("headerName", excelHeader.headerName()) ;
        }
        List<String> list = new ArrayList<String>() ;
        for(Field field : fields) {
            if(field.isAnnotationPresent(ExcelColumn.class)) {
                /**
                 * 获取用@ExcelColumn注解的entity变量 为excelColumn
                 */
                ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class) ;
                list.add(excelColumn.columnName()) ;
            }
        }
        results.put("columnNames", list) ;
        results.put("appDatas", appDatas) ;
        return (new ExcelUtils().exportContainDataExcelSheet(results, clazz));
    }

    public static <T> List<T> importData(String filePath,Class<?> clazz) {
        List<T> result = null;
        try {
            result = (List<T>)new ExcelUtils().importExcelData(filePath,clazz);
        } catch (Exception e) {
            ;
        }
        return result;
    }
}
