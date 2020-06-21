package com.company.salesProcessingApp.cache;

import com.company.salesProcessingApp.sales.AdjustmentRecord;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;

/**
 * Cache to store Adjustment details of all products
 */
public class AdjustmentCache {
    //private static final Logger LOGGER = LoggerFactory.getLogger(AdjustmentCache.class);

    private long timeToLive;
    private LRUMap cacheMap;

    public AdjustmentCache(long timeToLive, int maxItems) {
        this.timeToLive = timeToLive * 1000;

        cacheMap = new LRUMap(maxItems);
    }

    public void put(Integer key, AdjustmentRecord value) {
        synchronized (cacheMap) {
            cacheMap.put(key, value);
        }
    }

    public void remove(Integer key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    public void getAll() {
        AdjustmentRecord value;
        synchronized (cacheMap) {
            MapIterator itr = cacheMap.mapIterator();

            while (itr.hasNext()) {
                itr.next();
                value = (AdjustmentRecord) itr.getValue();

                System.out.println(String.format("ProductType: %s, AdjustmentType: %s, AdjustmentPrice: %f", value.getProductType(), value.getOperation(), value.getPrice()));
            }
        }
    }
}
