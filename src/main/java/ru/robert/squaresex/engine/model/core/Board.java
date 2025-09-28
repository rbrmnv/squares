package ru.robert.squaresex.engine.model.core;


import java.util.Arrays;

public class Board {

    public static final char EMPTY = '.';
    private final int BOARD_SIZE;
    private final char[][] CELLS;

    public Board(int BOARD_SCALE) {
        this.BOARD_SIZE = BOARD_SCALE;
        this.CELLS = new char[BOARD_SCALE][BOARD_SCALE];
        for (int y = 0; y < BOARD_SCALE; y++) {
            Arrays.fill(CELLS[y], EMPTY);
        }
    }

    public int getBoardScale() {
        return BOARD_SIZE;
    }

    public char getCell(int x, int y) {
        if (!inBounds(x, y)) throw new IllegalArgumentException("Out of bounds");
        return CELLS[y][x];
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    public boolean place(char color, int x, int y) {
        if (!inBounds(x, y)) return false;
        if (CELLS[y][x] != EMPTY) return false;
        CELLS[y][x] = color;
        return true;
    }

    public boolean isFull() {
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                if (CELLS[y][x] == EMPTY) return false;
            }
        }
        return true;
    }

    public void print() {
        System.out.print("    ");
        for (int x = 1; x <= BOARD_SIZE; x++) {
            System.out.printf("%2d ", x);
        }
        System.out.println();

        System.out.print("   ");
        for (int x = 0; x < BOARD_SIZE; x++) {
            System.out.print("---");
        }
        System.out.println();

        for (int y = 0; y < BOARD_SIZE; y++) {
            System.out.printf("%2d |", y + 1);
            for (int x = 0; x < BOARD_SIZE; x++) {
                System.out.printf(" %s ", CELLS[y][x]);
            }
            System.out.println();
        }
    }

    public Character checkWinner() {
        for (char color : new char[]{'w', 'b'}) {
            Character winner = checkSquaresForColor(color);
            if (winner != null) {
                return winner;
            }
        }
        return null;
    }

    private Character checkSquaresForColor(char color) {
        int n = BOARD_SIZE;

        for (int y1 = 0; y1 < n; y1++) {
            for (int x1 = 0; x1 < n; x1++) {
                if (CELLS[y1][x1] != color) continue;

                for (int y2 = 0; y2 < n; y2++) {
                    for (int x2 = 0; x2 < n; x2++) {
                        if (x1 == x2 && y1 == y2) continue;
                        if (CELLS[y2][x2] != color) continue;

                        int dx = x2 - x1;
                        int dy = y2 - y1;

                        int[][] rotations = {
                                {-dy, dx},
                                {dy, -dx}
                        };

                        for (int[] rot : rotations) {
                            int rx = rot[0];
                            int ry = rot[1];

                            int x3 = x1 + rx;
                            int y3 = y1 + ry;
                            int x4 = x2 + rx;
                            int y4 = y2 + ry;

                            if (inBounds(x3, y3) && inBounds(x4, y4) &&
                                    CELLS[y3][x3] == color &&
                                    CELLS[y4][x4] == color) {

                                return color;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
