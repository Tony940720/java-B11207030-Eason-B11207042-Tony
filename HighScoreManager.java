import java.io.*;
import java.util.*;

public class HighScoreManager {
    private static final String FILE_NAME = "scores.txt";
    private static final int MAX_SCORES = 10;

    public static void addScore(String name, int score) {
        List<ScoreEntry> scores = loadScores();
        scores.add(new ScoreEntry(name, score));
        scores.sort((a, b) -> b.score - a.score);
        if (scores.size() > MAX_SCORES) scores = scores.subList(0, MAX_SCORES);
        saveScores(scores);
    }

    public static List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2)
                    scores.add(new ScoreEntry(parts[0], Integer.parseInt(parts[1])));
            }
        } catch (IOException ignored) {}
        return scores;
    }

    private static void saveScores(List<ScoreEntry> scores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (ScoreEntry entry : scores)
                writer.write(entry.name + "," + entry.score + "\n");
        } catch (IOException ignored) {}
    }

    public static class ScoreEntry {
        public String name;
        public int score;
        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}
