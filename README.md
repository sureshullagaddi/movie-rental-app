
# Movie Rental Application

The application processes rental data and calculates charges and reward points for each customer. It demonstrates basic object-oriented programming principles and is structured to be easily extendable for future enhancements.

## API Reference

- http://localhost:8080/swagger-ui/index.html#/
- After analysis, the following RESTful API endpoints were created to handle invoice generation:
    - `/api/rental/invoice/{customerId}`: Generates an invoice for a specific customer based on their ID.
    - `/api/rental/invoice`: Generates an invoice for a specific customer based on their name.
    - The API supports three media types for responses:
        - `text/plain`: Returns a plain text invoice.
        - `application/json`: Returns a JSON representation of the invoice.
        - `application/pdf`: Returns a PDF document of the invoice.

## Features

- Calculates rental charges based on the type of movie and rental duration.
- Computes frequent renter points for each transaction.
- Generates detailed invoices using the customer's ID and name.
- Supports responses in text, JSON, and PDF formats.

## Design Decisions

- The system was planned to be implemented using Java, Builder design pattern for simplicity and later thought to use StringBuilder and String format because dynamically builds the report and gives performance and flexibility.
- Built a RESTful API to enhance scalability, modularity, and integration with modern front-end frameworks/ other services.
- text/plain - used StringBuilder and String format for dynamic report generation.

- application/json - used JSON for structured data representation.
    - Since invoice format is semi-structured and consistent, the best approach is: parse the text into a Java object using DTO, This gives you full control, error handling, and clean output.
- application/pdf - used OpenHTMLToPDF to generate PDF invoices from HTML templates, allowing for rich formatting and layout control.
  parse the text into a Java object.

- For data storage, used H2 in-memory database for simplicity and ease of testing, assignment requirements, and quick setup.
    - H2 is lightweight and easy to configure, making it suitable for development and testing purposes.
    - It supports SQL queries, which allows for easy data manipulation and retrieval.
- Later we can easily switch to a more robust database like MySQL or PostgreSQL if needed since used JPA for data access.
- I have modeled the database schema using Entity-Relationship (ER) diagrams to visualize the relationships between entities using ER diagrams.
### ER Diagrams
![ER Diagram](images/customer.png)
![ER Diagram](images/movie.png)
![ER Diagram](images/movie-pricing.png)
![ER Diagram](images/movie-rental.png)


## Technologies Used

- Programming Language: Java 21
- Frameworks: Spring Boot 3.5.0, JPA
- Build Tool: Gradle
- Libraries Used:
    - Used StringBuilder and String format for dynamic report generation.
    - Unit Testing: JUnit 5, Extended Mockito
    - Lombok: For reducing boilerplate code
    - Storage: H2 Database
    - Template PDF: OpenHTMLToPDF
    - Cache: Caffeine (lightweight)
    - Reactive Programming (Non-blocking): WebFlux
    - Reactive Retry Logic: Retries up to 3 times if invoice generation fails
    - API Documentation: Swagger
    - API Testing: Postman
    - Dependency Injection: Spring Framework
    - Logging: SLF4J with Logback
    - Version Control: Git

## Getting Started

To run the application:
1. Clone the repository.
2. Install dependencies (if any).
3. Run the main class `MovieRentalApplication.java` as a Spring Boot application.
4. The application will automatically create tables in the H2 database and insert sample data.
5. Import the Postman collection to test the API endpoints.

### Media Type Support
- `Accept: text/plain`
- `Accept: application/json`
- `Accept: application/pdf`

You can import the Postman collections from `main/resource/postman-collections` to test the API.

## Author
- **Name**: Suresh Ullagaddi

## Future Improvements

- **Containerization with Docker**  
  Containerize the application using Docker to ensure consistent environments across development, testing, and production.

- **Publishing to Docker Hub**  
  Publish Docker images to Docker Hub for streamlined distribution and deployment.

- **Cloud Orchestration with Kubernetes**  
  Deploy and manage the application using Kubernetes in a cloud environment to achieve scalability, high availability, and automated rollouts.

- **Event-Driven Architecture with Apache Kafka**  
  Integrate Apache Kafka to enable real-time, event-driven communication between services, enhancing responsiveness and decoupling components.

- **Artifact Management with Nexus Repository Manager**  
  Use Nexus to manage build artifacts, dependencies, and Docker images securely and efficiently.

- **Centralized Logging**  
  Integrate centralized logging to improve observability and monitoring, making it easier to track and debug issues across distributed systems.
