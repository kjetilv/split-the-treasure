import java.util.ArrayList;
import java.util.List;

public record Solution(int pirates, int targetValue, Gems loot, List<Gems> parts) {

    public Solution(int targetValue, int pirates, List<Gems> parts) {
        this(pirates, targetValue, null, parts);
    }

    public Solution add(Gems part) {
        List<Gems> copy = new ArrayList<>(parts);
        copy.add(part);
        return new Solution(targetValue, pirates, copy);
    }

    public Solution forLoot(Gems loot) {
        return new Solution(targetValue, pirates, loot, parts);
    }
}
