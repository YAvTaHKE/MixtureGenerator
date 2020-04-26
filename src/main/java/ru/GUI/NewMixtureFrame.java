package ru.GUI;

import ru.GUI.Utilities.HtmlStyler;
import ru.MixtureGenerator;
import ru.Oxide;
import ru.RawMaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.Map;

public class NewMixtureFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JScrollPane scrollPane_1;
    private JScrollPane scrollPane_2;
    private JTable table_1;
    private JTable table_2;
    private JButton button;

    // Название столбцов
    private String[] columnsName = {
            "Сырье",
            "Доля от массы, %",
            "<html>Цена BK5, RMB</html>",
            "Цена BK6, RMB",
            "Доля от объема, %",
            "Объем, м3",
            "Масса, кг",
            "Плотность, кг/м\u00B3",
            "MgO", "Al\u2082O\u2083",
            "SiO\u2082",
            "CaO",
            "Fe\u2082O\u2083",
            "TiO\u2082",
            "C",
            "LOI"};
    private final int DEFAULT_COLUMS_COUNT = columnsName.length;
    private final int DEFAULT_ROW_COUNT = 11;
    // Данные для таблицы по умолчанию
    private Object[][] defaultTableData = new Object[DEFAULT_ROW_COUNT][DEFAULT_COLUMS_COUNT];

    public NewMixtureFrame() throws HeadlessException {

        //настройки фрейма
        this.setTitle("New mixture");
        this.setDefaultCloseOperation((WindowConstants.DISPOSE_ON_CLOSE));
        this.setResizable(true);
        this.setSize(829, 800); //размеры фрейма
        this.setLocationRelativeTo(null); //окно в центре экрана

        //контейнер верхнего уровня
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);
        contentPane.setLayout(null);//абсолютное позиционирование по координатам внутри контейнера

        //легковесный контейнер
        scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(5, 5, 800, 220);
        contentPane.add(scrollPane_1);

        scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(5, 300, 800, 220);
        contentPane.add(scrollPane_2);

        initDefaultData(); //Создание данный для таблицы по умолчанию
        columnsName = htmlFormatter(columnsName); //Форматирование наименований колонок

        table_1 = new JTable(new DefaultTableModel(defaultTableData, columnsName)); //Основная таблица
        initTableView(table_1); //настройка стиля таблицы
        setComboEditor(0); //установка раскрывающегостя списка в 0 колонку
        addModelListener(table_1); //установка слушателя событий
        scrollPane_1.setViewportView(table_1); //добавление таблицы в scrollPane

        table_2 = new JTable(new DefaultTableModel(defaultTableData, columnsName));
        initTableView(table_2);
        scrollPane_2.setViewportView(table_2); //добавление таблицы в scrollPane

        String name = "Button\nButton";
        button = new JButton("<html>" + name.replaceAll("\\n", "<br>") + "</html>");
        //button.setSize(50, 100);
        button.setBounds(5, 230, 100, 50);
        contentPane.add(button);
    }

    //Инициализация таблицы по умолчанию
    private void initDefaultData(){
        for (int i = 0; i < DEFAULT_ROW_COUNT; i++){
            RawMaterial rm = MixtureGenerator.rawList.get(i);
            int j = 0;
            defaultTableData[i][j++] = rm;
            defaultTableData[i][j++] = rm.getPrice();
            defaultTableData[i][j++] = String.valueOf(0); //proportion
        }
    }

    //Установить стиль наименования колонок в HTML формате
    private String[] htmlFormatter(String[] strings){
        String[] str = new String[strings.length];

        for (int i = 0; i < strings.length; i++) {
            HtmlStyler style = new HtmlStyler(strings[i]);
            style.center();
            style.bold();
            str[i] = style.getHtmlString();
        }
        return str;
    }

    //Инициализация таблицы
    private void initTableView(JTable table){

        //Выровнить наименования колонок "по цетру"
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        //Выровнить данные в таблице "по центру"
        table.setDefaultRenderer(table.getColumnClass(1), new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.setHorizontalAlignment(SwingConstants.CENTER);
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return this;
            }
        });


        // Автоматическая установка ширины столбцов
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader th = table.getTableHeader();
        System.out.println(th.getDefaultRenderer());

        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int prefWidth =
                    Math.round(
                            (float) th.getFontMetrics(
                                    th.getFont()).getStringBounds(th.getTable().getColumnName(i).replaceAll("\\<.*?\\>", ""),
                                    th.getGraphics()
                            ).getWidth()
                    );

            if (prefWidth > 40)
                column.setPreferredWidth(prefWidth + 10);
            else column.setPreferredWidth(50);
        }


        //Заполнять форму таблицей
        table.setFillsViewportHeight(true);
        //Увеличить высоту строки заголовка
        //table.getTableHeader().setPreferredSize(new Dimension(0, 60));
    }

    private void setComboEditor(int columnNum){

        JComboBox<RawMaterial> combo = new JComboBox<RawMaterial>(MixtureGenerator.rawList.toArray(new RawMaterial[MixtureGenerator.rawList.size()]));
        // Редактор ячейки с раскрывающимся списком
        DefaultCellEditor editor = new DefaultCellEditor(combo);
        // Определение редактора ячеек для первой колонки
        table_1.getColumnModel().getColumn(columnNum).setCellEditor(editor);
    }

    private void addModelListener(JTable table) {
        table.getModel().addTableModelListener(
                new TableModelListener()
                {
                    @Override
                    public void tableChanged(TableModelEvent e) {
                        System.out.println("Тип события - " + e.getType() + " Column - " + e.getColumn() + " Row - " + e.getFirstRow());
                        switch (e.getColumn()) {
                            case 0:
                                updateDataRow(e.getFirstRow());
                                break;
                            case 2:
                                try {
                                    Double.valueOf((String) table.getModel().getValueAt(e.getFirstRow(), e.getColumn()));
                                } catch (NumberFormatException exc) {
                                    JOptionPane.showMessageDialog(NewMixtureFrame.this, "Не правильный формат данных. Введите числовое значение.");
                                }
                                updateDataRow(e.getFirstRow());
                                break;
                        }
                    }
                });
    }

    //Обновление данных в таблице
    public void updateDataRow(int row) {
        RawMaterial rm = (RawMaterial) table_1.getModel().getValueAt(row, 0);
        TableModel model = table_1.getModel();
            int j = 1;
            model.setValueAt(rm.getPrice(), row, j++);
            String pr = (String) model.getValueAt(row, j++);
            Double proportion = Double.valueOf(pr);
            Double mass = 1000*proportion/100;
            model.setValueAt(mass, row, j++);
            model.setValueAt(mass/rm.getBD(), row, j++);
            model.setValueAt(rm.getBD(), row, j++);

            Map<Oxide, Double> map = rm.getChemicalAnalysis();

            for (Oxide oxide : Oxide.values()) {
                model.setValueAt(map.get(oxide)*proportion/100, row, j++);
            }
    }
}
