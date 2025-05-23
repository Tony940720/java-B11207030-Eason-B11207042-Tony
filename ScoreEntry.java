public class ScoreEntry implements Comparable<ScoreEntry> {
    public String name;
    public int score;

    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        return Integer.compare(other.score, this.score); // 排序用，高分在前
    }
}
