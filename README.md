# Student Manager

Full-stack academic management platform: student tracking, absences, grade reports, and audit logs, with role-based access (Manager and Admin).

> **Project Status:** the backend (Spring Boot, JWT security, PostgreSQL, Docker) is functional. The Angular frontend is in progress (login, registration, and student list completed).

Project carried out as part of an internship.

---

## Features

### Manager
- Creation, modification, and archiving of students (archived, never physically deleted)
- Paginated search with combinable filters (name, study level, enrollment year, absences) and column sorting
- Absence tracking with automatically updated counter
- Grade report management

### Admin
- All Manager rights
- Validation, blocking, and unblocking of Manager accounts (`Pending` → `Approved` ⇄ `Blocked`)
- Audit log consultation (read-only, paginated)

### Security
- Stateless server-side authentication via signed JWT token (2-hour validity)
- Passwords hashed with BCrypt
- Registration always creates a `Manager` account with `Pending` status: role and status are enforced by the server, never by the client
- A blocked account loses access immediately, even with a still-valid token
- Identical error message for an unknown email and a wrong password
- Secrets (JWT key, database password, Admin credentials) provided via environment variables, never in the code
- A first Admin account is created at startup from environment variables

---

## Technical Stack

| Layer | Technologies |
| :--- | :--- |
| Backend | Java 17, Spring Boot 3.3, Spring Security, Spring Data JPA, Hibernate, Lombok, jjwt |
| Database | PostgreSQL 16 |
| Frontend | Angular (standalone components, signals), SCSS, Dark/Light theme |
| Tools | Maven, Docker, Docker Compose |

---

## Repository Structure

```text
Student Manager/
├── Backend/               API Spring Boot (+ Dockerfile)
├── frontend/              Angular Application
├── docker-compose.yml     PostgreSQL + backend
├── .env.example           Environment variables template
└── README.md
```

### Backend Architecture

```text
controllers → services (interface + implementation) → repositories → PostgreSQL
                    ↑
          Dto, entities, enums, specifications, security, config
```

- One service per business entity (single responsibility)
- Controllers handle DTOs only, never entities
- Dynamic search using JPA Specifications and SQL-side pagination
- Absence counter updated atomically, in the same transaction as the absence

---

## Running the Project with Docker

### Prerequisites
- Docker Desktop running

### 1. Configure Environment

Copy `.env.example` to `.env` and fill in your own values:

```text
POSTGRES_DB=student_manager
POSTGRES_USER=student_user
POSTGRES_PASSWORD=<a password>

JWT_SECRET=<base64 key of at least 32 bytes>
ADMIN_EMAIL=<admin email>
ADMIN_PASSWORD=<a password>
```

Generate a JWT key (PowerShell):

```powershell
$b = New-Object byte[] 32; [Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b); [Convert]::ToBase64String($b)
```

The `.env` file is ignored by Git: never version control it.

### 2. Start

```bash
docker compose up -d --build
```

The API is available at `http://localhost:8081`. The Admin account is created on first startup.

### 3. Useful Commands

```bash
docker compose ps                         # status and ports
docker compose logs backend --tail 50     # backend logs
docker compose down                       # stop (data is preserved)
docker compose down -v                    # stop AND delete database
```

---

## Running the Frontend

```bash
cd frontend
npm install
ng serve
```

Open `http://localhost:4200`. API URL can be configured in `src/environments/environment.development.ts`.

---

## API Overview

| Method | Endpoint | Access | Description |
| :--- | :--- | :--- | :--- |
| POST | `/api/auth/register` | Public | Create a Manager account (`Pending`) |
| POST | `/api/auth/login` | Public | Obtain a JWT token |
| GET | `/api/students/search` | Manager, Admin | Paginated search with filters |
| GET | `/api/students/{id}` | Manager, Admin | Student details |
| POST | `/api/students` | Manager, Admin | Create a student |
| PUT | `/api/students/{id}` | Manager, Admin | Update a student |
| PATCH | `/api/students/{id}/archive` | Manager, Admin | Archive a student |
| GET | `/api/admin/users` | Admin | List accounts |
| PATCH | `/api/admin/users/{id}/validate` | Admin | Validate a Manager |
| PATCH | `/api/admin/users/{id}/block` | Admin | Block a Manager |
| PATCH | `/api/admin/users/{id}/unblock` | Admin | Unblock a Manager |

Search example:

```text
GET /api/students/search?keyword=ben&studyLevels=L2&studyLevels=L3&minAbsences=3&page=0&size=20&sort=lastName,asc
```

The token is passed in the `Authorization: Bearer <token>` header.

---

## Roadmap

- [x] Entities, DTOs, repositories, business services
- [x] Paginated search with dynamic filters
- [x] Spring Security: JWT, BCrypt, roles, account statuses
- [x] Docker Compose (PostgreSQL + backend)
- [x] Angular: login, registration, navigation bar, student list
- [ ] Angular: student form and student profile (absences, reports)
- [ ] Angular: account management and audit log (Admin)
- [ ] Automatic recording of sensitive actions in the audit log
- [ ] Global exception handler with a uniform error format
- [ ] Automated tests
- [ ] OpenAPI / Swagger documentation
- [ ] PDF file uploads (medical file, reports)
- [ ] Frontend container (nginx) in Docker Compose

---

## Known Limitations

- The JWT token is stored in `localStorage`: acceptable for a demonstration, but vulnerable to XSS attacks (an HttpOnly cookie would be safer).
- No refresh token nor server-side logout: a token remains valid until its expiration (however, a blocked account is denied immediately).
- PDF fields (medical file, reports) currently store a filename, not an uploaded file.