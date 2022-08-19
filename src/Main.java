public final class Main {

    public static void main(String[] args) {
        Gems gems = new Gems(
            3, 3, 2, 2, 1, 1, 4
        );
        gems.solutions(4).forEach(System.out::println);
    }
}
