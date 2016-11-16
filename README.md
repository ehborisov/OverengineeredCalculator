# OverengineeredCalculator

Usage:
1) generate all sources
**mvn clean org.jvnet.jaxb2.maven2:maven-jaxb2-plugin:generate org.antlr:antlr4-maven-plugin:antlr4 compile**

2) make an artifact for deployment
**mvn -pl calc-server war:war**

3) Deploy calc-server-1.0-SNAPSHOT.war locally

4) Run tests
**mvn test**

see client.properties for base url configurations
