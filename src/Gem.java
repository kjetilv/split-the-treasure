 record Gem(int value) implements Comparable<Gem> {

    @Override
    public int compareTo(Gem o) {
        return Gems.COMPARATOR.compare(this, o);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
