public class Test {
    public static void main(String[] args) {
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 10);
        StdDraw.setYscale(0, 10);
        for (int i = 0; i < 10000; i++) {
            StdDraw.circle(i, getY(i * 1000),0.5);
        }
        StdDraw.show();
    }

    public static double getY(int x) {
        return (double) ((10.0 / (1.0 + Math.exp(0.0005 * (5000.0 - x)))));
    }
}
