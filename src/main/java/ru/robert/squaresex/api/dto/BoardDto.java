package ru.robert.squaresex.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardDto {
    private int size;
    private String data;
    private String nextPlayerColor;
}