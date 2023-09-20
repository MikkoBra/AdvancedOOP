FROM maven:latest

COPY . ./

RUN mvn package -Dmaven.test.skip -Dcheckstyle.skip
ENTRYPOINT ["java", "-cp", "stock-market/target/stock-market-1.0-SNAPSHOT-jar-with-dependencies.jar", "trader-app/src/main/java/nl/rug/aoop/Main.java"]