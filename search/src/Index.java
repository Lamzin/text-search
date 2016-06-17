import java.util.Vector;

public class Index {

    public int documentId;

    public Vector<Position> positions;

    public Index(int documentId) {
        this.documentId = documentId;
        this.positions = new Vector<Position>();
    }

    public Vector<Integer> Build(String find) {
        Vector<String> words = Cleaner.GetWords(find);
        Vector<Integer> hashes = new Vector<Integer>();
        for (String word : words) {
            hashes.add(Utils.StringToCRC32(word));
        }

        Vector<PositionNode> findPositions = new Vector<PositionNode>();
        for (Integer item : hashes) {
            findPositions.add(new PositionNode(item, -2));
        }

        Vector<PositionNode> sortedPositions = new Algorithms().mergekSortedPositions(this.positions);

        Vector<Integer> entryPotisions = new Algorithms().KMP(findPositions, sortedPositions);

        for (Integer item : entryPotisions) {
            System.out.printf("Doc: %d, pos: %d\n", this.documentId, item);
        }

        return entryPotisions;

//        for (PositionNode node : sortedPositions) {
//            System.out.printf("%d %d\n", node.hash, node.pos);
//        }
//        System.out.printf("-------------------------------\n\n");

    }

}
