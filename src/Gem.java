import java.util.Comparator;

record Gem(int value) implements Comparable<Gem> {

    @Override
    public int compareTo(Gem that) {
        return COMPARATOR.compare(this, that);
    }

    private static final Comparator<Gem> COMPARATOR =
        Comparator.comparing(Gem::value).reversed();

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
