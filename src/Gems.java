import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

record Gems(List<Gem> gems) {

    Gems(int... values) {
        this(Arrays.stream(values).mapToObj(Gem::new).sorted().toList());
    }

    Stream<Solution> solutions(int pirates) {
        if (value() % pirates != 0) {
            throw new IllegalStateException(this.value() + "/" + pirates + ", remaining " + this.value() % pirates);
        }
        return solutions(pirates, value() / pirates)
            .distinct()
            .map(solution ->
                solution.forLoot(this));
    }

    private Stream<Solution> solutions(int pirates, int shareValue) {
        if (gems.isEmpty()) {
            return Stream.empty();
        }
        if (value() == shareValue) {
            return Stream.of(
                new Solution(pirates, shareValue, Collections.singletonList(this)));
        }
        return powerSet(new Gems(), shareValue).flatMap(share ->
            gemsAfterRemoving(share).solutions(pirates, shareValue).map(solution ->
                solution.with(share)));
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

    private Stream<Gems> powerSet(Gems accumulated, int shareValue) {
        return viableGems(shareValue, accumulated.value())
            .flatMap(gem -> {
                Gems remainingGems = this.remove(gem);
                Gems nextAccumulated = accumulated.add(gem);
                return nextAccumulated.value() == shareValue
                    ? Stream.of(nextAccumulated)
                    : remainingGems.powerSet(nextAccumulated, shareValue);
            });
    }

    private Stream<Gem> viableGems(int shareValue, int accumulatedValue) {
        return this.gems().stream()
            .filter(gem ->
                gem.value() + accumulatedValue <= shareValue
            );
    }

    private Gems gemsAfterRemoving(Gems share) {
        List<Gem> remaining = new ArrayList<>(this.gems);
        share.gems.forEach(remaining::remove);
        return new Gems(remaining);
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


