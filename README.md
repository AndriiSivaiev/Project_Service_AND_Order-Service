# Order-Service и Order-Status-Service

Этот проект содержит два микросервиса, которые взаимодействуют с Apache Kafka для отправки и 
обработки сообщений. Данный проект демонстрирует, как настроить Kafka Producer и Consumer в 
Spring Boot приложениях.

## Структура проекта

Проект состоит из двух основных сервисов:

1. **Order-Service**:
    - **Описание**: REST API для приема заказов и отправки сообщений в Kafka.
    - **Эндпоинт**:
        - `POST /order`: Принимает объект заказа с полями `product` и `quantity`, отправляет его как событие `OrderEvent` в Kafka топик `order-topic`.
    - **KafkaListener**: Слушает события из топика `order-status-topic` и выводит информацию в лог.

2. **Order-Status-Service**:
    - **Описание**: Сервис, который слушает события из топика `order-topic`, обрабатывает их и отправляет новые события в топик `order-status-topic`.
    - **KafkaListener**: Слушает события из топика `order-topic`, создает событие `OrderStatusEvent` и отправляет его в топик `order-status-topic`.

## Зависимости

- **Java**: 17+
- **Spring Boot**: 3.x
- **Kafka**: 2.8+
- **Docker**: Для запуска Kafka и Zookeeper

## Запуск Kafka и Zookeeper

Для запуска Kafka и Zookeeper используется Docker Compose. Пример `docker-compose.yml` файла:

```yaml
version: '3.8'

services:
  zookeeper:
    image: confluentinc/cp-zookeeper:7.7.0
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:7.7.0
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LOG4J_LOGGERS: "kafka.controller=INFO, kafka.producer.async.DefaultEventHandler=INFO, state.change.logger=INFO"
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://$DOCKERHOST:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_AUTO_CREATE_TOPIC_ENABLE: 'true'


## Доступ к API

### Order-Service

**Order-Service** предоставляет REST API для создания заказов и отправки сообщений в Kafka.

#### Эндпоинты

1. **Создание заказа**
   - **URL**: `/order`
   - **Метод**: `POST`
   - **Описание**: Принимает JSON-сущность заказа, состоящую из полей `product` и `quantity`, 
     и отправляет событие `OrderEvent` в Kafka топик `order-topic`.
   - **Пример запроса**:
```bash
     curl -X POST http://localhost:8080/order \
     -H "Content-Type: application/json" \
     -d '{
           "product": "Laptop",
           "quantity": 2
         }'
     ```
   - **Тело запроса**:
     ```json
     {
       "product": "Laptop",
       "quantity": 2
     }
     ```
   - **Ответ**: 201 Created (успешный запрос не возвращает тело ответа).

### Order-Status-Service

**Order-Status-Service** не имеет публичных эндпоинтов. Он слушает сообщения из Kafka топика `order-topic`, обрабатывает их и отправляет новые сообщения в `order-status-topic`.

### Логи KafkaListeners

Оба сервиса содержат KafkaListeners для обработки событий из Kafka топиков:

- **Order-Service**: Слушает события из топика `order-status-topic` и выводит информацию о полученном событии в консоль.
- **Order-Status-Service**: Слушает события из топика `order-topic` и отправляет события с информацией о статусе заказа в топик `order-status-topic`.
