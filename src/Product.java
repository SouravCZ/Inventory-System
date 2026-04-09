import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private int threshold;

    public Product(int id, String name, int quantity, double price, int threshold) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.threshold = threshold;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public int getThreshold() { return threshold; }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}