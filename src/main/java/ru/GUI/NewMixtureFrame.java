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
    private JScrollPane scrollPane_3;
    private JTable table_1;
    private JTable table_2;
    private JTable table_3;

    public NewMixtureFrame() throws HeadlessException {

        table_1 = new JTable();
        initTableView(table_1, new TableModel_1(MixtureGenerator.rawList));
        setComboEditor(table_1,0); //установка раскрывающегостя списка в 0 колонку
        updateSummary();

        table_2 = new JTable();
        initTableView(table_2, new TableModel_2(MixtureGenerator.rawList));

        table_3 = new JTable(new TableModel_3());
        table_3.getColumnModel().getColumn(0).setPreferredWidth(200);
        //initTableView(table_3, new TableModel_3());

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

        addModelListener(table_1); //установка слушателя событий

        //настройки фрейма
        this.setTitle("New mixture");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize(925, 650); //размеры фрейма
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
        table.setDefaultRenderer(Double.class, new CustomCellRenderer(1));

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


    private void addModelListener(JTable table) {
        table.getModel().addTableModelListener(
                new TableModelListener()
                {
                    @Override
                    public void tableChanged(TableModelEvent e) {
                        //System.out.println("Тип события - " + e.getType() + " Column - " + e.getColumn() + " Row - " + e.getFirstRow());
                        if (e.getFirstRow() == 11) return;
                        switch (e.getColumn()) {
                            case 0: {
                                RawMaterial rm = (RawMaterial) table.getModel().getValueAt(e.getFirstRow(), 0);
                                if (rm.getName().equals("-")) {
                                    table.getModel().setValueAt(0.0D, e.getFirstRow(), 1);//установить пропорцию = 0;
                                }
                                updateDataRow(e.getFirstRow());
                                updateSummary();

                                //обновить зависимости
                                updateDataRow(e.getFirstRow());
                                updateSummary();
                                break;
                            }
                            case 1: {
                                updateDataRow(e.getFirstRow());
                                updateSummary();

                                //обновить зависимости
                                updateDataRow(e.getFirstRow());
                                updateSummary();
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
        Double BD = rm.getBD();
        Double volume = 0.0D;
        if (BD != 0) {
            volume = mass / rm.getBD();
        }

        model.setValueAt(rm.getPriceBK5(), row, 2); //Цена BK5, RMB
        model.setValueAt(rm.getPriceBK6(), row, 3); //Цена BK6, RMB
        model.setValueAt(volume * 100 /(Double) model.getValueAt(11, 5), row, 4); //Доля от объема, %
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

    public void updateSummary(){
        TableModel model = table_1.getModel();
        Double sumProportion = 0.0D;
        Double sumVolume = 0.0D;
        Double sumMass = 0.0D;
        Double sumMg = 0.0D;
        Double sumAl = 0.0D;
        Double sumSi = 0.0D;
        Double sumCa = 0.0D;
        Double sumFe = 0.0D;
        Double sumTi = 0.0D;
        Double sumC = 0.0D;
        Double sumLOI = 0.0D;

        for (int i = 0; i < table_1.getRowCount() - 1; i++) {

            sumProportion += (Double) model.getValueAt(i, 1);
            Double vol = (Double) model.getValueAt(i, 5);
            sumVolume = sumVolume + vol;
            sumMass += (Double) model.getValueAt(i, 6);
            sumMg += (Double) model.getValueAt(i, 8);
            sumAl += (Double) model.getValueAt(i, 9);
            sumSi += (Double) model.getValueAt(i, 10);
            sumCa += (Double) model.getValueAt(i, 11);
            sumFe += (Double) model.getValueAt(i, 12);
            sumTi += (Double) model.getValueAt(i, 13);
            sumC += (Double) model.getValueAt(i, 14);
            sumLOI += (Double) model.getValueAt(i, 15);
        }

        model.setValueAt(sumProportion, table_1.getRowCount()-1, 1);
        model.setValueAt(sumVolume, table_1.getRowCount()-1, 5);
        model.setValueAt(sumMass, table_1.getRowCount()-1, 6);
        model.setValueAt(sumMass/sumVolume*0.98, table_1.getRowCount()-1, 7);
        model.setValueAt(sumMg, table_1.getRowCount()-1, 8);
        model.setValueAt(sumAl, table_1.getRowCount()-1, 9);
        model.setValueAt(sumSi, table_1.getRowCount()-1, 10);
        model.setValueAt(sumCa, table_1.getRowCount()-1, 11);
        model.setValueAt(sumFe, table_1.getRowCount()-1, 12);
        model.setValueAt(sumTi, table_1.getRowCount()-1, 13);
        model.setValueAt(sumC, table_1.getRowCount()-1, 14);
        model.setValueAt(sumLOI, table_1.getRowCount()-1, 15);
    }


    private static JComponent makeTitledPanel(String title, JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(c);
        p.setBorder(BorderFactory.createTitledBorder(title));
        return p;
    }

}
