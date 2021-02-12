import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.nanoTime;

public class SimularPair {
    public class Node implements Comparable<Node> {
        public Integer value;
        final Node parent;

        public Node(int value, Node parent) {
            this.value = value;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node o) {
            return this.value.compareTo(o.value);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return value.equals(node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
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
        HashMap<Node, Node> nodes = new HashMap<>();
        long result = 0;
        SimularPair s = new SimularPair();

        Node dummyParent = s.new Node(0, null);
        String[] edgesRowItems = br.readLine().split(" ");
//        TreeSet<Integer> t = new TreeSet<>();
        int _parent = Integer.parseInt(edgesRowItems[0].trim());
        int child = Integer.parseInt(edgesRowItems[1].trim());
        Node parent = s.new Node(_parent, null);
        nodes.put(parent, parent);
        parent = nodes.get(parent);
        Node newNode = s.new Node(child, parent);
        nodes.put(newNode, newNode);
        if (Math.abs(child - _parent) <= k)
            result++;
        int lastParent = _parent;
        int lastChild = child;
        Deque<Integer> parents = new ArrayDeque<>();
        parents.push(_parent);
//        parents.push(child);
//        t.add(_parent);
//        t.add(child);
        LinkedList<Integer> toAdd = new LinkedList<>();
//        int parentCount = 0;
//        int childCount = 0;
//        int foundCount = 0;
//        int notFoundCount = 0;

        for (int edgesRowItr = 1; edgesRowItr < n - 1; edgesRowItr++) {
            edgesRowItems = br.readLine().split(" ");
            _parent = Integer.parseInt(edgesRowItems[0].trim());
            child = Integer.parseInt(edgesRowItems[1].trim());
            dummyParent.value = _parent;
            parent = nodes.get(dummyParent);
            newNode = s.new Node(child, parent);
            if (_parent == lastChild) {
//                t.add(_parent);
                parents.push(_parent);
//                childCount++;
            } else if (_parent != lastParent) {
                if (!parents.contains(_parent)) {
//                    notFoundCount++;
                    toAdd.clear();
                    toAdd.push(_parent);
                    Integer v = 0;
                    while (true) {
                        parent = parent.parent;
                        if (parents.contains(parent.value)) {
                            while (parents.peek() != parent.value) {
                                v = parents.pop();
//                                t.remove(v);
                            }
                            while (!toAdd.isEmpty()) {
                                v = toAdd.pop();
//                                t.add(v);
                                parents.push(v);
                            }
                            break;
                        } else {
                            toAdd.push(parent.value);
                        }
                    }
//                    t.clear();
//                    parents.clear();
//                    while (parent != null) {
//                        t.add(parent.value);
//                        parents.push(parent.value);
//                        parent = parent.parent;
//                    }
                } else {
//                    foundCount++;
                    while (true) {
                        if (_parent == parents.pop())
                            parents.push(_parent);
                            break;
                        }
                    }

            }
//            else
//                parentCount++;
            lastChild = child;
            lastParent = _parent;

            nodes.put(newNode, newNode);
            AtomicInteger _child = new AtomicInteger(child);
            result += parents.parallelStream().filter(v -> (v < _child.get() + k + 1) && (v > _child.get() - k - 1) ).count();
//            Integer lower = t.lower(child + k + 1);
//            Integer higher = t.higher(child - k - 1);
//            if (lower == null || higher == null)
//                continue;
//            result += t.headSet(lower).size() - t.headSet(higher).size() + 1;
        }
        System.out.println("result: " + result);
//        System.out.printf("parentFound %d   childFound %d   foundCount %d   notFoundCount %d\n", parentCount, childCount, foundCount, notFoundCount);
        System.out.println("elapsed milliseconds: " + ((nanoTime() - startTime) / 1000000));
    }
}
