public class Node {
    int x, y;
    Node next;
    Node prev;
    Direction direction;
    boolean isHead;
    public Node(int x, int y, Node next, Direction direction, boolean isHead) {
        this.x = x;
        this.y = y;
        this.next = next;
        this.direction = direction;
        this.isHead = isHead;
    }
}
