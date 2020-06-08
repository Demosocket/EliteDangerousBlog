FROM openjdk:8-jdk-alpine

ADD build/libs/blog-0.0.1.jar blog.jar

VOLUME /tmp

EXPOSE 8080

ENTRYPOINT ["java","-jar","/blog.jar"]