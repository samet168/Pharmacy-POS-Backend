# API Contract Matrix

## Overview
This document tracks the API contracts between the frontend and backend systems, identifying mismatches and validation status.

| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|

## Categories
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Categories | `/categories` | GET | `/api/v1/categories` | `{ page, size }` | `List<CategoryResponse>` | ✅ MATCH |
| Get by Organization | `/categories` | GET | `/api/v1/categories/organization/{id}` | `{ page, size }` | `List<CategoryResponse>` | ✅ MATCH |
| Get by ID | `/categories/[id]` | GET | `/api/v1/categories/{id}` | N/A | `CategoryResponse` | ✅ MATCH |
| Create Category | `/categories` | POST | `/api/v1/categories` | `CategoryRequest` | `CategoryResponse` | ✅ MATCH |
| Update Category | `/categories/[id]` | PUT | `/api/v1/categories/{id}` | `CategoryRequest` | `CategoryResponse` | ✅ MATCH |
| Delete Category | `/categories/[id]` | DELETE | `/api/v1/categories/{id}` | N/A | `Void` | ✅ MATCH |

## Products
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Products | `/products` | GET | `/api/v1/products` | `{ page, size }` | `PageResponse<ProductResponse>` | ✅ MATCH |
| Get by Organization | `/products` | GET | `/api/v1/products/organization/{id}` | `{ page, size }` | `PageResponse<ProductResponse>` | ✅ MATCH |
| Get by ID | `/products/[id]` | GET | `/api/v1/products/{id}` | N/A | `ProductResponse` | ✅ MATCH |
| Create Product | `/products` | POST | `/api/v1/products` | `FormData(product, file)` | `ProductResponse` | ✅ MATCH |
| Update Product | `/products/[id]` | PUT | `/api/v1/products/{id}` | `FormData(product, file)` | `ProductResponse` | ✅ MATCH |
| Delete Product | `/products/[id]` | DELETE | `/api/v1/products/{id}` | N/A | `Void` | ✅ MATCH |
| Search Products | `/products` | GET | `/api/v1/products/search` | `{ organizationId, query, branchId, page, size }` | `PageResponse<ProductResponse>` | ✅ MATCH |
| Get by Barcode | `/products` | GET | `/api/v1/products/barcode/{barcode}` | `{ organizationId }` | `ProductResponse` | ✅ MATCH |

## Customers
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Customers | `/customers` | GET | `/api/v1/customers` | `{ page, size }` | `PageResponse<CustomerResponse>` | ✅ MATCH |
| Get by Organization | `/customers` | GET | `/api/v1/customers/organization/{id}` | `{ page, size }` | `PageResponse<CustomerResponse>` | ✅ MATCH |
| Get by Phone | `/customers` | GET | `/api/v1/customers/organization/{id}/phone/{phone}` | N/A | `CustomerResponse` | ✅ MATCH |
| Search Customers | `/customers` | GET | `/api/v1/customers/search` | `{ organizationId, query, page, size }` | `PageResponse<CustomerResponse>` | ✅ MATCH |
| Get by ID | `/customers/[id]` | GET | `/api/v1/customers/{id}` | N/A | `CustomerResponse` | ✅ MATCH |
| Create Customer | `/customers` | POST | `/api/v1/customers` | `FormData(customer, file)` | `CustomerResponse` | ✅ MATCH |
| Update Customer | `/customers/[id]` | PUT | `/api/v1/customers/{id}` | `FormData(customer, file)` | `CustomerResponse` | ✅ MATCH |
| Delete Customer | `/customers/[id]` | DELETE | `/api/v1/customers/{id}` | N/A | `Void` | ✅ MATCH |

## Orders
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Orders | `/orders` | GET | `/api/v1/orders` | `{ organizationId, branchId, page, size }` | `PageResponse<OrderResponse>` | ✅ MATCH |
| Get by ID | `/orders/[id]` | GET | `/api/v1/orders/{id}` | N/A | `OrderResponse` | ✅ MATCH |
| Delete Order | `/orders/[id]` | DELETE | `/api/v1/orders/{id}` | N/A | `Void` | ✅ MATCH |
| Update Order | `/orders/[id]` | PUT | `/api/v1/orders/{id}` | `Partial<CheckoutRequest>` | `OrderResponse` | ⚠️ NEEDS VERIFICATION |
| Checkout | `/pos/checkout` | POST | `/api/v1/orders/checkout` | `CheckoutRequest` | `CheckoutResponse` | ✅ MATCH |

## Purchase Orders
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Purchase Orders | `/purchase-orders` | GET | `/api/v1/purchase-orders` | `{ organizationId, branchId, page, size }` | `PageResponse<PurchaseOrderResponse>` | ⚠️ NEEDS VERIFICATION |
| Get by ID | `/purchase-orders/[id]` | GET | `/api/v1/purchase-orders/{id}` | N/A | `PurchaseOrderResponse` | ⚠️ NEEDS VERIFICATION |
| Create Purchase Order | `/purchase-orders` | POST | `/api/v1/purchase-orders` | `PurchaseOrderRequest` | `PurchaseOrderResponse` | ⚠️ NEEDS VERIFICATION |
| Update Purchase Order | `/purchase-orders/[id]` | PUT | `/api/v1/purchase-orders/{id}` | `PurchaseOrderRequest` | `PurchaseOrderResponse` | ⚠️ NEEDS VERIFICATION |
| Delete Purchase Order | `/purchase-orders/[id]` | DELETE | `/api/v1/purchase-orders/{id}` | N/A | `Void` | ⚠️ NEEDS VERIFICATION |

## Goods Receipts
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Goods Receipts | `/goods-receipts` | GET | `/api/v1/goods-receipts` | `{ organizationId, branchId, page, size }` | `PageResponse<GoodsReceiptResponse>` | ⚠️ NEEDS VERIFICATION |
| Get by ID | `/goods-receipts/[id]` | GET | `/api/v1/goods-receipts/{id}` | N/A | `GoodsReceiptResponse` | ⚠️ NEEDS VERIFICATION |
| Create Goods Receipt | `/goods-receipts` | POST | `/api/v1/goods-receipts` | `GoodsReceiptRequest` | `GoodsReceiptResponse` | ⚠️ NEEDS VERIFICATION |
| Update Goods Receipt | `/goods-receipts/[id]` | PUT | `/api/v1/goods-receipts/{id}` | `GoodsReceiptRequest` | `GoodsReceiptResponse` | ⚠️ NEEDS VERIFICATION |
| Delete Goods Receipt | `/goods-receipts/[id]` | DELETE | `/api/v1/goods-receipts/{id}` | N/A | `Void` | ⚠️ NEEDS VERIFICATION |

## Suppliers
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Suppliers | `/catalog/suppliers` | GET | `/api/v1/suppliers` | `{ organizationId, page, size }` | `PageResponse<SupplierResponse>` | ⚠️ NEEDS VERIFICATION |
| Get by ID | `/catalog/suppliers/[id]` | GET | `/api/v1/suppliers/{id}` | N/A | `SupplierResponse` | ⚠️ NEEDS VERIFICATION |
| Create Supplier | `/catalog/suppliers` | POST | `/api/v1/suppliers` | `SupplierRequest` | `SupplierResponse` | ⚠️ NEEDS VERIFICATION |
| Update Supplier | `/catalog/suppliers/[id]` | PUT | `/api/v1/suppliers/{id}` | `SupplierRequest` | `SupplierResponse` | ⚠️ NEEDS VERIFICATION |
| Delete Supplier | `/catalog/suppliers/[id]` | DELETE | `/api/v1/suppliers/{id}` | N/A | `Void` | ⚠️ NEEDS VERIFICATION |

## Authentication
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| Login | `/login` | POST | `/api/v1/auth/login` | `{ username, password }` | `{ accessToken, refreshToken, user }` | ✅ MATCH |
| Refresh Token | N/A | POST | `/api/v1/auth/refresh` | `{ refreshToken }` | `{ accessToken, refreshToken }` | ✅ MATCH |
| Get Current User | N/A | GET | `/api/v1/auth/me` | N/A | `UserResponse` | ✅ MATCH |
| Logout | N/A | POST | `/api/v1/auth/logout` | N/A | `Void` | ✅ MATCH |

## Dashboard
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| Get Overview | `/dashboard` | GET | `/api/v1/dashboard/overview` | `{ organizationId, branchId, startDate, endDate }` | `DashboardOverview` | ✅ MATCH |

## Branches
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Branches | `/branches` | GET | `/api/v1/branches` | `{ organizationId, page, size }` | `PageResponse<BranchResponse>` | ⚠️ NEEDS VERIFICATION |
| Get by ID | `/branches/[id]` | GET | `/api/v1/branches/{id}` | N/A | `BranchResponse` | ⚠️ NEEDS VERIFICATION |
| Create Branch | `/branches` | POST | `/api/v1/branches` | `BranchRequest` | `BranchResponse` | ⚠️ NEEDS VERIFICATION |
| Update Branch | `/branches/[id]` | PUT | `/api/v1/branches/{id}` | `BranchRequest` | `BranchResponse` | ⚠️ NEEDS VERIFICATION |
| Delete Branch | `/branches/[id]` | DELETE | `/api/v1/branches/{id}` | N/A | `Void` | ⚠️ NEEDS VERIFICATION |

## Users
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Users | `/users` | GET | `/api/v1/users` | `{ organizationId, branchId, page, size }` | `PageResponse<UserResponse>` | ⚠️ NEEDS VERIFICATION |
| Get by ID | `/users/[id]` | GET | `/api/v1/users/{id}` | N/A | `UserResponse` | ⚠️ NEEDS VERIFICATION |
| Create User | `/users` | POST | `/api/v1/users` | `UserRequest` | `UserResponse` | ⚠️ NEEDS VERIFICATION |
| Update User | `/users/[id]` | PUT | `/api/v1/users/{id}` | `UserRequest` | `UserResponse` | ⚠️ NEEDS VERIFICATION |
| Delete User | `/users/[id]` | DELETE | `/api/v1/users/{id}` | N/A | `Void` | ⚠️ NEEDS VERIFICATION |

## Roles
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Roles | `/roles-permissions` | GET | `/api/v1/roles` | `{ organizationId, page, size }` | `PageResponse<RoleResponse>` | ⚠️ NEEDS VERIFICATION |
| Get by ID | `/roles-permissions/[id]` | GET | `/api/v1/roles/{id}` | N/A | `RoleResponse` | ⚠️ NEEDS VERIFICATION |
| Create Role | `/roles-permissions` | POST | `/api/v1/roles` | `RoleRequest` | `RoleResponse` | ⚠️ NEEDS VERIFICATION |
| Update Role | `/roles-permissions/[id]` | PUT | `/api/v1/roles/{id}` | `RoleRequest` | `RoleResponse` | ⚠️ NEEDS VERIFICATION |
| Delete Role | `/roles-permissions/[id]` | DELETE | `/api/v1/roles/{id}` | N/A | `Void` | ⚠️ NEEDS VERIFICATION |

## Shifts
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| Get Current Shift | `/shifts/current` | GET | `/api/v1/shifts/current` | `{ branchId }` | `ShiftResponse` | ⚠️ NEEDS VERIFICATION |
| Open Shift | `/shifts/open` | POST | `/api/v1/shifts/open` | `ShiftRequest` | `ShiftResponse` | ⚠️ NEEDS VERIFICATION |
| Close Shift | `/shifts/current` | POST | `/api/v1/shifts/close` | `{ shiftId, closingAmount }` | `ShiftResponse` | ⚠️ NEEDS VERIFICATION |
| Get Shift History | `/shifts` | GET | `/api/v1/shifts` | `{ branchId, page, size }` | `PageResponse<ShiftResponse>` | ⚠️ NEEDS VERIFICATION |

## Payments
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| List All Payments | `/sales/payments` | GET | `/api/v1/payments` | `{ organizationId, branchId, page, size }` | `PageResponse<PaymentResponse>` | ⚠️ NEEDS VERIFICATION |
| Get by ID | `/sales/payments/[id]` | GET | `/api/v1/payments/{id}` | N/A | `PaymentResponse` | ⚠️ NEEDS VERIFICATION |

## Inventory
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| Get Branch Inventory | `/inventory` | GET | `/api/v1/inventory/branch/{branchId}` | `{ page, size }` | `PageResponse<BranchInventoryResponse>` | ⚠️ NEEDS VERIFICATION |
| Get Low Stock | `/inventory/low-stock` | GET | `/api/v1/inventory/low-stock` | `{ organizationId, branchId }` | `List<ProductResponse>` | ⚠️ NEEDS VERIFICATION |
| Get Expiring Products | `/inventory/expiring` | GET | `/api/v1/inventory/expiring` | `{ organizationId, branchId, days }` | `List<ProductBatchResponse>` | ⚠️ NEEDS VERIFICATION |
| Get Expired Products | `/inventory/expired` | GET | `/api/v1/inventory/expired` | `{ organizationId, branchId }` | `List<ProductBatchResponse>` | ⚠️ NEEDS VERIFICATION |

## Reports
| Feature | Frontend Route | HTTP Method | Backend Endpoint | Request | Response | Status |
|---|---|---|---|---|---|---|
| Sales Reports | `/reports/sales` | GET | `/api/v1/reports/sales` | `{ organizationId, branchId, startDate, endDate }` | `SalesReportResponse` | ⚠️ NEEDS VERIFICATION |
| Product Reports | `/reports/products` | GET | `/api/v1/reports/products` | `{ organizationId, branchId, startDate, endDate }` | `ProductReportResponse` | ⚠️ NEEDS VERIFICATION |
| Customer Reports | `/reports/customers` | GET | `/api/v1/reports/customers` | `{ organizationId, branchId, startDate, endDate }` | `CustomerReportResponse` | ⚠️ NEEDS VERIFICATION |
| Purchase Reports | `/reports/purchases` | GET | `/api/v1/reports/purchases` | `{ organizationId, branchId, startDate, endDate }` | `PurchaseReportResponse` | ⚠️ NEEDS VERIFICATION |
| Inventory Reports | `/reports/inventory` | GET | `/api/v1/reports/inventory` | `{ organizationId, branchId, startDate, endDate }` | `InventoryReportResponse` | ⚠️ NEEDS VERIFICATION |

## Status Legend
- ✅ MATCH - Frontend and backend contracts are aligned
- ⚠️ NEEDS VERIFICATION - Contract needs to be verified
- ❌ MISMATCH - Frontend and backend contracts do not match
- 🔧 FIX IN PROGRESS - Currently being fixed
- ❌ NOT CONNECTED - Frontend feature not connected to backend

## Notes
- Backend returns all responses wrapped in `ApiResponse<T>` with structure `{ success: boolean, data: T, message?: string }`
- Frontend API client automatically unwraps the `ApiResponse<T>` wrapper
- File uploads use `multipart/form-data` with field names `product`, `customer`, etc.
- Pagination uses Spring Data Pageable with parameters `page` and `size`
- Authentication uses JWT Bearer tokens stored in localStorage
- 401 errors trigger automatic token refresh via refresh token
