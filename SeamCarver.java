/* *****************************************************************************
 *  Name:    Devin Plumb
 *  NetID:   dplumb
 *  Precept: P06
 *
 *  Description:  Mutable data type capable of image-resizing. Takes in a
 *                Picture object and can identify and remove seams of minimum
 *                energy.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class SeamCarver {

    private int[][] rgb; // stores red green blue components of each pixel

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("null argument");
        }
        rgb = new int[picture.width()][picture.height()];
        for (int col = 0; col < picture.width(); col++)
            for (int row = 0; row < picture.height(); row++)
                rgb[col][row] = picture.getRGB(col, row);
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(rgb.length, rgb[0].length);
        for (int col = 0; col < picture.width(); col++)
            for (int row = 0; row < picture.height(); row++)
                picture.setRGB(col, row, rgb[col][row]);
        return picture;
    }

    // width of current picture
    public int width() {
        return rgb.length;
    }

    // height of current picture
    public int height() {
        return rgb[0].length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height()) {
            throw new IllegalArgumentException("input outside range");
        }
        return Math.sqrt(squaredEnergy(x, y));
    }

    // squared energy of pixel at column x and row y, saves time
    private double squaredEnergy(int x, int y) {
        int xLeft = x - 1;
        int xRight = x + 1;
        int yBelow = y - 1;
        int yAbove = y + 1;

        if (x == 0)
            xLeft = width() - 1;
        if (x == width() - 1)
            xRight = 0;
        if (y == 0)
            yBelow = height() - 1;
        if (y == height() - 1)
            yAbove = 0;

        double xGradient = sum(colors(xRight, y), colors(xLeft, y));
        double yGradient = sum(colors(x, yAbove), colors(x, yBelow));

        return xGradient + yGradient;
    }

    // gets the color values of an individual pixel
    private int[] colors(int x, int y) {
        int color = rgb[x][y];
        int hex = 256;
        int blue = color & (hex - 1);
        int green = (color & ((hex - 1) * hex)) >> 8;
        int red = (color & ((hex - 1) * hex * hex)) >> 16;
        return new int[] { blue, green, red };
    }

    // sums the color gradients
    private double sum(int[] colors1, int[] colors2) {
        double sum = 0;
        for (int i = 0; i < colors1.length; i++) {
            sum += Math.pow(colors1[i] - colors2[i], 2);
        }
        return sum;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] distTo = new double[height()][width()];
        double[][] energy = new double[height()][width()];
        int[][] edgeTo = new int[height()][width()];
        IndexMinPQ<Double> pq = new IndexMinPQ<Double>(height() * width() + 1);
        for (int height = 0; height < height(); height++) {
            energy[height][0] = energy(0, height);
            distTo[height][0] = energy[height][0];
            pq.insert(height, energy[height][0]);
            for (int width = 1; width < width(); width++) {
                energy[height][width] = energy(width, height);
                distTo[height][width] = Double.POSITIVE_INFINITY;
            }
        }
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if (v < height() * (width() - 1)) {
                int start, end;
                if (v % height() == 0)
                    start = v + height();
                else
                    start = v + height() - 1;
                if (v % height() == height() - 1)
                    end = v + height();
                else
                    end = v + height() + 1;
                for (int w = start; w <= end; w++) {
                    relax(v, w, distTo, energy, edgeTo, pq);
                }
            }
        }
        double min = Double.POSITIVE_INFINITY;
        int v = (width() - 1) * height();
        for (int i = (width() - 1) * height(); i < width() * height(); i++) {
            if (distTo[i % height()][i / height()] < min) {
                v = i;
                min = distTo[i % height()][i / height()];
            }
        }
        int[] seam = new int[width()];
        for (int i = width() - 1; i >= 0; i--) {
            seam[i] = v % height();
            v = edgeTo[v % height()][v / height()];
        }
        return seam;
    }

    // relaxes an edge, updating the to vertex's distanceTo
    private void relax(int v, int w, double[][] distTo, double[][] energy,
                       int[][] edgeTo, IndexMinPQ<Double> pq) {
        if (distTo[w % height()][w / height()] > distTo[v % height()][v / height()]
                + energy[w % height()][w / height()]) {
            distTo[w % height()][w / height()] = distTo[v % height()][v / height()]
                    + energy[w % height()][w / height()];
            edgeTo[w % height()][w / height()] = v;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w % height()][w / height()]);
            else pq.insert(w, distTo[w % height()][w / height()]);
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        transpose();
        int[] seam = findHorizontalSeam();
        transpose();
        return seam;
    }

    // exchanges i,j for j,i
    private void transpose() {
        int[][] temp = new int[rgb[0].length][rgb.length];
        for (int col = 0; col < temp.length; col++)
            for (int row = 0; row < temp[0].length; row++)
                temp[col][row] = rgb[row][col];
        rgb = temp;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException("wrong length array");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height()) {
                throw new IllegalArgumentException("input outside range");
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("not connected seam");
            }
        }
        if (height() == 1) {
            throw new IllegalArgumentException("height is 1");
        }
        int[][] temp = new int[rgb.length][rgb[0].length - 1];
        for (int col = 0; col < temp.length; col++) {
            for (int row = 0; row < seam[col]; row++)
                temp[col][row] = rgb[col][row];
            for (int row = seam[col]; row < temp[0].length; row++)
                temp[col][row] = rgb[col][row + 1];
        }
        rgb = temp;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException("wrong length array");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width()) {
                throw new IllegalArgumentException("input outside range");
            }
            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException("not connected seam");
            }
        }
        if (width() == 1) {
            throw new IllegalArgumentException("width is 1");
        }
        transpose();
        removeHorizontalSeam(seam);
        transpose();
    }

    //  unit testing (required)
    public static void main(String[] args) {
    /*
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        StdOut.println(sc.energy(0, 0));
        if (sc.width() != picture.width())
            StdOut.println("Error 1");
        if (sc.height() != picture.height())
            StdOut.println("Error 2");
        int[] hSeam = sc.findHorizontalSeam();
        sc.removeHorizontalSeam(hSeam);
        int[] vSeam = sc.findVerticalSeam();
        sc.removeVerticalSeam(vSeam);
        if (sc.width() != picture.width() - 1)
            StdOut.println("Error 3");
        if (sc.height() != picture.height() - 1)
            StdOut.println("Error 4");
        sc.picture().show();
    */
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        Stopwatch stopwatch = new Stopwatch();
        int[] vSeam = sc.findVerticalSeam();
        sc.removeVerticalSeam(vSeam);
        StdOut.println(stopwatch.elapsedTime());

    }
}
