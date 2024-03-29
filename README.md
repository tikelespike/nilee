# Nilee

A [Vaadin](https://vaadin.com/)-based digital character sheet application
for [Dungeons and Dragons](https://dnd.wizards.com/) 5th edition.

## Screenshots
![grafik](https://github.com/tikelespike/nilee/assets/85690358/9c4cf920-f3c7-40c7-9ea4-2868af5ccb95)
![grafik](https://github.com/tikelespike/nilee/assets/85690358/b886fd01-238e-40b4-94bb-b794019b874b)
![grafik](https://github.com/tikelespike/nilee/assets/85690358/17d8e293-5ece-4906-8f9e-ac79d4f9da71)
![grafik](https://github.com/tikelespike/nilee/assets/85690358/05da4a25-93a2-40aa-bfd4-23c1cdd9a49c)

## Running the application

This project consists of a Maven application and a PostgreSQL database which is also required. Using the
provided `docker-compose.yml` file, you can easily start both. To run this in development mode, only start a PostgreSQL
database and build and run the app using maven (in Intellij for example, you can just click the run button).

### Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `app/target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/app-1.0-SNAPSHOT.jar`

You can then build a Docker image using the provided Dockerfile.

## Project structure

The project currently consists of two modules:

- `core` constitutes the central library containing the game logic, which is then used by the app module
    - `tikelespike.nilee.core.character` contains the character model, including properties and statistics, modeled as
      described by the D&D 5e rules.
    - `tikelespike.nilee.core.data` contains code for accessing the database. Currently, persistence is implemented
      using JPA, and persistent data includes application users as well as most information about their characters.
    - `tikelespike.nilee.core.dice` contains code for rolling dice and calculating results.
    - `tikelespike.nilee.core.events` contains the generic event system of this application.
    - `tikelespike.nilee.core.game` contains game-wide logic and session management (i.e. when different users and their
      characters come together to play)
    - `tikelespike.nilee.core.i18n` contains internationalization logic (code for supporting different languages).
    - `tikelespike.nilee.core.property` contains the generic property system. A property models the dynamic calculation
      of a value for a character, which in D&D is often based on some sort of base value and a number of modifiers
      applied to it.
    - `tikelespike.nilee.core.util` contains generic utility classes.
- `app` contains the Vaadin application (mostly UI code)
    - `MainLayout.java` in `app/src/main/java` contains the navigation setup (i.e., the
      side/top bar and the main menu). This setup uses
      [App Layout](https://vaadin.com/docs/components/app-layout).
    - `views` package in `app/src/main/java` contains the server-side Java views.
    - `views` folder in `app/frontend/` contains the client-side JavaScript views (mostly generated by Vaadin).
    - `themes` folder in `frontend/` contains the custom CSS styles.

## About

This project is currently in a very early WIP stage and only provides a fraction of the functionality needed for a full character sheet. Also, I will not necessarily prioritize the most crucial features, since this is first and foremost intended as a developer playground for myself.

This project uses Java 21, Vaadin 24.1.12, and Spring Boot 3.
