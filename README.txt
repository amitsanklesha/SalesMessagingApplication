Application: Sales messaging application
Version: 1.0


The problem
- Implement a small message processing application that satisfies the below requirements for processing sales notification messages.
You should assume that an external company will be sending you the input messages,
but for the purposes of this exercise you are free to define the interfaces.

Processing requirements
- All sales must be recorded
- All messages must be processed
- After every 10th message received your application should log a report detailing the number of sales of each product and their total value.
- After 50 messages your application should log that it is pausing, stop accepting new messages and log a report of the adjustments
that have been made to each sale type while the application was running.

Sales and Messages
- A sale has a product type field and a value – you should choose sensible types for these.
- Any number of different product types can be expected. There is no fixed set.
- A message notifying you of a sale could be one of the following types
   Message Type 1 – contains the details of 1 sale E.g apple at 10p
   Message Type 2 – contains the details of a sale and the number of occurrences of that sale. E.g 20 sales of apples at 10p each.
   Message Type 3 – contains the details of a sale and an adjustment operation to be applied to all stored sales of this product type.
   Operations can be add, subtract, or multiply e.g Add 20p apples would instruct your application to add 20p to each sale of apples you have recorded.


NOTES:
JDK version requirement: 8.0 OR higher


Process and Technical details:
In this sample sales message processing application, Producer Consumer framework have been used to create sales messages and send across to consumer
that will process all incoming messages. Ideally, we should be using a Kafka or a like high performance low latency implementation like a Chronicle queue
to ensure all messages are persisted on disk immediately and there is no loss of data. Additionally, interprocess communication can also happen
via these frameworks.

A huge distributed Java application cannot be written without Spring however since we had the limitation of adding 1 or max 2 external dependencies only,
I had to ignore usage of Spring (as it would need adding spring-core, spring-beans, spring-context, sprint-context-support at the least) and slf4j for
logging (I had to use System.out.println at all places for now) and even distributed caching maybe such as ehcache.
I have used a simple caching mechanism and processed the messages as mentioned in the requirements.

The SalesConsumer process consumes the messages sent by SalesProducer and displays sales details after every 10th message received and also displays
adjustment record details after every 50th message received.
This sample application for now doesnt have any JUnits or test interfaces however a real world application should follow TDD approach.


To run the application:
Simply build the application using Maven and run the main class com.company.salesProcessingApp.SalesProcessingMain.
Test data is set in SalesProducer class with dummy values which can be easily changed if required.


Output:
2 sample outputs have been saved in this project.
SampleOutput.txt - contains all sales message details generated from Producer, consumed into Consumer and then shows the actual output
as per requirements - i.e. sales details after every 10th message entry and adjustment details after every 50th message entry.
CurtailedSampleOutput.txt - shows the same details as above but only the main requirements output. For sake of understanding and review,
I have removed the Producer and Consumer statements in this file.
