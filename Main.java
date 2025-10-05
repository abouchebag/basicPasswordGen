import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Random;

public class Main extends JFrame {

    // --- 1. GUI Components ---
    private JTextField passwordField;
    private JButton generateButton;
    private JButton copyButton;
    private JLabel lengthLabel;
    private JSpinner lengthSpinner;

    // --- 2. Character Pools (from previous example) ---
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALL_CHARS = LOWERCASE + UPPERCASE + NUMBERS + SYMBOLS;

    // --- 3. Constructor: Builds the GUI ---
    public Main() {
        setTitle("Secure Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLayout(new BorderLayout(10, 10)); // Use BorderLayout for overall structure

        // Setup central panel for controls and display
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // A. Password Length Control
        lengthLabel = new JLabel("Password Length:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        controlPanel.add(lengthLabel, gbc);

        // Spinner to select length (min 8, max 30, initial 8, step 1)
        SpinnerModel model = new SpinnerNumberModel(8, 8, 30, 1);
        lengthSpinner = new JSpinner(model);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        controlPanel.add(lengthSpinner, gbc);

        // B. Generate Button
        generateButton = new JButton("Generate Password");
        // Attach the action listener
        generateButton.addActionListener(e -> generateAndUpdatePassword());
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        controlPanel.add(generateButton, gbc);

        // C. Password Display Field
        passwordField = new JTextField("Click 'Generate Password'");
        passwordField.setEditable(false);
        passwordField.setFont(new Font("Monospaced", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span two columns
        controlPanel.add(passwordField, gbc);

        // D. Copy Button
        copyButton = new JButton("Copy to Clipboard");
        copyButton.addActionListener(e -> copyToClipboard());
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        controlPanel.add(copyButton, gbc);

        add(controlPanel, BorderLayout.CENTER);

        pack(); // Adjusts size based on components
        setLocationRelativeTo(null); // Center on screen
        setVisible(true);
    }

    // --- 4. Logic Methods ---

    private void generateAndUpdatePassword() {
        // Get the current length from the spinner
        int length = (Integer) lengthSpinner.getValue();

        // Generate the new password using the core logic
        String newPassword = generatePassword(length);

        // Update the JTextField with the new password
        passwordField.setText(newPassword);
    }

    private String generatePassword(int length) {
        Random random = new Random();
        StringBuilder password = new StringBuilder(length);

        // Ensure at least one Symbol and one Number (and one lowercase and one uppercase for security)
        password.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        password.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));

        // Fill the remaining characters
        for (int i = 0; i < length - 4; i++) {
            int randomIndex = random.nextInt(ALL_CHARS.length());
            password.append(ALL_CHARS.charAt(randomIndex));
        }

        // Shuffle the result for true randomness
        return shuffleString(password.toString(), random);
    }

    private String shuffleString(String text, Random random) {
        char[] characters = text.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            // Swap characters
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

    // --- 5. Clipboard Copy Feature ---

    private void copyToClipboard() {
        String password = passwordField.getText();
        if (password.isEmpty() || password.equals("Click 'Generate Password'")) {
            JOptionPane.showMessageDialog(this, "Please generate a password first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringSelection selection = new StringSelection(password);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

        JOptionPane.showMessageDialog(this, "Password copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    // --- 6. Main Entry Point ---

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
