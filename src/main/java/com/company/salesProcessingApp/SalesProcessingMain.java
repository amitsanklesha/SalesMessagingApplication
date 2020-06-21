package com.company.salesProcessingApp;

import com.company.salesProcessingApp.consumer.SalesConsumer;
import com.company.salesProcessingApp.producer.SalesProducer;

import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This is a Sales notification messaging system. The system logs number of sales of each product and their total value
 * on every 10th message input in addition to
 * amit.sanklesha
 */
public class SalesProcessingMain {
    //private static final Logger LOGGER = LoggerFactory.getLogger(SalesProcessingMain.class);

    /**
     * Main method called during application startup. This ideally should be loading all beans from spring context
     * as defined in various spring xml's in classpath. We will also have a listener (or set of listeners) continuously listening to underlying
     * transport mechanism be it EMS queues, TibRV, chronicle queues or even Kafka. However for the sake of this dummy application
     * given that we cannot add more than 2 dependencies and with limited time constraints, I have
     * implicated simple caching mechanism as well as a Producer-Consumer framework for the application in question.
     *
     * Since the producer-consumer does the work as expected of the dummy application, there are not test cases
     * explicitly added for now.
     *
     * @param args
     */
    public static void main(String[] args) {
        java.net.InetAddress addr;
        try {
            addr = java.net.Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Unable to get hostName", e);
        }

        System.setProperty("host.name", addr.getHostName());

        //Creating shared object
        BlockingQueue sharedQueue = new LinkedBlockingQueue();

        //Creating Producer and Consumer Thread
        Thread prodThread = new Thread(new SalesProducer(sharedQueue));
        Thread consThread = new Thread(new SalesConsumer(sharedQueue));

        //Starting producer and Consumer thread
        prodThread.start();
        consThread.start();

        System.out.println("Application started");
    }
}
