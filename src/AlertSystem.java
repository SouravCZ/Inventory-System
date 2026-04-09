public class AlertSystem {

    public boolean isLowStock(Product p) {
        return p.getQuantity() <= p.getThreshold();
    }
}