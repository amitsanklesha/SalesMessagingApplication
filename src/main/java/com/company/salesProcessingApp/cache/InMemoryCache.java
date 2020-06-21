package com.company.salesProcessingApp.cache;

import com.company.salesProcessingApp.sales.SaleRecordPerProductType;
import com.company.salesProcessingApp.sales.SaleRecordPerProductType.Operation;
import com.company.salesProcessingApp.sales.SalesRecord.ProductType;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

/**
 * Cache to store Sales details per product Type
 * This cache will store the quantity of products sold and the total price accumulate for the product.
 */
public class InMemoryCache {
    //private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryCache.class);
    private long timeToLive;
    private LRUMap cacheMap;

    public InMemoryCache(long timeToLive, int maxItems) {
        this.timeToLive = timeToLive * 1000;

        cacheMap = new LRUMap(maxItems);
    }

    public void put(ProductType key, SaleRecordPerProductType value) {
        SaleRecordPerProductType newValue = null;
        synchronized (cacheMap) {
            if (get(key) != null) {
                newValue = get(key).update(value);
            } else {
                newValue = value;
            }
            cacheMap.put(key, newValue);
        }
    }

    public void put(ProductType key, Operation operation, double price) {
        SaleRecordPerProductType value = null;
        synchronized (cacheMap) {
            if (get(key) != null) {
                value = get(key).update(operation, price);
                cacheMap.put(key, value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public SaleRecordPerProductType get(ProductType key) {
        synchronized (cacheMap) {
            SaleRecordPerProductType saleRecordPerProductType = (SaleRecordPerProductType) cacheMap.get(key);

            if (saleRecordPerProductType == null)
                return null;
            else {
                return saleRecordPerProductType;
            }
        }
    }

    public void remove(ProductType key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    public void getAll() {
        ProductType key;
        SaleRecordPerProductType value;
        synchronized (cacheMap) {
            MapIterator itr = cacheMap.mapIterator();

            while (itr.hasNext()) {
                key = (ProductType) itr.next();
                value = (SaleRecordPerProductType) itr.getValue();

                System.out.println(String.format("ProductType: %s, Quantity sold: %d, Total Value/Price: %f", key, value.getQuantity(), value.getPrice()));
            }
        }
    }
}
