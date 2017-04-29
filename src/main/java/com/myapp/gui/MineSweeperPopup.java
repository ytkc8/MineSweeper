package com.myapp.gui;

import javax.swing.*;
import java.awt.*;

public class MineSweeperPopup extends JFrame {
    public MineSweeperPopup(String message) {
        setTitle(message);
        MineSweeperPanel popupPanel = new MineSweeperPanel(200, 30);
        popupPanel.setLayout(null);
        JButton popupButton = new JButton(message);
        popupButton.setBounds(0, 0, 200, 30);
        popupPanel.add(popupButton);
        getContentPane().add(popupPanel, BorderLayout.CENTER);
        pack();
    }
}
