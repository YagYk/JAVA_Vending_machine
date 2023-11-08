import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class VendingMachineGUI extends JPanel {
    private Map<String, Product> inventory;
    private int balance;
    private VendingMachine vendingMachine;
    private BufferedImage backgroundImage;

    private JButton displayProductsButton;
    private JButton insertCoinButton;
    private JButton purchaseProductButton;
    private JTextField coinTextField;
    private JTextField productTextField;
    private JTextArea outputTextArea;
    private JList<String> productsList;

    public VendingMachineGUI(VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
        this.inventory = vendingMachine.getInventory();
        this.balance = vendingMachine.getBalance();

        setLayout(null);

        try {
            backgroundImage = ImageIO.read(new File("src/images/vending_machine_bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setPreferredSize(new Dimension(1920, 1080));

        // Add the "Display Products" button back
        displayProductsButton = createButton("Display Products", 50, 400, 150, 30);
        add(displayProductsButton);
        displayProductsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                displayProducts();
            }
        });

        insertCoinButton = createButton("Insert Coin", 50, 440, 150, 30);
        insertCoinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int coinAmount = 0;
                try {
                    coinAmount = Integer.parseInt(coinTextField.getText());
                    coinTextField.setText(""); // Clear the coinTextField after use
                    coinTextField.requestFocus(); // Set focus to the coinTextField
                } catch (NumberFormatException ex) {
                    outputTextArea.append("Invalid coin amount. Please enter a number.\n");
                    return;
                }
                insertCoin(coinAmount);
            }
        });
        purchaseProductButton = createButton("Purchase Product", 50, 480, 150, 30);
        purchaseProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productName = productTextField.getText().trim();
                productTextField.setText(""); // Clear the productTextField after use
                productTextField.requestFocus(); // Set focus to the productTextField
                purchaseProduct(productName);
            }
        });

        coinTextField = createTextField(300, 440, 270, 30);
        productTextField = createTextField(300, 480, 270, 30);

        outputTextArea = createTextArea(300, 520, 270, 150);

        DefaultListModel<String> productsListModel = new DefaultListModel<>();
        productsList = new JList<>(productsListModel);
        JScrollPane productsScrollPane = new JScrollPane(productsList);
        /*productsScrollPane.setBounds(550, 10, 200, 190);*/
        add(productsScrollPane);

        add(insertCoinButton);
        add(purchaseProductButton);
        add(coinTextField);
        add(productTextField);
        add(outputTextArea);
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        return button;
    }

    private JTextField createTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        return textField;
    }

    private JTextArea createTextArea(int x, int y, int width, int height) {
        JTextArea textArea = new JTextArea();
        textArea.setBounds(x, y, width, height);
        textArea.setEditable(false);
        return textArea;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void displayProducts() {
        DefaultListModel<String> productsListModel = new DefaultListModel<>();
        for (String productName : inventory.keySet()) {
            Product product = inventory.get(productName);
            String productInfo = productName + " - $" + product.getPrice();
            productsListModel.addElement(productInfo);
            JScrollPane productsScrollPane = new JScrollPane(productsList);
            productsScrollPane.setBounds(300, 365, 200, 60); // Adjust the position and dimensions here
            add(productsScrollPane);
        }
        productsList.setModel(productsListModel);
    }


    public void insertCoin(int coinAmount) {
        if (coinAmount == 1 || coinAmount == 2 || coinAmount == 5) {
            balance += coinAmount;
            outputTextArea.append("Coin inserted. Current balance: $" + balance + "\n");
        } else {
            outputTextArea.append("Invalid coin amount. Please enter 1, 2, or 5.\n");
        }
    }

    public void purchaseProduct(String productName) {
        if (inventory.containsKey(productName)) {
            Product product = inventory.get(productName);
            int price = product.getPrice();

            if (balance >= price) {
                outputTextArea.append("You purchased " + productName + " for $" + price + "\n");
                balance -= price;
                product.setQuantity(product.getQuantity() - 1);

                if (product.getQuantity() == 0) {
                    inventory.remove(productName);
                    outputTextArea.append(productName + " is out of stock.\n");
                }
            } else {
                outputTextArea.append("Insufficient balance. Please insert more coins.\n");
            }
        } else {
            outputTextArea.append("Product not found.\n");
        }
    }
}
