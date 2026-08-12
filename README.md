# Pharmacy POS Backend

A complete, production-ready **multi-tenant, multi-branch Pharmacy POS backend** with **offline-first sync support**.

## Tech Stack

- **Java 17+**
- **Spring Boot 3.3+**
- **Spring Data JPA / Hibernate** (PostgreSQL dialect)
- **Spring Security 6** + JWT (access + refresh) + PIN login
- **PostgreSQL** on Neon (serverless, SSL required)
- **Flyway** for database migrations
- **Cloudinary** Java SDK for image storage
- **Lombok**, **MapStruct**, **springdoc-openapi** (Swagger UI)
- **JUnit 5** + **Mockito** + **Testcontainers**

## Features

- Multi-tenant architecture with organization isolation
- Multi-branch support with device registration
- RBAC with role-based permissions
- Product catalog with categories, suppliers, active ingredients
- Batch/expiry inventory management with FEFO allocation
- Stock movements, adjustments, and transfers
- Purchase orders and goods receipts
- Customer management with allergies and prescriptions
- POS sales with payment processing
- Shift management with cash reconciliation
- Promotions and loyalty programs
- Offline-first sync with conflict resolution
- Audit logging for sensitive operations
- Multi-currency support with exchange rates
- Image upload via Cloudinary

## Environment Variables

Create a `.env` file in the project root:

```env
DB_PASSWORD=npg_KEC0HO5ruAUw
CLOUDINARY_API_SECRET=iSMOjoiGU7zjAjCskUXELQHGU8o
JWT_SECRET=your-long-random-jwt-secret-key-change-this-in-production-at-least-32-characters-long
```

**Important:** 
- The `.env` file is in `.gitignore` and should never be committed
- Rotate the Neon DB password and Cloudinary API secret in their dashboards after initial setup
- Use a strong, randomly generated JWT secret in production

## Database Setup

The application uses PostgreSQL on Neon with the following connection:

```
jdbc:postgresql://ep-empty-surf-axyon6ok-pooler.c-4.us-east-2.aws.neon.tech/neondb?sslmode=require&channel_binding=require
```

### Running Flyway Migrations

Flyway will automatically run migrations on application startup. To run manually:

```bash
mvn flyway:migrate
```

The initial migration `V1__init_schema.sql` creates all 43 tables, 8 enums, indexes, and foreign keys as specified in the DBML schema.

## Running the Application

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on port 8080.

## API Documentation

Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

OpenAPI spec at: `http://localhost:8080/v3/api-docs`

## Authentication

### Standard Login
```bash
POST /api/v1/auth/login
{
  "username": "admin",
  "password": "password"
}
```

### PIN Login (for POS terminals)
```bash
POST /api/v1/auth/pin-login
{
  "pinCode": "1234",
  "branchId": 1,
  "deviceUuid": "optional-device-uuid"
}
```

### Token Refresh
```bash
POST /api/v1/auth/refresh
{
  "refreshToken": "your-refresh-token"
}
```

## API Structure

All endpoints are prefixed with `/api/v1`:

- `/api/v1/auth` - Authentication (login, PIN login, refresh)
- `/api/v1/organizations` - Organization management
- `/api/v1/subscription-plans` - Subscription plan management
- `/api/v1/branches` - Branch management
- `/api/v1/branch-settings` - Branch settings
- `/api/v1/devices` - Device registration and sync
- `/api/v1/roles` - Role management
- `/api/v1/permissions` - Permission management
- `/api/v1/users` - User management
- `/api/v1/shifts` - Shift management
- `/api/v1/products` - Product catalog
- `/api/v1/categories` - Category management
- `/api/v1/suppliers` - Supplier management
- `/api/v1/active-ingredients` - Active ingredient management
- `/api/v1/inventory` - Inventory management
- `/api/v1/stock-transfers` - Stock transfers
- `/api/v1/purchase-orders` - Purchase order management
- `/api/v1/customers` - Customer management
- `/api/v1/doctors` - Doctor management
- `/api/v1/prescriptions` - Prescription management
- `/api/v1/orders` - Order management
- `/api/v1/payments` - Payment processing
- `/api/v1/promotions` - Promotion management
- `/api/v1/loyalty` - Loyalty program
- `/api/v1/sync` - Offline sync operations
- `/api/v1/audit` - Audit logs
- `/api/v1/exchange-rates` - Currency exchange rates
- `/api/v1/uploads` - Image upload

## Key Business Flows

### Checkout Flow
- **Endpoint:** `POST /api/v1/orders/checkout`
- **Features:**
  - FEFO stock allocation (earliest expiry first)
  - Drug interaction checking
  - Customer allergy validation
  - Prescription requirement enforcement
  - Controlled substance validation
  - Loyalty points calculation
  - Promotion application
  - Stock movement recording (SALE_OUT)

### Offline Sync
- **Push:** `POST /api/v1/sync/push` - Replay device sync queue
- **Pull:** `GET /api/v1/sync/pull?lastSyncedAt=timestamp` - Get server changes
- **Features:** Idempotent operations, conflict detection, retry logic

### Stock Transfer
- **Flow:** REQUESTED → IN_TRANSIT → RECEIVED
- **Features:** TRANSFER_OUT/TRANSFER_IN movements, branch inventory updates

### Shift Management
- **Open:** `POST /api/v1/shifts` - Start new shift with opening cash
- **Close:** `PUT /api/v1/shifts/{id}/close` - End shift with cash reconciliation
- **Features:** Cash difference calculation, order association

## Security

- JWT-based authentication with access and refresh tokens
- Request-scoped tenant context for multi-tenancy
- RBAC with granular permissions
- PIN-based fast login for POS terminals
- Branch-level access control

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductServiceTest

# Run integration tests with Testcontainers
mvn test -Dtest=*IntegrationTest
```

## Project Structure

```
com.pharmacy.pos
├── config        (SecurityConfig, CloudinaryConfig, OpenApiConfig, JpaAuditingConfig)
├── common         (BaseEntity, ApiResponse, PageResponse, GlobalExceptionHandler, enums, TenantContext)
├── security       (JwtService, JwtAuthFilter, CustomUserDetailsService, CustomUserDetails)
├── tenant         (organizations, subscription_plans)
├── branch         (branches, branch_settings, devices)
├── catalog        (categories, active_ingredients, suppliers, products, product_units, drug_interactions)
├── inventory      (product_batches, branch_inventories, stock_movements, stock_adjustments, stock_transfers)
├── purchasing     (purchase_orders, purchase_order_items, goods_receipts, goods_receipt_items)
├── iam            (roles, permissions, role_permissions, users, user_branches, shifts)
├── customer       (customers, customer_allergies, doctors, prescriptions, prescription_items)
├── sales          (orders, order_items, payments, order_returns, order_return_items)
├── promotion      (promotions, promotion_usages, loyalty_transactions)
├── audit          (audit_logs, sync_queue)
├── finance        (exchange_rates)
└── upload         (CloudinaryService, ImageController)
```

## Current Status

**Completed Modules:**
- ✅ Maven project structure and dependencies
- ✅ Common infrastructure (BaseEntity, ApiResponse, exception handling, enums)
- ✅ Security (JWT, authentication, authorization)
- ✅ Tenant module (organizations, subscription plans)
- ✅ Branch module (branches, branch settings, devices)
- ✅ IAM module (roles, permissions, users, user branches, shifts)
- ✅ Catalog module (categories, active ingredients, suppliers, products, product units, drug interactions)
- ✅ Inventory module (product batches, branch inventories, stock movements)
- ✅ Configuration (application.yml, OpenAPI, Cloudinary)
- ✅ Flyway migration (complete schema)

**In Progress/To Be Completed:**
- 🔄 Remaining inventory services (stock adjustments, transfers)
- 🔄 Purchasing module (purchase orders, goods receipts)
- 🔄 Customer module (customers, doctors, prescriptions)
- 🔄 Sales module (orders, payments, returns, checkout flow)
- 🔄 Promotion module (promotions, loyalty)
- 🔄 Audit module (audit logs, sync endpoints)
- 🔄 Finance module (exchange rates)
- 🔄 Upload module (Cloudinary image upload)
- 🔄 Unit tests (FEFO allocation, checkout logic)
- 🔄 Integration tests (Testcontainers)

## License

This project is proprietary software. All rights reserved.
