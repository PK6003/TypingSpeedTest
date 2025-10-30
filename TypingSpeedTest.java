import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class TypingSpeedTest extends JFrame {
    private JTextArea textToType, userInput;
    private JLabel timerLabel, accuracyLabel, wpmLabel, avgTimeLabel;
    private JButton startButton;
    private int timeLeft = 60;
    private Timer timer;
    private boolean running = false;
    private long startTime, endTime;

    private final String paragraph = "⚡ Speed typing tests measure how quickly and accurately you can type text. ⌨️ Practice regularly to improve your accuracy and words per minute. 🚀";

    public TypingSpeedTest() {
        setTitle("Typing Speed Test ⏱️");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topPanel.setBackground(new Color(240, 240, 240));
        timerLabel = new JLabel("⏰ Time: 60s");
        timerLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        startButton = new JButton("Start Test 🚀");
        startButton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        startButton.setBackground(new Color(100, 150, 250));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> startTest());
        topPanel.add(timerLabel);
        topPanel.add(startButton);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        textToType = new JTextArea(paragraph);
        textToType.setLineWrap(true);
        textToType.setWrapStyleWord(true);
        textToType.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        textToType.setEditable(false);
        textToType.setBackground(new Color(230, 230, 230));
        userInput = new JTextArea();
        userInput.setLineWrap(true);
        userInput.setWrapStyleWord(true);
        userInput.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        userInput.setEnabled(false);
        centerPanel.add(new JScrollPane(textToType));
        centerPanel.add(new JScrollPane(userInput));

        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        accuracyLabel = new JLabel("🎯 Accuracy: 0%");
        accuracyLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        wpmLabel = new JLabel("💨 WPM: 0");
        wpmLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        avgTimeLabel = new JLabel("⌛ Avg Time: -");
        avgTimeLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        bottomPanel.add(accuracyLabel);
        bottomPanel.add(wpmLabel);
        bottomPanel.add(avgTimeLabel);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void startTest() {
        if (running) return;
        running = true;
        userInput.setEnabled(true);
        userInput.setText("");
        userInput.requestFocus();
        startTime = System.currentTimeMillis();

        timerLabel.setText("⏰ Time: 60s");
        timeLeft = 60;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timeLeft--;
                    timerLabel.setText("⏰ Time: " + timeLeft + "s");

                    if (timeLeft <= 0) {
                        timer.cancel();
                        running = false;
                        endTime = System.currentTimeMillis();
                        finishTest();
                    }
                });
            }
        }, 1000, 1000);
    }

    private void finishTest() {
        userInput.setEnabled(false);
        String typed = userInput.getText();
        int correctChars = 0;
        for (int i = 0; i < Math.min(typed.length(), paragraph.length()); i++) {
            if (typed.charAt(i) == paragraph.charAt(i)) {
                correctChars++;
            }
        }

        double accuracy = ((double) correctChars / paragraph.length()) * 100;
        long timeTaken = (endTime - startTime) / 1000;
        int wordsTyped = typed.trim().isEmpty() ? 0 : typed.split("\\s+").length;
        double wpm = (timeTaken > 0) ? (wordsTyped / (double) timeTaken) * 60 : 0;

        accuracyLabel.setText(String.format("🎯 Accuracy: %.2f%%", accuracy));
        wpmLabel.setText(String.format("💨 WPM: %.1f", wpm));

        if (accuracy == 100.0 && wordsTyped > 0) {
            double avgTimePerWord = (double) timeTaken / wordsTyped;
            avgTimeLabel.setText(String.format("⌛ Avg Time: %.2fs/word", avgTimePerWord));
        } else {
            avgTimeLabel.setText("⌛ Avg Time: -");
        }

        JOptionPane.showMessageDialog(this,
                "🕒 Time's up!\n🎯 Accuracy: " + String.format("%.2f%%", accuracy) +
                        "\n💨 WPM: " + String.format("%.1f", wpm),
                "✅ Test Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TypingSpeedTest::new);
    }
}
