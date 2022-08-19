import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

record Gems(List<Gem> gems) {

    Gems(int... values) {
        this(entries(values));
    }

    List<Solution> solutions(int pirates) {
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

    private List<Gems> powerSet(int value) {
        return powerSet(value, new Gems()).toList();
    }

    private Gems add(Gem gem) {
        List<Gem> newGems = new ArrayList<>(gems);
        newGems.add(new Gem(gem.value()));
        return new Gems(newGems.stream().sorted().toList());
    }

    private Gems remove(Gem gem) {
        List<Gem> newGems = new ArrayList<>(gems);
        newGems.remove(gem);
        return new Gems(newGems);
    }

    private int value() {
        return gems().stream().mapToInt(Gem::value).sum();
    }

    private Stream<Gems> powerSet(int targetValue, Gems acc) {
        List<Gem> candidates = this.gems().stream()
            .filter(gem ->
                gem.value() + acc.value() <= targetValue)
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

    private Gems remove(Gems gems) {
        List<Gem> copy = new ArrayList<>(this.gems);
        gems.gems.forEach(copy::remove);
        return new Gems(copy);
    }

    public static final Comparator<Gem> COMPARATOR = Comparator.comparing(Gem::value).reversed();

    private static List<Gem> entries(int[] values) {
        return Arrays.stream(values).mapToObj(Gem::new).sorted().toList();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" +
               "[" + value() + "] " +
               gems.stream()
                   .map(Gem::toString)
                   .collect(
                       Collectors.joining(" ")) +
               "]";
    }
}


