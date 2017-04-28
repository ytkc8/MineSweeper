package com.myapp.domain;

public class Mode {
    private int size;
    private int bombNum;
    private int buttonSize;

    public Mode(String difficulty) {
        this.buttonSize = 40;
        switch (difficulty) {
            case "hard":
                this.size = 10;
                this.bombNum = 20;
                break;
            case "middle":
                this.size = 8;
                this.bombNum = 10;
                break;
            case "easy":
                this.size = 5;
                this.bombNum = 5;
                break;
        }
    }

    public int getSize() {
        return size;
    }

    public int getBombNum() {
        return bombNum;
    }

    public int getButtonSize() {
        return buttonSize;
    }
}
