public final class Main {

    public static void main(String[] args) {
        new Gems(3, 3, 2, 2, 1, 1, 4)
            .solutions(4)
            .forEach(System.out::println);
    }
}
