FROM java:openjdk-8-alpine
COPY ./ /tucson/
WORKDIR /tucson/
RUN chmod +x gradlew
RUN ./gradlew build
ENV GRD_OPTS --console=plain --no-daemon
EXPOSE 20504
CMD ./gradlew runNode $GRD_OPTS