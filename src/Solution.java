import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

record Solution(Gems loot, int pirates, int shareValue, List<Gems> shares) {

    Solution(int pirates, int share, List<Gems> parts) {
        this(null, pirates, share, parts.stream().sorted().toList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[/" + pirates + ": " +
               shares.stream().map(String::valueOf).collect(Collectors.joining(" ")) +
               "]";
    }

    static long count(Gems gems, int pirates, Consumer<Solution> forEach) {
        return gems.solutions(pirates)
            .map(Solution::verified)
            .peek(forEach)
            .count();
    }

    Solution verified() {
        if (shares.size() != pirates ||
            shares.stream().anyMatch(share -> share.value() != shareValue) ||
            loot.value() != shares.stream().mapToInt(Gems::value).sum()
        ) {
            throw new IllegalStateException(this + " is not a valid solution");
        }
        return this;
    }

    Solution add(Gems share) {
        List<Gems> copy = new ArrayList<>(shares);
        copy.add(share);
        return new Solution(pirates, shareValue, copy);
    }

    Solution forLoot(Gems loot) {
        return new Solution(loot, pirates, shareValue, shares);
    }
}
