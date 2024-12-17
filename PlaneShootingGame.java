import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

//Plane Shooting Game - Mô Tả Trò Chơi
//Trò Chơi:
//Tên: Plane Shooting Game
//Thể Loại: Trò Chơi Bắn Súng
//Mục Tiêu: Điều khiển máy bay để né tránh va chạm với vật cản và tiêu diệt chúng.

//Điều Khiển:
//Di Chuyển: Sử dụng các phím mũi tên lên, xuống, trái, phải.
//Bắn: Nhấn SPACE.
//Khởi Động Lại: Nhấn R hoặc E sau khi máy bay bị hủy diệt.
//Đồ Họa:
//Nền: Màn hình màu đen.
//Xác Sống: Vật cản màu đỏ kiểu dáng hình vuông.
//Máy Bay: Màu xanh lá cây, kích thước 40x40 pixel.
//Người Thực Hiện: Khải Hoàn




public class PlaneShootingGame extends JPanel implements ActionListener, KeyListener {

    private final int WIDTH = 800, HEIGHT = 600;
    private final int PLAYER_SIZE = 40, ZOMBIE_SIZE = 30, BULLET_SIZE = 5;
    private int playerX, playerY, zombieSpeed, bulletY;
    private boolean isGameOver = false;
    private ArrayList<Rectangle> zombies;
    private ArrayList<Rectangle> bullets;
    private Random random;
    private Timer timer;

    public PlaneShootingGame() {
        setFocusable(true);
        addKeyListener(this);
        setBackground(Color.BLACK);

        playerX = WIDTH / 2;
        playerY = HEIGHT - PLAYER_SIZE * 2;
        zombieSpeed = 5;
        zombies = new ArrayList<>();
        bullets = new ArrayList<>();
        random = new Random();

        timer = new Timer(50, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            spawnZombie();
            moveZombies();
            moveBullets();
            checkCollision();
        }
        repaint();
    }

    private void spawnZombie() {
        if (random.nextInt(100) < 5) {
            int x = random.nextInt(WIDTH - ZOMBIE_SIZE);
            zombies.add(new Rectangle(x, 0, ZOMBIE_SIZE, ZOMBIE_SIZE));
        }
    }

    private void moveZombies() {
        for (Rectangle zombie : zombies) {
            zombie.y += zombieSpeed;
        }
    }

    private void moveBullets() {
        for (Rectangle bullet : bullets) {
            bullet.y -= 10;  // Tốc độ đạn
        }
        // Xoá đạn đã ra khỏi màn hình
        bullets.removeIf(bullet -> bullet.y + BULLET_SIZE < 0);
    }

    private void checkCollision() {
        // Kiểm tra va chạm giữa đạn và xác sống
        for (Iterator<Rectangle> it = zombies.iterator(); it.hasNext(); ) {
            Rectangle zombie = it.next();
            for (Iterator<Rectangle> bulletIt = bullets.iterator(); bulletIt.hasNext(); ) {
                Rectangle bullet = bulletIt.next();
                if (zombie.intersects(bullet)) {
                    it.remove();
                    bulletIt.remove();
                    break;
                }
            }
            if (zombie.intersects(new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE))) {
                isGameOver = true;
                timer.stop();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!isGameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 0) {
                playerX -= 10;  // Di chuyển trái
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < WIDTH - PLAYER_SIZE) {
                playerX += 10;  // Di chuyển phải
            }
            if (e.getKeyCode() == KeyEvent.VK_UP && playerY > 0) {
                playerY -= 10;  // Di chuyển lên
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN && playerY < HEIGHT - PLAYER_SIZE) {
                playerY += 10;  // Di chuyển xuống
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                fireBullet();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R || e.getKeyCode() == KeyEvent.VK_E) {
            resetGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void fireBullet() {
        bullets.add(new Rectangle(playerX + PLAYER_SIZE / 2 - BULLET_SIZE / 2, playerY, BULLET_SIZE, BULLET_SIZE));
    }

    private void resetGame() {
        playerX = WIDTH / 2;
        playerY = HEIGHT - PLAYER_SIZE * 2;
        zombies.clear();
        bullets.clear();
        isGameOver = false;
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isGameOver) {
            g.setColor(Color.GREEN);
            g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);

            g.setColor(Color.RED);
            for (Rectangle zombie : zombies) {
                g.fillRect(zombie.x, zombie.y, ZOMBIE_SIZE, ZOMBIE_SIZE);
            }

            g.setColor(Color.YELLOW);
            for (Rectangle bullet : bullets) {
                g.fillRect(bullet.x, bullet.y, BULLET_SIZE, BULLET_SIZE);
            }
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Game Over! Press R or E to Restart", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Plane Shooting Game");
        PlaneShootingGame game = new PlaneShootingGame();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
