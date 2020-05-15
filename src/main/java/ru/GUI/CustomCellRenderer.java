package ru.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class CustomCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    int precision = 0;
    Number numberValue;
    NumberFormat nf;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //установка отображения основной части таблицы
        super.setHorizontalAlignment(SwingConstants.CENTER);
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        //super.setOpaque(true);
        //super.setBackground(Color.ORANGE);

        if (column == 7 ) {
            Object obj = table.getValueAt(row, 0);
            if (obj != null) {
                if (obj.toString().equals("-")) {
                    JLabel c = new JLabel("-");
                    c.setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            }
            JLabel c = new JLabel(String.format(Locale.GERMANY, "%.2f", value));
            if(row == 11){
                c.setOpaque(true);
                //c.setFont(new Font(c.getFont().getName(),Font.BOLD, 12));
            } else {
                c.setFont(new Font(c.getFont().getName(),Font.PLAIN, 12));
            }
            c.setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }


        //Отображать прочерк, если не задан сырьевой компонент
        Object obj = table.getValueAt(row, 0);
        if (obj != null) {
            if (obj.toString().equals("-")) {
                JLabel c = new JLabel("-");
                c.setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        }
        if (column == 1 && row != 11) {
            JLabel c = new JLabel(String.format(Locale.GERMANY, "%.1f %%", value));
            c.setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }


        //Установка отображения строки суммирования результатов
        if (row == 11) {
            JLabel c = new JLabel(value != null ? String.format("%.1f", value) : "");
            c.setOpaque(true);
            //c.setBackground(Color.YELLOW);
            c.setHorizontalAlignment(SwingConstants.CENTER);
            if (column == 1) {
                try {
                    c.setText(String.format(Locale.GERMANY,"%.1f %%", value));
                    Double sumProportion = (Double) value;
                    if (sumProportion > 101.8D || sumProportion < 100) {
                        c.setForeground(Color.RED);
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                    c.setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            return c;
        }
        return this;
    }

    public CustomCellRenderer(int p_precision) {
        super();
        setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        precision = p_precision;
        nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(p_precision);
        nf.setMaximumFractionDigits(p_precision);
    }

    @Override
    public void setValue(Object value) {
        if ((value != null) && (value instanceof Number)) {
            numberValue = (Number) value;
            value = nf.format(numberValue.doubleValue());
        }
        super.setValue(value);
    }


}
