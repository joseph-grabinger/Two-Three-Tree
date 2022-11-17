import java.util.stream.IntStream;

/**
 * METHOD A WAS USED FOR THIS IMPLEMENTATION OF THE 2-3-TREE
 */
public class Main {
    private static final int valueCount = 5000000;

    public static void main(String[] args) {
        System.out.println("Starting...");

        TwoThreeTree tree = new TwoThreeTree();
        test(tree);

        // tree.insert((float) 5);
        // tree.insert((float) 7);
        // tree.insert((float) 9);
        // tree.insert((float) 4.6);
        // tree.insert((float) 3.2);
        // tree.insert((float) 5.8);
        // tree.insert((float) 11);
        // tree.insert((float) 8);
        // tree.printTree();
        // System.out.println(tree.search(8));
        // tree.delete((float) 9);
        // tree.delete((float) 8);
        // tree.delete((float) 3.2);
        // tree.printTree();

    }

    /**
     * Performance testing
     * 
     * @param tree the tree to test
     */
    private static void test(TwoThreeTree tree) {
        var startTime = System.currentTimeMillis();
        IntStream.rangeClosed(0, valueCount).forEach((i) -> {
            if (i % 1000 == 0) {
                IntStream.rangeClosed(0, 2000).forEach((j) -> {
                    var val = (float) Math.random() * 100;
                    var closestKey = tree.search(val);
                    if (j % 4 == 0) {
                        tree.delete(closestKey);
                    }
                });
            }
            tree.insert((float) Math.random() * 100);
        });

        var Δtime = (double) ((System.currentTimeMillis()) - startTime) / 1000;
        System.out.println(String.format("The test took %1$s seconds.", Δtime));
    }
}