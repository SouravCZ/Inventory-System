import java.util.ArrayList;
import java.io.*;

public class InventoryManager {
    private ArrayList<Product> products;
    private final String FILE_NAME = "inventory.dat";

    public InventoryManager() {
        loadFromFile();
    }

    public void addProduct(Product p) {
        products.add(p);
        saveToFile();
    }

    public void removeProduct(int id) {
        products.removeIf(p -> p.getId() == id);
        saveToFile();
    }

    public Product findProduct(int id) {
        for (Product p : products) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    // 🔥 FIX METHOD (this was missing earlier)
    public void updateProduct() {
        saveToFile();
    }

    // SAVE
    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(products);
        } catch (Exception e) {
            System.out.println("Error saving data");
        }
    }

    // LOAD
    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            products = (ArrayList<Product>) ois.readObject();
        } catch (Exception e) {
            products = new ArrayList<>();
        }
    }
}