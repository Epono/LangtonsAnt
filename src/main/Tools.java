package main;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Tools {
    // squareSugoii(20, 100, 3);
    public static void squareSugoii(int width, int height, int size) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x <= size - 1 || x >= width - size || y <= size - 1 || y >= height - size) {
                    System.out.print("* ");
                } else {
                    System.out.print("  ");
                }
            }

            System.out.println();
        }
    }

    // primeNumber(100000000);
    public static void primeNumber(int max) {
        double start = System.currentTimeMillis();

        ArrayList<Integer> primeNumbers = new ArrayList<Integer>();
        primeNumbers.add(2);

        for (int numberTested = 3; numberTested < max; numberTested++) {
            boolean isPrime = true;
            int l = 0;
            for (int j = primeNumbers.get(l); j <= Math.sqrt(numberTested); j = primeNumbers.get(++l)) {
                if (numberTested % j == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime) {
                primeNumbers.add(numberTested);
                // System.out.println(numberTested);
            }
        }

        double end = System.currentTimeMillis();

        System.out.println(primeNumbers.size() + " nombres premiers inférieurs à " + max + " trouvés en "
                + (end - start) / 1000 + " s");
    }

    public static void power2() throws IOException {
        FileWriter f = new FileWriter("power2.txt");

        BigDecimal lol = new BigDecimal("1");
        f.append(lol.toString());
        f.append("\n\n");
        while (true) {
            lol = lol.multiply(new BigDecimal("2"));
            f.append(lol.toString());
            f.append("\n\n");
        }
        // f.close();
    }

    public static void exponential() throws IOException {
        FileWriter f = new FileWriter("exponential.txt");

        BigDecimal lol = BigDecimal.TEN;
        f.append(lol.toString());
        f.append("\n\n");
        while (true) {
            lol = lol.multiply(lol);
            f.append(lol.toString());
            f.append("\n\n");
        }
        // f.close();
    }
}
