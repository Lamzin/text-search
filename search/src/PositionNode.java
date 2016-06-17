public class PositionNode {

    public int hash, pos;

    PositionNode(int hash, int pos) {
        this.hash = hash;
        this.pos = pos;
    }

    public String toString() {
        String ans = "{%d; %d}";
        return String.format(ans, this.hash, this.pos);
    }


}
