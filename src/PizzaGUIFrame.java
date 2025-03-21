import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PizzaGUIFrame extends JFrame {
    private JRadioButton thinCrust, regularCrust, deepDishCrust;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppings;
    private JTextArea receiptArea;
    private JButton orderButton, clearButton, quitButton;

    private static final double[] SIZE_PRICES = {8.00, 12.00, 16.00, 20.00};
    private static final double TAX_RATE = 0.07;

    public PizzaGUIFrame() {
        setTitle("Pizza Order Form");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create and add panels
        add(createCrustPanel(), BorderLayout.WEST);
        add(createSizePanel(), BorderLayout.EAST);
        add(createToppingsPanel(), BorderLayout.CENTER);
        add(createReceiptPanel(), BorderLayout.SOUTH);
        add(createButtonPanel(), BorderLayout.NORTH);

        setVisible(true);
    }

    private JPanel createCrustPanel() {
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(new TitledBorder("Select Crust"));
        crustPanel.setLayout(new GridLayout(3, 1));

        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");

        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);

        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDishCrust);

        return crustPanel;
    }

    private JPanel createSizePanel() {
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(new TitledBorder("Select Size"));
        sizePanel.setLayout(new FlowLayout());

        String[] sizes = {"Small ($8.00)", "Medium ($12.00)", "Large ($16.00)", "Super ($20.00)"};
        sizeComboBox = new JComboBox<>(sizes);

        sizePanel.add(sizeComboBox);
        return sizePanel;
    }

    private JPanel createToppingsPanel() {
        JPanel toppingsPanel = new JPanel();
        toppingsPanel.setBorder(new TitledBorder("Select Toppings ($1 each)"));
        toppingsPanel.setLayout(new GridLayout(3, 2));

        String[] toppingNames = {"Pepperoni", "Mushrooms", "Onions", "Sausage", "Bacon", "Pineapple"};
        toppings = new JCheckBox[toppingNames.length];

        for (int i = 0; i < toppingNames.length; i++) {
            toppings[i] = new JCheckBox(toppingNames[i]);
            toppingsPanel.add(toppings[i]);
        }

        return toppingsPanel;
    }

    private JPanel createReceiptPanel() {
        JPanel receiptPanel = new JPanel();
        receiptPanel.setBorder(new TitledBorder("Receipt"));
        receiptArea = new JTextArea(10, 40);
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);

        receiptPanel.add(scrollPane);
        return receiptPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // Add button listeners
        orderButton.addActionListener(e -> generateReceipt());
        clearButton.addActionListener(e -> clearForm());
        quitButton.addActionListener(e -> confirmQuit());

        return buttonPanel;
    }

    private void generateReceipt() {
        String crust = thinCrust.isSelected() ? "Thin" :
                regularCrust.isSelected() ? "Regular" :
                        deepDishCrust.isSelected() ? "Deep-dish" : null;

        if (crust == null) {
            JOptionPane.showMessageDialog(this, "Please select a crust type.");
            return;
        }

        int sizeIndex = sizeComboBox.getSelectedIndex();
        String size = sizeComboBox.getItemAt(sizeIndex);
        double basePrice = SIZE_PRICES[sizeIndex];

        List<String> selectedToppings = new ArrayList<>();
        for (JCheckBox topping : toppings) {
            if (topping.isSelected()) {
                selectedToppings.add(topping.getText());
            }
        }

        if (selectedToppings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one topping.");
            return;
        }

        double toppingsCost = selectedToppings.size();
        double subTotal = basePrice + toppingsCost;
        double tax = subTotal * TAX_RATE;
        double total = subTotal + tax;

        DecimalFormat df = new DecimalFormat("$#.00");
        StringBuilder receipt = new StringBuilder();
        receipt.append("=========================================\n");
        receipt.append(String.format("%-20s %s\n", "Crust & Size", "Price"));
        receipt.append(String.format("%-20s %s\n", crust + " " + size, df.format(basePrice)));

        receipt.append("\nIngredients:\n");
        for (String topping : selectedToppings) {
            receipt.append(String.format("%-20s %s\n", topping, "$1.00"));
        }

        receipt.append("\n-----------------------------------------\n");
        receipt.append(String.format("%-20s %s\n", "Sub-total:", df.format(subTotal)));
        receipt.append(String.format("%-20s %s\n", "Tax:", df.format(tax)));
        receipt.append("=========================================\n");
        receipt.append(String.format("%-20s %s\n", "Total:", df.format(total)));
        receipt.append("=========================================\n");

        receiptArea.setText(receipt.toString());
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepDishCrust.setSelected(false);
        sizeComboBox.setSelectedIndex(0);
        for (JCheckBox topping : toppings) {
            topping.setSelected(false);
        }
        receiptArea.setText("");
    }

    private void confirmQuit() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
