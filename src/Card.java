import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Card extends JPanel {
    private final String frontText;
    private final String backText;
    private boolean isFlipping = false;
    private float rotationY = 0;
    static int counter = 1;
    private static final List<Integer> NumcardList = new ArrayList<>();
    static boolean statusGame = false;
    private boolean isCorrect = false;

    public Card(String frontText, String backText) {
        this.frontText = frontText;
        this.backText = backText;
        setPreferredSize(new Dimension(200, 100));
        setOpaque(false);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isFlipping && rotationY != 180) {
                    startFlipAnimationBack();
                }
            }
        });
    }

    private void startFlipAnimationBack() {
        isFlipping = true;
        NumcardList.add(Integer.parseInt(backText));
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotationY += 10;
                if (rotationY >= 180) {
                    rotationY = 180;
                    isFlipping = false;
                    ((Timer) e.getSource()).stop();
                    if (Integer.parseInt(backText) != counter) {
                        GameCard.reversAllCard();
                        counter = 1;
                        isCorrect = false;
                    } else {
                        counter++;
                        if ((Integer.parseInt(GameCard.getNbCard()) + 1) == counter) {
                            statusGame = true;
                            counter = 1;
                        }
                        isCorrect = true;
                    }
                }
                repaint();
            }
        });
        timer.start();
    }

    public void startFlipAnimationFront() {
        isFlipping = true;
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rotationY -= 10;
                if (rotationY <= 0) {
                    rotationY = 0;
                    isFlipping = false;
                    ((Timer) e.getSource()).stop();
                    isCorrect = false;
                }
                repaint();
            }
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();


        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, width, height);

        double scale = Math.abs(Math.cos(Math.toRadians(rotationY)));

        g2d.translate(width / 2.0, height / 2.0);
        g2d.scale(scale, 1);
        g2d.translate(-width / 2.0, -height / 2.0);

        if (rotationY < 90) {
            drawFrontFace(g2d, width, height);
        } else {
            drawBackFace(g2d, width, height);
        }
    }

    private void drawFrontFace(Graphics2D g2d, int width, int height) {
        if (isCorrect) {
            g2d.setColor(new Color(0, 128, 0));
        } else {
            g2d.setColor(new Color(70, 130, 180));
        }
        g2d.fillRect(10, 10, width - 20, height - 20);

        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(10, 10, width - 20, height - 20);

        // Draw front text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Serif", Font.BOLD, 40));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(frontText);
        int textHeight = fm.getAscent();
        g2d.drawString(frontText, (width - textWidth) / 2, (height + textHeight) / 2);
    }

    private void drawBackFace(Graphics2D g2d, int width, int height) {
        // Draw rectangular back face
        if (isCorrect) {
            g2d.setColor(new Color(0, 128, 0));
        } else {
            g2d.setColor(new Color(105, 105, 105));

        }
        g2d.fillRect(10, 10, width - 20, height - 20);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(10, 10, width - 20, height - 20);


        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Serif", Font.BOLD, 40));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(backText);
        int textHeight = fm.getAscent();
        g2d.drawString(backText, (width - textWidth) / 2, (height + textHeight) / 2);
    }

    public float getRotationY() {
        return rotationY;
    }
}