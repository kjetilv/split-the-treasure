import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;

record Solution(int pirates, int shareValue, Gems loot, List<Gems> lootShare) {

    static long count(Gems gems, int pirates, Consumer<Solution> sink) {
        LongAdder count = new LongAdder();
        gems.solutions(pirates)
            .map(Solution::verify)
            .peek(solution ->
                count.increment())
            .forEach(sink);
        return count.longValue();
    }

    Solution(int pirates, int share, List<Gems> parts) {
        this(pirates, share, null, parts);
    }

    Solution verify() {
        if (loot.value() != lootShare.stream().mapToInt(Gems::value).sum()) {
            throw new IllegalStateException(this + " is not a valid solution");
        }
        return this;
    }

    Solution with(Gems share) {
        List<Gems> copy = new ArrayList<>(lootShare);
        copy.add(share);
        return new Solution(pirates, shareValue, copy);
    }

    Solution forLoot(Gems loot) {
        return new Solution(pirates, shareValue, loot, lootShare);
    }
}
