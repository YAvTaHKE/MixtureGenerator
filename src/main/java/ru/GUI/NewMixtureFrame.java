package ru.GUI;
;
import ru.MixtureGenerator;
import ru.Oxide;
import ru.RawMaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class NewMixtureFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JScrollPane scrollPane_1;
    private JScrollPane scrollPane_2;
    private JTable table_1;
    private JTable table_2;

    private final int DEFAULT_ROW_COUNT = 11;
    // Данные для таблицы по умолчанию
    private ArrayList<RawMaterial> tableData;

    public NewMixtureFrame() throws HeadlessException {

        initDefaultData(); //Создание данный для таблицы по умолчанию

        table_1 = new JTable();
        initTableView(table_1, new TableModel_1(tableData));
        setComboEditor(table_1,0); //установка раскрывающегостя списка в 0 колонку

        table_2 = new JTable();
        initTableView(table_2, new TableModel_2(tableData));

        //контейнер верхнего уровня
        contentPane = new JPanel(new GridLayout(2, 1));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);

        scrollPane_1 = new JScrollPane(table_1);
        scrollPane_1.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.add(makeTitledPanel("Расчет шихты", scrollPane_1));

        scrollPane_2 = new JScrollPane(table_2);
        scrollPane_2.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.add(makeTitledPanel("Данные по сырью", scrollPane_2));


        //addModelListener(table_1); //установка слушателя событий

        //настройки фрейма
        this.setTitle("New mixture");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize(829, 600); //размеры фрейма
        this.setLocationRelativeTo(null); //окно в центре экрана
        this.setVisible(true);
    }

    //Инициализация таблицы по умолчанию
    private void initDefaultData(){
        tableData = new ArrayList<>();

        for (int i = 0; i < DEFAULT_ROW_COUNT; i++){
            if (i >= MixtureGenerator.rawList.size()) {
                break;
            }
            tableData.add(MixtureGenerator.rawList.get(i));
        }
    }


    //Инициализация таблицы, важна последовательность
    private void initTableView(JTable table, TableModel tableModel){
        //Установить вид заголовка
        DefaultTableColumnModel tableColumnModel = new DefaultTableColumnModel() {
            public void addColumn(TableColumn column) {
                //This works, but is a bit kludgey as it creates an unused JTableHeader object for each column:
                column.setHeaderRenderer(new JTableHeader().getDefaultRenderer());
                super.addColumn(column);
            }
        };

        table.setColumnModel(tableColumnModel);
        table.setModel(tableModel);

        //Выровнить наименования колонок "по цетру"
        //((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

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
        //table.setFillsViewportHeight(true);

    }

    private void setComboEditor(JTable table, int columnNum){

        JComboBox<RawMaterial> combo = new JComboBox<RawMaterial>(MixtureGenerator.rawList.toArray(new RawMaterial[MixtureGenerator.rawList.size()]));
        // Определение редактора ячеек для первой колонки
        table.getColumnModel().getColumn(columnNum).setCellEditor(new DefaultCellEditor(combo));
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
            model.setValueAt(rm.getPriceBK5(), row, j++);
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

    private static JComponent makeTitledPanel(String title, JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(c);
        p.setBorder(BorderFactory.createTitledBorder(title));
        return p;
    }
}
