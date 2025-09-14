# Translation Management Services

The Translation Management Service is a RESTful API-driven service that allows storing, retrieving, updating, and searching translations for multiple locales. It supports tagging translations for different contexts (web, mobile, desktop) and provides efficient JSON export for frontend applications.

✨ Features
Multi-locale Support: Store translations for various languages (en, fr, es, etc.)
Tagging System: Organize translations by context (web, mobile, desktop)
CRUD Operations: Create, read, update, and delete translations
Advanced Search: Search translations by key, locale, content, or tags
JSON Export: Efficient export of translations for frontend applications
JWT Authentication: Secure token-based authentication
Performance Optimized: All endpoints respond in <200ms, export in <500ms
API Documentation: OpenAPI/Swagger documentation

🛠️ Technology Stack
Framework: Spring Boot 3.5.5
Database: MySQL
Security: Spring Security with JWT authentication
API Documentation: Springdoc OpenAPI
Testing: JUnit 5, Mockito, Spring Test
Build Tool: Maven
Performance: Optimized SQL queries with proper indexing

📋 Prerequisites
Before running this application, ensure you have the following installed:
Java 17 or higher
Maven 3.6 or higher
MySQL 5.7 or higher (for production)

🚀 Installation & Setup
1. Clone the Repository

git clone https://github.com/your-username/translation-management-service.git
cd translation-management-service

2. Configure Database
Create a MySQL database named translationmanagement and update the application properties:

CREATE DATABASE translationmanagement;

3. Configure Application
Update src/main/resources/application.properties with your database credentials:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/translationmanagement
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
app.jwt.secret=your-super-secret-jwt-key-with-minimum-256-bits
app.jwt.expiration=86400000 # 24 hours in milliseconds

🔐 Authentication
1. Obtain JWT Token
bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
Response:

json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "admin",
  "roles": ["ROLE_ADMIN"]
}
2. Use Token in Requests
Include the token in the Authorization header:

text
Authorization: Bearer YOUR_TOKEN_HERE
📚 API Documentation
Endpoints
Method      	Endpoint	                       Description	          Authentication
POST	      /api/auth/login	            Login and obtain JWT token	      None
POST	     /api/translations	          Create a new translation	        Required
GET	      /api/translations/{id}	      Get a translation by ID	          Required
PUT	      /api/translations/{id}	      Update a translation	            Required
DELETE	   /api/translations/{id}	      Delete a translation	            Required
GET	      /api/translations/search	    Search translations	              Required
GET	      /api/translations/export	    Export translations for a locale	Required
GET	      /api/translations/locales	    Get all available locales	        Required
Example Requests
Create a Translation
curl -X POST http://localhost:8080/api/translations \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "key": "welcome.message",
    "locale": "en",
    "content": "Welcome to our application!"
  }' \
  --param "tags=web,mobile"

Export Translations

curl -X GET \
  'http://localhost:8080/api/translations/export?locale=en' \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
