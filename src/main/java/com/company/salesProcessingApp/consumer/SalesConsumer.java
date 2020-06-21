package com.company.salesProcessingApp.consumer;

import com.company.salesProcessingApp.cache.AdjustmentCache;
import com.company.salesProcessingApp.cache.InMemoryCache;
import com.company.salesProcessingApp.sales.AdjustmentRecord;
import com.company.salesProcessingApp.sales.SaleRecordPerProductType;
import com.company.salesProcessingApp.sales.SaleRecordPerProductType.Operation;
import com.company.salesProcessingApp.sales.SalesRecord;

import java.util.concurrent.BlockingQueue;

/**
 * Consumer application to consume incoming sales messages from an external system.
 * Ideally this will be a listener listening to underlying transport layer be it EMS queues or TibRV or another
 *
 * For interprocess communication between microservices withing our application, we can use Apache Kafka
 * or chronicle queues.
 */
public class SalesConsumer implements Runnable {
    //private static final Logger LOGGER = LoggerFactory.getLogger(SalesConsumer.class);
    private final BlockingQueue sharedQueue;
    private final InMemoryCache productCache;
    private final AdjustmentCache adjustmentCache;

    private static int COUNTER = 0;
    private static int ADJUSTMENT_COUNTER = 0;

    private static int ADJUSTMENT_PAUSE_TIME_IN_MILLIS = 5000;

    private static int ADJUSTMENT_RECORD_DISPLAY_COUNT = 50;
    private static int SALES_RECORD_DISPLAY_COUNT = 10;

    public SalesConsumer(BlockingQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
        // Below cache is initialized with relatively high values; ideally we should be using a high performance
        // low latency framework here such as maybe Apache Kafka or even Chronicle queues.

        // So that durability is maintained and once we receive a message on the consumer here and in the queue,
        // the same needs to be immediately persisted to disk to avoid loss of messages (in worst case scenarios);

        // Personally - I have used Chronicle files in my project which does the same inter-process communication
        // for high performance low latency usage.

        // The messages once written to disk (as happens with Chronicle files) can then be used for replay scenarios
        // as well as knowing the state of application / processes at any given point in time
        // to understand the message flow between processes and even debug if any time lag during interprocess communication.

        // However since for this sample application, we are limited to add max 2 dependencies or even just 1 and with limited time constraints,
        // I have used simple caching strategy here. Infact we could have gone for ehcache as the distributed Java cache which
        // I have used in my project.

        productCache = new InMemoryCache(1000, 1000);
        adjustmentCache = new AdjustmentCache(200, 300);
    }

    public void run() {
        while (true) {
            try {
                SalesRecord salesRecord = (SalesRecord) sharedQueue.take();
                COUNTER++;
                // In addition to below logging which basically states receipt of incoming message,
                // usage of Kafka or Chronicle queues and then disk/db persistence immediately at this step
                // would mean no loss of incoming messages.
                //System.out.println("Consumed Sale Number " + COUNTER + ": " + salesRecord);

                /*
                Below snippet adds sales details to cache. There are 2 caches: productCache containing all sales records per product type and
                adjustmentCache containing adjustment details for all products.
                 */
                if (salesRecord.getOperation() != Operation.NONE) {
                    adjustmentCache.put(++ADJUSTMENT_COUNTER, new AdjustmentRecord(salesRecord.getProductType(), salesRecord.getOperation(), salesRecord.getPrice()));
                    productCache.put(salesRecord.getProductType(), salesRecord.getOperation(), salesRecord.getPrice());
                } else {
                    productCache.put(salesRecord.getProductType(), new SaleRecordPerProductType(salesRecord.getQuantity(), salesRecord.getPrice()));
                }

                /*
                Below snippet displays the adjustment record history after every ADJUSTMENT_RECORD_DISPLAY_COUNT iteration (i.e. after every 50th iterations) in our case.
                It will display the adjustment details applied so far right from the first message till now for each product type.
                 */
                if (COUNTER % ADJUSTMENT_RECORD_DISPLAY_COUNT == 0) {
                    System.out.println("\n\n\n\n*****************************");
                    System.out.println("Pausing for " + ADJUSTMENT_PAUSE_TIME_IN_MILLIS + " milliseconds to print adjustment details applied so far:");
                    int differenceTime = 2000;
                    int preWaitTime = ADJUSTMENT_PAUSE_TIME_IN_MILLIS - differenceTime;
                    Thread.sleep(preWaitTime);
                    System.out.println("Adjustment Details applied so far:");
                    adjustmentCache.getAll();
                    System.out.println("*****************************\n\n\n\n");
                    Thread.sleep(differenceTime);
                }

                /*
                Below snippet displays sales details of each product type after every SALES_RECORD_DISPLAY_COUNT iteration (i.e. after every 10 iterations) in our case.
                It will display the products sold along with the total price for each product accumulated so far.
                 */
                if (COUNTER % SALES_RECORD_DISPLAY_COUNT == 0) {
                    System.out.println("\n\n\n\n*****************************");
                    System.out.println("Details of products sold after " + COUNTER / SALES_RECORD_DISPLAY_COUNT + " iteration(s) of " + SALES_RECORD_DISPLAY_COUNT + "  sales messages each:");
                    productCache.getAll();
                    System.out.println("*****************************\n\n\n\n");
                }

            } catch (InterruptedException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Severe issue in SalesConsumer: " + ex.getMessage());
            }
        }
    }
}
