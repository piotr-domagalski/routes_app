# build
FROM gradle:9.5-jdk21 AS build

WORKDIR /workspace

COPY . .

# workaround for gradle complaining about missing dir
# allows ./app/ to be added to .dockerignore - speeds up builds,
# avoids cache invalidation and backend rebuilds when frontend changes
RUN mkdir app

RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle :backend:installDist --no-daemon

# runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /workspace/backend/build/install/backend app

EXPOSE 8080

ENTRYPOINT ["app/bin/backend"]
