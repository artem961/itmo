# Building
FROM maven:3.9.9-amazoncorretto-17-debian AS builder

WORKDIR /app

COPY /pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests


# Configuring wildfly
FROM quay.io/wildfly/wildfly

ARG DATABASE_URL
ARG DB_USERNAME
ARG DB_PASSWORD

ENV DATABASE_URL=${DATABASE_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}

ADD wildfly/customization /opt/jboss/wildfly/customization/
COPY wildfly/modules /opt/jboss/wildfly/modules/

RUN /opt/jboss/wildfly/bin/add-user.sh -a -u ${DB_USERNAME} -p ${DB_PASSWORD} -g 'ManagementRealm' -s

USER root

RUN chmod +x /opt/jboss/wildfly/customization/execute.sh

USER jboss

RUN /opt/jboss/wildfly/customization/execute.sh

COPY --from=builder /app/target/*war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080 9990

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

