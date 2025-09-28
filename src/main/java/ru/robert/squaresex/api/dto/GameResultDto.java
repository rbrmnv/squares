package ru.robert.squaresex.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GameResultDto {
    private boolean gameOver;
    private String result;
    private SimpleMoveDto move;
}
