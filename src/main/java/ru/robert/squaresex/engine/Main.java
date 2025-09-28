package ru.robert.squaresex.engine;


import ru.robert.squaresex.engine.model.core.CommandParser;
import ru.robert.squaresex.engine.model.core.GameEngine;
import ru.robert.squaresex.engine.model.impl.CompPlayer;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Squares - console game started.");
        Scanner scanner = new Scanner(System.in);
        GameEngine engine = new GameEngine();
        CommandParser parser = new CommandParser();

        boolean running = true;
        while (running) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            CommandParser.ParsedCommand cmd = parser.parse(line);
            if (!cmd.valid) {
                System.out.println("Incorrect command");
                continue;
            }

            switch (cmd.type) {
                case HELP -> System.out.println(CommandParser.HELP_TEXT);
                case EXIT -> running = false;
                case GAME -> {
                    try {
                        engine.startNewGame(cmd.gameN, cmd.p1Type, cmd.p1Color, cmd.p2Type, cmd.p2Color);
                        if (engine.getCurrent() instanceof CompPlayer) engine.makeAutoMove();
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Incorrect command");
                    }
                }
                case MOVE -> {
                    if (!engine.isGameRunning()) {
                        System.out.println("Incorrect command");
                        break;
                    }
                    try {
                        boolean moved = engine.playerMove(cmd.moveX, cmd.moveY);
                        if (!moved) {
                            System.out.println("Invalid move");
                        } else engine.makeAutoMove();
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Invalid move");
                    }
                }
                default -> System.out.println("Incorrect command");
            }
        }
        scanner.close();
    }
}
