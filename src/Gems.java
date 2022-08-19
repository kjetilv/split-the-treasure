import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Gems(List<Gem> gems) {

    public Gems(int... values) {
        this(entries(values));
    }

    public List<Solution> solutions(int pirates) {
        if (value() % pirates != 0) {
            throw new IllegalStateException(this.value() + "/" + pirates + ", remaining " + this.value() % pirates);
        }
        int targetValue = value() / pirates;
        return solutions(pirates, targetValue).stream()
            .distinct()
            .map(solution ->
                solution.forLoot(this))
            .toList();
    }

    private List<Solution> solutions(int pirates, int targetValue) {
        if (gems.isEmpty()) {
            return Collections.emptyList();
        }
        if (value() == targetValue) {
            return Collections.singletonList(
                new Solution(targetValue, pirates, Collections.singletonList(this)));
        }
        List<Gems> powerSet = powerSet(targetValue);
        if (powerSet.isEmpty()) {
            return Collections.emptyList();
        }
        return powerSet.stream().flatMap((Gems share) -> {
            Gems remaining = remove(share);
            List<Solution> solutions = remaining.solutions(pirates, targetValue);
            return solutions.stream()
                .map(solution ->
                    solution.add(share));
        }).toList();
    }

    private Gems remove(Gems powerSet) {
        List<Gem> copy = new ArrayList<>(gems);
        powerSet.gems.forEach(copy::remove);
        return new Gems(copy);
    }

    public List<Gems> powerSet(int value) {
        return powerSet(value, new Gems()).toList();
    }

    public static final Comparator<Gem> COMPARATOR = Comparator.comparing(Gem::value).reversed();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" +
               gems.stream()
                   .map(Gem::toString)
                   .collect(
                       Collectors.joining(" ")) +
               "]";
    }

    private Stream<Gems> powerSet(int targetValue, Gems acc) {
        List<Gem> candidates = this.gems().stream()
            .filter(gem1 ->
                gem1.value() + acc.value() <= targetValue)
            .toList();
        return candidates
            .stream()
            .flatMap(gem -> {
                Gems next = this.remove(gem);
                Gems nextAcc = acc.add(gem);
                return nextAcc.value() == targetValue
                    ? Stream.of(nextAcc)
                    : next.powerSet(targetValue, nextAcc);
            });
    }

    public Gems add(Gem gem) {
        List<Gem> newGems = new ArrayList<>(gems);
        newGems.add(new Gem(gem.value()));
        return new Gems(newGems.stream().sorted().toList());
    }

    public Gems remove(Gem gem) {
        List<Gem> newGems = new ArrayList<>(gems);
        newGems.remove(gem);
        return new Gems(newGems);
    }

    public int value() {
        return gems().stream().mapToInt(Gem::value).sum();
    }

    public record Gem(int value) implements Comparable<Gem> {

        @Override
        public int compareTo(Gem o) {
            return COMPARATOR.compare(this, o);
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    private static List<Gem> entries(int[] values) {
        return Arrays.stream(values).mapToObj(Gem::new).sorted().toList();
    }
}


