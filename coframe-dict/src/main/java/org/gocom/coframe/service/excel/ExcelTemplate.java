package org.gocom.coframe.service.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

public class ExcelTemplate {
    private static Logger log = LoggerFactory.getLogger(ExcelTemplate.class);

    /**
     * Excel模板定义的输出字段名数组
     */
    private String[] fieldNames;

    /**
     * 输出的起始行,默认为-1,不输出
     */
    private int startRow = -1;

    /**
     * 默认字体大小
     */
    @SuppressWarnings("unused")
	private int fontSize = 10;

    /**
     * 默认字体
     */
    @SuppressWarnings("unused")
	private String fontName = "宋体";

    /**
     * 是否设置信息标题栏边框,默认情况不设置边框
     */
    private boolean titleCellBold = false;

    /**
     * 是否设置空白栏边框，默认情况不设置边框
     */
    private boolean blankCellBold = false;

    private HSSFCellStyle borderStyle = null;

    private HSSFCellStyle noneStyle = null;

    /**
     * 关键字 &-表示模版信息内容字段 #-表示模版明细内容字段
     */
    private final String TITLE_FLAG = "&";
    private final String CONTENT_FLAG = "#";
    private final String FIELD_AUTO_ID = "_id";
    private int autoRowId = 1; // 默认行号
    private POIFSFileSystem poiFileSystem = null;

    private HSSFWorkbook hssfWorkbook = null;
    private HSSFSheet sheet = null;

    /**
     * 默认构造函数
     */
    public ExcelTemplate(Resource excelTpl) throws IOException {
        InputStream inputStream = excelTpl.getInputStream();

        this.poiFileSystem = new POIFSFileSystem(inputStream);
        this.hssfWorkbook = new HSSFWorkbook(this.poiFileSystem);

        /**
         * 初始化工作模版，获取模版配置起始行(start)以及对应字段填充位置(fieldNames) 调用此方法前需要设置fs 和 sheet 如果
         * sheet为空则取 设置sheet=wb.getSheetAt(0)
         */
        if (sheet == null) {
            setSheet(this.hssfWorkbook.getSheetAt(0));
        }
        initialize(sheet);
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public HSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }

    /**
     * 设置标题栏是否需要边框
     */
    public void setTitleCellBold(boolean titleCellBold) {
        this.titleCellBold = titleCellBold;
    }

    /**
     * 设置空白行是否需要显示边框
     *
     * @param blankCellBold
     */
    public void setBlankCellBold(boolean blankCellBold) {
        this.blankCellBold = blankCellBold;
    }

    /**
     * 设置字体大小，默认10号字体
     *
     * @param size
     */
    public void setFontSize(int size) {
        this.fontSize = size;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    /**
     * 初始化工作模版，获取模版配置起始行(start)以及对应字段填充位置(fieldNames)
     *
     * @param sheet
     */
	private boolean initialize(HSSFSheet sheet) {
        boolean setStart = false;
        try {
            if (sheet != null) {
                int rows = sheet.getPhysicalNumberOfRows();
                for (int r = 0; r < rows; r++) {
                    HSSFRow row = sheet.getRow(r);

                    if (row != null) {
                        int cells = row.getPhysicalNumberOfCells();
                        for (short c = 0; c < cells; c++) {
                            HSSFCell cell = row.getCell(c);
                            if (cell != null) {
                                String value = null;
                                if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                                    value = "" + cell.getNumericCellValue();
                                } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                                    value = "" + cell.getBooleanCellValue();
                                } else {
                                    value = cell.getRichStringCellValue().getString();
                                }
                                if (value != null && !"".equals(value)) {
                                    value = value.trim();
                                    // 内容数据
                                    if (value.startsWith(CONTENT_FLAG)) {
                                        if (!setStart) {
                                            this.startRow = r;// 设置内容填充起始行，从字段设置行的下一行开始
                                            this.fieldNames = new String[cells];
                                            setStart = true;
                                        }
                                        this.fieldNames[c] = value.substring(1);// 初始化内容字段
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            setStart = false;
        }
        return setStart;
    }

    /**
     * 生成填充模版标题数据
     *
     * @throws Exception
     */
	public void generateTitleData(String title) {
        HSSFSheet sheet = getSheet();
        int rows = sheet.getPhysicalNumberOfRows();

        for (int r = 0; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);
            if (row != null) {
                int cells = row.getPhysicalNumberOfCells();
                for (short c = 0; c < cells; c++) {
                    HSSFCell cell = row.getCell(c);
                    if (cell != null) {
                        if (cell.getCellTypeEnum() == CellType.STRING) {
                            String value = cell.getRichStringCellValue().getString();
                            if (value != null) {
                                value = value.trim();
                                if (value.startsWith(TITLE_FLAG)) {
                                    if (title == null) {
                                        title = "";
                                    }
                                    // 重建Cell，填充标题值
                                    cell = row.createCell((short) c);
                                    cell.setCellType(CellType.STRING);
                                    cell.setCellValue(new HSSFRichTextString(title));

                                    if (!titleCellBold) {
                                        cell.setCellStyle(noneStyle);
                                    } else {
                                        cell.setCellStyle(borderStyle);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * 将指定的对象数组resulset输出到指定的Excel位置
     */
    public void generateContentData(List<ExcelDictLine> resultSet) {
        HSSFSheet sheet = getSheet();
        for (ExcelDictLine content : resultSet) {
            autoRowId++;
            startRow++;
            HSSFRow row = sheet.createRow(startRow);
            genOneLine(row, content);
            shiftDown(sheet, startRow, sheet.getLastRowNum(), 1);
        }
    }

    /**
     * 将指定的对象数组resulset输出到指定的Excel位置
     */
    public void generateDictData(List<ExcelDictLine> resultSet) {
        HSSFSheet sheet = getSheet();
        for (ExcelDictLine dict : resultSet) {
            HSSFRow row = sheet.createRow(startRow);
            genOneLine(row, dict);
            shiftDown(sheet, startRow, sheet.getLastRowNum(), 0);
            autoRowId++;
            startRow++;
        }
    }

    /**
     * 生成一行excel数据
     *
     * @param row
     */
    private void genOneLine(HSSFRow row, ExcelDictLine dict) {
        BeanWrapper dictBean = PropertyAccessorFactory.forBeanPropertyAccess(dict);
        for (int i = 0; i < fieldNames.length; i++) {
            // 输出自动生成的行号
            if (fieldNames[i] != null && fieldNames[i].equals(FIELD_AUTO_ID)) {
                HSSFCell cell = row.createCell((short) i);
                cell.setCellStyle(borderStyle);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(autoRowId);
                continue;
            }

            if (fieldNames[i] != null) {
                HSSFCell cell = row.createCell((short) i);
                cell.setCellStyle(borderStyle);
                if (dict != null) {
                    Object value = dictBean.getPropertyValue(fieldNames[i]);
                    if (value != null) {
                        if (value instanceof Double || value instanceof BigDecimal) {
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(Double.parseDouble(value.toString()));
                        } else {
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(new HSSFRichTextString(value.toString()));
                        }
                    } else {
                        cell.setCellType(CellType.BLANK);
                    }

                } else {

                    cell.setCellType(CellType.BLANK);
                    if (!blankCellBold) {
                        cell.setCellStyle(noneStyle);
                    } else {
                        cell.setCellStyle(borderStyle);
                    }
                }
            }
        }
    }

    public void writeToStream(OutputStream out) throws IOException {
        hssfWorkbook.write(out);
    }

//    private HSSFCellStyle getBorderStyle(HSSFWorkbook wb) {
//        HSSFCellStyle style = wb.createCellStyle();
//        HSSFFont font = wb.createFont();
//        font.setFontHeightInPoints((short) fontSize);
//        font.setFontName(fontName);
//        style.setFont(font);
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
//        return style;
//    }

//    private HSSFCellStyle getNoneStyle(HSSFWorkbook wb) {
//        HSSFCellStyle style = wb.createCellStyle();
//        HSSFFont font = wb.createFont();
//        font.setFontHeightInPoints((short) fontSize);
//        font.setFontName(fontName);
//        style.setFont(font);
//        style.setBorderBottom(BorderStyle.NONE);
//        style.setBorderLeft(BorderStyle.NONE);
//        style.setBorderRight(BorderStyle.NONE);
//        style.setBorderTop(BorderStyle.NONE);
//        return style;
//    }

    /**
     * 向下平推表格，并复制格式与内容
     *
     * @param thisrow：当前行号
     * @param lastrow：最后行号
     * @param shiftcount：平推量
     */
    private void shiftDown(HSSFSheet sheet, int thisrow, int lastrow, int shiftcount) {
        sheet.shiftRows(thisrow, lastrow, shiftcount);

        for (int z = 0; z < shiftcount; z++) {
            HSSFRow row = sheet.getRow(thisrow);
            HSSFRow oldrow = sheet.getRow(thisrow + shiftcount);
            // 将各行的行高复制
            oldrow.setHeight(row.getHeight());
            // 将各个单元格的格式复制
            for (short i = 0; i <= oldrow.getPhysicalNumberOfCells(); i++) {

                HSSFCell cell = row.createCell(i);
                HSSFCell oldcell = oldrow.getCell(i);

                if (oldcell != null) {
                    switch (oldcell.getCellTypeEnum()) {
                        case STRING:
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(oldcell.getRichStringCellValue());
                            break;
                        case NUMERIC:
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(oldcell.getNumericCellValue());
                            break;
                        default:
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(oldcell.getRichStringCellValue());

                    }
                    cell.setCellStyle(oldcell.getCellStyle());
                }
            }

            // 将有列跨越的复制
            List<CellRangeAddress> regions = findRegion(sheet, oldrow);
            if (regions.size() != 0) {
                for (CellRangeAddress range : regions) {
                    range.setFirstRow(row.getRowNum());
                    range.setLastRow(row.getRowNum());
                    sheet.addMergedRegion(range);
                }
            }
            thisrow++;
        }
    }

    /**
     * 查找所有的合并单元格
     *
     * @return
     */
    private List<CellRangeAddress> findRegion(HSSFSheet sheet, HSSFRow oldRow) {
        List<CellRangeAddress> regions = new ArrayList<>();
        int num = sheet.getNumMergedRegions();
        int curRowId = oldRow.getRowNum();
        for (int i = 0; i < num; i++) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            if (region.getFirstRow() == region.getLastRow() && region.getFirstRow() == curRowId) {
                regions.add(region);
            }
        }
        return regions;
    }

    /**
     * 将目标Excel文件的内容导入到默认数据源数据表
     *
     * @return 返回1 导入成功
     */
    @SuppressWarnings("resource")
	public ImportDict importDictData(InputStream excelFileInputStream, int submitCount) throws IOException {
        ImportDict importDict = new ImportDict();
        HSSFWorkbook source = new HSSFWorkbook(new POIFSFileSystem(excelFileInputStream));
        for (int sheetCount = 0; sheetCount < hssfWorkbook.getNumberOfSheets(); sheetCount++) {
            // 设置模板
            HSSFSheet sheet = hssfWorkbook.getSheetAt(sheetCount);
            setSheet(sheet);
            // 如果无法初始化，跳过当前sheet
            if (!initialize(sheet)) {
                continue;
            }
            // 设置输入文件信息
            HSSFSheet srcSheet = source.getSheetAt(sheetCount);
            int rows = srcSheet.getPhysicalNumberOfRows();
            // 从以#开头的的字段名行开始
            int cellNo = getDictCellNo(this.fieldNames);
            for (int rowCount = startRow + 1; rowCount < rows; rowCount++) {
                ExcelDictLine line = getDictLine(srcSheet.getRow(rowCount), cellNo);
                if (line != null && line.getDictTypeCode() != null) {
                    importDict.addLine(line);
                }
            }
        }
        return importDict;
    }

    /**
     * 批量保存数据
     *
     * @return
     */
//    private int saveData(List<ExcelDictLine> list) {
//        int ret = list.size();
//
//        //        DasEntityHelper.saveEntities(list.toArray(new DataObject[ret]));
//
//        // TODO SAVE OBJ
//        list.clear();
//
//        return ret;
//    }

    /**
     * 返回dictid所在列的序号 -1表示不存在
     *
     * @param fields
     * @return
     */
    private int getDictCellNo(String[] fields) {
        for (int i = 0; i < fields.length; i++) {
            if ("dictid".equals(fields[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取字典记录
     *
     * @param sourceRow
     * @param cellNo
     * @return
     */
    private ExcelDictLine getDictLine(HSSFRow sourceRow, int cellNo) {
        if (sourceRow != null) {
            ExcelDictLine line = new ExcelDictLine();
            // 以下构造导入的实体对象，并根据Excel单元格的内容填充实体属性值
            for (int cellCount = 0; cellCount < fieldNames.length; cellCount++) {
                String propertyName = fieldNames[cellCount];
                HSSFCell cell = sourceRow.getCell((short) cellCount);
                if (propertyName.equals("_id")) {
                    propertyName = "id";
                }
                setDataValue(line, propertyName, cell);
            }
            return line;
        } else {
            return null;
        }
    }

    /**
     * 设置一个属性值
     *
     * @param line
     * @param propertyName
     * @param cell
     * @return
     */
	private boolean setDataValue(ExcelDictLine line, String propertyName, HSSFCell cell) {
        BeanWrapper lineBean = PropertyAccessorFactory.forBeanPropertyAccess(line);
        if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
            return false;
        } else {

            String value = null;

            if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = dateFormat.format((cell.getDateCellValue()));

                } else {
                    value = String.valueOf((long) cell.getNumericCellValue());
                }
            } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                value = cell.getBooleanCellValue() + "";
            } else {
                value = cell.getRichStringCellValue().getString();
            }
            TypeDescriptor typeDescriptor = lineBean.getPropertyTypeDescriptor(propertyName);
            if (typeDescriptor.getType().isAssignableFrom(Integer.class)) {
                // 防止可能出现的Excel表格读取自动加.号
                if (value.contains(".")) {
                    value = value.substring(0, value.indexOf("."));
                }
                Integer integer = ChangeUtil.toInteger(value);
                if (integer != null) {
                    lineBean.setPropertyValue(propertyName, integer);
                } else {
                    log.trace("row {},property value is null: {}", cell.getRowIndex(), propertyName);
                }

            } else {
                if (StringUtils.isEmpty(value)) {
                    value = null;
                }
                lineBean.setPropertyValue(propertyName, value);
            }
        }

        return true;
    }
}