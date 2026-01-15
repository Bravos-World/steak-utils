# Common Utils

A Spring Boot utility library providing cryptographic services, exception handling, Redis serialization, and helper utilities for Java 25+ applications.


## Requirements

- **Java 25+**
- **Spring Boot 4.0+**

## Installation

### Maven

Add the JitPack repository:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency:

```xml
<dependency>
    <groupId>com.github.bravos</groupId>
    <artifactId>common-utils</artifactId>
    <version>1.1.4</version>
</dependency>
```

### Gradle (Kotlin DSL)

Add the JitPack repository:

```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}
```

Add the dependency:

```kotlin
dependencies {
    implementation("com.github.bravos:common-utils:1.1.4")
}
```

## Features

- [Cryptographic Services](#cryptographic-services)
  - [AES Encryption](#aes-encryption-service)
  - [RSA Encryption](#rsa-service)
  - [HMAC-SHA512 Signing](#hmac-512-service)
  - [JWT Token Management](#jwt-service)
- [Exception Handling](#exception-handling)
- [Redis Serialization](#redis-serialization)
- [Helper Utilities](#helper-utilities)
  - [DateTimeHelper](#datetimehelper)
  - [RandomGenerator](#randomgenerator)
  - [Snowflake ID Generator](#snowflake-id-generator)
  - [DefaultObjectMapper](#defaultobjectmapper)

---

## Cryptographic Services

Cryptographic services are auto-configured when the library is included. You can inject them directly into your Spring components.

### AES Encryption Service

Provides AES-256-GCM encryption and decryption.

```java
@Autowired
private AesEncryptionService aesEncryptionService;

// Generate a secret key
String secretKey = aesEncryptionService.generateSecretKey();

// Encrypt data
String encrypted = aesEncryptionService.encrypt("Hello, World!", secretKey);

// Decrypt data
String decrypted = aesEncryptionService.decrypt(encrypted, secretKey);
```

### RSA Service

Provides RSA-2048 encryption, decryption, and digital signatures using OAEP with SHA-256.

```java
@Autowired
private RSAService rsaService;

// Generate key pair
KeyPair keyPair = rsaService.generateKeyPair();
String privateKey = rsaService.getPrivateKeyFromKeyPair(keyPair);
String publicKey = rsaService.getPublicKeyFromKeyPair(keyPair);

// Encrypt with public key
String encrypted = rsaService.encrypt("Secret message", publicKey);

// Decrypt with private key
String decrypted = rsaService.decrypt(encrypted, privateKey);

// Create digital signature
String signature = rsaService.getSignatureData("Data to sign", privateKey);

// Verify signature
boolean isValid = rsaService.verifyData("Data to sign", signature, publicKey);
```

### HMAC-512 Service

Provides HMAC-SHA512 signing and verification.

```java
@Autowired
private Hmac512Service hmac512Service;

// Sign data
String signature = hmac512Service.signData("Data to sign", "your-secret-key");

// Verify signature
boolean isValid = hmac512Service.verifyData("Data to sign", "your-secret-key", signature);
```

### JWT Service

Provides JWT token generation and validation using RS256 algorithm. Requires a `GeneralKeyPair` bean to be configured.

#### Configuration

First, define a `GeneralKeyPair` bean in your application:

```java
@Configuration
public class JwtConfig {

    @Autowired
    private RSAService rsaService;

    @Bean
    public GeneralKeyPair generalKeyPair() {
        // Option 1: Generate new keys
        KeyPair keyPair = rsaService.generateKeyPair();
        return new GeneralKeyPair(keyPair.getPrivate(), keyPair.getPublic());
        
        // Option 2: Load from configuration
        // PrivateKey privateKey = rsaService.convertPrivateKey(privateKeyString);
        // PublicKey publicKey = rsaService.convertPublicKey(publicKeyString);
        // return new GeneralKeyPair(privateKey, publicKey);
    }
}
```

#### Usage

```java
@Autowired
private JwtService jwtService;

// Generate token
JwtTokenClaims claims = JwtTokenClaims.builder()
    .id(12345L)
    .authorities(List.of("ROLE_USER", "ROLE_ADMIN"))
    .iat(DateTimeHelper.currentTimeMillis())
    .exp(DateTimeHelper.currentTimeMillis() + 3600000) // 1 hour
    .jti(snowflake.next())
    .metadata(Map.of("email", "user@example.com"))
    .build();

String token = jwtService.generateToken(claims);

// Validate and extract claims
JwtTokenClaims extractedClaims = jwtService.getClaims(token);
```

---

## Exception Handling

The library provides pre-defined exception classes and a global exception handler for Spring MVC applications.

### Exception Classes

| Exception | HTTP Status |
|-----------|-------------|
| `BadRequestException` | 400 Bad Request |
| `UnauthorizeException` | 401 Unauthorized |
| `ForbiddenException` | 403 Forbidden |
| `NotFoundException` | 404 Not Found |
| `ConflictDataException` | 409 Conflict |
| `TooManyRequestException` | 429 Too Many Requests |
| `InternalErrorException` | 500 Internal Server Error |

### Usage

```java
// Throw with message only
throw new NotFoundException("User not found");

// Throw with message and custom error code
throw new BadRequestException("Invalid email format", "user.email.invalid");
```

### Enable Global Exception Handler

Add the `@EnableBlockingGlobalException` annotation to your main application class:

```java
@SpringBootApplication
@EnableBlockingGlobalException
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### Error Response Format

```json
{
    "detail": "User not found",
    "code": "resource.not.found"
}
```

For validation errors:

```json
{
    "detail": "Invalid request parameters",
    "code": "invalid.parameter",
    "errors": {
        "email": "must not be blank",
        "age": "must be greater than 0"
    }
}
```

---

## Redis Serialization

The library provides a `SimpleRedisSerializer` that handles various data types with type-safe serialization.

### Enable Redis Auto Configuration

Add the `@EnableRedisAutoConfig` annotation to your application:

```java
@SpringBootApplication
@EnableRedisAutoConfig
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### Configuration Properties

Configure Redis connection in `application.properties` or `application.yml`:

```yaml
redis:
  host: localhost
  port: 6379
  password: your-password  # optional
```

### Usage

```java
@Autowired
private RedisTemplate<String, Object> redisTemplate;

// Store values (type is preserved)
redisTemplate.opsForValue().set("key:string", "Hello");
redisTemplate.opsForValue().set("key:number", 12345L);
redisTemplate.opsForValue().set("key:boolean", true);
redisTemplate.opsForValue().set("key:object", myObject);

// Retrieve values
String str = (String) redisTemplate.opsForValue().get("key:string");
Long num = (Long) redisTemplate.opsForValue().get("key:number");
```

### Supported Types

- `String`
- `byte[]`
- `Character`
- `Boolean`
- `Enum`
- `Number` (Byte, Short, Integer, Long, Float, Double, BigDecimal)
- Objects (serialized as JSON)

---

## Helper Utilities

### DateTimeHelper

A utility class for converting between different date-time representations and epoch milliseconds.

```java
// Get current time in milliseconds
long now = DateTimeHelper.currentTimeMillis();

// Convert epoch to various types
Instant instant = DateTimeHelper.toInstant(epochMillis);
LocalDateTime localDateTime = DateTimeHelper.toLocalDateTime(epochMillis);
ZonedDateTime zonedDateTime = DateTimeHelper.toZonedDateTime(epochMillis);
OffsetDateTime offsetDateTime = DateTimeHelper.toOffsetDateTime(epochMillis);
Date date = DateTimeHelper.toDate(epochMillis);
String isoString = DateTimeHelper.toISOString(epochMillis);

// Convert back to epoch milliseconds
long millis = DateTimeHelper.from(localDateTime);
long millis = DateTimeHelper.from(zonedDateTime);
long millis = DateTimeHelper.fromISOString("2025-01-15T10:30:00Z");

// Get current LocalDateTime
LocalDateTime now = DateTimeHelper.now();
```

> **Tip:** Set the default timezone using JVM argument: `-Duser.timezone=America/New_York`

### RandomGenerator

Secure random generation utilities.

```java
// Generate a 64-byte URL-safe Base64 encoded secret key
String secretKey = RandomGenerator.generateSecretKey();

// Generate random numeric string
String otp = RandomGenerator.randomNumberString(6, false);  // e.g., "482957"
String otpWithLeadingZero = RandomGenerator.randomNumberString(6, true);  // e.g., "048293"

// Generate random alphanumeric string
String code = RandomGenerator.randomAlphaNumericString(10);  // e.g., "aB3xK9mN2p"

// Generate secure random bytes
byte[] bytes = RandomGenerator.randomSecureBytes(32);

// Generate random boolean
boolean flag = RandomGenerator.randomBoolean();
```

### Snowflake ID Generator

Twitter Snowflake-style unique ID generator.

```java
// Create with machine ID (0-1023)
Snowflake snowflake = new Snowflake(1);

// Or with custom epoch
Snowflake snowflake = new Snowflake(1, customEpochMillis);

// Generate unique ID
long id = snowflake.next();

// Extract information from ID
long timestamp = snowflake.extractTimestamp(id);
long machineId = snowflake.extractMachineId(id);
```

**ID Structure:**
- 41 bits: timestamp (milliseconds since epoch)
- 10 bits: machine ID (0-1023)
- 12 bits: sequence number (0-4095)

> **Note:** Default epoch is January 1, 2025 UTC.

### DefaultObjectMapper

Pre-configured Jackson ObjectMapper instance.

```java
ObjectMapper objectMapper = DefaultObjectMapper.get();
```

**Configuration:**
- Ignores unknown properties during deserialization
- Automatically finds and registers Jackson modules

---

## Auto-Configuration

The following services are auto-configured:

| Service | Condition |
|---------|-----------|
| `RSAService` | Always (if not already defined) |
| `AesEncryptionService` | Always (if not already defined) |
| `Hmac512Service` | Always (if not already defined) |
| `ObjectMapper` | Always (if not already defined) |
| `JwtService` | When `GeneralKeyPair` bean is present |

You can override any auto-configured bean by defining your own:

```java
@Bean
public RSAService rsaService() {
    return new CustomRSAServiceImpl();
}
```

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

