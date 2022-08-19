import java.util.ArrayList;
import java.util.List;

record Solution(int pirates, int shareValue, Gems loot, List<Gems> lootShare) {

    Solution(int pirates, int share, List<Gems> parts) {
        this(pirates, share, null, parts);
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
