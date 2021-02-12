import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;

public class CountLuck {

    class Position {
        final int x;
        final int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
    static HashSet<Position> goals;
    static Position goal;
    static int L;
    static int W;
    static boolean onlyM(Position p, char [] [] m) {
        int x = p.x;
        int y = p.y;
        if (x > 0 && m[y][x - 1] == '.')
            return false;
        if (x < W - 1 && m[y][x + 1] == '.')
            return false;
        if (y > 0 && m[y - 1][x] == '.')
            return false;
        if (y < L - 1 && m[y + 1][x] == '.')
            return false;
        return true;
    }
    static int moveAndBranch(CountLuck s,int wands, Position position, char [] [] m) {
        HashSet<Position> paths = null;
        Position nearGoal = null;
        while (true) {
            if (goals.contains(position)) {
                return wands + (onlyM(position, m) ? 0 : 1);
            }

            int y = position.y;
            int x = position.x;
            m[y][x] = 'x';
            paths = new HashSet<>();
            position = null;
            if (y > 0 && m[y - 1][x] == '.') {
                position = s.new Position(x, y - 1);
                if (goals.contains(position))
//                    return wands + (onlyM(position, m) ? 0 : 1);
                    nearGoal = position;
                paths.add(position);
            }
            if (y < L - 1 && m[y + 1][x] == '.') {
                position = s.new Position(x, y + 1);
                if (goals.contains(position))
                    nearGoal = position;
//
//                    return wands + + (onlyM(position, m) ? 0 : 1);
                paths.add(position);
            }
            if (x > 0 && m[y][x -1] == '.') {
                position = s.new Position(x - 1, y);
                if (goals.contains(position))
    //                    return wands + (onlyM(position, m) ? 0 : 1);
                    nearGoal = position;

                paths.add(position);
            }
            if (x < W - 1 && m[y][x + 1] == '.') {
                position = s.new Position(x + 1, y);
                if (goals.contains(position)) {
                    nearGoal = position;
//                    return wands + (onlyM(position, m) ? 0 : 1)
                }
                paths.add(position);
            }
            if (paths.size() == 1) {
                m[y][x] = 'x';
                m[position.y][position.x] = 'M';
                if (nearGoal != null)
                    return wands + (onlyM(position, m) ? 0 : 1);
            }
            else {
                if (nearGoal != null)
                    return wands + (onlyM(position, m) ? 0 : 1) + 1;
                break;
            }
        }
        if (paths.size() == 0)
            return -1;
        Iterator value = paths.iterator();
        while (value.hasNext()) {
            int w = moveAndBranch(s, wands + 1, (Position)value.next(), m);
            if (w != -1)
                return w;
        }
        return -1;
    }
    // Complete the countLuck function below.
    static String countLuck(String[] matrix, int k) {
        System.out.println("++++++++++++++++k = " + k);
        CountLuck s = new CountLuck();
        L = matrix.length;
        W = matrix[0].length();
        char [] [] m = new char[L][W];
        // System.out.println("m[0][0]: " + m[0][0] + " == \0: "  + (m[0][0] == '\0' ));
        boolean goalFound = false;
        boolean sourceFound = false;
        HashSet<Position> paths = null;
        goal = null;
        for (int y = 0;y < L;y++)
            for (int x = 0;x < W ;x++) {
                if (m[y][x] == '\0')
                    m[y][x] = matrix[y].charAt(x);
                if (m[y][x] == '*') {
//                    assertFalse(goalFound);
                    if (goalFound)
                        continue;
                    goal = s.new Position(x, y);
                    goals = new HashSet<>();
                    goalFound = true;
                    if (y > 0 && matrix[y - 1].charAt(x) == '.')
                        goals.add(s.new Position(x, y - 1));
                    if (y < L - 1 && matrix[y + 1].charAt(x) == '.')
                        goals.add(s.new Position(x, y + 1));
                    if (x > 0 && matrix[y].charAt(x - 1) == '.')
                        goals.add(s.new Position(x - 1, y));
                    if (x < W - 1 && matrix[y].charAt(x + 1) == '.')
                        goals.add(s.new Position(x + 1, y));
                }
                else if (m[y][x] == 'M') {
                    System.out.printf("M found at x %d  y %d\n", x, y);
                    // System.out.printf("m[0][1] %c m[0][2] %c\n", m[0][1], m[0][2]);
                    paths = new HashSet<>();
                    sourceFound = true;
                    Position position = null;
                    if (y > 0 && m[y - 1][x] == '.') {
                        position = s.new Position(x, y - 1);
                        paths.add(position);
                    }
                    if (y < L - 1 && m[y + 1][x] == '\0')
                        m[y + 1][x] = matrix[y + 1].charAt(x);
//                    if (y < L - 1)
//                        System.out.printf("x %d y: %d  L: %d m[y + 1][x] %c\n",
//                                x, y, L, m[y + 1][x]);
                    if (y < L - 1 && m[y + 1][x] == '.') {
                        position = s.new Position(x, y + 1);
                        paths.add(position);
                    }
                    if (x > 0 && m[y][x - 1] == '.') {
                        position = s.new Position(x - 1, y);
                        paths.add(position);
                    }
                    if (x < W - 1 && m[y][x + 1] == '\0')
                        m[y][x + 1] = matrix[y].charAt(x + 1);
//                    if (x < W - 1)
//                        System.out.printf("x %d y: %d  L: %d m[y][x + 1] %c\n",
//                                x, y, L, m[y][x + 1]);
                    if (x < W - 1 && m[y][x + 1] == '.') {
                        position = s.new Position(x + 1, y);
                        paths.add(position);
                    }
                    sourceFound = true;
                }

            }
        assertTrue(sourceFound && goalFound);
        System.out.println("made m");
        Iterator value = paths.iterator();
        while (value.hasNext()) {
            Position p = (Position)value.next();
            int w = moveAndBranch(s,(paths.size() > 1 ? 1 : 0), p, m);
            if (w != -1) {
                System.out.printf("w: %d  k: %d\n", w, k);
//                if (w > 1)
//                    w -= 1;
                // if (!onlyM(goal, p , m))
                //   w += 1;
                return (w == k) ?  "Impressed" : "Oops!";
            }
        }
        System.out.println("Not Found");
        return "Oops!";
    }

    private static Scanner scanner = null;

    public static void main(String[] args) throws IOException {
//        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));
        BufferedInputStream in = new BufferedInputStream(
                new FileInputStream("/home/mark/IdeaProjects/countLuck/data/countLuck.p2.all.txt"));
        scanner = new Scanner(in);
        int t = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        for (int tItr = 0; tItr < t; tItr++) {
            String[] nm = scanner.nextLine().split(" ");

            int n = Integer.parseInt(nm[0]);

            int m = Integer.parseInt(nm[1]);

            String[] matrix = new String[n];

            for (int i = 0; i < n; i++) {
                String matrixItem = scanner.nextLine();
                matrix[i] = matrixItem;
            }

            int k = scanner.nextInt();
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            String result = countLuck(matrix, k);
            System.out.println("Result: " + result);

//            bufferedWriter.write(result);
//            bufferedWriter.newLine();
        }

//        bufferedWriter.close();

        scanner.close();
    }

}
