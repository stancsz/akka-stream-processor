# kafka consumer demo

This is a quick md document on how to test the consumers

![kafka-consumer-demo-1](kafka-consumer-demo.assets/kafka-consumer-demo-1.gif)

the topics are being passed to akka's alpakka consumer's subscription topics.

in this demo, I'll be using datagrip to update the mysql db directly.

![kafka-consumer-demo-2](kafka-consumer-demo.assets/kafka-consumer-demo-2.gif)

adding a new record in `CourierTest`

![kafka-consumer-demo-3](kafka-consumer-demo.assets/kafka-consumer-demo-3.gif)

observed stream in the consumer

![kafka-consumer-demo-4](kafka-consumer-demo.assets/kafka-consumer-demo-4.gif)

adding a new record in OrderTest

![kafka-consumer-demo-5](kafka-consumer-demo.assets/kafka-consumer-demo-5.gif)

observed stream in the consumer



Next, will implement the orders and courier matching as a lambda function to process these data. Possibly temporarily writing a in memory database such as Redis or something similar to implement this feature. 

Question: 

Recommendations?