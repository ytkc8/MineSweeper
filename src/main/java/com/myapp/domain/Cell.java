package com.myapp.domain;

public class Cell {
    private int vertical;
    private int horizontal;
    private int fieldNum;
    boolean hasBomb;
    boolean hasFlag;
    boolean wasOpened;

    public Cell(int vertical, int horizontal) {
        this.vertical = vertical;
        this.horizontal = horizontal;
        this.fieldNum = 0;
        this.hasBomb = false;
        this.hasFlag = false;
        this.wasOpened = false;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(int horizontal) {
        this.horizontal = horizontal;
    }

    public int getFieldNum() {
        return fieldNum;
    }

    public void setFieldNum(int fieldNum) {
        this.fieldNum = fieldNum;
    }

    public boolean isHasBomb() {
        return hasBomb;
    }

    public void setHasBomb(boolean hasBomb) {
        this.hasBomb = hasBomb;
    }

    public boolean isHasFlag() {
        return hasFlag;
    }

    public void setHasFlag(boolean hasFlag) {
        this.hasFlag = hasFlag;
    }

    public boolean isWasOpened() {
        return wasOpened;
    }

    public void setWasOpened(boolean wasOpened) {
        this.wasOpened = wasOpened;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cell cell = (Cell) o;

        if (vertical != cell.vertical) return false;
        if (horizontal != cell.horizontal) return false;
        if (fieldNum != cell.fieldNum) return false;
        if (hasBomb != cell.hasBomb) return false;
        if (hasFlag != cell.hasFlag) return false;
        return wasOpened == cell.wasOpened;
    }

    @Override
    public int hashCode() {
        int result = vertical;
        result = 31 * result + horizontal;
        result = 31 * result + fieldNum;
        result = 31 * result + (hasBomb ? 1 : 0);
        result = 31 * result + (hasFlag ? 1 : 0);
        result = 31 * result + (wasOpened ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "vertical=" + vertical +
                ", horizontal=" + horizontal +
                ", fieldNum=" + fieldNum +
                ", hasBomb=" + hasBomb +
                ", hasFlag=" + hasFlag +
                ", wasOpened=" + wasOpened +
                '}';
    }
}
