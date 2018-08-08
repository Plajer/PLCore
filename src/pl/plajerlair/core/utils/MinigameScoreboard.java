package pl.plajerlair.core.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Create game scoreboard here
 */
public class MinigameScoreboard {

    private static final List<ChatColor> colors = Arrays.asList(ChatColor.values());

    private final Scoreboard scoreboard;
    private final Objective objective;

    private final List<BoardLine> boardLines = new ArrayList<>();

    public MinigameScoreboard(String displayName, List<String> lines) {
        Validate.isTrue(lines.size() < colors.size(), "Too many lines!");
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("MagickScoreboard", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(displayName);
        for(int i = 0; i < colors.size(); i++) {
            final ChatColor color = colors.get(i);
            final Team team = scoreboard.registerNewTeam("line" + i);
            team.addEntry(color.toString());
            boardLines.add(new BoardLine(color, i, team));
        }
        for(int i = 0; i < lines.size(); i++) setValue(i, lines.get(i));
    }

    private BoardLine getBoardLine(int line) {
        return boardLines.stream().filter(boardLine -> boardLine.getLine() == line).findFirst().orElse(null);
    }

    /**
     * Set scoreboard value to specified string
     *
     * @param line  line to set
     * @param value value to set
     */
    public void setValue(int line, String value) {
        final BoardLine boardLine = getBoardLine(line); //get our board line
        Validate.notNull(boardLine, "Unable to find BoardLine with index of " + line + ".");
        objective.getScore(boardLine.getColor().toString()).setScore(line);
        boardLine.getTeam().setPrefix(value);
    }

    /**
     * Remove specified line, line numbers won't be updated/sorted, must do it manually
     *
     * @param line line to remove
     */
    public void removeLine(int line) {
        final BoardLine boardLine = getBoardLine(line);
        Validate.notNull(boardLine, "Unable to find BoardLine with index of " + line + ".");
        scoreboard.resetScores(boardLine.getColor().toString());
    }

    /**
     * Display built scoreboard to player
     *
     * @param player player to receive scoreboard
     */
    public void display(Player player) {
        player.setScoreboard(this.scoreboard);
    }

    class BoardLine {

        private final ChatColor color;
        private final int line;
        private final Team team;

        public BoardLine(ChatColor color, int line, Team team) {
            this.color = color;
            this.line = line;
            this.team = team;
        }

        public ChatColor getColor() {
            return color;
        }

        public int getLine() {
            return line;
        }

        public Team getTeam() {
            return team;
        }
    }

}
