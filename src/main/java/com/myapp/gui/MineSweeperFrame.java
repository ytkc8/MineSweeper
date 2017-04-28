package com.myapp.gui;

import com.myapp.domain.Mode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MineSweeperFrame extends JFrame implements ActionListener {
    private MineSweeperPanel mineSweeperPanel;

    public MineSweeperFrame(Mode mode) {
        setTitle("MineSweeper");
        mineSweeperPanel = new MineSweeperPanel(mode);
        drawButton(mode);

        getContentPane().add(mineSweeperPanel, BorderLayout.CENTER);
        pack();
    }

    private void drawButton(Mode mode) {
        mineSweeperPanel.setLayout(null);
        int size = mode.getSize();
        int buttonSize = mode.getButtonSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton jButton = new JButton();
                jButton.setBackground(Color.LIGHT_GRAY);
                jButton.setBounds(j * buttonSize, i * buttonSize, buttonSize, buttonSize);
                jButton.addActionListener(this);

                mineSweeperPanel.add(jButton);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println();
    }
}
