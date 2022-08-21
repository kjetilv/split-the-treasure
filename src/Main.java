import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Main {

    public static void main(String[] args) {
        int range = 6;
        Gems gems = new Gems(IntStream.range(0, range)
            .map(i -> i + 1)
            .flatMap(i ->
                IntStream.range(0, 1 + range  - i)
                    .map(j -> i))
            .toArray());
        int pirates = 7;
        List<Solution> solutions = Collections.synchronizedList(new ArrayList<>());

        Consumer<Solution> print = System.out::println;
        Consumer<Solution> add = solutions::add;

        long solutionCount = Solution.count(gems, pirates, print.andThen(add));

        if (solutionCount == 0) {
            System.out.println("No solutions for " + pirates + " pirates: " + gems);
        } else {
            System.out.println(solutionCount + " solutions for " + pirates + " pirates: " + gems);
            if (solutions.size() != new HashSet<>(solutions).size()) {
                System.out.println();
                System.out.println("Duplicate solutions:");
                solutions.stream()
                    .collect(Collectors.groupingBy(Record::hashCode))
                    .forEach((__, value) ->
                        System.out.println("  " + value.get(0) + " " + value.size()));
            }
        }
    }
}
