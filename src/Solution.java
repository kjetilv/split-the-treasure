import java.util.ArrayList;
import java.util.List;

record Solution(int pirates, int targetValue, Gems loot, List<Gems> parts) {

    Solution(int targetValue, int pirates, List<Gems> parts) {
        this(pirates, targetValue, null, parts);
    }

    Solution add(Gems part) {
        List<Gems> copy = new ArrayList<>(parts);
        copy.add(part);
        return new Solution(targetValue, pirates, copy);
    }

    Solution forLoot(Gems loot) {
        return new Solution(targetValue, pirates, loot, parts);
    }
}
