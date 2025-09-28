package ru.robert.squaresex.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.robert.squaresex.api.dto.BoardDto;
import ru.robert.squaresex.api.dto.GameResultDto;
import ru.robert.squaresex.api.service.GameService;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/{rules}/nextMove")
    public ResponseEntity<GameResultDto> getNextMove(
            @PathVariable String rules,
            @RequestBody BoardDto boardDto
    ) {
        try {
            return ResponseEntity.ok(gameService.processNextMove(boardDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GameResultDto(true, "ERROR", null));
        }
    }
}
