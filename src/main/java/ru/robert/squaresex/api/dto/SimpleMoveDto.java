package ru.robert.squaresex.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SimpleMoveDto {
    private int x;
    private int y;
    private String color;
}
