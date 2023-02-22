DB_NAME := template-service
DB_CONNECTION := postgresql://postgres:postgres@localhost:5432/$(DB_NAME)?sslmode=disable

createdb:
	docker exec -it postgres createdb --username=postgres --owner=postgres $(DB_NAME)

dropdb:
	docker exec -it postgres dropdb --username=postgres $(DB_NAME)

install:
	mvn clean install

package:
	mvn clean package

clean:
	mvn clean

.PHONY: createdb dropdb install package clean
