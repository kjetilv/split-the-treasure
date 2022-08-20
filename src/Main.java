import java.util.stream.IntStream;

public final class Main {

    public static void main(String[] args) {
        Gems gems = new Gems(5, 5, 4, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1);
        int pirates = 6;
        long count = Solution.count(gems, pirates, System.out::println);

        if (count == 0) {
            System.out.println("No solutions for " + pirates + " pirates: " + gems);
        } else {
            System.out.println(count + " solutions for " + pirates + " pirates: " + gems);
        }
    }
}
