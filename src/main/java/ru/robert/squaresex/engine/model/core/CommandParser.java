package ru.robert.squaresex.engine.model.core;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {
    public static enum CmdType { GAME, MOVE, HELP, EXIT, UNKNOWN }

    public static class ParsedCommand {
        public CmdType type = CmdType.UNKNOWN;
        public boolean valid = false;
        public int gameN;
        public String p1Type;
        public char p1Color;
        public String p2Type;
        public char p2Color;
        public int moveX;
        public int moveY;
    }

    public static final String HELP_TEXT =
            "Commands:\n" +
                    "GAME N, TYPE1 C1, TYPE2 C2 - start a new game. TYPE = user|comp, C = W|B\n" +
                    "MOVE X, Y - make a move (only if it's the user's turn)\n" +
                    "HELP - show help\n" +
                    "EXIT - quit the game";
    private static final Pattern GAME_PATTERN = Pattern.compile(
            "^GAME\\s+(\\d+)\\s*,\\s*(user|comp)\\s+([WBwb])\\s*,\\s*(user|comp)\\s+([WBwb])\\s*$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern MOVE_PATTERN = Pattern.compile(
            "^MOVE\\s+(\\d+)\\s*,\\s*(\\d+)\\s*$",
            Pattern.CASE_INSENSITIVE);

    public ParsedCommand parse(String line) {
        ParsedCommand pc = new ParsedCommand();
        line = line.trim();

        if (line.equalsIgnoreCase("HELP")) {
            pc.type = CmdType.HELP;
            pc.valid = true;
            return pc;
        }
        if (line.equalsIgnoreCase("EXIT")) {
            pc.type = CmdType.EXIT;
            pc.valid = true;
            return pc;
        }

        Matcher gm = GAME_PATTERN.matcher(line);
        if (gm.matches()) {
            pc.type = CmdType.GAME;
            pc.valid = true;
            pc.gameN = Integer.parseInt(gm.group(1));
            pc.p1Type = gm.group(2).toLowerCase(Locale.ROOT);
            pc.p1Color = Character.toUpperCase(gm.group(3).charAt(0));
            pc.p2Type = gm.group(4).toLowerCase(Locale.ROOT);
            pc.p2Color = Character.toUpperCase(gm.group(5).charAt(0));
            return pc;
        }

        Matcher mm = MOVE_PATTERN.matcher(line);
        if (mm.matches()) {
            pc.type = CmdType.MOVE;
            pc.valid = true;
            pc.moveX = Integer.parseInt(mm.group(1));
            pc.moveY = Integer.parseInt(mm.group(2));
            return pc;
        }

        pc.type = CmdType.UNKNOWN;
        pc.valid = false;
        return pc;
    }
}
