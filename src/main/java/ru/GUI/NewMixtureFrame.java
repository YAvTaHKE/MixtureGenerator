package ru.GUI;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.w3c.dom.ls.LSOutput;
import ru.MixtureGenerator;
import ru.Oxide;
import ru.RawMaterial;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.sql.SQLOutput;
import java.util.Map;

public class NewMixtureFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    // Название столбцов
    private String[] columns = { "Raw materials", "Price, RMB", "Proportion, %", "Weight, kg", "Volume, %", "BD, г/ см\u00B3", "MgO", "Al\u2082O\u2083", "SiO\u2082", "CaO", "Fe\u2082O\u2083", "TiO\u2082", "C", "LOI"};
    private final int DEFAULT_COLUMS_COUNT = columns.length;
    private final int DEFAULT_ROW_COUNT = 11;


    // Данные для таблицы по умолчанию
    private Object[][] defaultData = new Object[DEFAULT_ROW_COUNT][DEFAULT_COLUMS_COUNT];

    JTable table;


    public NewMixtureFrame() throws HeadlessException {
        super("New mixture");
        this.setDefaultCloseOperation((WindowConstants.DISPOSE_ON_CLOSE));
        this.setResizable(true);
        this.setLocationRelativeTo(null); //окно в центре экрана
        setSize(830, 330);

        initDefaultData();
        initTable();
    }
    //Инициализация таблицы по умолчанию
    private void initDefaultData(){
        for (int i = 0; i < DEFAULT_ROW_COUNT; i++){
            RawMaterial rm = MixtureGenerator.rawList.get(i);
            int j = 0;
            defaultData[i][j++] = rm;
            defaultData[i][j++] = rm.getPrice();
            defaultData[i][j++] = String.valueOf(0); //proportion
        }
    }
    //Инициализация таблицы
    private void initTable(){

        // Создание модели таблицы
        DefaultTableModel model = new DefaultTableModel(defaultData, columns) {
            private static final long serialVersionUID = 1L;

            // Функция получения типа столбца
            /*public Class<?> getColumnClass(int column) {
                return defaultData[0][column].getClass();
            }*/

        };
        // Создание таблицы
        table = new JTable(model);

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

        // Установка ширины столбцов
        setColumnsWidth(table);
        // Раскрывающийся список из объектов RawMaterials

        JComboBox<RawMaterial> combo = new JComboBox<RawMaterial>(MixtureGenerator.rawList.toArray(new RawMaterial[MixtureGenerator.rawList.size()]));

        // Редактор ячейки с раскрывающимся списком
        DefaultCellEditor editor = new DefaultCellEditor(combo);
        // Определение редактора ячеек для первой колонки
        table.getColumnModel().getColumn(0).setCellEditor(editor);
        //Заполнять форму таблицей
        //table.setFillsViewportHeight(false);
        // Вывод окна на экран
        getContentPane().add(new JScrollPane(table));

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
                                    Double.valueOf((String) model.getValueAt(e.getFirstRow(), e.getColumn()));
                                } catch (NumberFormatException exc) {
                                    JOptionPane.showMessageDialog(NewMixtureFrame.this, "Не правильный формат данных. Введите числовое значение.");
                                }
                                updateDataRow(e.getFirstRow());
                                break;
                        }
                    }
                });
    }

    //Автоматическая настройка ширины столбцов по содержимому
    public final static void setColumnsWidth(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader th = table.getTableHeader();
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int prefWidth =
                    Math.round(
                            (float) th.getFontMetrics(
                                    th.getFont()).getStringBounds(th.getTable().getColumnName(i),
                                    th.getGraphics()
                            ).getWidth()
                    );

            if (prefWidth > 40)
            column.setPreferredWidth(prefWidth + 10);
            else column.setPreferredWidth(50);
        }
    }

    public void updateDataRow(int row) {
        RawMaterial rm = (RawMaterial) table.getModel().getValueAt(row, 0);
        TableModel model = table.getModel();
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
