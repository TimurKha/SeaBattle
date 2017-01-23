/**
 *
 *
 *
 * @author	Timur Khairulin
 * @version
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Field extends JPanel {

    private Timer tmDraw;
    private Image background, deck, killed, injured, win, lose, bomb;
    private JButton start, quit;
    private Game myGame;
    private int mouseX, mouseY;

    public Field() {
        addMouseListener(new myMouse1());
        addMouseMotionListener(new myMouse2());
        setFocusable(true);
        // Create new game
        myGame = new Game();
        myGame.start();
        try {
            background = ImageIO.read(new File("img\\background.png"));
            deck = ImageIO.read(new File("img\\deck.png"));
            injured = ImageIO.read(new File("img\\injured.png"));
            killed = ImageIO.read(new File("img\\killed.png"));
            win = ImageIO.read(new File("img\\win.png"));
            lose = ImageIO.read(new File("img\\lose.png"));
            bomb = ImageIO.read(new File("img\\bomb.png"));
        } catch (Exception ex) {
        }
        // Create timer for draw field
        tmDraw = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                repaint();
            }
        });
        tmDraw.start();
        setLayout(null);
        start = new JButton();
        start.setText("New Game");
        start.setForeground(Color.BLUE);
        start.setFont(new Font("serif", 0, 30));
        start.setBounds(130, 450, 200, 80);
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                myGame.start();
            }
        });
        add(start);

        quit = new JButton();
        quit.setText("Exit");
        quit.setForeground(Color.RED);
        quit.setFont(new Font("serif", 0, 30));
        quit.setBounds(530, 450, 200, 80);
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        add(quit);
    }


    public void paintComponent(Graphics gr) {
        // Clean playing field
        super.paintComponent(gr);
        gr.drawImage(background, 0, 0, 900, 600, null);
        gr.setFont(new Font("serif", 3, 40));
        gr.setColor(Color.BLUE);
        gr.drawString("Computer", 150, 50);
        gr.drawString("Player", 590, 50);

        // Drawing playing fields of computers and the Player on basis of arrays
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // Computer field
                if (myGame.arrComputer[i][j] != 0) {
                    // If a wounded ship deck
                    if ((myGame.arrComputer[i][j] >= 8) && (myGame.arrComputer[i][j] <= 11)) {
                        gr.drawImage(injured, 100 + j * 30, 100 + i * 30, 30, 30, null);
                    }
                    // If the deck is completely downed ship
                    else if (myGame.arrComputer[i][j] >= 15) {
                        gr.drawImage(killed, 100 + j * 30, 100 + i * 30, 30, 30, null);
                    }
                    // Was a shot
                    if (myGame.arrComputer[i][j] >= 5) {
                        gr.drawImage(bomb, 100 + j * 30, 100 + i * 30, 30, 30, null);
                    }
                }
                // Player Field
                if (myGame.arrPlayer[i][j] != 0) {
                    // If it a ship deck
                    if ((myGame.arrPlayer[i][j] >= 1) && (myGame.arrPlayer[i][j] <= 4)) {
                        gr.drawImage(deck, 500 + j * 30, 100 + i * 30, 30, 30, null);
                    }
                    // If a wounded ship deck
                    else if ((myGame.arrPlayer[i][j] >= 8) && (myGame.arrPlayer[i][j] <= 11)) {
                        gr.drawImage(injured, 500 + j * 30, 100 + i * 30, 30, 30, null);
                    }
                    // If the deck is completely downed ship
                    else if (myGame.arrPlayer[i][j] >= 15) {
                        gr.drawImage(killed, 500 + j * 30, 100 + i * 30, 30, 30, null);
                    }
                    // was a shot
                    if (myGame.arrPlayer[i][j] >= 5) {
                        gr.drawImage(bomb, 500 + j * 30, 100 + i * 30, 30, 30, null);
                    }
                }
            }
        }
        gr.setColor(Color.RED);
        if ((mouseX > 100) && (mouseY > 100) && (mouseX < 400) && (mouseY < 400)) {
            if ((myGame.endGame == 0) && (myGame.compTurn == false)) {
                int i = (mouseY - 100) / 30;
                int j = (mouseX - 100) / 30;
                if (myGame.arrComputer[i][j] <= 4)
                    gr.fillRect(100 + j * 30, 100 + i * 30, 30, 30);
            }
        }
        // Drawing playing field grid with blue lines
        gr.setColor(Color.BLUE);
        for (int i = 0; i <= 10; i++) {
            // Drawing grid lines computer field
            gr.drawLine(100 + i * 30, 100, 100 + i * 30, 400);
            gr.drawLine(100, 100 + i * 30, 400, 100 + i * 30);
            // Drawing grid lines player field
            gr.drawLine(500 + i * 30, 100, 500 + i * 30, 400);
            gr.drawLine(500, 100 + i * 30, 800, 100 + i * 30);
        }

        gr.setFont(new Font("serif", 0, 20));
        gr.setColor(Color.RED);
        // Introduction of numbers and letters on the left and top of the playing fields
        for (int i = 1; i <= 10; i++) {
            // Output numbers
            gr.drawString("" + i, 73, 93 + i * 30);
            gr.drawString("" + i, 473, 93 + i * 30);
            // Output letters
            gr.drawString("" + (char) ('A' + i - 1), 78 + i * 30, 93);
            gr.drawString("" + (char) ('A' + i - 1), 478 + i * 30, 93);
        }
        // End of the game
        if (myGame.endGame == 1) // The player wins
        {
            gr.drawImage(win, 300, 200, 300, 100, null);
        } else if (myGame.endGame == 2) // The computer wins
        {
            gr.drawImage(lose, 300, 200, 300, 100, null);
        }
    }
    public class myMouse1 implements MouseListener {
        public void mouseClicked(MouseEvent e) {
        }

        // When you press the mouse button
        public void mousePressed(MouseEvent e) {
            // If done by a single click of the left mouse button
            if ((e.getButton() == 1) && (e.getClickCount() == 1)) {
                // Get the coordinates of the mouse
                mouseX = e.getX();
                mouseY = e.getY();
                // The cursor is in the computer field
                if ((mouseX > 100) && (mouseY > 100) && (mouseX < 400) && (mouseY < 400)) {
                    // Not end of the game
                    if ((myGame.endGame == 0) && (myGame.compTurn == false)) {
                        // Calculate the number of rows in the array
                        int i = (mouseY - 100) / 30;
                        // Calculate the element number of the row in the array
                        int j = (mouseX - 100) / 30;
                        // Check if can shot
                        if (myGame.arrComputer[i][j] <= 4)
                            // Make a shot
                            myGame.playerShot(i, j);
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    public class myMouse2 implements MouseMotionListener {
        public void mouseDragged(MouseEvent e) {
        }

        // When you move the mouse
        public void mouseMoved(MouseEvent e) {
            // Get the coordinates of the mouse
            mouseX = e.getX();
            mouseY = e.getY();
            // The cursor is in the player's field
            if ((mouseX >= 100) && (mouseY >= 100) && (mouseX <= 400) && (mouseY <= 400))
                setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            else
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
