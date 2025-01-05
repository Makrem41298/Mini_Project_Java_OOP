import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameCard extends JFrame {
   private static String nbCard;
    private int  time;
   private String   inputTime;
    private JLabel timeLabel;

    boolean isValid;
    private List<Integer>   number= new ArrayList<>();
    static List<Card> cardList = new ArrayList<>();
    JButton button= new JButton("inverse");



    public GameCard() {


        this.setTitle("Game Card");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1500,900);
        this.setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        JPanel panelPrencipal = new JPanel();
        panelPrencipal.setLayout(new BorderLayout());
        panelPrencipal.add(panel, BorderLayout.CENTER);


        do {
            nbCard = JOptionPane.showInputDialog(null, "Enter the number of cards you want to play");

            // Validate input using regex
            Pattern validationNbCard = Pattern.compile("^\\d+$");
            Matcher validationNbCardMatcher = validationNbCard.matcher(nbCard);
            isValid = validationNbCardMatcher.matches();

            if (!isValid) {
                JOptionPane.showMessageDialog(null, "You have entered an invalid number of cards. Please try again.");
            }
        } while (!isValid);
        do {
          inputTime =JOptionPane.showInputDialog(null, "Enter the time of game in seconds");

            // Validate input using regex
            Pattern validationNbCard = Pattern.compile("^\\d+$");
            Matcher validationNbCardMatcher = validationNbCard.matcher(inputTime);
            isValid = validationNbCardMatcher.matches();

            if (!isValid) {
                JOptionPane.showMessageDialog(null, "You have entered an invalid number of time. Please try again.");
            }
        } while (!isValid);
        this.time=Integer.parseInt(inputTime);

        for (int i = 1; i <= Integer.parseInt(nbCard); i++) {
            number.add(i);


        }
        Collections.shuffle(number);
        System.out.println(number);

        panel.setLayout(new GridLayout(3,Integer.parseInt(nbCard)/3));
        cardList.clear();
        for (int i = 0; i < Integer.parseInt(nbCard); i++ ) {
            System.out.println(number.get(i));

            cardList.add(new Card("Card",number.get(i).toString()));
            panel.add(cardList.get(i));



        }
        timeLabel =new JLabel(time+"s",SwingConstants.CENTER);
        panelPrencipal.add(timeLabel, BorderLayout.NORTH);
        timeLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // 20px top and bottom padding
        timeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (Card.statusGame){
                JOptionPane.showMessageDialog(null,"YOU WON THE GAME");
                Card.statusGame=false;
                GameOver();

                scheduler.shutdown();

            }

            if (time > 0) {
                time--;
                timeLabel.setText(time+"s");
                if (time<=10){
                    timeLabel.setBackground(Color.RED);
                    timeLabel.setOpaque(time % 2 == 0);

                }

            } else {
                timeLabel.setBackground(Color.RED);
                timeLabel.setOpaque(true);
                JOptionPane.showMessageDialog(null, "You have left the game. Please try again.");

                GameOver();
                scheduler.shutdown(); // Stop the timer
            }
        }, 0, 1, TimeUnit.SECONDS);



        this.getContentPane().add(panelPrencipal);
        this.setVisible(true);





    }



    public static void reversAllCard(){
        for (int i = 0; i < Integer.parseInt(nbCard); i++) {
            if (cardList.get(i).getRotationY()!=0)
                cardList.get(i).startFlipAnimationFront();

        }
    }
    public void GameOver(){
        int option = JOptionPane.showConfirmDialog(
                null, // Parent component
                "Do you want to repeat again ?", // Message
                "Confirmation", // Title
                JOptionPane.YES_NO_OPTION // Option type
        );
        if (option == JOptionPane.YES_OPTION) {
            this.dispose();
            GameCard.reversAllCard();
            new GameCard();

        }
        if (option == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    public static String getNbCard() {
        return nbCard;
    }
}
