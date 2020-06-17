package ru.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class CustomCellRendererTable_3 extends DefaultTableCellRenderer{

        private static final long serialVersionUID = 1L;
        int precision = 0;
        Number numberValue;
        NumberFormat nf;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            //установка отображения основной части таблицы
            super.setHorizontalAlignment(SwingConstants.CENTER);
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value != null){
                super.setToolTipText(value.toString());
            }
            super.setHorizontalAlignment(SwingConstants.CENTER);

            //super.setOpaque(true);
            //super.setBackground(Color.ORANGE);

            return this;
        }

        public CustomCellRendererTable_3(int p_precision) {
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
