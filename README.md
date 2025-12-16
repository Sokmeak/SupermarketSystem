# Supermarket Product Management System

A comprehensive product and category management system built with Spring Boot and Thymeleaf.

## 📋 Project Overview

This is a full-stack web application that allows users to manage products and categories in a supermarket inventory system. The application features CRUD operations, search and filter capabilities, and a clean, responsive UI built with Bootstrap 5.

## 🚀 Technologies Used

### Backend

- **Java 21** - Programming language
- **Spring Boot 3.3.5** - Framework
  - Spring Web - REST API and MVC
  - Spring Data JPA - Database ORM
  - Spring Boot Validation - Form validation
  - Spring Boot Actuator - Application monitoring
- **Hibernate 6.5.3** - ORM implementation
- **PostgreSQL** - Database
- **Gradle 8.11.1** - Build tool

### Frontend

- **Thymeleaf** - Server-side template engine
- **Bootstrap 5.3.3** - CSS framework
- **Font Awesome 6** - Icons

## ✨ Features

### Product Management

- ✅ Create, Read, Update, Delete (CRUD) products
- ✅ Upload product images
- ✅ Assign products to categories
- ✅ Search products by keyword (name, code)
- ✅ Filter products by category
- ✅ Form validation with error handling

### Category Management

- ✅ Create, Read, Update, Delete (CRUD) categories
- ✅ View product count per category
- ✅ Automatic seeding of 29 initial categories
- ✅ Category statistics dashboard
- ✅ Hierarchical category display (main categories and subcategories)

### UI/UX

- ✅ Responsive design (mobile-friendly)
- ✅ Modern card-based layout
- ✅ Navigation bar with breadcrumbs
- ✅ Statistics cards with icons
- ✅ Clean table layouts
- ✅ Confirmation dialogs for delete operations

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/tutorial/tutorial/
│   │   ├── TutorialApplication.java          # Main application class
│   │   ├── Product/
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java    # Product HTTP endpoints
│   │   │   ├── service/
│   │   │   │   └── ProductService.java       # Product business logic
│   │   │   ├── repository/
│   │   │   │   └── ProductRepository.java    # Product data access
│   │   │   ├── entity/
│   │   │   │   └── ProductEntity.java        # Product JPA entity
│   │   │   ├── dto/
│   │   │   │   └── ProductDTO.java           # Product data transfer object
│   │   │   └── config/
│   │   │       └── DataSeeder.java           # Database seeder
│   │   └── Category/
│   │       ├── controller/
│   │       │   └── CategoryController.java   # Category HTTP endpoints
│   │       ├── service/
│   │       │   └── CategoryService.java      # Category business logic
│   │       ├── repository/
│   │       │   └── CategoryRepository.java   # Category data access
│   │       └── entity/
│   │           └── CategoryEntity.java       # Category JPA entity
│   └── resources/
│       ├── application.properties            # Application configuration
│       └── templates/
│           ├── products.html                 # Main layout template
│           └── fragments/
│               ├── header.html               # Common HTML head
│               ├── list_product.html         # Product list view
│               ├── form.html                 # Create product form
│               ├── editForm.html             # Edit product form
│               ├── category_list.html        # Category list view
│               ├── category_form.html        # Create category form
│               └── category_edit.html        # Edit category form
└── test/
    └── java/com/tutorial/tutorial/
        └── TutorialApplicationTests.java
```

## 🗄️ Database Schema

### Products Table

| Column       | Type         | Description                  |
| ------------ | ------------ | ---------------------------- |
| id           | BIGINT       | Primary key (auto-increment) |
| product_code | VARCHAR(255) | Unique product code          |
| name         | VARCHAR(255) | Product name                 |
| price        | DOUBLE       | Product price                |
| image        | VARCHAR(255) | Image URL/path               |
| category_id  | BIGINT       | Foreign key to categories    |
| created_at   | TIMESTAMP    | Creation timestamp           |
| updated_at   | TIMESTAMP    | Last update timestamp        |

### Categories Table

| Column      | Type         | Description                  |
| ----------- | ------------ | ---------------------------- |
| id          | BIGINT       | Primary key (auto-increment) |
| name        | VARCHAR(255) | Category name                |
| description | TEXT         | Category description         |
| created_at  | TIMESTAMP    | Creation timestamp           |
| updated_at  | TIMESTAMP    | Last update timestamp        |

## 🛠️ Setup Instructions

### Prerequisites

- Java 21 or higher
- PostgreSQL 12 or higher
- Gradle 8.11.1 (included via wrapper)

### 1. Clone the Repository

```bash
git clone <repository-url>
cd TP05-Thymleaf_SarenSokmeakGIC-Ce20211376
```

### 2. Configure Database

Create a PostgreSQL database:

```sql
CREATE DATABASE supermarket;
```

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/supermarket
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server Configuration
server.port=8003
```

### 3. Build the Project

```bash
./gradlew clean build -x test
```

### 4. Run the Application

```bash
./gradlew bootRun
```

The application will start on **http://localhost:8003**

### 5. Initial Data

On first run, the application automatically seeds 29 categories:

**Main Categories:**

- Electronics (5 subcategories)
- Plastics (4 subcategories)
- Textiles (4 subcategories)
- Chemicals (4 subcategories)
- Machinery & Capital Goods (5 subcategories)
- 6 general categories (Office Supplies, Food & Beverages, Healthcare, Automotive, Sports, Furniture)

## 🌐 Application Routes

### Product Routes

| Method | Endpoint         | Description          |
| ------ | ---------------- | -------------------- |
| GET    | /                | List all products    |
| GET    | /?keyword=...    | Search products      |
| GET    | /?categoryId=... | Filter by category   |
| GET    | /create-form     | Show create form     |
| POST   | /save-product    | Create new product   |
| GET    | /edit/{code}     | Show edit form       |
| POST   | /edit/{code}     | Update product       |
| GET    | /remove/{code}   | Delete product       |
| GET    | /files/{file}    | Serve product images |

### Category Routes

| Method | Endpoint                | Description         |
| ------ | ----------------------- | ------------------- |
| GET    | /categories             | List all categories |
| GET    | /categories/create      | Show create form    |
| POST   | /categories/save        | Create new category |
| GET    | /categories/edit/{id}   | Show edit form      |
| POST   | /categories/update/{id} | Update category     |
| GET    | /categories/delete/{id} | Delete category     |

## 🎨 Architecture Pattern

The application follows the **MVC (Model-View-Controller)** pattern with a **Service Layer**:

```
Controller → Service → Repository → Database
                ↓
              Entity
                ↓
               DTO
                ↓
              View
```

- **Controller**: Handles HTTP requests and responses
- **Service**: Contains business logic
- **Repository**: Data access layer (JPA)
- **Entity**: Database models
- **DTO**: Data transfer objects for form validation
- **View**: Thymeleaf templates

## 📝 Key Implementation Details

### Product-Category Relationship

- **@ManyToOne** relationship from Product to Category
- Cascading deletes are handled at the service layer
- Categories track their products via **@OneToMany**

### Image Upload

- Images stored in `images/` directory
- Files served via `/files/{filename}` endpoint
- Directory auto-created if missing

### Search & Filter

- Keyword search uses JPQL `LIKE` queries
- Category filter uses JPA repository queries
- Combined search and filter supported

### Form Validation

- Jakarta Validation annotations (@NotNull, @NotEmpty, @Min)
- Server-side validation in controllers
- Error messages displayed in forms

## 🔧 Configuration

### Change Server Port

Edit `application.properties`:

```properties
server.port=8080
```

### Enable Debug Logging

```properties
logging.level.org.springframework=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Disable Category Seeding

Comment out or remove the `@Component` annotation from `DataSeeder.java`

## 🧪 Testing

Run tests:

```bash
./gradlew test
```

Build without tests:

```bash
./gradlew clean build -x test
```

## 📦 Building for Production

Create executable JAR:

```bash
./gradlew bootJar
```

The JAR file will be in `build/libs/`

Run the JAR:

```bash
java -jar build/libs/tutorial-0.0.1-SNAPSHOT.jar
```

## 🐛 Troubleshooting

### Port Already in Use

```bash
# Kill process on port 8003
lsof -ti:8003 | xargs kill -9
```

### Database Connection Issues

- Verify PostgreSQL is running: `psql -U postgres`
- Check database exists: `\l`
- Verify credentials in `application.properties`

### Gradle Issues

```bash
# Clean Gradle cache
./gradlew clean
rm -rf .gradle
```

### Java Version Issues

```bash
# Check Java version
java -version

# Should show Java 21 or higher
```


## 📄 License

This project is created for educational purposes.

## 🔗 Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Bootstrap 5 Documentation](https://getbootstrap.com/docs/5.3/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)

---

**Built with ❤️ using Spring Boot and Thymeleaf**
