import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class VendingMachine extends JFrame {
    private Map<String, Product> inventory;
    private int balance;
    private VendingMachineGUI vendingMachineGUI;

    public VendingMachine() {
        inventory = new HashMap<>();
        balance = 0;
        setTitle("Vending Machine");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        vendingMachineGUI = new VendingMachineGUI(this);

        add(vendingMachineGUI, BorderLayout.NORTH);

        // Initialize products
        addProduct("Soda", 2, 10);
        addProduct("Chips", 1, 15);
        addProduct("Candy", 1, 20);
    }

    public void addProduct(String name, int price, int quantity) {
        inventory.put(name, new Product(name, price, quantity));
    }

    public void insertCoin(int coin) {
        balance += coin;
    }

    public void displayProducts() {
        vendingMachineGUI.displayProducts();
    }

    public void purchaseProduct(String productName) {
        vendingMachineGUI.purchaseProduct(productName);
    }

    public Map<String, Product> getInventory() {
        return inventory;
    }

    public int getBalance() {
        return balance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VendingMachine().setVisible(true);
        });
    }
}
