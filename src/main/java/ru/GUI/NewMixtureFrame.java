package ru.GUI;
;
import ru.GUI.Listeners.Table_1_Listener;
import ru.GUI.Listeners.Table_3_Listener;
import ru.GUI.Utilities.DefaultMixtures;
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
    private JScrollPane scrollPane_3;
    private JTable table_1;
    private JTable table_2;
    private JTable table_3;
    private Table_1_Listener table_1_listener;
    private Table_3_Listener table_3_listener;

    public NewMixtureFrame() throws HeadlessException {

        table_1 = new JTable();
        initTableView(table_1, new TableModel_1(DefaultMixtures.MC(MixtureGenerator.rawList)));
        setComboEditor(table_1,0); //установка раскрывающегостя списка в 0 колонку


        table_2 = new JTable(new TableModel_2(DefaultMixtures.MC(MixtureGenerator.rawList)));
        //initTableView(table_2, new TableModel_2(DefaultMixtures.MC(MixtureGenerator.rawList)));

        table_3 = new JTable(new TableModel_3());
        table_3.getColumnModel().getColumn(0).setPreferredWidth(200);

        table_1_listener = new Table_1_Listener(table_1, table_3);
        table_1_listener.updateSummary();
        table_1_listener.updateVolume();
        table_3_listener = new Table_3_Listener(table_1, table_3);
        table_1_listener.updateTable_3();
        
        //контейнер верхнего уровня
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.setContentPane(contentPane);

        JPanel panel = new JPanel(new BorderLayout());

        scrollPane_1 = new JScrollPane(table_1);
        scrollPane_1.setBorder(new EmptyBorder(5,5,5,5));
        scrollPane_1.setPreferredSize(new Dimension(400, 250));
        scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        panel.add(makeTitledPanel("Расчет шихты", scrollPane_1), BorderLayout.SOUTH);


        scrollPane_3 = new JScrollPane(table_3);
        scrollPane_3.setBorder(new EmptyBorder(5,5,5,5));
        scrollPane_3.setPreferredSize(new Dimension(0, 70));
        panel.add(makeTitledPanel("Хим-состав изделия", scrollPane_3), BorderLayout.NORTH);

        contentPane.add(panel, BorderLayout.NORTH);

        scrollPane_2 = new JScrollPane(table_2);
        scrollPane_2.setBorder(new EmptyBorder(5,5,5,5));
        scrollPane_2.setPreferredSize(new Dimension(400, 200));
        contentPane.add(makeTitledPanel("Данные по сырью", scrollPane_2), BorderLayout.CENTER);

        table_1.getModel().addTableModelListener(table_1_listener);
        table_3.getModel().addTableModelListener(table_3_listener);

        table_3.setDefaultRenderer(Double.class, new CustomCellRendererTable_3(2));

        //настройки фрейма
        this.setTitle("New mixture");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize(925, 660); //размеры фрейма
        this.setLocationRelativeTo(null); //окно в центре экрана
        this.setVisible(true);
    }

    //Инициализация таблицы, важна последовательность
    private void initTableView(JTable table, TableModel tableModel){
        //Установить вид заголовка
        DefaultTableColumnModel tableColumnModel = new DefaultTableColumnModel() {
            public void addColumn(TableColumn column) {
                column.setHeaderRenderer(new JTableHeader().getDefaultRenderer());
                super.addColumn(column);
            }
        };

        table.setColumnModel(tableColumnModel);
        table.setModel(tableModel);

        //Установить свой рендер на отображение ячеек
        table.setDefaultRenderer(Double.class, new CustomCellRendererTable_1(1));

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

        ArrayList<RawMaterial> list = new ArrayList<>();
        MixtureGenerator.rawList.forEach((k, v) -> list.add(v));
        list.add(new RawMaterial());

        JComboBox<RawMaterial> combo = new JComboBox<RawMaterial>(list.toArray(new RawMaterial[MixtureGenerator.rawList.size()]));

        // Определение редактора ячеек для первой колонки
        table.getColumnModel().getColumn(columnNum).setCellEditor(new DefaultCellEditor(combo));
    }

    private static JComponent makeTitledPanel(String title, JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(c);
        p.setBorder(BorderFactory.createTitledBorder(title));
        return p;
    }


}
