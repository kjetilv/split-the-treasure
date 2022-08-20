import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sangupta.bloomfilter.BloomFilter;
import com.sangupta.bloomfilter.impl.InMemoryBloomFilter;

record Gems(List<Gem> gems) implements Comparable<Gems> {

    Gems(int... values) {
        this(Arrays.stream(values).mapToObj(Gem::new).sorted().toList());
    }

    Stream<Solution> solutions(int pirates) {
        BloomFilter<Solution> bloomFilter =
            new InMemoryBloomFilter<>(100_000, 0.001);
        if (value() % pirates != 0) {
            throw new IllegalStateException(this.value() + "/" + pirates + ", remaining " + this.value() % pirates);
        }
        return solutions(pirates, value() / pirates, bloomFilter)
            .map(solution ->
                solution.forLoot(this));
    }

    private Stream<Solution> solutions(int pirates, int shareValue, BloomFilter<Solution> bloomFilter) {
        if (gems.isEmpty()) {
            return Stream.empty();
        }
        if (value() == shareValue) {
            Solution solution = new Solution(pirates, shareValue, Collections.singletonList(this));
            if (bloomFilter.contains(solution)) {
                return Stream.empty();
            }
            bloomFilter.add(solution);
            return Stream.of(solution);
        }
        return powerSet(new Gems(), shareValue)
            .flatMap(share ->
                gemsAfterRemoving(share)
                    .solutions(pirates, shareValue, bloomFilter)
                    .map(solution ->
                        solution.with(share)));
    }

    int value() {
        return gems().stream().mapToInt(Gem::value).sum();
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

    private Stream<Gems> powerSet(Gems accumulated, int shareValue) {
        return viableGems(shareValue, accumulated.value())
            .distinct()
            .flatMap(gem ->
                powerSet(accumulated, gem, shareValue));
    }

    private Stream<Gems> powerSet(Gems accumulated, Gem gem, int shareValue) {
        Gems remainingGems = this.remove(gem);
        Gems nextAccumulated = accumulated.add(gem);
        return nextAccumulated.value() == shareValue
            ? Stream.of(nextAccumulated)
            : remainingGems.powerSet(nextAccumulated, shareValue);
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

    private static final Comparator<Gems> COMPARATOR =
        Comparator.comparing((Gems gems) -> gems.gems().size())
            .reversed()
            .thenComparing(Gems::hashCode);

    @Override
    public int compareTo(Gems that) {
        return COMPARATOR.compare(this, that);
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


