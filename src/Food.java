public class Food {
    int x, y;
    public Food() {
        x = (int)(Math.random() * Main.SIZE);
        y = (int)(Math.random() * Main.SIZE);
        boolean isOnSnake = true;
        while(isOnSnake) {
            isOnSnake = false;
            for(Node current = Main.head; current != null; current = current.next) {
                if(current.x == x && current.y == y) {
                    isOnSnake = true;
                    break;
                }
            }
            if(isOnSnake) {
                x = (int)(Math.random() * Main.SIZE);
                y = (int)(Math.random() * Main.SIZE);
            }
        }
    }
}
