# Hostfully Code Sample
Sample consists of two parts: 1) start API; and 2) start web.

## Start API
In a separate shell, from the `/api` directory, run: `./gradlew run`

## Start Web
In a separate shell, from the `/web` directory, run: `yarn start`

A browser window should pop up at `http://localhost:3000/`

## Testing
Tests are in `/api/src/test` and are functional tests to exercise the API. They can be run with `./gradlew test`

A simple smoke test in the front end is available at: `/src/App.test.tsx`. It can be run with `yarn test`
