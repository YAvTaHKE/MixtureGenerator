package GUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import GUI.Listeners.MenuOpenListener;
import GUI.Listeners.MenuNewMixtureListener;

public class MainFrame extends JFrame {

    JMenuItem itemOpen;
    JMenuItem itemNewMixture;

    public MainFrame(){
        super("Mixture generator");
        this.setSize(360, 415);
        this.setDefaultCloseOperation((WindowConstants.DO_NOTHING_ON_CLOSE));
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null); //окно в центре экрана
        //Устанавливаем зависимость внешнего вида окна от платформы
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName ());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        buildMenuBar();
        setListeners();
    }

    private void buildMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);
            itemOpen = new JMenuItem(("Open"));
            menuFile.add(itemOpen);
            itemNewMixture = new JMenuItem("New mixture");
            menuFile.add(itemNewMixture);
            JMenuItem itemClose = new JMenuItem("Close");
            menuFile.add(itemClose);

        JMenu menuHelp = new JMenu("HELP");
        menuBar.add(menuHelp);
            JMenuItem about = new JMenuItem("About program");
            menuHelp.add(about);

        this.setJMenuBar(menuBar);
    }

    private void setListeners(){
        // Вывод окна выбора файла при нажатии MainFrame/MenuBar/MenuFile/Open
        itemOpen.addActionListener(new MenuOpenListener());

        //меню MainFrame/MenuBar/MenuFile/New mixture
        itemNewMixture.addActionListener(new MenuNewMixtureListener());

        // Подтверждение закрытия окна при нажатии на "крестик"
        this.addWindowListener (new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Потверждение закрытия окна JFrame
                int res = JOptionPane.showConfirmDialog(MainFrame.this, "Выйти из программы?");
                if (res == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
    }
}
