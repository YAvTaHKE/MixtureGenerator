
import java.util.ArrayList;

public class MixtureGenerator {
    //База по сырьевым материалам
    private static ArrayList<RawMaterial> rawList = new ArrayList();
    private static final String sourceBookPath = "C:\\GIT\\JavaRush\\MixtureGenerator\\src\\main\\resources\\RM.xls";
    private static final String destBookPath = "C:\\GIT\\JavaRush\\MixtureGenerator\\src\\main\\resources\\destRM.xls";

    public static void main(String[] args) {

        ExcelManager excelManager = new ExcelManager(sourceBookPath, destBookPath);
        //Вывести данные в консоль
        excelManager.showFileInConsole(0, "|%-19s|");
        //Получить список экземпляров сырьевых материалов
        rawList = excelManager.getRawMaterials(0, 2);
        //Вывести список в консоль
        rawList.forEach(rm -> System.out.println(rm));
        //записать новый Excel файл с данными
        excelManager.createXLS(rawList, 4, 2);

    }
}
