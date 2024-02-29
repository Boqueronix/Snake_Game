import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    static final int STARTINGSIZE = 2;
    static final int SIZE = 45;
    static Node head;
    static Node tail;
    static ArrayList<Food> food = new ArrayList<>();
    static ArrayList<Direction> queue = new ArrayList<>();
    static boolean gameRunning;
    static long time;
    static int score;
    static int desiredFood;

    static JFrame frame;

    public static void main(String[] args) {
        StdDraw.frame.setVisible(false);
        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Main Menu");
        JPanel panel = new JPanel();
        panel.add(new JLabel("Maximum Score: " + getHighScore()));
        JButton button = new JButton("Start Game");
        button.addActionListener(e -> {
            gameRunning = true;
            time = 0;
            score = 0;
        });
        panel.add(button);
        frame.setContentPane(panel);
        frame.setVisible(true);
        while (!gameRunning) {
            StdDraw.pause(100);
            while (gameRunning) {
                startGame();
            }
        }
    }



    static void startGame() {
        frame.setVisible(false);
        StdDraw.setTitle("Snake Game");
        StdDraw.frame.setVisible(true);
        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, SIZE);
        StdDraw.setYscale(0, SIZE);
        head = new Node(22, 22, null, Direction.DOWN, true);
        Node current = head;
        for(int i = 1; i < STARTINGSIZE; i++) {
            current.next = new Node(22, 22 + i, null, Direction.DOWN, false);
            current.next.prev = current;
            current = current.next;
        }
        current = head;
        while (current.next != null) {
            current = current.next;
        }
        tail = current;
        while(gameRunning) {
            tick();
            time++;
            StdDraw.pause(100);
        }
    }

    static void tick() {
        move();
        checkFood();
        checkCollision();
        if(!gameRunning) {
            reset();
            return;
        }
        draw();
    }

    static void move() {
        if(!queue.isEmpty()) {
            Direction first = queue.remove(0);
            if (head.direction == Direction.UP && first != Direction.DOWN) {
                head.direction = first;
            } else if (head.direction == Direction.DOWN && first != Direction.UP) {
                head.direction = first;
            } else if (head.direction == Direction.LEFT && first != Direction.RIGHT) {
                head.direction = first;
            } else if (head.direction == Direction.RIGHT && first != Direction.LEFT) {
                head.direction = first;
            }
        }
        int x = head.x;
        int y = head.y;
        Node current = Main.tail;
        while (current != null) {
            switch(current.direction) {
                case UP -> current.y++;
                case DOWN -> current.y--;
                case LEFT -> current.x--;
                case RIGHT -> current.x++;
            }
            if (current.prev != null) {
                current.direction = current.prev.direction;
            }
            current = current.prev;
        }
    }

    static void checkFood() {
        for(int i = 0; i < food.size(); i++) {
            if(head.x == food.get(i).x && head.y == food.get(i).y) {
                score++;
                food.set(i, new Food());
                switch(tail.direction) {
                    case UP -> tail.next = new Node(tail.x, tail.y - 1, null, tail.direction, true);
                    case DOWN -> tail.next = new Node(tail.x, tail.y + 1, null, tail.direction, true);
                    case LEFT -> tail.next = new Node(tail.x + 1, tail.y, null, tail.direction, true);
                    case RIGHT -> tail.next = new Node(tail.x - 1, tail.y, null, tail.direction, true);
                }
                tail.next.prev = tail;
                tail = tail.next;
            }
        }
        if (food.isEmpty()) {
            food.add(new Food());
        }
        desiredFood = (int) (10.0 / (1.0 + Math.exp(0.007 * (400.0 - time))) + 0.5);
        if (food.size() < desiredFood) {
            food.add(new Food());
        }
    }

    static void checkCollision() {
        if(head.x < 0 || head.x > SIZE || head.y < 0 || head.y > SIZE) {
            gameRunning = false;
        }
        Node current = head.next;
        while(current != null) {
            if (head.x == current.x && head.y == current.y) {
                System.out.println("Collision with self");
                gameRunning = false;
                break;
            }
            current = current.next;
        }
    }

    static void draw() {
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        Node current = head;
        while(current != null) {
            StdDraw.filledSquare(current.x, current.y, 0.5);
            current = current.next;
        }
        StdDraw.setPenColor(StdDraw.RED);
        for(Food f : food) {
            StdDraw.filledCircle(f.x, f.y, 0.5);
        }
        StdDraw.show();
    }

    static int getHighScore() {
        int highscore = 0;
        try {
            Scanner scanner = new Scanner(new File("src/highscore.txt"));
            highscore = scanner.nextInt();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return highscore;
    }

    static void reset() {
        StdDraw.frame.setVisible(false);
        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Restart Game");
        button.addActionListener(e -> {
            gameRunning = true;
            time = 0;
            score = 0;
        });
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(new JLabel("Game Over! Score: " + score));
        frame.setTitle("Game Over");
        int highscore = getHighScore();
        if(score > highscore) {
            highscore = score;
            try {
                FileWriter writer = new FileWriter("src/highscore.txt");
                writer.write(Integer.toString(highscore));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            panel.add(new JLabel("Highsore: " + highscore));
            panel.add(new JLabel("New Highscore!"));
        } else {
            panel.add(new JLabel("Highscore: " + highscore));
        }
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public static void keyTyped(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> queue.add(Direction.UP);
            case KeyEvent.VK_DOWN -> queue.add(Direction.DOWN);
            case KeyEvent.VK_LEFT -> queue.add(Direction.LEFT);
            case KeyEvent.VK_RIGHT -> queue.add(Direction.RIGHT);
        }
    }
}