import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelManager {

    //Excel книга
    private HSSFWorkbook bookRM;

    public ExcelManager(String bookPath, int headRow){
        try {
        File bookFile = new File(bookPath);
        bookRM = (HSSFWorkbook) WorkbookFactory.create(bookFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EncryptedDocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //выводит в консоль содержание страницы книги
    public void showFileInConsole(int sheetNum, String format){

        HSSFSheet sheet = bookRM.getSheetAt(sheetNum);
        // Автоматическое определение граничных строк обработки (по наличию информации)
        int rowStart = sheet.getFirstRowNum();
        int rowEnd = sheet.getLastRowNum();

        //перебираем все ячейки установленной области
        for (int rw = rowStart; rw < rowEnd; rw++) {
            HSSFRow row = sheet.getRow(rw);
            if (row == null) {
                continue;
            }
            //Вывод строки в консоль
            short minCol = row.getFirstCellNum();
            short maxCol = row.getLastCellNum();

            for (short col = minCol; col < maxCol; col++) {
                HSSFCell cell = row.getCell(col);
                if (cell == null) {
                    continue;
                }
                DataFormatter formatter = new DataFormatter();
                System.out.print(String.format(format, formatter.formatCellValue(cell)));
            }
            System.out.println();
        }
    }

    //Возвращает совокупность объектов сырьевых материалов на странице книги
    public ArrayList<RawMaterial> getRawMaterials (int sheetNum, int headRow){
        ArrayList<RawMaterial> rawMaterials = new ArrayList<>();

        HSSFSheet sheet = bookRM.getSheetAt(sheetNum);
        // Устанавливаем границы данных
        int rowStart = sheet.getFirstRowNum() + headRow;
        int rowEnd = sheet.getLastRowNum();

        //перебираем все ячейки установленной области
        for (int rw = rowStart; rw < rowEnd; rw++) {
            HSSFRow row = sheet.getRow(rw);
            if (row == null) {
                continue;
            }
            rawMaterials.add(newRM(row));
        }
        return rawMaterials;
    }

    //Формирует объект сырьевого материала
    private RawMaterial newRM (HSSFRow row) {
        DataFormatter formatter = new DataFormatter();
        //номер первой колонки
        short minCol = row.getFirstCellNum();

        RawMaterial rawMaterial = new RawMaterial();

        rawMaterial.setName(formatter.formatCellValue(row.getCell(minCol++)));

        try {
            rawMaterial.setPrice(Integer.parseInt(formatter.formatCellValue(row.getCell(minCol++))));
        } catch (NumberFormatException e) {
            rawMaterial.setPrice(0);
        }

        rawMaterial.setChemicalAnalysis(row, minCol++);

        try {
            rawMaterial.setBD(Double.valueOf(formatter.formatCellValue(row.getCell(minCol))));
        } catch (NumberFormatException e) {
            rawMaterial.setBD(0d);
        }
        return rawMaterial;
    }
}
