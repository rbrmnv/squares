package ru.robert.squaresex.api.service;

import org.springframework.stereotype.Service;
import ru.robert.squaresex.api.dto.BoardDto;
import ru.robert.squaresex.api.dto.GameResultDto;
import ru.robert.squaresex.api.dto.SimpleMoveDto;
import ru.robert.squaresex.engine.model.core.GameEngine;

@Service
public class GameService {

    public GameResultDto processNextMove(BoardDto boardDto) {

        GameEngine engine = new GameEngine();
        engine.loadBoardFromDto(boardDto);

        Character winner = engine.getBoard().checkWinner();
        if (winner != null) {
            return new GameResultDto(true, mapWinnerToMessage(engine,winner), null);
        }
        if (engine.getBoard().isFull()) {
            return new GameResultDto(true, "DRAW", null);
        }

        int[] move = engine.computeNextMove();
        if (move == null) {
            return new GameResultDto(true, "DRAW", null);
        }

        SimpleMoveDto moveDto = new SimpleMoveDto(move[0]+1, move[1]+1, boardDto.getNextPlayerColor());
        winner = engine.getBoard().checkWinner();
        if (winner != null) {
            return new GameResultDto(true, mapWinnerToMessage(engine,winner), moveDto);
        }
        if (engine.getBoard().isFull()) {
            return new GameResultDto(true, "DRAW", moveDto);
        }

        return new GameResultDto(false, "IN_PROGRESS", moveDto);
    }

    private String mapWinnerToMessage(GameEngine engine, char winner) {
        if (engine.getPlayer1().getColor() == winner) {
            return "UserWin" + "_" + winner;
        } else if (engine.getPlayer2().getColor() == winner) {
            return "CompWin" + "_" + winner;
        }
        return "Unknown";
    }
}
