import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Main {

    public static void main(String[] args) {
        Consumer<Solution> print = System.out::println;

        Collection<Solution> solutions = Collections.synchronizedCollection(new ArrayList<>());
        Consumer<Solution> add = solutions::add;

        Gems gems = gems();
        System.out.println("Solving for " + gems);
        System.out.println();

        long solutionCount = Solution.count(gems, 7, print.andThen(add));

        if (solutionCount == 0) {
            System.out.println("No solutions for " + 7 + " pirates: " + gems);
        } else {
            System.out.println();
            System.out.println(solutionCount + " solutions for " + 7 + " pirates: " + gems);
            if (solutions.size() != new HashSet<>(solutions).size()) {
                System.out.println();
                System.out.println("Duplicate solutions:");
                solutions.stream()
                    .collect(Collectors.groupingBy(Record::hashCode))
                    .forEach((__, value) -> {
                        if (value.size() > 1) {
                            System.out.println("  " + value.get(0) + " " + value.size());
                        }
                    });
            }
        }
    }

    private static Gems gems() {
        int range = 6;
        return new Gems(IntStream.range(0, range)
            .map(i -> i + 1)
            .flatMap(i ->
                IntStream.range(0, 1 + range - i)
                    .map(j -> i))
            .toArray());
    }
}
