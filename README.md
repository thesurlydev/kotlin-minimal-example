# kotlin-minimal-example

An example of using kotlin-minimal for a simple CRUD API over HTTP/JSON.

## Goals

* Minimal annotations - just one: @Serializable
* Minimal dependencies
* No reflection


## Compile and Package

```shell
./gradlew clean build
```

## Run

```shell
java -jar build/libs/kotlin-minimal-example-0.1.0-standalone.jar
```

## database

To effectively reset the database:
```shell
docker compose down -v
rm -rf data
mkdir -p data/db
docker compose up -d
```
The `*.sql` files get run in natural order which is why they're prefixed.

The custom `pg_hba.conf` is necessary when using colima on Apple Silicon.
