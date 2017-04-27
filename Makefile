start:
	@./gradlew clean build && java -jar build/libs/MineSweeper-1.0-SNAPSHOT.jar

tests:
	@./gradlew clean build test
