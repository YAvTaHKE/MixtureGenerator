package ru.GUI;
;
import ru.MixtureGenerator;
import ru.Oxide;
import ru.RawMaterial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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

    public NewMixtureFrame() throws HeadlessException {

        table_1 = new JTable();
        initTableView(table_1, new TableModel_1(MixtureGenerator.rawList));
        setComboEditor(table_1,0); //установка раскрывающегостя списка в 0 колонку

        table_2 = new JTable();
        initTableView(table_2, new TableModel_2(MixtureGenerator.rawList));

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


        addModelListener(table_1); //установка слушателя событий

        //настройки фрейма
        this.setTitle("New mixture");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize(925, 600); //размеры фрейма
        this.setLocationRelativeTo(null); //окно в центре экрана
        this.setVisible(true);
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
        table.setDefaultRenderer(Double.class, new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.setHorizontalAlignment(SwingConstants.CENTER);
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                return this;
            }
        });

        // Автоматическая установка ширины столбцов
        /*table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
        }*/

        //Ручная установка ширины колонки
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < table.getColumnCount(); i++) {
            int colWidth;
            switch (i) {
                case 0: {colWidth = 80; break;}
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:{colWidth = 60; break;}
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15: {colWidth = 40; break;}
                default: {colWidth = 40;}
            }
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(colWidth);
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
                            case 0: {
                                RawMaterial rm = (RawMaterial) table.getModel().getValueAt(e.getFirstRow(), 0);
                                System.out.println(rm.getBD());
                                updateDataRow(e.getFirstRow());
                                break;
                            }
                            case 1: {
                                updateDataRow(e.getFirstRow());
                                break;
                            }
                        }
                    }
                });
    }

    //Обновление данных в таблице
    public void updateDataRow(int row) {
        RawMaterial rm = (RawMaterial) table_1.getModel().getValueAt(row, 0);
        TableModel model = table_1.getModel();

        Double proportion = (Double) model.getValueAt(row, 1);
        double mass = 1000*proportion/100;
        Double volume = mass / rm.getBD();

        model.setValueAt(rm.getPriceBK5(), row, 2); //Цена BK5, RMB
        model.setValueAt(rm.getPriceBK6(), row, 3); //Цена BK6, RMB
        model.setValueAt(0.0D, row, 4); //Доля от объема, %
        model.setValueAt(volume, row, 5); //Объем, м3
        model.setValueAt(mass, row, 6); //Масса, кг
        model.setValueAt(rm.getBD(), row, 7); //BD
        int j = 8;
        //Химия
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
