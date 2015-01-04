FROM dockerfile/java:oracle-java8

ADD eip-rushucloud-0.0.1.jar /opt/eip-rushucloud/

EXPOSE 8082

WORKDIR /opt/eip-rushucloud/

CMD ["java", "-Xmx1024m", "-jar", "eip-rushucloud-0.0.1.jar"]