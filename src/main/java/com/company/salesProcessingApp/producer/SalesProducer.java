package com.company.salesProcessingApp.producer;

import com.company.salesProcessingApp.sales.SaleRecordPerProductType.Operation;
import com.company.salesProcessingApp.sales.SalesRecord;
import com.company.salesProcessingApp.sales.SalesRecord.ProductType;

import java.util.concurrent.BlockingQueue;

/**
 * Producer of sales messages containing details of all products sold and the price per product.
 * It has variants where a sales record can just mention the price of the product sold.
 * OR quantity of the products sold along with price per product.
 * OR an adjustment data stating if price needs to be updated per productType for all sold products so far.
 */
public class SalesProducer implements Runnable {
    //private static final Logger LOGGER = LoggerFactory.getLogger(SalesProducer.class);
    private int TOTAL_PRODUCT_COUNT = 120;
    private final BlockingQueue sharedQueue;
    private SalesRecord salesRecord;

    public SalesProducer(BlockingQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    public void run() {
        for (int i = 1; i <= TOTAL_PRODUCT_COUNT; i++) {
            try {
                if (i % 100 == 0) {
                    salesRecord = new SalesRecord(ProductType.MOBILE, Operation.ADD, 2.0);
                } else if (i % 50 == 0) {
                    salesRecord = new SalesRecord(ProductType.MICROWAVE, Operation.MULTIPLY, 2.0);
                } else if (i % 20 == 0) {
                    salesRecord = new SalesRecord(ProductType.MOBILE, 40.0, 5);
                } else if (i % 2 == 0) {
                    salesRecord = new SalesRecord(ProductType.MICROWAVE, 30.0);
                } else {
                    salesRecord = new SalesRecord(ProductType.MOBILE, 50.0);
                }
                //System.out.println("Produced: " + salesRecord);
                sharedQueue.put(salesRecord);
            } catch (InterruptedException ex) {
                throw new RuntimeException("Severe issue in SalesProducer: " + ex.getMessage());
            }
        }
    }
}
