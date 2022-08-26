FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY target/order-service-1.0.jar OrderService.jar
ENTRYPOINT ["java", "-jar", "OrderService.jar"]

# docker run -d --network ecommerce-network `
#     --name order-service `
#     -e "spring.cloud.config.uri=http://config-service:8888" `
#     -e "spring.rabbitmq.host=rabbitmq" `
#     -e "spring.zipkin.base-url=http://zipkin:9411" `
#     -e "spring.datasource.url=jdbc:mariadb://mariadb:3306/mydb" `
#     -e "eureka.client.serviceUrl.defaultZone=http://discovery-service:8761/eureka/" `
#     -e "logging.file=/api-logs/orders-ws.log" `
#     mmsnow/order-service:1.0