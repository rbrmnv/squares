package ru.robert.squaresex.engine.model.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.robert.squaresex.api.dto.BoardDto;
import ru.robert.squaresex.engine.model.core.Board;
import ru.robert.squaresex.engine.model.impl.CompPlayer;
import ru.robert.squaresex.engine.model.Player;
import ru.robert.squaresex.engine.model.impl.UserPlayer;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class GameEngine {

    private Board board;
    private Player player1;
    private Player player2;
    private Player current;
    private boolean running = false;
    private final Random rnd = new Random();

    public void startNewGame(int boardSize, String p1Type, char p1Color, String p2Type, char p2Color) {
        if (boardSize <= 2) throw new IllegalArgumentException("Board size must be > 2");
        if (p1Color == p2Color) throw new IllegalArgumentException("Players must have different colors");

        board = new Board(boardSize);
        player1 = createPlayer(p1Type, p1Color);
        player2 = createPlayer(p2Type, p2Color);
        current = player1;
        running = true;

        board.print();
    }

    private Player createPlayer(String type, char color) {
        if ("user".equalsIgnoreCase(type)) return new UserPlayer(color);
        return new CompPlayer(color);
    }

    public boolean isGameRunning() {
        return running;
    }

    public boolean playerMove(int xUser, int yUser) {
        if (!running) throw new IllegalArgumentException("Game not running");
        if (!(current instanceof UserPlayer)) throw new IllegalArgumentException("Not user's turn");

        int col = xUser - 1;
        int row = yUser - 1;

        boolean ok = board.place(current.getColor(), col, row);
        if (!ok) {
            return false;
        }

        board.print();
        afterMoveProcessing();
        return true;
    }

    public void makeAutoMove() {
        if (!running || !(current instanceof CompPlayer)) return;

        int[] mv = computeNextMove();
        if (mv == null) {
            System.out.println("Game finished. Draw");
            running = false;
            return;
        }

        board.place(current.getColor(), mv[0], mv[1]);
        board.print();

        afterMoveProcessing();
    }


    private void afterMoveProcessing() {

        Character winner = board.checkWinner();
        if (winner != null) {
            System.out.printf("Game finished. %c wins!%n", winner);
            running = false;
            return;
        }

        if (board.isFull()) {
            System.out.println("Game finished. Draw");
            running = false;
            return;
        }

        current = (current == player1) ? player2 : player1;

        if (running && current instanceof CompPlayer) {
            makeAutoMove();
        }
    }

    public void loadBoardFromDto(BoardDto dto) {
        this.board = new Board(dto.getSize());

        if (player1 == null) player1 = new CompPlayer('b');
        if (player2 == null) player2 = new CompPlayer('w');

        char[] chars = dto.getData().replace("\n", "").replace("\r", "").toCharArray();
        int idx = 0;
        for (int y = 0; y < dto.getSize(); y++) {
            for (int x = 0; x < dto.getSize(); x++) {
                if (idx >= chars.length) break;
                char c = chars[idx++];
                if (c != ' ' && c != '.') {
                    board.place(c, x, y);
                }
            }
        }

        char next = dto.getNextPlayerColor().charAt(0);
        this.current = (player1.getColor() == next) ? player1 : player2;
    }


    public int[] computeNextMove() {
        if (!(current instanceof CompPlayer)) return null;

        List<int[]> empties = new ArrayList<>();
        int n = board.getBoardScale();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (board.getCell(x, y) == Board.EMPTY) {
                    empties.add(new int[]{x, y});
                }
            }
        }

        if (empties.isEmpty()) return null;

        int[] chosen = empties.get(rnd.nextInt(empties.size()));
        return chosen;
    }

}
