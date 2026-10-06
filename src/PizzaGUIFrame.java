import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PizzaGUIFrame extends JFrame {

    // Crust radio buttons
    private JRadioButton thinCrust;
    private JRadioButton regularCrust;
    private JRadioButton deepCrust;

    // Size combo box
    private JComboBox<String> sizeBox;

    // Toppings checkboxes
    private JCheckBox[] toppingBoxes;

    // Receipt area
    private JTextArea receiptArea;

    public PizzaGUIFrame() {
        super("Pizza Order Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildGUI();

        setSize(700, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildGUI() {
        setLayout(new BorderLayout());

        // ===== CRUST PANEL =====
        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(BorderFactory.createTitledBorder("Crust Type"));

        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepCrust = new JRadioButton("Deep-Dish");

        ButtonGroup crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepCrust);

        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepCrust);

        // ===== SIZE PANEL =====
        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder("Pizza Size"));

        String[] sizes = {"Small - $8", "Medium - $12", "Large - $16", "Super - $20"};
        sizeBox = new JComboBox<>(sizes);

        sizePanel.add(sizeBox);

        // ===== TOPPINGS PANEL =====
        JPanel toppingsPanel = new JPanel();
        toppingsPanel.setBorder(BorderFactory.createTitledBorder("Toppings ($1 each)"));
        toppingsPanel.setLayout(new GridLayout(3, 2));

        String[] toppingNames = {
                "Pepperoni", "Sausage", "Bacon",
                "Mushrooms", "Onions", "Monster Tentacles"
        };

        toppingBoxes = new JCheckBox[toppingNames.length];
        for (int i = 0; i < toppingNames.length; i++) {
            toppingBoxes[i] = new JCheckBox(toppingNames[i]);
            toppingsPanel.add(toppingBoxes[i]);
        }

        // ===== RECEIPT PANEL =====
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setBorder(BorderFactory.createTitledBorder("Order Receipt"));

        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(receiptArea);

        receiptPanel.add(scrollPane);

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();

        JButton orderBtn = new JButton("Order");
        JButton clearBtn = new JButton("Clear");
        JButton quitBtn = new JButton("Quit");

        orderBtn.addActionListener(new OrderListener());
        clearBtn.addActionListener(e -> clearForm());
        quitBtn.addActionListener(e -> quitProgram());

        buttonPanel.add(orderBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(quitBtn);

        // ===== LAYOUT =====
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(crustPanel);
        topPanel.add(sizePanel);
        topPanel.add(toppingsPanel);

        add(topPanel, BorderLayout.NORTH);
        add(receiptPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class OrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String crust = getCrustSelection();
            String size = (String) sizeBox.getSelectedItem();
            int basePrice = getBasePrice(size);

            StringBuilder toppings = new StringBuilder();
            int toppingCount = 0;

            for (JCheckBox box : toppingBoxes) {
                if (box.isSelected()) {
                    toppings.append(box.getText()).append("\n");
                    toppingCount++;
                }
            }

            if (crust == null || toppingCount == 0) {
                JOptionPane.showMessageDialog(PizzaGUIFrame.this,
                        "You must select a crust and at least one topping.");
                return;
            }

            double subTotal = basePrice + toppingCount;
            double tax = subTotal * 0.07;
            double total = subTotal + tax;

            receiptArea.setText(
                    "=========================================\n" +
                            "Type of Crust & Size\t\tPrice\n" +
                            crust + " + " + size + "\t\t$" + basePrice + "\n\n" +
                            "Ingredients\t\t\tPrice\n" +
                            toppings +
                            "\nSub-total:\t\t\t$" + String.format("%.2f", subTotal) + "\n" +
                            "Tax:\t\t\t\t$" + String.format("%.2f", tax) + "\n" +
                            "---------------------------------------------------------------------\n" +
                            "Total:\t\t\t\t$" + String.format("%.2f", total) + "\n" +
                            "=========================================\n"
            );
        }
    }

    private String getCrustSelection() {
        if (thinCrust.isSelected()) return "Thin";
        if (regularCrust.isSelected()) return "Regular";
        if (deepCrust.isSelected()) return "Deep-Dish";
        return null;
    }

    private int getBasePrice(String size) {
        if (size.contains("Small")) return 8;
        if (size.contains("Medium")) return 12;
        if (size.contains("Large")) return 16;
        return 20;
    }

    private void clearForm() {
        thinCrust.setSelected(false);
        regularCrust.setSelected(false);
        deepCrust.setSelected(false);

        sizeBox.setSelectedIndex(0);

        for (JCheckBox box : toppingBoxes) {
            box.setSelected(false);
        }

        receiptArea.setText("");
    }

    private void quitProgram() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
