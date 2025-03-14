import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    final int MAX_COL = 7;
    final int MAX_ROW = 6;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 600;
    public static final int INFOHEIGHT = 100;
    final int SQUARE_SIZE = 100;
    final int HALF_SQUARE_SIZE = SQUARE_SIZE / 2;
    final int PIECE_SIZE = (int) (SQUARE_SIZE * 0.75);
    final int FPS = 60;

    Thread gameThread;
    Board board = new Board();
    Mouse mouse = new Mouse();
    Keyboard keyboard = new Keyboard();

    public static final int RED = 1;
    public static final int BLUE = 2;
    public static final int DRAW = 3;
    int currentTurn = RED;

    int[][] gameBoard = new int[6][7];

    boolean gameOver = false;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT + INFOHEIGHT));
        addMouseListener(mouse);
        addKeyListener(keyboard);
        setFocusable(true);
    }

    public void resetBoard(int[][] gameBoard) {

        for (int i = 0; i < MAX_ROW; i++) {
            for (int j = 0; j < MAX_COL; j++) {
                gameBoard[i][j] = 0;
            }
        }

        currentTurn = RED;
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void checkWin() {

        int countPiece = 0;
        int count1 = 0;
        int count2 = 0;

        for (int row = 0; row < MAX_ROW; row++) {

            count1 = 0;
            count2 = 0;

            for (int col = 0; col < MAX_COL; col++) {

                if (gameBoard[row][col] == 0) {
                    count1 = 0;
                    count2 = 0;
                } else if (gameBoard[row][col] == 1) {
                    countPiece++;
                    count1++;
                    count2 = 0;
                } else if (gameBoard[row][col] == 2) {
                    countPiece++;
                    count1 = 0;
                    count2++;
                }
                if (countPiece == MAX_COL * MAX_ROW) {
                    currentTurn = DRAW;
                    gameOver = true;
                }
                if (count1 == 4 || count2 == 4) {
                    gameOver = true;
                }
            }
        }

        for (int col = 0; col < MAX_COL; col++) {

            count1 = 0;
            count2 = 0;

            for (int row = 0; row < MAX_ROW; row++) {

                if (gameBoard[row][col] == 0) {
                    count1 = 0;
                    count2 = 0;
                } else if (gameBoard[row][col] == 1) {
                    count1++;
                    count2 = 0;
                } else if (gameBoard[row][col] == 2) {
                    count1 = 0;
                    count2++;
                }
                if (count1 == 4 || count2 == 4) {
                    gameOver = true;
                }
            }
        }

        for (int col = 0; col < MAX_COL; col++) {

            count1 = 0;
            count2 = 0;
            int r = 0;
            int c = col;

            while (r < MAX_ROW && c < MAX_COL) {

                if (gameBoard[r][c] == 0) {
                    count1 = 0;
                    count2 = 0;
                } else if (gameBoard[r][c] == 1) {
                    count1++;
                    count2 = 0;
                } else if (gameBoard[r][c] == 2) {
                    count1 = 0;
                    count2++;
                }
                if (count1 == 4 || count2 == 4) {
                    gameOver = true;
                }

                r++;
                c++;
            }
        }

        for (int row = 1; row < MAX_ROW; row++) {

            count1 = 0;
            count2 = 0;
            int r = row;
            int c = 0;

            while (r < MAX_ROW && c < MAX_COL) {

                if (gameBoard[r][c] == 0) {
                    count1 = 0;
                    count2 = 0;
                } else if (gameBoard[r][c] == 1) {
                    count1++;
                    count2 = 0;
                } else if (gameBoard[r][c] == 2) {
                    count1 = 0;
                    count2++;
                }
                if (count1 == 4 || count2 == 4) {
                    gameOver = true;
                }

                r++;
                c++;
            }
        }

        for (int col = 0; col < MAX_COL; col++) {

            count1 = 0;
            count2 = 0;
            int r = 0;
            int c = col;

            while (r < MAX_ROW && c > 0) {

                if (gameBoard[r][c] == 0) {
                    count1 = 0;
                    count2 = 0;
                } else if (gameBoard[r][c] == 1) {
                    count1++;
                    count2 = 0;
                } else if (gameBoard[r][c] == 2) {
                    count1 = 0;
                    count2++;
                }
                if (count1 == 4 || count2 == 4) {
                    gameOver = true;
                }

                r++;
                c--;
            }
        }

        for (int row = 1; row < MAX_ROW; row++) {

            count1 = 0;
            count2 = 0;
            int r = row;
            int c = MAX_COL - 1;

            while (r < MAX_ROW && c >= 0) {

                if (gameBoard[r][c] == 0) {
                    count1 = 0;
                    count2 = 0;
                } else if (gameBoard[r][c] == 1) {
                    count1++;
                    count2 = 0;
                } else if (gameBoard[r][c] == 2) {
                    count1 = 0;
                    count2++;
                }
                if (count1 == 4 || count2 == 4) {
                    gameOver = true;
                }

                r++;
                c--;
            }
        }
    }

    private void update() {

        if (gameOver == false) {

            if (mouse.pressed) {

                int col = mouse.x / SQUARE_SIZE;
                int row = -1;
                for (int i = 0; i < MAX_ROW; i++) {
                    if (gameBoard[i][col] == 0) {
                        row = i;
                    }
                }
                if (row == -1) {
                    mouse.pressed = false;
                    return;
                }

                if (currentTurn == RED) {
                    gameBoard[row][col] = 1;
                } else if (currentTurn == BLUE) {
                    gameBoard[row][col] = 2;
                }

                if (currentTurn == RED) {
                    currentTurn = BLUE;
                } else if (currentTurn == BLUE) {
                    currentTurn = RED;
                }

                mouse.pressed = false;

                checkWin();
            }
        }

        if (gameOver) {
            if (keyboard.spacePressed) {

                keyboard.spacePressed = false;
                gameOver = false;
                resetBoard(gameBoard);
                mouse.pressed = false;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        board.draw(g2);
        g2.setColor(Color.black);
        g2.fillRect(0, HEIGHT, WIDTH, INFOHEIGHT);

        for (int row = 0; row < MAX_ROW; row++) {

            for (int col = 0; col < MAX_COL; col++) {

                if (gameBoard[row][col] == RED) {
                    g2.setColor(Color.red);
                    g2.fillOval(col * SQUARE_SIZE + HALF_SQUARE_SIZE / 4, row * SQUARE_SIZE + HALF_SQUARE_SIZE / 4,
                            PIECE_SIZE, PIECE_SIZE);
                }
                if (gameBoard[row][col] == BLUE) {
                    g2.setColor(Color.blue);
                    g2.fillOval(col * SQUARE_SIZE + HALF_SQUARE_SIZE / 4, row * SQUARE_SIZE + HALF_SQUARE_SIZE / 4,
                            PIECE_SIZE, PIECE_SIZE);
                }
            }
        }

        g2.setFont(new Font("Ink Free", Font.PLAIN, 90));
        String s = "";

        if (currentTurn == RED) {
            g2.setColor(Color.red);
            s = "Red's Turn";
            g2.drawString(s, 145, 680);
        } else {
            g2.setColor(Color.blue);
            s = "Blue's Turn!";
            g2.drawString(s, 120, 680);
        }

        if (gameOver) {

            g2.setColor(Color.black);
            g2.fillRect(0, HEIGHT, WIDTH, INFOHEIGHT);

            g2.setFont(new Font("Ink Free", Font.PLAIN, 90));
            String d = "";

            if (currentTurn == BLUE) {
                g2.setColor(Color.red);
                d = "Red Wins!";
                g2.drawString(d, 175, 680);
            } else if (currentTurn == RED) {
                g2.setColor(Color.blue);
                d = "Blue Wins!";
                g2.drawString(d, 160, 680);
            } else if (currentTurn == DRAW) {
                g2.setColor(Color.white);
                d = "It's a draw!";
                g2.drawString(d, 130, 680);
            }
        }
    }
}
