import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

record Solution(int pirates, int shareValue, Gems loot, List<Gems> lootShare) {

    Solution(int pirates, int share, List<Gems> parts) {
        this(pirates, share, null, parts.stream().sorted().toList());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + pirates + "p*" + shareValue + " " + loot + " => " + lootShare + "]";
    }

    static long count(Gems gems, int pirates, Consumer<Solution> forEach) {
        return gems.solutions(pirates)
            .distinct()
            .map(Solution::verified)
            .peek(forEach)
            .count();
    }

    Solution verified() {
        if (lootShare.size() != pirates ||
            lootShare.stream().anyMatch(share -> share.value() != shareValue) ||
            loot.value() != lootShare.stream().mapToInt(Gems::value).sum()
        ) {
            throw new IllegalStateException(this + " is not a valid solution");
        }
        return this;
    }

    Solution add(Gems share) {
        List<Gems> copy = new ArrayList<>(lootShare);
        copy.add(share);
        return new Solution(pirates, shareValue, copy);
    }

    Solution forLoot(Gems loot) {
        return new Solution(pirates, shareValue, loot, lootShare);
    }
}
