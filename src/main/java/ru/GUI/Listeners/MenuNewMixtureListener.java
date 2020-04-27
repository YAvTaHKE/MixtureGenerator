package ru.GUI.Listeners;

import ru.GUI.NewMixtureFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuNewMixtureListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        new NewMixtureFrame();
    }
}
