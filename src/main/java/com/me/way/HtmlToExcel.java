package com.me.way;

import com.me.config.UtilConfig;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;
@Component
public class HtmlToExcel {
    /**
     * @param html      字符串的html
     * @param sheetName sheet页的名字
     * @param filePath  保存地址
     * @throws IOException
     */
    private static HtmlToExcel onself;

    @Resource
    private UtilConfig utilConfig;

    @PostConstruct
    private void init() {
        onself= this;
    }

    public static void toExcel(String html, String sheetName, String filePath,String fileName) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        //表头单元格风格
        HSSFCellStyle thStyle = wb.createCellStyle();
        thStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        thStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //上下左右居中
        thStyle.setAlignment(HorizontalAlignment.CENTER);
        thStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //边框
        thStyle.setBorderTop(BorderStyle.THIN);
        thStyle.setBorderRight(BorderStyle.THIN);
        thStyle.setBorderBottom(BorderStyle.THIN);
        thStyle.setBorderLeft(BorderStyle.THIN);
        thStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
        thStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
        thStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
        thStyle.setLeftBorderColor(IndexedColors.WHITE.getIndex());
        //字体
        Font thFont = wb.createFont();
        thFont.setColor(IndexedColors.BLACK.getIndex());
        thFont.setBold(true);
        thStyle.setFont(thFont);
        Sheet sheet = wb.createSheet(sheetName);

        //获取html的数据
        List<List<Map<String, String>>> excelData = getExcelData(html);
        //处理数据
        for (int rowNum = 0; rowNum < excelData.size(); rowNum++) {
            //外层是循环行，每循环一次，创建一个行的对象
            Row row = sheet.createRow(rowNum);
            //设置行的高度
            row.setHeightInPoints(25);
            for (int cellNum = 0; cellNum < excelData.get(rowNum).size(); cellNum++) {

                //处理跨行跨列
                if ((excelData.get(rowNum).get(cellNum).get("colspanValue") != null) && (excelData.get(rowNum).get(cellNum).get("rowspanValue") != null)) {
                    int colspanValue = Integer.parseInt(excelData.get(rowNum).get(cellNum).get("colspanValue"));
                    int rowspanValue = Integer.parseInt(excelData.get(rowNum).get(cellNum).get("rowspanValue"));
                    sheet.addMergedRegion(new CellRangeAddress(rowNum, rowspanValue + rowNum - 1, cellNum, cellNum + colspanValue - 1));
                } else {
                    if (excelData.get(rowNum).get(cellNum).get("colspanValue") != null) {
                        int colspanValue = Integer.parseInt(excelData.get(rowNum).get(cellNum).get("colspanValue"));
                        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, cellNum, cellNum + colspanValue - 1));
                    }
                    if (excelData.get(rowNum).get(cellNum).get("rowspanValue") != null) {
                        int rowspanValue = Integer.parseInt(excelData.get(rowNum).get(cellNum).get("rowspanValue"));
                        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowspanValue + rowNum - 1, cellNum, cellNum));
                    }
                }

                //内层循环每行的单元格，每循环一次，创建一个单元格的对象
                Cell cell = row.createCell(cellNum);
                //赋值
                cell.setCellValue(excelData.get(rowNum).get(cellNum).get("value"));

                //设置样式
                if (excelData.get(rowNum).get(cellNum).get("style") != null) {
                    if ("th".equals(excelData.get(rowNum).get(cellNum).get("style"))) {
                        cell.setCellStyle(thStyle);
                    }
                    if ("td".equals(excelData.get(rowNum).get(cellNum).get("style"))) {

                        //表体
                        HSSFCellStyle tdStyle = wb.createCellStyle();
                        tdStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        //上下左右居中
                        tdStyle.setAlignment(HorizontalAlignment.CENTER);
                        tdStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        //边框
                        tdStyle.setBorderTop(BorderStyle.THIN);
                        tdStyle.setBorderRight(BorderStyle.THIN);
                        tdStyle.setBorderBottom(BorderStyle.THIN);
                        tdStyle.setBorderLeft(BorderStyle.THIN);
                        tdStyle.setTopBorderColor(IndexedColors.WHITE.getIndex());
                        tdStyle.setRightBorderColor(IndexedColors.WHITE.getIndex());
                        tdStyle.setBottomBorderColor(IndexedColors.WHITE.getIndex());
                        tdStyle.setLeftBorderColor(IndexedColors.WHITE.getIndex());

                        if (rowNum % 2 == 0) {
                            tdStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
                        } else {
                            tdStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        }
                        cell.setCellStyle(tdStyle);
                    }
                }

                //设置宽度
                sheet.setColumnWidth(cellNum, (excelData.get(rowNum).get(cellNum).get("value").length() + 20) * 256);
            }
        }

        HSSFPalette palette = wb.getCustomPalette();

        //获取配置的颜色
        palette.setColorAtIndex(IndexedColors.ORANGE.getIndex(),
                (byte) 97, //RGB red (0-255)
                (byte) 191, //RGB green
                (byte) 130 //RGB blue
        );
        palette.setColorAtIndex(IndexedColors.BLUE.getIndex(),
                (byte) 233, //RGB red (0-255)
                (byte) 244, //RGB green
                (byte) 232 //RGB blue
        );
        try (OutputStream fileOut = new FileOutputStream(filePath + fileName)) {
            wb.write(fileOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<List<Map<String, String>>> getExcelData(String tableHtml) {

        Document document = Jsoup.parse(tableHtml);

        //取得表体的html
        Elements tbody = document.select("tbody").select("tr");

        //获取table的最大的列数
        List<Integer> tdSize = new ArrayList<>();
        for (Element element : tbody) {
            tdSize.add(element.select("td").size());
        }
        Collections.sort(tdSize);

        //最后一行就是整个表格最大的列数
        int rowCellNum = tdSize.get(tdSize.size() - 1);

        //整个table的<tr>
        Elements trAll = document.select("tr");
        //tr就是整个表格的行数
        int excelDataSize = trAll.size();

        //存放表格数据
        List<List<Map<String, String>>> excelData = new ArrayList<>();

        //先创建空的excel数据
        for (int i = 0; i < excelDataSize; i++) {
            excelData.add(new ArrayList<>());
            for (int j = 0; j < rowCellNum; j++) {
                excelData.get(i).add(new HashMap<>());
            }
        }

        //按行数进行循环
        for (int rowNum = 0; rowNum < excelData.size(); rowNum++) {
            //取得每一行的html
            Element element = trAll.get(rowNum);

            //这一行的表头
            Elements th = element.select("th");
            Elements td = element.select("td");

            //列数
            int index = 0;

            //循环一行的数据
            for (int cellNum = 0; cellNum < excelData.get(rowNum).size(); cellNum++) {

                //只有value==null的才是没有被赋值的,因为跨行跨列的已经赋值过了
                if (excelData.get(rowNum).get(cellNum).get("value") == null) {
                    //取一个单元格对象
                    Element cell = null;
                    //样式的标识
                    String style = "";

                    //表头th处理
                    if (th.size() != 0) {
                        //这是表头的样式
                        style = "th";
                        excelData.get(rowNum).get(cellNum).put("style", style);
                        if (th.size() > index) cell = th.get(index);
                    }

                    //表体td处理
                    if (td.size() != 0) {
                        //这是表体的样式
                        style = "td";
                        excelData.get(rowNum).get(cellNum).put("style", style);
                        if (td.size() > index) cell = td.get(index);
                    }
                    //单元格的值
                    String value = cell == null ? "" : cell.text();

                    //处理跨行跨列
                    String cellHtml = cell == null ? "" : cell.outerHtml();
                    //rowspan和colspan都有值的情况下
                    if ((cellHtml.indexOf("rowspan") != -1 && cellHtml.indexOf("\"\"") == -1) || (cellHtml.indexOf("colspan") != -1 && cellHtml.indexOf("\"\"") == -1)) {
                        String rowspanValue = "0";
                        String colspanValue = "0";

                        //rowspan有值 取值并给到单元格对象
                        if (cellHtml.indexOf("rowspan") != -1 && cellHtml.indexOf("\"\"") == -1) {
                            String rowspanValueStr = cellHtml.substring(cellHtml.indexOf("rowspan") + 7);
                            rowspanValueStr = rowspanValueStr.substring(rowspanValueStr.indexOf("\"") + 1);
                            rowspanValueStr = rowspanValueStr.substring(0, rowspanValueStr.indexOf("\""));
                            rowspanValue = rowspanValueStr;
                            excelData.get(rowNum).get(cellNum).put("rowspanValue", rowspanValue);
                        }
                        //colspan有值 取值并给到单元格对象
                        if (cellHtml.indexOf("colspan") != -1 && cellHtml.indexOf("\"\"") == -1) {
                            String colspanValueStr = cellHtml.substring(cellHtml.indexOf("colspan") + 7);
                            colspanValueStr = colspanValueStr.substring(colspanValueStr.indexOf("\"") + 1);
                            colspanValueStr = colspanValueStr.substring(0, colspanValueStr.indexOf("\""));
                            colspanValue = colspanValueStr;
                            excelData.get(rowNum).get(cellNum).put("colspanValue", colspanValue);
                        }

                        //这个单元格又跨行又跨列
                        if (Integer.parseInt(rowspanValue) > 0 && Integer.parseInt(colspanValue) > 0) {

                            //把他跨列的单元格给赋上值 , 没有赋值就是null  ,  和559行的逻辑呼应
                            for (int i = 1; i < Integer.parseInt(rowspanValue); i++) {
                                excelData.get(rowNum + i).get(cellNum).put("value", value);
                                excelData.get(rowNum + i).get(cellNum).put("style", style);

                                //把他跨行的单元格给赋上值 , 没有赋值就是null  ,  和559行的逻辑呼应
                                for (int j = 1; j < Integer.parseInt(colspanValue); j++) {
                                    excelData.get(rowNum).get(cellNum + j).put("value", value);
                                    excelData.get(rowNum).get(cellNum + j).put("style", style);
                                    excelData.get(rowNum + i).get(cellNum + j).put("value", value);
                                    excelData.get(rowNum + i).get(cellNum + j).put("style", style);
                                }
                            }
                        }
                        //跨行或者跨列
                        else {
                            //跨列
                            if (Integer.parseInt(rowspanValue) > 0) {
                                //把他跨列的单元格给赋上值 , 没有赋值就是null  ,  和559行的逻辑呼应
                                for (int i = 1; i < Integer.parseInt(rowspanValue); i++) {
                                    excelData.get(rowNum + i).get(cellNum).put("value", value);
                                    excelData.get(rowNum + i).get(cellNum).put("style", style);
                                }
                            }
                            if (Integer.parseInt(colspanValue) > 0) {
                                //把他跨行的单元格给赋上值 , 没有赋值就是null  ,  和559行的逻辑呼应
                                for (int j = 1; j < Integer.parseInt(colspanValue); j++) {
                                    excelData.get(rowNum).get(cellNum + j).put("value", value);
                                    excelData.get(rowNum).get(cellNum + j).put("style", style);
                                }
                            }
                        }
                    }
                    excelData.get(rowNum).get(cellNum).put("value", value);
                    index = index + 1;
                }
            }
        }
        return excelData;
    }
    public String toExcelImpl(String name) throws IOException {
        String fileName = UploadImg.getRandom() + ".xls";
        File directory = new File("");
        String file= directory.getCanonicalPath();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file+onself.utilConfig.getDevelopPath2()+name)));
        String text=null;
        StringBuilder html=new StringBuilder();
        while ((text=reader.readLine())!=null){
            html.append(text);
        }
        try {
            HtmlToExcel.toExcel(html.toString(), "测试", file+onself.utilConfig.getDevelopPath1(),fileName);
            DownloadHtml downloadHtml=new DownloadHtml();
            downloadHtml.clearModel();
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
