import java.util.Comparator;

record Gem(int value) implements Comparable<Gem> {

    public static final Comparator<Gem> COMPARATOR = Comparator.comparing(Gem::value).reversed();

    @Override
    public int compareTo(Gem that) {
        return COMPARATOR.compare(this, that);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
