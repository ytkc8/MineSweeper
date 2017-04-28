package com.myapp.gui;

import com.myapp.domain.Mode;

import javax.swing.*;
import java.awt.*;

public class MineSweeperPanel extends JPanel {
    public MineSweeperPanel(Mode mode) {
        int size = mode.getSize() * mode.getButtonSize();
        setPreferredSize(new Dimension(size, size));
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
    }
}
