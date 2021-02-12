import java.io.*;
import java.util.Deque;
import java.util.LinkedList;
import java.util.TreeSet;

import static java.lang.System.nanoTime;

public class SimularPair {
    public class Node implements Comparable<Node> {
        final Integer value;
        final Node parent;

        public Node(int value, Node parent) {
            this.value = value;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node o) {
            return this.value.compareTo(o.value);
        }
    }

    public static void main(String[] args) throws IOException {

        long startTime = nanoTime();
        BufferedInputStream in = new BufferedInputStream(
                new FileInputStream("/home/mark/IdeaProjects/countLuck/data/similarPair.p9.txt"));
        BufferedReader br = new BufferedReader(new
                InputStreamReader(in));
//            Scanner scanner = new Scanner(in);
        String[] nk = br.readLine().split(" ");

        int n = Integer.parseInt(nk[0].trim());

        int k = Integer.parseInt(nk[1].trim());
        TreeSet<Node> nodes = new TreeSet<>();
        long result = 0;
        SimularPair s = new SimularPair();


        String[] edgesRowItems = br.readLine().split(" ");
        TreeSet<Integer> t = new TreeSet<>();
        int _parent = Integer.parseInt(edgesRowItems[0].trim());
        int child = Integer.parseInt(edgesRowItems[1].trim());
        Node parent = s.new Node(_parent, null);
        nodes.add(parent);
        parent = nodes.floor(parent);
        Node newNode = s.new Node(child, parent);
        nodes.add(newNode);
        if (Math.abs(child - _parent) <= k)
            result++;
        int lastParent = _parent;
        int lastChild = child;
        Deque<Integer> parents = new LinkedList<>();
        parents.push(_parent);
//        parents.push(child);
        t.add(_parent);
//        t.add(child);

        for (int edgesRowItr = 1; edgesRowItr < n - 1; edgesRowItr++) {
            edgesRowItems = br.readLine().split(" ");
            _parent = Integer.parseInt(edgesRowItems[0].trim());
            child = Integer.parseInt(edgesRowItems[1].trim());
            parent = s.new Node(_parent, null);
//            TreeSet<Integer> t = new TreeSet<>();
            parent = nodes.floor(parent);
            newNode = s.new Node(child, parent);
            if (_parent != lastParent) {
                if (_parent == lastChild) {
                    t.add(_parent);
                    parents.push(_parent);
                }
                else if (!parents.contains(_parent)) {
//                    LinkedList<Integer> toAdd = new LinkedList<>();
//                    toAdd.push(_parent);
//                    Integer v = 0;
//                    while (true) {
//                        parent = parent.parent;
//                        if (t.contains(parent.value)) {
//                            while (parents.peek() != parent.value) {
//                                v = parents.pop();
//                                t.remove(v);
//                            }
//                            while (!toAdd.isEmpty()) {
//                              v = toAdd.pop();
//                              t.add(v);
//                              parents.push(v);
//                            }
//                            break;
//                        }
//                    }
                    t.clear();
                    parents.clear();
                    while (parent != null) {
                        t.add(parent.value);
                        parents.push(parent.value);
                        parent = parent.parent;
                    }
                }
                else
                    while (true) {
                        int p = parents.pop();
                        if (p != _parent)
                            t.remove(p);
                        else {
                            parents.push(p);
                            break;
                        }
                    }
            }
            lastChild = child;
            lastParent = _parent;

            nodes.add(newNode);

            Integer lower = t.lower(child + k + 1);
            Integer higher = t.higher(child - k - 1);
            if (lower == null || higher == null)
                continue;
            lower = t.headSet(lower).size();
//            lower -= ((lower == 0) ? 1 : 0);
            higher = t.headSet(higher).size() - 1;
//                    System.out.println(entry.parent.headSet(lower).size());
//                    System.out.println(entry.parent.headSet(higher).size());

//                    System.out.println("higher : " + higher + "  lower: " + lower);
            result += lower - higher;
//            t.add(child);
        }
        System.out.println("result: " + result);
        System.out.println("elapsed milliseconds: " + ((nanoTime() - startTime) / 1000000));
    }
}
