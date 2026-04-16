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

        // THEME COLORS
        Color bgColor = new Color(30, 30, 30);
        Color panelColor = new Color(45, 45, 45);
        Color accent = new Color(0, 150, 136);
        Color textColor = Color.WHITE;

        setTitle("Inventory Management System - OOP Project");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Inventory Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(textColor);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // TABLE
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Quantity", "Price"}, 0
        );

        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // TABLE STYLING
        table.setBackground(new Color(50, 50, 50));
        table.setForeground(Color.WHITE);
        table.setGridColor(Color.GRAY);
        table.setSelectionBackground(accent);
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setBackground(accent);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        // BUTTON PANEL
        JPanel panel = new JPanel();
        panel.setBackground(panelColor);

        JButton addBtn = createButton("Add Product", new Color(76, 175, 80));
        JButton deleteBtn = createButton("Delete Product", new Color(244, 67, 54));
        JButton updateBtn = createButton("Update Stock", new Color(33, 150, 243));
        JButton searchBtn = createButton("Search Product", new Color(255, 152, 0));

        panel.add(addBtn);
        panel.add(deleteBtn);
        panel.add(updateBtn);
        panel.add(searchBtn);

        mainPanel.add(panel, BorderLayout.SOUTH);
        add(mainPanel);

        refreshTable();

        // ================= ADD =================
        addBtn.addActionListener(e -> {
            try {
                String name = JOptionPane.showInputDialog("Enter Name:");
                if (name == null || name.trim().isEmpty()) {
                    showError("Invalid Name!");
                    return;
                }

                int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity:"));
                double price = Double.parseDouble(JOptionPane.showInputDialog("Enter Price:"));

                if (qty < 0 || price < 0) {
                    showError("Quantity/Price cannot be negative!");
                    return;
                }

                int id = generateUniqueId();

                Product p = new Product(id, name, qty, price, 5);
                manager.addProduct(p);

                if (alertSystem.isLowStock(p)) {
                    showWarning("⚠ Low Stock: " + p.getName());
                }

                refreshTable();
                showSuccess("Product Added Successfully");

            } catch (Exception ex) {
                showError("Invalid Input!");
            }
        });

        // ================= DELETE =================
        deleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID:"));

                Product p = manager.findProduct(id);
                if (p == null) {
                    showError("Product Not Found");
                    return;
                }

                manager.removeProduct(id);
                refreshTable();
                showSuccess("Product Deleted Successfully");

            } catch (Exception ex) {
                showError("Invalid Input!");
            }
        });

        // ================= UPDATE =================
        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID:"));
                int qty = Integer.parseInt(JOptionPane.showInputDialog("Enter New Quantity:"));

                if (qty < 0) {
                    showError("Quantity cannot be negative!");
                    return;
                }

                Product p = manager.findProduct(id);

                if (p != null) {
                    p.setQuantity(qty);
                    manager.saveChanges();

                    if (alertSystem.isLowStock(p)) {
                        showWarning("⚠ Low Stock: " + p.getName());
                    }

                    refreshTable();
                    showSuccess("Stock Updated Successfully");

                } else {
                    showError("Product Not Found");
                }

            } catch (Exception ex) {
                showError("Invalid Input!");
            }
        });

        // ================= SEARCH =================
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Product ID:"));

                Product p = manager.findProduct(id);

                if (p != null) {
                    JOptionPane.showMessageDialog(this,
                            "ID: " + p.getId() +
                                    "\nName: " + p.getName() +
                                    "\nQuantity: " + p.getQuantity() +
                                    "\nPrice: " + p.getPrice(),
                            "Product Found",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("Product Not Found");
                }

            } catch (Exception ex) {
                showError("Invalid Input!");
            }
        });
    }

    // ================= HELPERS =================

    private int generateUniqueId() {
        int id = 1;
        for (Product p : manager.getProducts()) {
            if (p.getId() >= id) {
                id = p.getId() + 1;
            }
        }
        return id;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(150, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });

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

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}