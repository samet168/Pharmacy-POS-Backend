# Backend API Coverage Matrix

## Existing Controllers Audit

| Controller | Endpoints | Status | Missing Features |
|------------|-----------|--------|------------------|
| **AuthController** | /register, /login, /refresh, /pin-login, /me | ✅ Complete | None - /me includes permissions |
| **UserController** | CRUD with image upload | ✅ Complete | None |
| **PermissionController** | CRUD, /code/{code} | ✅ Complete | None |
| **RoleController** | CRUD | 🔍 Need Check | Role-permission assignment endpoint |
| **ShiftController** | CRUD, /{id}/close, /branch/{branchId} | ⚠️ Partial | GET /current, GET /user/{userId} |
| **AuditLogsController** | CRUD, /user/{userId}, /organization/{organizationId} | 🔍 Need Check | Advanced filters (from, to, branchId, action, etc.) |
| **ProductController** | CRUD with image, /organization/{organizationId}, /search, /barcode/{barcode} | ✅ Complete | None |
| **CategoryController** | CRUD, /organization/{organizationId} | ⚠️ Partial | Search endpoint |
| **SupplierController** | CRUD, /organization/{organizationId}, /search | ✅ Complete | None |
| **ActiveIngredientController** | CRUD, /organization/{organizationId} | 🔍 Need Check | Search endpoint |
| **DrugInteractionsController** | CRUD, /ingredient/{ingredientId} | 🔍 Need Check | /check endpoint for multiple ingredients |
| **ProductUnitsController** | CRUD, /product/{productId} | 🔍 Need Check | None |
| **CustomerController** | CRUD with image, /organization/{organizationId}/phone/{phone}, /search | ✅ Complete | None |
| **CustomerAllergyController** | CRUD | 🔍 Need Check | None |
| **DoctorController** | CRUD, /search | 🔍 Need Check | Verify search supports all needed filters |
| **PrescriptionController** | CRUD, /customer/{customerId}, /doctor/{doctorId} | 🔍 Need Check | /check-allergies endpoint |
| **DashboardController** | /overview, /sales, /products, /customers, /orders, /low-stock, /top-products, /recent-orders, /branches | ⚠️ Partial | Date filter parameters (from, to) |
| **OrderController** | /checkout, CRUD, /organization/{organizationId} with branch filter | ⚠️ Partial | PUT endpoint (exists in code but needs verification), DELETE |
| **PaymentController** | CRUD, /order/{orderId} | ⚠️ Partial | List endpoint with filters (branchId, paymentMethod, status, from, to) |
| **OrderReturnController** | /process, /{id}, /order/{orderId} | ⚠️ Partial | List endpoint with pagination and filters |
| **LoyaltyController** | CRUD, /organization/{organizationId}, /organization/{organizationId}/active | 🔍 Need Check | /customer/{customerId}, /customer/{customerId}/points, /customer/{customerId}/transactions |
| **PromotionsController** | CRUD, /organization/{organizationId}, /organization/{organizationId}/active, /organization/{organizationId}/active/{date}, /code/{code} | 🔍 Need Check | Verify /code/{code} provides validation |
| **BranchController** | CRUD, /organization/{organizationId} | 🔍 Need Check | None |
| **BranchSettingsController** | POST, /branch/{branchId} | ✅ Complete | None |
| **DeviceController** | CRUD, /sync/{deviceUuid}, /uuid/{deviceUuid}, /branch/{branchId} | ⚠️ Partial | Health endpoint /{id}/health |
| **BranchInventoryController** | CRUD, /branch/{branchId}, /branch/{branchId}/product/{productId}/available, /expiring, /expired, /low-stock | ✅ Complete | None |
| **ProductBatchController** | CRUD | 🔍 Need Check | None |
| **StockTransfersController** | Full workflow (CRUD, /approve, /receive, from-branch, to-branch, status) | ✅ Complete | None |
| **StockMovementsController** | CRUD, /reference, /branch/{branchId}, /batch/{batchId} | ⚠️ Partial | Filters (productId, movementType, date range) |
| **StockAdjustmentsController** | CRUD, /reason/{reason}, /product/{productId}, /branch/{branchId} | ✅ Complete | None |
| **PurchaseOrderController** | CRUD, /submit, /items, /cancel, /supplier/{supplierId}, /organization/{organizationId}/status/{status}, /branch/{branchId} | 🔍 Need Check | Verify all workflow states |
| **GoodsReceiptController** | CRUD, /purchase-order/{purchaseOrderId}, /branch/{branchId} | ⚠️ Partial | PUT endpoint (for editing) if workflow requires |
| **UploadsController** | POST, /image, DELETE | ✅ Complete | None |
| **OrganizationController** | CRUD, /slug/{slug} | ✅ Complete | None |
| **SubscriptionPlanController** | CRUD, /organization/{organizationId} | ✅ Complete | None |
| **SetupController** | /bootstrap, /fix-permissions, /create-admin | ✅ Complete | None |

## Implemented Controllers

| Controller | Status | Notes |
|------------|--------|-------|
| **ReportsController** | ✅ Complete | Sales, Products, Customers, Purchases, Inventory, Staff Performance reports implemented |
| **NotificationController** | ✅ Complete | Full notification management with read/unread tracking |

## Completed Enhancements

### Product Controller
- ✅ Added GET /api/v1/products/search endpoint
- ✅ Added GET /api/v1/products/barcode/{barcode} endpoint
- ✅ Added search query to ProductRepository

### Customer Controller
- ✅ Added GET /api/v1/customers/search endpoint
- ✅ Added search query to CustomerRepository

### Branch Inventory Controller
- ✅ Renamed to /api/v1/inventory for inventory endpoints
- ✅ Added GET /api/v1/inventory/expiring endpoint
- ✅ Added GET /api/v1/inventory/expired endpoint
- ✅ Added GET /api/v1/inventory/low-stock endpoint
- ✅ Added ExpiringProductResponse DTO
- ✅ Enhanced BranchInventoryService with expiry tracking

### Auth Controller
- ✅ Enhanced /auth/me to include permissions in response
- ✅ Returns authorities, role, organization, branch information

### Repository Enhancements
- ✅ OrderRepository - added findByOrganizationIdAndCreatedAtBetween
- ✅ PaymentRepository - added findByOrderIdIn
- ✅ OrderItemRepository - added findByOrderIdIn
- ✅ ProductRepository - added findByOrganizationId (List) and search query
- ✅ ProductBatchRepository - added findByExpiryDateBetween and findByExpiryDateBefore
- ✅ CustomerRepository - added findByOrganizationId (List) and search query
- ✅ BranchInventoryRepository - added findByBranchId
- ✅ PurchaseOrderRepository - added findByOrganizationIdAndCreatedAtBetween

### Entity Enhancements
- ✅ ProductBatch - added branchInventories relationship

## Missing Endpoints by Feature

### Search & Lookup
- GET /api/v1/categories/search - Category search
- GET /api/v1/active-ingredients/search - Active ingredient search

### Dashboard Enhancements
- GET /api/v1/dashboard/overview?from=YYYY-MM-DD&to=YYYY-MM-DD
- GET /api/v1/dashboard/sales?from=YYYY-MM-DD&to=YYYY-MM-DD
- GET /api/v1/dashboard/orders?from=YYYY-MM-DD&to=YYYY-MM-DD
- GET /api/v1/dashboard/customers?from=YYYY-MM-DD&to=YYYY-MM-DD
- GET /api/v1/dashboard/products?from=YYYY-MM-DD&to=YYYY-MM-DD

### User Management
- GET /api/v1/shifts/current - Current shift for user
- GET /api/v1/shifts/user/{userId} - Shifts by user
- PUT /api/v1/roles/{roleId}/permissions - Assign permissions to role

### Sales & Payments
- GET /api/v1/payments - List all payments with filters
- GET /api/v1/order-returns - List all returns with pagination

### Medical
- POST /api/v1/drug-interactions/check - Check interactions for multiple ingredients
- POST /api/v1/prescriptions/check-allergies - Verify this exists and works

### Enhancements
- Enhanced AuditLogs filters (from, to, branchId, action, etc.)
- Drug interaction check endpoint
- Device health endpoint /{id}/health
- StockMovements enhanced filters
- Category search
- Active ingredient search
- Loyalty customer-specific endpoints
- Reports export endpoints
- Global search endpoint

## Implementation Priority

### Phase 1: Critical Missing Endpoints (High Priority) - COMPLETED
1. ✅ ReportsController - All report endpoints
2. ✅ NotificationController - All notification endpoints
3. ✅ Product search and barcode lookup
4. ✅ Customer search endpoint
5. ✅ Expiring/Expired products endpoints
6. ⏳ Current shift endpoint
7. ⏳ Enhanced /auth/me with permissions (COMPLETED)
8. ⏳ Payment list endpoint
9. ⏳ OrderReturn list endpoint

### Phase 2: Enhancements (Medium Priority)
1. ⏳ Dashboard date filters
2. ⏳ Enhanced AuditLogs filters
3. ⏳ Role-permission assignment
4. ⏳ Drug interaction check endpoint
5. ⏳ Device health endpoint
6. ⏳ StockMovements enhanced filters

### Phase 3: Additional Features (Low Priority)
1. ⏳ Category search
2. ⏳ Active ingredient search
3. ⏳ Loyalty customer-specific endpoints
4. ⏳ Reports export endpoints
5. ⏳ Global search endpoint

## Next Steps

1. ✅ Audit all existing controllers
2. ✅ Create ReportsController
3. ✅ Create NotificationController
4. ✅ Add missing search endpoints
5. ✅ Add barcode lookup
6. ✅ Add expiring/expired endpoints
7. ⏳ Enhance Dashboard with date filters
8. ⏳ Add current shift endpoint to ShiftController
9. ⏳ Enhance PaymentController with list endpoint
10. ⏳ Enhance OrderReturnController with list endpoint
11. ⏳ Add role-permission assignment endpoint
12. ⏳ Test all new endpoints
13. ⏳ Verify Swagger documentation