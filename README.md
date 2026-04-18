This project is a Maven Spring Boot microservice that demonstrates an event-driven betting settlement pipeline using:

Apache Kafka → for publishing and consuming event outcomes
Apache RocketMQ → for producing bet and consuming settlement messages
In-memory repository → for storing and updating bets

Flow Summary
A REST endpoint receives an EventOutcome
    The event is published to Kafka
A Kafka consumer processes the event:
    Finds matching bets
    Produces settlement messages to RocketMQ
    Marks bets as settled

Technologies Used
    Java 17
    Spring Boot
    Spring Kafka
    Apache RocketMQ (Spring integration)
    Docker & Docker Compose
    Lombok

Prerequisites
    Java 17
    Maven
    Docker and Docker Compose

How to Run the Application
1. Start Messaging Infrastructure
    Start docker engine

2. From a terminal window navigate to project root and run:
   docker-compose up -d
This will start:
Zookeeper (port 2181)
Kafka (port 9092)
RocketMQ NameServer (port 9876)
RocketMQ Broker (port 10911)

3. Verify Containers
   docker ps
Make sure all containers are running

4. Build the Application
   mvn clean install

5. Run the Spring Boot Application
   mvn spring-boot:run

How to Use the Application
1. Publish an Event Outcome
   Invoke-RestMethod -Uri "http://localhost:8080/events/outcome" `
   -Method POST `
   -ContentType "application/json" `
   -Body (@{
   eventId = 200
   eventName = "Barcelona vs Real Madrid"
   winnerId = 2001
   } | ConvertTo-Json)

Internal Procedure
1. REST Controller receives request
2. Event is serialized and sent to Kafka
3. Kafka Consumer:
  Deserializes event
  Fetches matching bets
  Determines WON / LOST
  Sends settlement messages to RocketMQ
  Marks bets as SETTLED
  RocketMQ consumer process settlements

Example logs
c.c.kafka.producer.EventOutcomeProducer : Publishing EventOutcome to topic event-outcomes with key 200: {"eventId":200,"eventName":"Barcelona vs Real Madrid","winnerId":2001}
c.c.r.producer.BetSettlementProducer    : Sending BetSettlement to topic bet-settlements: {"betId":2,"status":"WON","amount":150.0}
c.c.r.consumer.BetSettlementConsumer    : Received settlement: BetSettlement(betId=2, status=WON, amount=150.0)
c.c.r.consumer.BetSettlementConsumer    : Paying user for bet 2 amount 150.0