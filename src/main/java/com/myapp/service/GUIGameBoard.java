package com.myapp.service;

import com.myapp.domain.ButtonCell;
import com.myapp.domain.Mode;
import com.myapp.gui.MineSweeperPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class GUIGameBoard extends JFrame implements ActionListener {
    private int size;
    private int bombNum;
    private boolean isFistSelect;
    private boolean wasGameOver;
    private List<ButtonCell> cells;
    private MineSweeperPanel mineSweeperPanel;

    public GUIGameBoard(Mode mode) {
        this.size = mode.getSize();
        this.bombNum = mode.getBombNum();
        this.isFistSelect = true;
        this.wasGameOver = false;
        this.cells = new ArrayList<>(size * bombNum);
        setTitle("MineSweeper");
        mineSweeperPanel = new MineSweeperPanel(mode);
        drawButton(this.size, mode.getButtonSize());

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
                cell.addActionListener(this);
                cells.add(cell);

                mineSweeperPanel.add(cell);
            }
        }
    }

    public int getBombNum() {
        return bombNum;
    }

    public List<ButtonCell> getCells() {
        return cells;
    }

    public boolean isFistSelect() {
        return isFistSelect;
    }

    public boolean isWasGameOver() {
        return wasGameOver;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonCell cell = (ButtonCell) e.getSource();
        openCell(cell.getVertical(), cell.getHorizontal());
    }

    public boolean isExist(int vertical, int horizontal) {
        if (vertical >= 0 && this.size > vertical) {
            if (horizontal >= 0 && this.size > horizontal) {
                return true;
            }
        }
        return false;
    }

    Optional<ButtonCell> getCell(int vertical, int horizontal) {
        if (isExist(vertical, horizontal)) {
           return Optional.of(this.cells.get(horizontal * this.size + vertical));
        }
        return Optional.empty();
    }

    int countAllBomb() {
        return this.cells
                .stream()
                .filter(cell -> cell.isHasBomb())
                .collect(toList())
                .size();
    }

    int countAroundBomb(int vertical, int horizontal) {
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

    public int countNotOpenCell() {
        return this.cells.stream()
                .filter(cell -> !cell.isWasOpened()).collect(toList()).size();
    }

    void setFistSelect(boolean fistSelect) {
        isFistSelect = fistSelect;
    }

    void setBomb() {
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

    void setFieldNum() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int count = countAroundBomb(j, i);
                getCell(j, i).ifPresent(cell -> cell.setFieldNum(count));
            }
        }
    }

    public void setCellFlag(int vertical, int horizontal) {
        getCell(vertical, horizontal).ifPresent(cell -> cell.setHasFlag(true));
    }

    public void openCell(int vertical, int horizontal) {
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
}
