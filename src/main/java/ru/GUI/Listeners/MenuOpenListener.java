package ru.GUI.Listeners;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuOpenListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выбор файла");
        //Установка фильтра на выбор файлов
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XLS File", "xls"));
        //Убирает из фильтра *AllFiles
        fileChooser.setAcceptAllFileFilterUsed(false);
        int result = fileChooser.showOpenDialog(null);
        // Если директория выбрана, покажем ее в сообщении
        if (result == JFileChooser.APPROVE_OPTION ){
            File file = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(null, fileChooser.getSelectedFile());
        }
    }
}
