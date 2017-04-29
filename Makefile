start_easy:
	@./gradlew clean build && java -jar build/libs/MineSweeper-1.0-SNAPSHOT.jar easy

start_middle:
	@./gradlew clean build && java -jar build/libs/MineSweeper-1.0-SNAPSHOT.jar middle

start_hard:
	@./gradlew clean build && java -jar build/libs/MineSweeper-1.0-SNAPSHOT.jar hard

tests:
	@./gradlew clean build test
