package com.myapp.service;

import com.myapp.domain.ButtonCell;
import com.myapp.domain.Mode;
import com.myapp.gui.MineSweeperPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class GUIGameBoard extends JFrame implements MouseListener {
    private int size;
    private int bombNum;
    private boolean isFistSelect;
    private boolean wasGameOver;
    private List<ButtonCell> cells;
    private MineSweeperPanel mineSweeperPanel;
    private JPopupMenu popup;
    private JMenuItem clearMessage;
    private JMenuItem gameOverMessage;

    public GUIGameBoard(Mode mode) {
        this.size = mode.getSize();
        this.bombNum = mode.getBombNum();
        this.isFistSelect = true;
        this.wasGameOver = false;
        this.cells = new ArrayList<>(size * bombNum);
        setTitle("MineSweeper");
        mineSweeperPanel = new MineSweeperPanel(mode);
        drawButton(this.size, mode.getButtonSize());
        popup = new JPopupMenu();
        gameOverMessage = new JMenuItem("* Game Over :( *");
        clearMessage = new JMenuItem("** Game Clear!! :) **");

        getContentPane().add(mineSweeperPanel, BorderLayout.CENTER);
        pack();
    }

    private void drawButton(int size, int buttonSize) {
        mineSweeperPanel.setLayout(null);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                ButtonCell cell = new ButtonCell(j, i);
                cell.setBackground(Color.LIGHT_GRAY);
                cell.setBounds(j * buttonSize, i * buttonSize, buttonSize, buttonSize);
                cell.addMouseListener(this);
                cells.add(cell);

                mineSweeperPanel.add(cell);
            }
        }
    }

    private int getBombNum() {
        return bombNum;
    }

    private boolean isFistSelect() {
        return isFistSelect;
    }

    private boolean isWasGameOver() {
        return wasGameOver;
    }

    private boolean isExist(int vertical, int horizontal) {
        if (vertical >= 0 && this.size > vertical) {
            if (horizontal >= 0 && this.size > horizontal) {
                return true;
            }
        }
        return false;
    }

    private Optional<ButtonCell> getCell(int vertical, int horizontal) {
        if (isExist(vertical, horizontal)) {
           return Optional.of(this.cells.get(horizontal * this.size + vertical));
        }
        return Optional.empty();
    }

    private int countAllBomb() {
        return this.cells
                .stream()
                .filter(ButtonCell::isHasBomb)
                .collect(toList())
                .size();
    }

    private int countAroundBomb(int vertical, int horizontal) {
        int count = 0;
        for (int i = horizontal - 1; i <= horizontal + 1; i++) {
            for (int j = vertical - 1; j <= vertical + 1; j++) {
                if (isExist(j, i) && getCell(j, i).get().isHasBomb()) {
                    count++;
                }
            }
        }
        return count;
    }

    private int countNotOpenCell() {
        return this.cells.stream()
                .filter(cell -> !cell.isWasOpened()).collect(toList()).size();
    }

    private void setBomb() {
        Random random = new Random();
        int cellNum = this.size * this.size;

        int bombCount = countAllBomb();
        while (bombCount < this.bombNum) {
            ButtonCell cell = this.cells.get(random.nextInt(cellNum));
            if (!cell.isWasOpened() && !cell.isHasBomb()) {
                cell.setHasBomb(true);
            }
            bombCount = countAllBomb();
        }
    }

    private void setFieldNum() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int count = countAroundBomb(j, i);
                getCell(j, i).ifPresent(cell -> cell.setFieldNum(count));
            }
        }
    }

    private void setCellFlag(int vertical, int horizontal) {
        getCell(vertical, horizontal).ifPresent(cell -> {
            cell.setHasFlag(!cell.isHasFlag());
            if (cell.isHasFlag()) {
                cell.setText("P");
            } else {
                cell.setText("");
            }
        });
    }

    private void openCell(int vertical, int horizontal) {
        getCell(vertical, horizontal).ifPresent(cell -> {
            if (cell.isWasOpened() || cell.isHasFlag()) return;
            if (cell.isHasBomb()) {
                this.wasGameOver = true;
                return;
            }
            cell.setWasOpened(true);
            initializeGame();
            cell.setEnabled(false);
            cell.setText(String.valueOf(cell.getFieldNum()));

            if (cell.getFieldNum() == 0) {
                int h = cell.getHorizontal();
                int v = cell.getVertical();
                for (int i = h - 1; i <= h + 1; i++) {
                    for (int j = v - 1; j <= v + 1; j++) {
                        getCell(j, i).ifPresent(nextCell -> {
                            openCell(nextCell.getVertical(), nextCell.getHorizontal());
                        });
                    }
                }
            }
        });
    }

    private void initializeGame() {
        if (isFistSelect()) {
            setBomb();
            setFieldNum();
            this.isFistSelect = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        ButtonCell cell = (ButtonCell) e.getSource();
        if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
            openCell(cell.getVertical(), cell.getHorizontal());
            if (isWasGameOver()) showPopup(e, gameOverMessage);
            if (countNotOpenCell() == getBombNum()) showPopup(e, clearMessage);
        }
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
            setCellFlag(cell.getVertical(), cell.getHorizontal());
        }
    }

    private void showPopup(MouseEvent e, JMenuItem item) {
        popup.add(item);
        popup.show(e.getComponent(), e.getX(), e.getY());
        System.exit(0);
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
