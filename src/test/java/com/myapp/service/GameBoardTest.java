package com.myapp.service;

import com.myapp.domain.Cell;
import com.myapp.domain.Mode;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class GameBoardTest {
    private GameBoard easyGameBoard;
    private GameBoard middleGameBoard;
    private GameBoard hardGameBoard;

    @Before
    public void setUp() throws Exception {
        easyGameBoard = new GameBoard(new Mode("easy"));
        middleGameBoard = new GameBoard(new Mode("middle"));
        hardGameBoard = new GameBoard(new Mode("hard"));
    }

    @Test
    public void test_isExist_returnsTrue() throws Exception {
        boolean exist1 = easyGameBoard.isExist(0, 0);
        boolean exist2 = easyGameBoard.isExist(0, 4);
        boolean exist3 = easyGameBoard.isExist(4, 0);
        boolean exist4 = easyGameBoard.isExist(4, 4);
        boolean exist5 = middleGameBoard.isExist(7, 7);
        boolean exist6 = hardGameBoard.isExist(9, 9);

        assertThat(exist1, is(true));
        assertThat(exist2, is(true));
        assertThat(exist3, is(true));
        assertThat(exist4, is(true));
        assertThat(exist5, is(true));
        assertThat(exist6, is(true));
    }

    @Test
    public void test_isExist_returnsFalse() throws Exception {
        boolean exist1 = easyGameBoard.isExist(5, 1);
        boolean exist2 = easyGameBoard.isExist(1, 5);
        boolean exist3 = easyGameBoard.isExist(5, 5);
        boolean exist4 = easyGameBoard.isExist(-1, 3);
        boolean exist5 = easyGameBoard.isExist(3, -1);
        boolean exist6 = easyGameBoard.isExist(-1, -1);

        assertThat(exist1, is(false));
        assertThat(exist2, is(false));
        assertThat(exist3, is(false));
        assertThat(exist4, is(false));
        assertThat(exist5, is(false));
        assertThat(exist6, is(false));
    }

    @Test
    public void test_getCell_returnsCell_onSuccess() throws Exception {
        Cell cell1 = easyGameBoard.getCell(2, 3).get();
        Cell cell2 = easyGameBoard.getCell(4, 0).get();

        assertThat(cell1.getVertical(), equalTo(2));
        assertThat(cell1.getHorizontal(), equalTo(3));
        assertThat(cell2.getVertical(), equalTo(4));
        assertThat(cell2.getHorizontal(), equalTo(0));
    }

    @Test
    public void test_getCell_returnsEmpty_onFailure() throws Exception {
        Optional<Cell> maybeCell1 = easyGameBoard.getCell(-1, 3);
        Optional<Cell> maybeCell2 = easyGameBoard.getCell(0, 5);

        assertThat(maybeCell1.isPresent(), is(false));
        assertThat(maybeCell2.isPresent(), is(false));
    }

    @Test
    public void test_countAllBomb() throws Exception {
        middleGameBoard.getCells()
                .stream()
                .filter(cell -> cell.getVertical() == 0)
                .forEach(cell -> cell.setHasBomb(true));

        hardGameBoard.getCells()
                .stream()
                .filter(cell -> cell.getVertical() > 5)
                .forEach(cell -> cell.setHasBomb(true));


        assertThat(easyGameBoard.countAllBomb(), equalTo(0));
        assertThat(middleGameBoard.countAllBomb(), equalTo(8));
        assertThat(hardGameBoard.countAllBomb(), equalTo(40));
    }

    @Test
    public void test_countAroundBomb() throws Exception {
        List<Cell> cells = easyGameBoard.getCells();
        cells.get(0).setHasBomb(true);
        cells.get(1).setHasBomb(true);
        cells.get(5).setHasBomb(true);
        cells.get(12).setHasBomb(true);

        assertThat(easyGameBoard.countAroundBomb(1, 1), equalTo(4));
        assertThat(easyGameBoard.countAroundBomb(2, 1), equalTo(2));
        assertThat(easyGameBoard.countAroundBomb(3, 3), equalTo(1));
    }

    @Test
    public void test_countNotOpenCell() throws Exception {
        assertThat(easyGameBoard.countNotOpenCell(), equalTo(25));

        easyGameBoard.getCell(0, 0).get().setWasOpened(true);
        easyGameBoard.getCell(0, 1).get().setWasOpened(true);
        easyGameBoard.getCell(2, 0).get().setWasOpened(true);
        easyGameBoard.getCell(3, 2).get().setWasOpened(true);
        easyGameBoard.getCell(1, 4).get().setWasOpened(true);

        assertThat(easyGameBoard.countNotOpenCell(), equalTo(20));
    }

    @Test
    public void test_setBomb_withCountAllBombMethod() throws Exception {
        easyGameBoard.setBomb();
        middleGameBoard.setBomb();
        hardGameBoard.setBomb();

        assertThat(easyGameBoard.countAllBomb(), equalTo(easyGameBoard.getBombNum()));
        assertThat(middleGameBoard.countAllBomb(), equalTo(middleGameBoard.getBombNum()));
        assertThat(hardGameBoard.countAllBomb(), equalTo(hardGameBoard.getBombNum()));
    }

    @Test
    public void test_setFieldNum_withCountAroundBombMethod() throws Exception {
        List<Cell> cells = easyGameBoard.getCells();
        cells.get(0).setHasBomb(true);
        cells.get(1).setHasBomb(true);
        cells.get(5).setHasBomb(true);
        cells.get(12).setHasBomb(true);

        easyGameBoard.setFieldNum();

        assertThat(cells.get(3).getFieldNum(), equalTo(0));
        assertThat(cells.get(6).getFieldNum(), equalTo(4));
        assertThat(cells.get(7).getFieldNum(), equalTo(2));
        assertThat(cells.get(8).getFieldNum(), equalTo(1));
        assertThat(cells.get(15).getFieldNum(), equalTo(0));
    }

    @Test
    public void test_setCellFlag() throws Exception {
        assertThat(easyGameBoard.getCell(2, 3).get().isHasFlag(), equalTo(false));
        assertThat(easyGameBoard.getCell(4, 4).get().isHasFlag(), equalTo(false));

        easyGameBoard.setCellFlag(2, 3);
        easyGameBoard.setCellFlag(4, 4);

        assertThat(easyGameBoard.getCell(2, 3).get().isHasFlag(), equalTo(true));
        assertThat(easyGameBoard.getCell(4, 4).get().isHasFlag(), equalTo(true));
    }

    @Test
    public void test_openCell_whenSetFieldNum_onSuccess() throws Exception {
        easyGameBoard.setFistSelect(false);
        easyGameBoard.getCell(0, 0).get().setFieldNum(1);
        easyGameBoard.getCell(1, 0).get().setFieldNum(1);
        easyGameBoard.getCell(2, 0).get().setFieldNum(1);
        easyGameBoard.getCell(3, 0).get().setFieldNum(1);
        easyGameBoard.getCell(4, 0).get().setFieldNum(1);
        easyGameBoard.getCell(0, 1).get().setFieldNum(1);
        easyGameBoard.getCell(1, 1).get().setFieldNum(1);
        easyGameBoard.getCell(2, 1).get().setFieldNum(1);
        easyGameBoard.getCell(3, 1).get().setFieldNum(1);
        easyGameBoard.getCell(4, 1).get().setFieldNum(1);
        easyGameBoard.getCell(0, 2).get().setFieldNum(0);
        easyGameBoard.getCell(1, 2).get().setFieldNum(0);
        easyGameBoard.getCell(2, 2).get().setFieldNum(0);
        easyGameBoard.getCell(3, 2).get().setFieldNum(1);
        easyGameBoard.getCell(4, 2).get().setFieldNum(1);
        easyGameBoard.getCell(0, 3).get().setFieldNum(1);
        easyGameBoard.getCell(1, 3).get().setFieldNum(1);
        easyGameBoard.getCell(2, 3).get().setFieldNum(1);
        easyGameBoard.getCell(3, 3).get().setFieldNum(1);
        easyGameBoard.getCell(4, 3).get().setFieldNum(1);
        easyGameBoard.getCell(0, 4).get().setFieldNum(1);
        easyGameBoard.getCell(1, 4).get().setFieldNum(1);
        easyGameBoard.getCell(2, 4).get().setFieldNum(1);
        easyGameBoard.getCell(3, 4).get().setFieldNum(1);
        easyGameBoard.getCell(4, 4).get().setFieldNum(1);
        assertThat(easyGameBoard.getCell(0, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(1, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(2, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(3, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(4, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(0, 1).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(1, 1).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(2, 1).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(3, 1).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(4, 1).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(0, 2).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(1, 2).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(2, 2).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(3, 2).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(4, 2).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(0, 3).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(1, 3).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(2, 3).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(3, 3).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(4, 3).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(0, 4).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(1, 4).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(2, 4).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(3, 4).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(4, 4).get().isWasOpened(), equalTo(false));

        easyGameBoard.openCell(2, 2);

        assertThat(easyGameBoard.getCell(0, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(1, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(2, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(3, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(4, 0).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(0, 1).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(1, 1).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(2, 1).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(3, 1).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(4, 1).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(0, 2).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(1, 2).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(2, 2).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(3, 2).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(4, 2).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(0, 3).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(1, 3).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(2, 3).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(3, 3).get().isWasOpened(), equalTo(true));
        assertThat(easyGameBoard.getCell(4, 3).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(0, 4).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(1, 4).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(2, 4).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(3, 4).get().isWasOpened(), equalTo(false));
        assertThat(easyGameBoard.getCell(4, 4).get().isWasOpened(), equalTo(false));
    }

    @Test
    public void test_openCell_whenWasOpened() throws Exception {
        easyGameBoard.setFistSelect(false);
        easyGameBoard.getCell(1, 1).get().setWasOpened(true);

        easyGameBoard.openCell(1, 1);

        assertThat(hardGameBoard.isWasGameOver(), equalTo(false));
        assertThat(easyGameBoard.getCell(1, 1).get().isWasOpened(), equalTo(true));
    }

    @Test
    public void test_openCell_whenHasFlag() throws Exception {
        easyGameBoard.setFistSelect(false);
        easyGameBoard.getCell(1, 1).get().setHasFlag(true);

        easyGameBoard.openCell(1, 1);

        assertThat(hardGameBoard.isWasGameOver(), equalTo(false));
        assertThat(easyGameBoard.getCell(1, 1).get().isWasOpened(), equalTo(false));
    }

    @Test
    public void test_openCell_whenHasBomb() throws Exception {
        easyGameBoard.setFistSelect(false);
        assertThat(hardGameBoard.getCell(2, 4).get().isWasOpened(), equalTo(false));
        hardGameBoard.getCell(2, 4).get().setHasBomb(true);

        hardGameBoard.openCell(2, 4);

        assertThat(hardGameBoard.isWasGameOver(), equalTo(true));
        assertThat(hardGameBoard.getCell(2, 4).get().isWasOpened(), equalTo(false));
    }

    @Test
    public void test_openCell_whenNotExistCell() throws Exception {
        easyGameBoard.openCell(1, 6);

        assertThat(easyGameBoard.isWasGameOver(), equalTo(false));
    }

    @Test
    public void test_initializeGame_isFisrt() throws Exception {
        assertThat(easyGameBoard.isFistSelect(), equalTo(true));
        assertThat(easyGameBoard.countAllBomb(), equalTo(0));

        easyGameBoard.initializeGame();

        assertThat(easyGameBoard.countAllBomb(), equalTo(5));
        assertThat(easyGameBoard.isFistSelect(), equalTo(false));
    }

    @Test
    public void test_initializeGame_isNotFisrt() throws Exception {
        easyGameBoard.setFistSelect(false);
        assertThat(easyGameBoard.countAllBomb(), equalTo(0));

        easyGameBoard.initializeGame();

        assertThat(easyGameBoard.countAllBomb(), equalTo(0));
        assertThat(easyGameBoard.isFistSelect(), equalTo(false));
    }
}