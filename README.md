## Веб-сервис сокращённых ссылок "UrlShortCut"

### Описание:

Spring Boot веб-сервис для сокращения длинных URL-адресов с возможностью регистрации сайтов и статистики переходов.  
Аналог сервисов типа 'Bitly', но с минималистичным API и авторизацией через JWT.

### Основные возможности:

- **Генерация коротких кодов** для длинных URL (например, `http://example.com/example` → `/abc1234`).
- **Регистрация сайтов** и ссылок для формирования коротких ссылок.
- **Статистика переходов** по коротким ссылкам.
- **JWT-аутентификация** для доступа к API.
- **REST API** с JSON-форматом запросов/ответов.

### Используемые технологии:

- **Backend**:
    - Java 21
    - Spring Boot 3.x
    - Spring Data JPA (Hibernate)
    - Lombok
    - PostgreSQL
- **Безопасность**:
    - JWT (JSON Web Tokens)
    - BCrypt для хранения паролей
- **Документация**:
    - Swagger (OpenAPI 3.0)
- **Тестирование**:
    - JUnit 5, Mockito, Testcontainers

### Окружение:

+ Java 21
+ Maven
+ PostgreSQL

### Запуск сервиса:

    mvn spring-boot:run

Приложение будет доступно на http://localhost:8080.

### Документация API:

После запуска откройте Swagger UI:

    http://localhost:8080/swagger-ui/index.html

## API Endpoints

#### Без авторизации

    GET /redirect/{семизначный_код} – запрос переадресации.

#### Регистрация

    POST /api/auth/signup
      {
        "username" : "user",
        "password" : "password",
        "roles": ["admin", "user"]
      }

#### Авторизация

    POST /api/auth/signin
      {
        "username" : "user",
        "password" : "password"  
      }
    
В ответ получаем временный JWT-токен:
    
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaWtoYWlsIiwiaWF0IjoxNzUzNzA0NjU3LCJleHAiOjE3NTM3MDUyNTd9.a0HsmoYsFp6qMcDwq2gyCr6zzE1NQVmjbpo8H2vv6iw"
    }

#### Регистрация доменного имени (для авторизированного пользователя)

    POST /api/site/registration
      {
        "url" : "https://www.pochta.ru/business"
      }

#### Получение кода, ассоциированного с ссылкой (для авторизированного пользователя)

    POST /api/site/convert
      {
        "url" : "https://www.pochta.ru/business"
      }

В ответ получаем временный {семизначный_код}:
    
    {
      "message": "3yMjVMV"
    }

,который можно использовать неавторизированными пользователями для получения ссылок следующим образом:

    http://localhost:8080/redirect/{семизначный_код}

#### Получение статистики (для авторизированного пользователя)
Статистику получаем по тем доменным именам, которые были добавлены в приложение с авторизированной в настоящий момент учётной записи
(Для получения статистики по всем зарегистрированным доменным именам требуется зайти в приложение пользователем с правами ROLE_ADMIN) 

    GET /api/site/statistic

Результат получаем в следующем виде:

    {
      "sites": [
        {
          "domainName": "docs.oracle.com",
           "total": 3
        },
        {
            "domainName": "gitverse.ru",
            "total": 7
        },
        {
            "domainName": "dzen.ru",
            "total": 4
        },
        {
            "domainName": "docs.spring.io",
            "total": 3
        },
        {
            "domainName": "www.pochta.ru",
            "total": 3
        }
      ]
    }

### Контакты

mikhail.cherneyev@yandex.ru