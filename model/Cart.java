// src/main/java/Patika_9_Week_ETicaretSitesi/model/Cart.java
package Patika_9_Week_ETicaretSitesi.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
    private final Map<Long, Integer> items = new LinkedHashMap<>(); // productId -> qty
    public void add(Long productId, int qty) { items.merge(productId, qty, Integer::sum); }
    public void remove(Long productId) { items.remove(productId); }
    public Map<Long,Integer> getItems() { return items; }
    public boolean isEmpty() { return items.isEmpty(); }
    public void clear() { items.clear(); }
}
