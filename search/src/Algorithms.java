import javafx.geometry.Pos;

import java.util.*;


public class Algorithms {

    public class Node {
        public int hash, pos, x, y;
        public Node(int hash, int pos, int x, int y) {
            this.hash = hash;
            this.pos = pos;
            this.x = x;
            this.y = y;
        }
    }

    public Vector<PositionNode> mergekSortedPositions(Vector<Position> arrays) {
        Vector<PositionNode> rst = new Vector<PositionNode>();
        if (arrays == null || arrays.size() == 0) {
            return rst;
        }

        PriorityQueue<Node> queue = new PriorityQueue<Node>(arrays.size(),
                new Comparator<Node>() {
                    public int compare(Node a, Node b){
                        return a.pos - b.pos;
                    }
                }
        );

        //init
        for (int i = 0; i < arrays.size(); i++) {
            if (arrays.elementAt(i).positions.length != 0) {
                queue.offer(new Node(arrays.elementAt(i).hash, arrays.elementAt(i).positions[0], i, 0));
            }
        }

        Node node;

        while (!queue.isEmpty()) {
            node = queue.poll();
            if (rst.size() > 0 && rst.elementAt(rst.size() - 1).pos + 1 != node.pos) {
                rst.add(new PositionNode(-1, -1));
            }
            rst.add(new PositionNode(node.hash, node.pos));
            if (node.y < arrays.elementAt(node.x).positions.length - 1) {
                queue.offer(
                        new Node(
                                arrays.elementAt(node.x).hash,
                                arrays.elementAt(node.x).positions[node.y + 1],
                                node.x,
                                node.y + 1
                        )
                );
            }
        }

        return rst;

    }

    public Vector<Integer> KMP(Vector<PositionNode> find, Vector<PositionNode> text) {
        Vector<Integer> answer = new Vector<Integer>();

        Vector<PositionNode> all = new Vector<PositionNode>();
        for(PositionNode node : find) {
            all.add(node);
        }
        all.add(new PositionNode(0, 0));
        for(PositionNode node : text) {
            all.add(node);
        }

        if (all.size() == 0) {
            return answer;
        }

        int[] pi = new int[all.size()];
        pi[0] = 0;
        for (int i = 1; i < all.size(); i++) {
            int j = pi[i - 1];
            while (j > 0 && all.elementAt(i).hash != all.elementAt(j).hash) {
                j = pi[j - 1];
            }
            if (all.elementAt(i).hash == all.elementAt(j).hash) {
                j++;
            }
            if (j == find.size()) {
                answer.add(all.elementAt(i).pos - find.size());
            }

            pi[i] = j;
        }

        return answer;
    }


}
