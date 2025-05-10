# Monsterini Backend

A Spring Boot backend application for the Monsterini game, a location-based gamified experience that generates personalized side quests for users based on their preferences and nearby points of interest.

## Features

- **User Management**: Registration, login, and profile management
- **Leaderboard System**: Track user XP and display top performers
- **Location-based Gameplay**: Integration with geographical points of interest
- **AI-Generated Side Quests**: Personalized quests using Google's Gemini AI
- **Questionnaire System**: Collect user preferences to enhance quest generation
- **RESTful API**: Comprehensive API for frontend integration

## Technologies Used

- **Java 21**
- **Spring Boot 3.4.5**
- **Spring Data JPA**
- **PostgreSQL**
- **Docker & Docker Compose**
- **Google Gemini AI**
- **Apache Parquet**
- **Swagger/OpenAPI**

## Prerequisites
- Java 21 or higher
- Docker and Docker Compose
- Google Gemini API key

## Setup Instructions

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/monsterini-backendini.git
cd monsterini-backendini
```

### 2. Configure environment variables

Create a `.env` file in the project root with the following content:

```
GEMINI_API_KEY=your_gemini_api_key_here
```

### 3. Start the PostgreSQL database

```bash
docker-compose up -d
```

### 4. Build and run the application

```bash
./gradlew bootRun
```

The application will be available at `http://localhost:8080`

## API Documentation

Once the application is running, you can access the Swagger UI documentation at:

```
http://localhost:8080/swagger-ui.html
```

### Key Endpoints

- **User Management**
  - `POST /api/users/register` - Register a new user
  - `POST /api/users/login` - User login
  - `GET /api/users/{id}` - Get user details
  - `PUT /api/users/{id}` - Update user details

- **Leaderboard**
  - `GET /api/users/leaderboard` - Get top users by XP

- **Geopoints**
  - `GET /api/places/search` - Search for places by tags

- **Side Quests**
  - `GET /api/users/sidequest/{userId}` - Generate a side quest for a user

- **Gemini AI**
  - `GET /api/gemini/test` - Test the Gemini AI integration

## Data Import

The application can import geopoint data from Parquet files. To enable this feature, uncomment the code in `ParquetReaderRunner.java` and ensure the Parquet file is available in the `src/main/resources/data/` directory.

## Database Schema

The application uses the following main entities:

- **MonsteriniUser**: Stores user information and XP
- **Questionnaire**: Stores user preferences for quest generation
- **Geopoint**: Represents geographical points of interest

## Development

### Building the project

```bash
./gradlew build
```

### Running tests

```bash
./gradlew test
```

## Docker Support

The application includes Docker Compose configuration for the PostgreSQL database. To run the entire application in Docker, you can create a Dockerfile and add it to the compose configuration.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
