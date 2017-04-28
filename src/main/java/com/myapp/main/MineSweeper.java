package com.myapp.main;

import com.myapp.domain.Mode;
import com.myapp.service.GUIGameBoard;
import com.myapp.service.GameBoard;

import javax.swing.*;
import java.util.Scanner;

public class MineSweeper {
    public static void main(String arg[]) {
//        playGUI(new Mode("easy"));
        playConsole(new Mode("easy"));
    }

    private static void playGUI(Mode mode) {
        GUIGameBoard guiGameBoard = new GUIGameBoard(mode);
        guiGameBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiGameBoard.setVisible(true);
    }

    private static void playConsole(Mode mode) {
        GameBoard gameBoard = new GameBoard(mode);
        gameBoard.printPlayContent();

        while (gameBoard.countNotOpenCell() != gameBoard.getBombNum()) {
            int vertical = inputNumber("vertical");
            if (vertical == -1) continue;

            int horizontal = inputNumber("horizontal");
            if (horizontal == -1) continue;

            if (!gameBoard.isExist(vertical, horizontal)) {
                System.out.println("** Selected Cell is NOT exist. Please retry! **");
                continue;
            }

            opration(gameBoard, vertical, horizontal);

            if (gameBoard.isWasGameOver()) {
                System.out.println("*** Game Over :( ***");
                System.exit(1);
            }

            gameBoard.printPlayContent();
        }

        System.out.println("**** Game Clear!! :) ****\n");
        gameBoard.printBoardContent();
    }

    private static int inputNumber(String axis) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Please input " + axis + " number > ");
            return scanner.nextInt();
        } catch (Exception e) {
            System.out.println("** Please input number! **");
            return -1;
        }
    }

    private static void opration(GameBoard gameBoard, int vertical, int horizontal) {
        Scanner scanner = new Scanner(System.in);

        boolean isNotSelected = true;
        while (isNotSelected) {
            System.out.println("f : set flag, o : open cell");
            System.out.print("Please input your operation > ");
            switch (scanner.next()) {
                case "p":
                    gameBoard.setCellFlag(vertical, horizontal);
                    isNotSelected = false;
                    break;
                case "o":
                    gameBoard.openCell(vertical, horizontal);
                    isNotSelected = false;
                    break;
                default:
                    System.out.println("** Selected Operation is NOT exist. Please retry! **");
                    break;
            }
        }
    }
}


