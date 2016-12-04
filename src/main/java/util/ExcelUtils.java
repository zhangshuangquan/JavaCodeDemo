package util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.annotation.ExcelColumn;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by zsq on 16/12/4.
 */
public class ExcelUtils {

    private final String DEFAULT_SHEET_NAME = "sheet1";
    public final static String EXCEL_TYPE_XLS = "xls";
    public final static String EXCEL_TYPE_XLSX = "xlsx";
    // === HSSFWorkbook对象
    private HSSFWorkbook workbook = null;
    // === XSSFWorkbook对象
    private XSSFWorkbook xssfWorkbook = null ;
    // === Excel文件的头部标题样式
    private HSSFCellStyle headerStyle = null;
    // === Excel文件的第二行提示信息样式
    private HSSFCellStyle warnerStyle = null;
    // === Excel文件列头的样式
    private HSSFCellStyle titleStyle = null;
    // === Excel文件的数据样式
    private HSSFCellStyle dataStyle = null;
    // === Excel文件中的错误数据的显示样式
    private HSSFCellStyle errorDataStyle = null;

    // ======================================== Excel 公共方法调用
    // =============================================
	/*
	 * 获取HSSFWorkbook对象
	 */
    private HSSFWorkbook getHSSFWorkbook() {

        return new HSSFWorkbook();
    }

    private HSSFWorkbook getHSSFWorkbook(POIFSFileSystem in) throws IOException {

        return new HSSFWorkbook(in);
    }

    private XSSFWorkbook getXSSFWorkbook(InputStream in) throws IOException {

        return new XSSFWorkbook(in) ;
    }

    // ======================================= 创建公共样式
    // ==============================================
	/*
	 * 设置Excel文件的头部标题的样式
	 */
    private void setHeaderCellStyles(HSSFWorkbook workbook, HSSFSheet sheet) {
        headerStyle = workbook.createCellStyle();

        // === 设置边框
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        // === 设置背景色
        headerStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // === 设置居中
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // === 设置字体
        HSSFFont font = workbook.createFont();
        font.setFontName("粗体");

        // === 设置字体大小
        font.setFontHeightInPoints((short) 16);

        // === 设置粗体显示
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        // === 选择需要用到的字体格式
        headerStyle.setFont(font);
        // === 设置自动换行
        // headerStyle.setWrapText(true) ;
        // sheet.autoSizeColumn((short)0, true); // === 调整第一列宽度
    }

    /*
     * 设置Excel文件的第二列的注意事项提示信息的样式
     */
    private void setWarnerCellStyles(HSSFWorkbook workbook, HSSFSheet sheet) {
        warnerStyle = workbook.createCellStyle();

        // === 设置边框
        warnerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        warnerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        warnerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        warnerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        // === 设置背景色
        warnerStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        warnerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // === 设置左对齐
        warnerStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        // === 设置字体
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");

        // === 设置字体大小
        font.setFontHeightInPoints((short) 10);

        // === 设置粗体显示
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        // === 设置字体颜色
        font.setColor(HSSFColor.RED.index);

        // === 选择需要用到的字体格式
        warnerStyle.setFont(font);

        // === 设置自动换行
        warnerStyle.setWrapText(true);
    }

    /*
     * 设置Excel文件的列头样式
     */
    private void setTitleCellStyles(HSSFWorkbook workbook, HSSFSheet sheet) {
        titleStyle = workbook.createCellStyle();
        // === 设置边框
        titleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        titleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        // === 设置背景色
        titleStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // === 设置居中
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // === 设置字体
        HSSFFont font = workbook.createFont();
        font.setFontName("粗体");

        // === 设置字体大小
        font.setFontHeightInPoints((short) 12);

        // === 设置粗体显示
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        // === 选择需要用到的字体格式
        titleStyle.setFont(font);

        // === 设置自动换行
        // titleStyle.setWrapText(true) ;
    }

    /*
     * 设置Excel文件的数据样式
     */
    private void setDataCellStyles(HSSFWorkbook workbook, HSSFSheet sheet) {
        dataStyle = workbook.createCellStyle();
        // === 设置单元格格式为文本格式
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        dataStyle.setDataFormat(dataFormat.getFormat("@"));
        dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        // === 设置背景色
        dataStyle.setFillForegroundColor(HSSFColor.WHITE.index);
        dataStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // === 设置居中
        dataStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        // === 设置字体
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");

        // === 设置字体大小
        font.setFontHeightInPoints((short) 11);

        // === 选择需要用到的字体格式
        dataStyle.setFont(font);

        // === 设置自动换行
        // dataStyle.setWrapText(true) ;
    }



    // ======================================= 创建公共数据
    // ==============================================
    private void createAppRowHeaderData(HSSFSheet sheet, String headerTitle, Integer cellHeaderNum) {

        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 800);

        HSSFCell headerCell = row.createCell(0);
        headerCell.setCellValue(new HSSFRichTextString(headerTitle));
        headerCell.setCellStyle(headerStyle);

        if (cellHeaderNum != 0) {
            for (int i = 1; i < cellHeaderNum; i++) {
                headerCell = row.createCell(i);
                headerCell.setCellStyle(headerStyle);
            }
            // === 合并头部单元格 参数：firstRow, lastRow, firstCol, lastCol
            sheet.addMergedRegion(new CellRangeAddress((short) 0, (short) 0, (short) 0, (short) (cellHeaderNum - 1)));
            // === 设置单元格自动列宽，中文支持较好
            // sheet.setColumnWidth(0, headerTitle.getBytes().length*2*256);
            for (int i = 0; i < cellHeaderNum; i++) {
                sheet.autoSizeColumn((short) i, true);
            }
        } else {
            sheet.autoSizeColumn((short) 0, true);
        }
    }

    private void createAppRowCellHeaderData(HSSFSheet sheet, List<String> cellHeader) {

        HSSFRow row = sheet.createRow(1);
        row.setHeight((short) 500);

        HSSFCell cellHeaderCell = null;
        if (cellHeader != null && cellHeader.size() > 0) {
            for (int i = 0; i < cellHeader.size(); i++) {
                cellHeaderCell = row.createCell(i);
                cellHeaderCell.setCellValue(new HSSFRichTextString(cellHeader.get(i)));
                cellHeaderCell.setCellStyle(titleStyle);
                // === 设置列宽
                sheet.setColumnWidth(i, (short) 7000);
            }
        }

    }

    private void createAppRowHasData(HSSFSheet sheet, List<Object> appData, Class<?> clazz, Integer cellHeaderNum) {

        HSSFRow row = null;
        HSSFCell cellAppDataCell = null;
        ExcelColumn excelColumn = null;
        if (cellHeaderNum != 0) {
            if (appData != null && appData.size() > 0) {
                // === 行记录数
                for (int i = 0; i < appData.size(); i++) {
                    // === 列记录数
                    row = sheet.createRow(i + 2);
                    Object o = appData.get(i);
                    Field[] fields = o.getClass().getDeclaredFields();
                    int j = 0;
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(ExcelColumn.class)) {
                            field.setAccessible(true);
                            excelColumn = field.getAnnotation(ExcelColumn.class);
                            try {
                                cellAppDataCell = row.createCell(j);
                                if(StringUtils.equals(excelColumn.autoIncrement(), "Y")){
                                    cellAppDataCell.setCellValue(new HSSFRichTextString((i+1)+""));
                                }else{
                                    Object value = field.get(o);
                                    if (value != null) {
                                        cellAppDataCell.setCellValue(new HSSFRichTextString(value.toString()));
                                    } else {
                                        cellAppDataCell.setCellValue(new HSSFRichTextString(""));
                                    }
                                }
                                cellAppDataCell.setCellStyle(dataStyle);
                                j++;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }


    public HSSFWorkbook exportContainDataExcelSheet(Map<String, Object> results, Class<?> clazz) {
        // ======================== 页签创建 ==========================
        // === 获取HSSFWorkbook对象
        workbook = getHSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet(DEFAULT_SHEET_NAME);
        // ========================= 样式设置 =========================
        // === 设置表头样式
        setHeaderCellStyles(workbook, sheet);
        // === 设置列头样式
        setTitleCellStyles(workbook, sheet);
        // === 设置数据样式
        setDataCellStyles(workbook, sheet);

        // ========================= 数据创建 ==========================
        // === 创建标题数据
        createAppRowHeaderData(sheet, results.get("headerName").toString(),
                ((List<String>) results.get("columnNames")).size());
        // === 创建列头数据信息
        createAppRowCellHeaderData(sheet, (List<String>) results.get("columnNames"));
        // === 为空模板创建初始化数据 空数据样式
        createAppRowHasData(sheet, (List<Object>) results.get("appDatas"), clazz,
                ((List<String>) results.get("columnNames")).size());
        return workbook;
    }



    public List<Object> importExcelData(String filePath, Class<?> clazz) throws FileNotFoundException, IOException, SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {

        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));
        workbook = getHSSFWorkbook(fs);

        HSSFSheet sheet = workbook.getSheetAt(0);
        return importExcelData(sheet, clazz);
    }

    public List<Object> importExcelData2007(InputStream in, Class<?> clazz) throws FileNotFoundException, IOException, SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        xssfWorkbook = getXSSFWorkbook(in) ;
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0) ;
        return importExcelData(xssfSheet, clazz) ;
    }

    public List<Object> importExcelData(InputStream in, Class<?> clazz) throws IOException,
            SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {

        POIFSFileSystem fs = new POIFSFileSystem(in);
        workbook = getHSSFWorkbook(fs);

        HSSFSheet sheet = workbook.getSheetAt(0);
        return importExcelData(sheet, clazz);
    }

    public List<Object> importExcelData(InputStream in, Class<?> clazz, String... sheetNames) throws IOException,
            SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {

        POIFSFileSystem fs = new POIFSFileSystem(in);
        workbook = getHSSFWorkbook(fs);

        List<Object> objs = new ArrayList<Object>();
        if (ArrayUtils.isEmpty(sheetNames)) {
            return objs;
        }
        for (String sheetName : sheetNames) {
            HSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                continue;
            }
            List<Object> obj = importExcelData(sheet, clazz);
            objs.addAll(obj);

        }
        return objs;
    }


    public List<Object> importExcelData(File file, Class<?> clazz) throws FileNotFoundException, IOException,
            SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {

        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
        workbook = getHSSFWorkbook(fs);

        HSSFSheet sheet = workbook.getSheetAt(0);
        return importExcelData(sheet, clazz);
    }


    private List<Object> importExcelData(Sheet sheet, Class<?> clazz) throws IOException,
            SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {

        // === 标记变量，消除全部的空行记录
        StringBuilder sb = new StringBuilder();

        // === 提取导入数据模板中的列头信息，即第2行的数据
        Row headerCellRow = sheet.getRow(1);
        Integer cellHeaderNum = Integer.valueOf(headerCellRow.getLastCellNum());
        Cell dataCell = null;
        Row dataRow = null;
        List<Object> rowList = new ArrayList<Object>();
        Map<String, String> columnMap = new HashMap<String, String>();

        dataRow = sheet.getRow(1);
        for (int m = 0; m < cellHeaderNum; m++) {
            String columnNameE = String.valueOf(dataRow.getCell(m).getRichStringCellValue().toString()).trim();
            // === 循环遍历字节码注解 获取属性名称
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ExcelColumn.class)) {
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                    String fieldName = field.getName();
                    if (StringUtils.equals(excelColumn.columnName().trim(), columnNameE)) {
                        columnMap.put(columnNameE, fieldName);
                    }
                }
            }
        }

        // === 循环遍历数据
        Integer rowNum = sheet.getLastRowNum();
        for (int i = 2; i <= rowNum; i++) {
            sb.delete(0, sb.length());
            sb.append(String.valueOf(i));
            dataRow = sheet.getRow(i);
            if (dataRow != null) {
                Object obj = clazz.newInstance();
                for (int j = 0; j < cellHeaderNum; j++) {
                    dataCell = dataRow.getCell(j);
                    // =================================== 读取Excel文件中的数据
                    // 文本，数值或日期类型的条件判断 开始 =============================
                    if (dataCell != null) {
                        Object value = "";
                        switch (dataCell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC:
                                if (HSSFDateUtil.isCellDateFormatted(dataCell)) {
                                    // === 如果是date类型则 ，获取该cell的date值
                                    // value =
                                    // HSSFDateUtil.getJavaDate(dataCell.getNumericCellValue()).toString();
                                    Date date = dataCell.getDateCellValue();
                                    // SimpleDateFormat sdf = new
                                    // SimpleDateFormat("yyyy-MM-dd") ;
                                    // value = sdf.format(date) ;
                                    value = date;
                                } else {// === 纯数字
                                    dataCell.setCellType(Cell.CELL_TYPE_STRING);
                                    value = String.valueOf(dataCell.getRichStringCellValue().toString());
                                }
                                break;

                            case HSSFCell.CELL_TYPE_STRING:
                                value = dataCell.getRichStringCellValue().toString();
                                break;

                            case HSSFCell.CELL_TYPE_FORMULA:
                                // === 读公式计算值
                                value = String.valueOf(dataCell.getNumericCellValue());
                                // === 如果获取的数据值为非法值,则转换为获取字符串
                                if (value.equals("NaN")) {
                                    value = dataCell.getRichStringCellValue().toString();
                                }
                                // cell.getCellFormula() ;//读公式
                                break;

                            case HSSFCell.CELL_TYPE_BOOLEAN:
                                value = dataCell.getBooleanCellValue();
                                break;

                            case HSSFCell.CELL_TYPE_BLANK:
                                value = "";
                                break;

                            case HSSFCell.CELL_TYPE_ERROR:
                                value = "";
                                break;

                            default:
                                value = dataCell.getRichStringCellValue().toString();
                                break;
                        }
                        sb.append(value);

                        // === 每一行数据的列头是否匹配，决定如何反射设置属性的值
                        String columnNameE = String.valueOf(
                                (sheet.getRow(1).getCell(j).getRichStringCellValue().toString())).trim();
                        String fieldName = columnMap.get(columnNameE);
                        Field f = obj.getClass().getDeclaredField(fieldName);
                        value = transValue(f, value);
                        f.setAccessible(true);
                        f.set(obj, value);
                    }
                    // =================================== 读取Excel文件中的数据
                    // 文本，数值或日期类型的条件判断 结束 =============================
                }
                if (trim(sb.toString()).equals(String.valueOf(i))) {
                    Collections.emptyList();
                } else {
                    rowList.add(obj);
                }
            }

        }

        return rowList;
    }

    private Object transValue(Field f,Object value){
        Type type = f.getGenericType();
        String typeName = type.toString();
        if(StringUtils.equals("class java.lang.Integer", typeName)){
            value = Integer.parseInt(value.toString());
        }else if(StringUtils.equals("class java.util.Date", typeName)){
            if(!(value instanceof Date)){
                value = null;
            }
        }
        return value;
    }

    private String trim(String str) {
        if (str == null) {
            return str;
        }
        return str.trim();
    }
}
