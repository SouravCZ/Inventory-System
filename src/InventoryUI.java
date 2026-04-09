import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InventoryUI extends JFrame {

    private InventoryManager manager;
    private DefaultTableModel tableModel;
    private AlertSystem alertSystem;

    public InventoryUI() {
        manager = new InventoryManager();
        alertSystem = new AlertSystem();

        setTitle("Inventory Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Inventory Management System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Quantity", "Price"}, 0
        );

        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 230, 230));

        JButton addBtn = createButton("Add Product", new Color(76, 175, 80));
        JButton deleteBtn = createButton("Delete Product", new Color(244, 67, 54));
        JButton updateBtn = createButton("Update Stock", new Color(33, 150, 243));

        panel.add(addBtn);
        panel.add(deleteBtn);
        panel.add(updateBtn);

        mainPanel.add(panel, BorderLayout.SOUTH);
        add(mainPanel);

        refreshTable();

        // ADD
        addBtn.addActionListener(e -> {
            try {
                String name = JOptionPane.showInputDialog("Enter Name:");
                int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity:"));
                double price = Double.parseDouble(JOptionPane.showInputDialog("Enter Price:"));

                int id = manager.getProducts().size() + 1;

                Product p = new Product(id, name, qty, price, 5);
                manager.addProduct(p);

                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });

        // DELETE
        deleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID:"));
                manager.removeProduct(id);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });

        // UPDATE
        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID:"));
                int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter New Quantity:"));

                Product p = manager.findProduct(id);

                if (p != null) {
                    p.setQuantity(qty);
                    manager.updateProduct();

                    if (alertSystem.isLowStock(p)) {
                        JOptionPane.showMessageDialog(this,
                                "⚠ Low Stock: " + p.getName());
                    }

                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Product Not Found");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(150, 35));
        return btn;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        for (Product p : manager.getProducts()) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getQuantity(), p.getPrice()
            });
        }
    }
}