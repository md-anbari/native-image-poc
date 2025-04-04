# 🚀 Spring Boot Native Image POC with GraalVM & Buildpacks

This project demonstrates how to build **Spring Boot** applications into lightweight, fast-starting native executables using **GraalVM Native Image** and the powerful integration provided by **Spring Boot Buildpacks** and the **Maven Native Plugin**.

---

## 🍃 What is GraalVM Native Image?

[GraalVM](https://www.graalvm.org/) is a high-performance JDK distribution from Oracle Labs, distinguished by its advanced optimizing compiler. One of its standout features is **Native Image**, which compiles Java applications ahead-of-time (AOT) into native executables.

Unlike traditional JVM applications that interpret and compile bytecode at runtime, **Native Image**:

- ✅ Performs class loading, dependency analysis, and compilation during build time.
- ✅ Produces a standalone executable, eliminating the need for a JVM at runtime.
- ✅ Achieves extremely fast startup times and a lower memory footprint, making it ideal for microservices, serverless applications, and containerized environments.

---

## 📦 What are Spring Boot Buildpacks?

**Spring Boot Buildpacks** are Cloud Native Buildpacks integrated directly into Spring Boot. They simplify containerization and native-image building by:

- Automatically managing dependencies and GraalVM environments.
- Providing zero-configuration native image builds out-of-the-box.
- Producing OCI-compliant Docker container images without writing Dockerfiles.

---

## 🎯 Project Goals & Features

In this project, we extensively explore the compatibility of native-image builds for various common Spring Boot functionalities, including:

- ✅ **Spring Web & Controllers**: REST APIs for basic CRUD operations.
- ✅ **Spring Data JPA & PostgreSQL**: Persistence layer with JSONB columns and entity relationships.
- ✅ **Spring Security**: Authentication and authorization via Basic Authentication (with potential for JWT integration).
- ✅ **Spring AOP**: Auditing aspects to demonstrate dynamic proxy handling.
- ✅ **Jackson & JSON processing**: Explicitly tested for serialization and deserialization, to illustrate handling reflection-heavy libraries.
- ✅ **Apache POI**: Excel file generation, a challenging native-image use case due to dynamic resource loading and reflection.
- 🚧 **Testing & Container Tests**: Unit tests, integration tests, and container tests with Testcontainers (still in progress).

---

## 📚 Reflection, Resources & Dynamic Features Handling in Native Images

**GraalVM Native Image** compiles under a **closed-world assumption**, meaning it needs complete knowledge of all dynamic behaviors at build time. Features such as reflection, dynamic proxies, serialization, and resource loading must therefore be declared explicitly. This project addresses these challenges using:

- ✅ **Spring Runtime Hints** (`RuntimeHintsRegistrar`):
  ```java
  @ImportRuntimeHints(DetailRuntimeHints.class)
  public class DetailRuntimeHints implements RuntimeHintsRegistrar {
      @Override
      public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
          hints.reflection().registerType(Detail.class,
                  MemberCategory.INVOKE_PUBLIC_METHODS,
                  MemberCategory.DECLARED_FIELDS);
      }
  }
  ```

- ✅ **Manual JSON configuration** (`reflect-config.json`) generated via the GraalVM agent:
  ```bash
  java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image -jar your-app.jar
  ```

If you encounter compatibility issues with libraries, consider the following:

- **Preferred**: Libraries explicitly supporting Native Image (see [GraalVM Reachability Metadata Repository](https://github.com/oracle/graalvm-reachability-metadata)).
- **Recommended**: Use GraalVM's tracing agent to automatically detect and generate configuration.
- **Fallback**: Manually write reflection or resource configurations as needed.

GraalVM Native Image now supports many popular libraries and frameworks out-of-the-box, including but not limited to:

- Spring Boot, Quarkus, Micronaut, Helidon
- Apache Tomcat, Netty
- Hibernate ORM, H2, PostgreSQL, MySQL, MariaDB
- Testcontainers, JUnit, Mockito
- And more—see [Ready for Native Image](https://www.graalvm.org/native-image/libraries-and-frameworks).

---

## 🗂️ Project Structure Overview

This Spring Boot project illustrates common architectural patterns, explicitly chosen to challenge and demonstrate native-image compatibility:

- **Domain Entities**: `EmployeeEntity` and `DetailEntity`
  - **Relationship**: One-to-many association (Employee → Details).
  - **Purposefully JSON-based**: To explicitly test JSON serialization and reflection.
- **Data Persistence**:
  - PostgreSQL database with JSONB columns for flexible schema.
  - Managed by Hibernate/JPA, leveraging Hypersistence Utils.
- **REST APIs**:
  - CRUD operations for employees and their associated details.
  - Excel file exports using Apache POI.
- **Security**:
  - Spring Security configured for Basic Authentication.
- **Auditing & Logging**:
  - Spring AOP aspects intercepting service methods for audit logging.

**Entities Example**:

```java
@Entity
@Table(name = "employees")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String position;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "employee_id")
    private List<DetailEntity> details;
}

@Entity
@Table(name = "details")
public class DetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String salary;
    private String contractType;
    private String year;
}
```

---

## 🛠️ Building and Running Native Images

### Using Spring Boot Buildpacks:

```bash
./mvnw -Pnative spring-boot:build-image
docker run -p 8080:8080 your-app:latest
```

### Using Maven Native Plugin:

```bash
./mvnw -Pnative native:compile
./target/native-image-poc
```

---

## ⚠️ Known Challenges & Ongoing Work

- **Apache POI (Excel)**: Requires extensive reflection configurations (in progress).
- **Container tests with Testcontainers**: Requires special hints and resource declarations (in progress).

This repository is actively evolving as we explore and overcome native-image compatibility challenges.


## 📖 Resources & Further Reading

- [Official GraalVM Website](https://www.graalvm.org/)
- [Spring Boot Native Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html)
- [Native Image Libraries & Frameworks](https://www.graalvm.org/native-image/libraries-and-frameworks)
- [GraalVM Reachability Metadata](https://github.com/oracle/graalvm-reachability-metadata)

