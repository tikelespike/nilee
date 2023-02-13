# Nilee

A [Vaadin](https://vaadin.com/)-based digital character sheet application for [Dungeons and Dragons](https://dnd.wizards.com/) 5th edition.

## Running the application

This project consists of a Maven application and a PostgreSQL database which is also required. Using the provided `docker-compose.yml` file, you can easily start both. To run this in development mode, only start a PostgreSQL database and build and run the app using maven (in Intellij for example, you can just click the run button).

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/nilee-1.0-SNAPSHOT.jar`

To deploy via docker compose, after running the maven package, run `docker compose up --build`.

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). This setup uses
  [App Layout](https://vaadin.com/docs/components/app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/` contains the client-side JavaScript views of your application.
- `themes` folder in `frontend/` contains the custom CSS styles.

## WIP Notes
This project is currently in a very early stage. Plans for the future include a plugin-based architecture for game content, automatic dice rolls & calculations and much more!
