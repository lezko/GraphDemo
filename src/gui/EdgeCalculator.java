package gui;

public class EdgeCalculator {
    public static double[] calc(double x1, double y1, double x2, double y2, double d) {
        if (x1 == x2) {
            return new double[]{x1, Math.max(y1, y2) - d, x2, Math.min(y1, y2) + d};
        }
        if (y1 == y2) {
            return new double[]{Math.min(x1, x2) + d, y1, Math.max(x1, x2) - d, y2};
        }

        if (x1 > x2) {
            double tmp = x1;
            x1 = x2;
            x2 = tmp;

            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }

        double k = (y2 - y1) / (x2 - x1);
        double b = (x2 * y1 - x1 * y2) / (x2 - x1);

        double[] tmp = quadraticEquation(k * k + 1, -(2 * x1 + 2 * k * k * x1), -d * d + x1 * x1 + k * k * x1 * x1);
        double x3 = Math.max(tmp[0], tmp[1]);
        double y3 = k * x3 + b;

        tmp = quadraticEquation(k * k + 1, -(2 * x2 + 2 * k * k * x2), -d * d + x2 * x2 + k * k * x2 * x2);
        double x4 = Math.min(tmp[0], tmp[1]);
        double y4 = k * x4 + b;

        return new double[]{x3, y3, x4, y4};
    }

    private static double[] quadraticEquation(double a, double b, double c) {
        double sqrtD = Math.sqrt(b * b - 4 * a * c);
        return new double[]{(-b - sqrtD) / (2 * a), (-b + sqrtD) / (2 * a)};
    }
}
