import java.awt.Color;
import java.awt.Graphics2D;

public class Board {

    final int MAX_COL = 7;
    final int MAX_ROW = 6;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 600;
    public static final int SQUARE_SIZE = 100;

    public void draw(Graphics2D g2) {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        for (int row = 1; row < MAX_ROW + 1; row++) {

            for (int col = 1; col < MAX_COL; col++) {

                g2.setColor(Color.green);
                g2.fillRect(col * SQUARE_SIZE - 5, 0, 10, HEIGHT);
                g2.fillRect(0, row * SQUARE_SIZE - 5, WIDTH, 10);
            }
        }
    }
}
