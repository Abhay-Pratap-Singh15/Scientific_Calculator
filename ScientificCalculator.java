import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScientificCalculator extends JFrame implements ActionListener {
    private JTextField textField;
    private JPanel buttonPanel;
    private boolean isScientific = false;

    public ScientificCalculator() {

        setTitle("Scientific Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        textField = new JTextField();
        textField.setEditable(false);
        textField.setPreferredSize(new Dimension(300, 150));
        textField.setFont(new Font("Segoe UI", Font.BOLD, 24));
        textField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4, true));
        add(textField, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(Color.BLACK);

        addSimpleCalculatorButtons();
        add(buttonPanel, BorderLayout.CENTER);


        pack();
        setResizable(false);
        setVisible(true);
    }

    private void addSimpleCalculatorButtons() {
        buttonPanel.removeAll();
        String[] simpleButtons = {
                "1", "2", "3", "/",
                "4", "5", "6", "*",
                "7", "8", "9", "-",
                "0", ".", "=", "+",
                "AC", "MO", "DEL", "HS"
        };

        for (String label : simpleButtons) {
            addButton(label);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void addScientificCalculatorButtons() {
        String[] scientificButtons = {
                "(", ")", "^", "sqrt",
                "cbrt", "log", "sin", "cos",
                "tan", "asin", "acos", "atan",
                "!", "%", "|x|"
        };

        for (String label : scientificButtons) {
            addButton(label);
        }
    }

    private void addButton(String label) {
        JButton button = new RoundedButton(label);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBackground(label.matches("[+\\-*/]") ? new Color(255, 140, 0) : new Color(169, 169, 169));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.addActionListener(this);
        buttonPanel.add(button);
    }

    private void toggleScientificMode() {
        isScientific = !isScientific;
        buttonPanel.removeAll();
        buttonPanel.setLayout(new GridLayout(isScientific ? 9 : 5, 4, 10, 10));
        addSimpleCalculatorButtons();
        if (isScientific) addScientificCalculatorButtons();
        pack();
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        String expression = textField.getText();

        switch (command) {
            case "=":
                try {
                    double result = evaluateExpression(expression);
                    textField.setText(Double.toString(result));
                } catch (ArithmeticException e) {
                    textField.setText("Error: " + e.getMessage());
                }
                break;
            case "AC":
                textField.setText("");
                break;
            case "MO":
                toggleScientificMode();
                break;
            case "DEL":
                if (!expression.isEmpty()) {
                    textField.setText(expression.substring(0, expression.length() - 1));
                }
            default:
                textField.setText(expression + command);
                break;
        }
    }
    private double evaluateExpression(String expression) {
        return new ExpressionParser().parse(expression);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScientificCalculator::new);
    }
}
