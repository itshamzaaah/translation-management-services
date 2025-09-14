<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
</head>
<body>

<h1> Translation Management Service</h1>
<p>A <strong>RESTful API-driven service</strong> for managing translations across multiple locales with tagging, search, and JSON export support. Built for high performance and secure integration into modern applications.</p>

<hr>

<h2> Features</h2>
<ul>
    <li><strong>Multi-locale Support:</strong> Store translations for various languages (en, fr, es, etc.)</li>
    <li><strong>Tagging System:</strong> Organize translations by context (web, mobile, desktop)</li>
    <li><strong>CRUD Operations:</strong> Create, read, update, and delete translations</li>
    <li><strong>Advanced Search:</strong> Search translations by key, locale, content, or tags</li>
    <li><strong>JSON Export:</strong> Efficient export of translations for frontend apps</li>
    <li><strong>JWT Authentication:</strong> Secure token-based authentication</li>
    <li><strong>Performance Optimized:</strong> Endpoints respond in &lt;200ms, export in &lt;500ms</li>
    <li><strong>API Documentation:</strong> OpenAPI/Swagger included</li>
</ul>

<hr>

<h2> Technology Stack</h2>
<ul>
    <li><strong>Framework:</strong> Spring Boot 3.5.5</li>
    <li><strong>Database:</strong> MySQL</li>
    <li><strong>Security:</strong> Spring Security with JWT Authentication</li>
    <li><strong>API Docs:</strong> Springdoc OpenAPI (Swagger)</li>
    <li><strong>Testing:</strong> JUnit 5, Mockito, Spring Test</li>
    <li><strong>Build Tool:</strong> Maven</li>
    <li><strong>Performance:</strong> Optimized SQL queries with proper indexing</li>
</ul>

<hr>

<h2> Prerequisites</h2>
<p>Before running this application, make sure you have:</p>
<ul>
    <li>Java 17 or higher</li>
    <li>Maven 3.6 or higher</li>
    <li>MySQL 5.7 or higher</li>
</ul>

<hr>

<h2> Installation & Setup</h2>

<h3>1. Clone the Repository</h3>
<pre><code>git clone https://github.com/your-username/translation-management-service.git
cd translation-management-service
</code></pre>

<h3>2. Configure Database</h3>
<pre><code>CREATE DATABASE translationmanagement;</code></pre>

<h3>3. Configure Application</h3>
<p>Update <code>src/main/resources/application.properties</code>:</p>
<pre><code>spring.datasource.url=jdbc:mysql://localhost:3306/translationmanagement
spring.datasource.username=your_username
spring.datasource.password=your_password

# JWT Configuration
app.jwt.secret=your-super-secret-jwt-key-with-minimum-256-bits
app.jwt.expiration=86400000 # 24 hours in milliseconds
</code></pre>

<hr>

<h2> Authentication</h2>

<h3>1. Obtain JWT Token</h3>
<pre><code>curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
</code></pre>

<p>Response:</p>
<pre><code>{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "username": "admin",
  "roles": ["ROLE_ADMIN"]
}</code></pre>

<h3>2. Use Token in Requests</h3>
<p>Add the token in the header:</p>
<pre><code>Authorization: Bearer YOUR_TOKEN_HERE</code></pre>

<hr>

<h2> API Endpoints</h2>
<table border="1" cellpadding="8" cellspacing="0">
    <thead>
        <tr>
            <th>Method</th>
            <th>Endpoint</th>
            <th>Description</th>
            <th>Authentication</th>
        </tr>
    </thead>
    <tbody>
        <tr><td>POST</td><td>/api/auth/login</td><td>Login and obtain JWT token</td><td>No</td></tr>
        <tr><td>POST</td><td>/api/translations</td><td>Create a new translation</td><td>Yes</td></tr>
        <tr><td>GET</td><td>/api/translations/{id}</td><td>Get a translation by ID</td><td>Yes</td></tr>
        <tr><td>PUT</td><td>/api/translations/{id}</td><td>Update a translation</td><td>Yes</td></tr>
        <tr><td>DELETE</td><td>/api/translations/{id}</td><td>Delete a translation</td><td>Yes</td></tr>
        <tr><td>GET</td><td>/api/translations/search</td><td>Search translations</td><td>Yes</td></tr>
        <tr><td>GET</td><td>/api/translations/export</td><td>Export translations for a locale</td><td>Yes</td></tr>
        <tr><td>GET</td><td>/api/translations/locales</td><td>Get all available locales</td><td>Yes</td></tr>
    </tbody>
</table>

<hr>

<h2> Example Requests</h2>

<h3>Create a Translation</h3>
<pre><code>curl -X POST http://localhost:8080/api/translations \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "key": "welcome.message",
    "locale": "en",
    "content": "Welcome to our application!"
  }' \
  --param "tags=web,mobile"
</code></pre>

<h3>Export Translations</h3>
<pre><code>curl -X GET \
  'http://localhost:8080/api/translations/export?locale=en' \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
</code></pre>

<hr>

<h2> API Documentation</h2>
<p>Swagger UI is available at:</p>
<p><code>http://localhost:8080/swagger-ui.html</code></p>

<hr>

<h2> Testing</h2>
<p>Run all tests with:</p>
<pre><code>mvn clean test</code></pre>

<hr>


</body>
</html>
