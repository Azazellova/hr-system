# HR System — микросервисная версия

## Сервисы
- `employee-service` — сотрудник, данные и рабочее время; PostgreSQL `employee_db`, порт 8082.
- `organization-service` — подразделения, должности и оргструктура; PostgreSQL `organization_db`, порт 8083.
- `hr-service` — кадровые заявки и стратегии; PostgreSQL `hr_db`, порт 8084. Сервис обращается HTTP к Employee, Organization и Document.
- `document-service` — создание и хранение кадровых документов; PostgreSQL `document_db`, порт 8085.
- `gateway` — Nginx reverse proxy на 8080.

Отдельный Auth Service не создавался: исходный проект содержит только `SecurityConfig` с разрешёнными запросами, без самостоятельной бизнес-логики аутентификации. Поэтому конфигурация Security сохранена внутри каждого сервиса.

## Импорт в IntelliJ IDEA
1. Распаковать архив в отдельную папку (не в текущую папку монолита).
2. IntelliJ: File → Open → выбрать корневую папку архива, где лежит агрегирующий `pom.xml`. Не открывай папку одного сервиса отдельно на первом этапе.
3. Подтвердить импорт Maven. В дереве Maven должны появиться четыре модуля: employee-service, organization-service, hr-service, document-service.
4. Если IDEA открыла папку, но не импортировала Maven: правой кнопкой по корневому `pom.xml` → Add as Maven Project, затем Maven → Reload All Projects.
5. Убедиться, что для каждого модуля папка `src/main/java` помечена как Sources Root (обычно Maven делает это автоматически).
6. Создать `.env` на основе `.env.example`, указать локальный пароль `POSTGRES_PASSWORD`.
7. Запустить Docker Desktop, открыть терминал в корне и выполнить `docker compose up --build`.

Для предварительной Maven-сборки без Docker, из корня проекта выполнить `mvnw.cmd clean package` (Windows). Для этого Maven Wrapper должен скачать Maven-дистрибутив, если его нет локально.

## Адреса
Gateway: http://localhost:8080 (основные API маршруты: `/employees`, `/departments`, `/positions`, `/hr/requests`, `/api/documents`)
Employee Swagger: http://localhost:8082/swagger-ui/index.html
Organization Swagger: http://localhost:8083/swagger-ui/index.html
HR Swagger: http://localhost:8084/swagger-ui/index.html
Document Swagger: http://localhost:8085/swagger-ui/index.html

## Важные заметки
- Исходные бизнес-классы сервисов перенесены без намеренного изменения логики; межсервисные вызовы HR реализованы HTTP RestClient.
- В HR сохранены payroll/accounting mock adapter.
- Сервисы используют отдельные БД; ссылки между Employee/Department/Position в DTO/entity остаются ID.
- `original-monolith-source` сохранён как исходный код для сверки.
- Эта версия требует Maven build/compose проверки в локальной среде. Она не была собрана в данном контейнере.
