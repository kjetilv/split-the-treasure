import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

record Gems(List<Gem> gems) implements Comparable<Gems> {

    Gems(int... values) {
        this(Arrays.stream(values).mapToObj(Gem::new).toList());
    }

    Gems(List<Gem> gems) {
        this.gems = gems.stream().sorted().toList();
    }

    @Override
    public int compareTo(Gems that) {
        return COMPARATOR.compare(this, that);
    }

    int value() {
        return gems().stream().mapToInt(Gem::value).sum();
    }

    Stream<Solution> solutions(int pirates) {
        if (value() % pirates != 0) {
            throw new IllegalStateException(this + "/" + pirates + ", remaining " + this.value() % pirates);
        }

        Predicate<Solution> solutionTracker = tracker();

        return solutions(
            pirates,
            value() / pirates,
            solutionTracker
        )
            .map(solution ->
                solution.forLoot(this));
    }

    private Stream<Solution> solutions(
        int pirates,
        int shareValue,
        Predicate<Solution> solutionTracker
    ) {
        if (value() == shareValue) {
            Solution solution =
                new Solution(pirates, shareValue, Collections.singletonList(this));
            return solutionTracker.test(solution)
                ? Stream.of(solution)
                : Stream.empty();
        }
        Predicate<Gems> subsetTracker = tracker();
        return powerSet(new Gems(), shareValue, subsetTracker)
            .flatMap(share -> {
                Gems subset = remove(share);
                if (subsetTracker.test(subset)) {
                    return subset
                        .solutions(pirates, shareValue, solutionTracker)
                        .map(solution ->
                            solution.add(share));
                } else {
                    return Stream.empty();
                }
            });
    }

    private Stream<Gems> powerSet(Gems accumulated, int shareValue, Predicate<Gems> subsetTracker) {
        return this.gems().stream()
            .filter(gem ->
                gem.value() + accumulated.value() <= shareValue)
            .flatMap(gem -> {
                Gems nextAccumulated = accumulated.add(gem);
                if (nextAccumulated.value() == shareValue && subsetTracker.test(nextAccumulated)) {
                    return Stream.of(nextAccumulated);
                }
                Gems reducedGems = this.remove(gem);
                if (subsetTracker.test(reducedGems)) {
                    return reducedGems.powerSet(nextAccumulated, shareValue, subsetTracker);
                }
                return Stream.empty();
            });
    }

    private Gems add(Gem gem) {
        List<Gem> copy = new ArrayList<>(gems);
        copy.add(new Gem(gem.value()));
        return new Gems(copy);
    }

    private Gems remove(Gem gem) {
        List<Gem> copy = new ArrayList<>(gems);
        copy.remove(gem);
        return new Gems(copy);
    }

    private Gems remove(Gems share) {
        List<Gem> remaining = new ArrayList<>(this.gems);
        share.gems.forEach(remaining::remove);
        return new Gems(remaining);
    }

    private static final Comparator<Gems> COMPARATOR =
        Comparator.comparing((Gems gems) -> gems.gems().size())
            .reversed()
            .thenComparing(gems -> gems.gems().get(0));

    private static <T> Predicate<T> tracker() {
        return ((Collection<T>) new HashSet<T>())::add;
    }

    @Override
    public String toString() {
        return value() + "[" +
               gems.stream()
                   .map(Gem::toString)
                   .collect(
                       Collectors.joining(" ")) +
               "](" + gems.size() + ")";
    }
}


