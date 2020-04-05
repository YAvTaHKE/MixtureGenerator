
import java.util.ArrayList;

public class MixtureGenerator {
    //База по сырьевым материалам
    private static ArrayList<RawMaterial> rawList = new ArrayList();
    private static final String bookPath = "C:\\GIT\\JavaRush\\MixtureGentrator\\src\\main\\resources\\RM.xls";

    public static void main(String[] args) {

        ExcelManager excelManager = new ExcelManager(bookPath, 2);
        //Вывести данные в консоль
        excelManager.showFileInConsole(0, "|%-19s|");
        //Получить список экземпляров сырьевых материалов
        rawList = excelManager.getRawMaterials(0, 2);
        //Вывести список в консоль
        rawList.forEach(rm -> System.out.println(rm));
    }
}
