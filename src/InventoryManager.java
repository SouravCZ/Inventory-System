import java.util.ArrayList;
import java.io.*;

public class InventoryManager {
    private ArrayList<Product> products;
    private final String FILE_NAME = "inventory.dat";

    public InventoryManager() {
        loadFromFile();
    }

    // ================= ADD =================
    public void addProduct(Product p) {
        products.add(p);
        saveToFile();
    }

    // ================= DELETE =================
    public void removeProduct(int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);

        if (removed) {
            saveToFile();
        }
    }

    // ================= FIND =================
    public Product findProduct(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    // ================= GET =================
    public ArrayList<Product> getProducts() {
        return products;
    }

    // ================= SAVE CHANGES =================
    public void saveChanges() {
        saveToFile();
    }

    // ================= SAVE TO FILE =================
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(products);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // ================= LOAD FROM FILE =================
    private void loadFromFile() {
        File file = new File(FILE_NAME);

        // If file doesn't exist → start fresh
        if (!file.exists()) {
            products = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            products = (ArrayList<Product>) ois.readObject();
        } catch (Exception e) {
            // If file is corrupted or empty → reset
            products = new ArrayList<>();
        }
    }
}