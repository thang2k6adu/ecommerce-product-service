# API CONTRACT - Product Service

Base URL: `http://localhost:8082`

No API version prefix is used at the moment.

## 1. Common Conventions

### Authentication and Authorization

- Public GET endpoints:
  - `GET /api/products/**`
  - `GET /api/categories/**`
- Authenticated endpoints:
  - All `/api/cart/**` endpoints currently require JWT because they are not whitelisted in `SecurityConfig`.
- ADMIN only endpoints:
  - `POST /api/products`
  - `PUT /api/products/{id}`
  - `DELETE /api/products/{id}`
  - `POST /api/products/{id}/images/upload`
  - `POST /api/products/sync-elasticsearch`
  - `POST /api/categories`
  - `PUT /api/categories/{id}`
  - `DELETE /api/categories/{id}`
  - `POST /api/brands`
  - `PUT /api/brands/{id}`
  - `DELETE /api/brands/{id}`

Bearer token header:

```http
Authorization: Bearer <JWT_TOKEN>
```

### Standard Response Envelope

All normal JSON responses use `ApiResponse<T>`:

```json
{
  "success": true,
  "message": "optional",
  "data": {},
  "error": null
}
```

Success responses may omit `message`.

### Error Response Format

Errors are also wrapped by `ApiResponse`, except some security-layer 401 responses that are generated before controller handling.

Common error examples:

```json
{
  "success": false,
  "error": "Product not found with id: 123"
}
```

Validation errors:

```json
{
  "success": false,
  "error": "Validation failed",
  "data": {
    "name": "Product name is required",
    "price": "Price is required"
  }
}
```

### Pagination Envelope

Paged endpoints return `PageResponse<T>`:

```json
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 0,
  "totalPages": 0,
  "last": true,
  "first": true
}
```

### Status Codes

- `200 OK` - successful read/update/delete actions
- `201 Created` - resource created successfully
- `204 No Content` - cart item removed or cart cleared
- `400 Bad Request` - validation or business rule failure
- `401 Unauthorized` - missing or invalid JWT
- `403 Forbidden` - authenticated but not allowed
- `404 Not Found` - resource does not exist
- `500 Internal Server Error` - unexpected failure

### Versioning

No `/api/v1` or other version prefix is defined yet.

## 2. Product Module

### Product Response Shape

`ProductResponse` fields:

- `id`: UUID
- `name`: string
- `slug`: string
- `description`: string or null
- `shortDescription`: string or null
- `price`: decimal
- `compareAtPrice`: decimal or null
- `costPrice`: decimal or null
- `category`: `CategoryResponse` or null
- `brand`: `BrandResponse` or null
- `status`: `DRAFT | PUBLISHED | ARCHIVED | OUT_OF_STOCK`
- `published`: boolean or null
- `featured`: boolean or null
- `stockQuantity`: integer or null
- `sku`: string or null
- `barcode`: string or null
- `weight`: decimal or null
- `weightUnit`: string or null
- `length`: decimal or null
- `width`: decimal or null
- `height`: decimal or null
- `dimensionUnit`: string or null
- `metaTitle`: string or null
- `metaDescription`: string or null
- `metaKeywords`: string or null
- `images`: array of `ProductImageResponse`
- `variants`: array of `ProductVariantResponse`
- `createdAt`: date-time
- `updatedAt`: date-time
- `publishedAt`: date-time or null

### Create / Update Product Request

`CreateProductRequest` is used for both create and update.

Request body fields:

- `name`: required string, max 200
- `slug`: optional string, max 220
- `description`: optional string
- `shortDescription`: optional string
- `price`: required decimal, must be greater than 0
- `compareAtPrice`: optional decimal
- `costPrice`: optional decimal
- `categoryId`: optional UUID
- `brandId`: optional UUID
- `status`: optional enum `DRAFT | PUBLISHED | ARCHIVED | OUT_OF_STOCK`
- `published`: optional boolean
- `featured`: optional boolean
- `stockQuantity`: optional integer, must be >= 0
- `sku`: optional string, max 50
- `barcode`: optional string
- `weight`: optional decimal
- `weightUnit`: optional string
- `length`: optional decimal
- `width`: optional decimal
- `height`: optional decimal
- `dimensionUnit`: optional string
- `metaTitle`: optional string, max 150
- `metaDescription`: optional string, max 300
- `metaKeywords`: optional string, max 200
- `images`: optional array of image objects
- `variants`: optional array of variant objects

Nested image object:

- `imageUrl`: optional string
- `altText`: optional string
- `isPrimary`: optional boolean
- `displayOrder`: optional integer

Nested variant object:

- `sku`: required string
- `size`: optional string
- `color`: optional string
- `material`: optional string
- `style`: optional string
- `stockQuantity`: optional integer, must be >= 0
- `priceAdjustment`: optional decimal
- `imageUrl`: optional string
- `weight`: optional decimal
- `barcode`: optional string
- `available`: optional boolean

### POST /api/products

- Auth: ADMIN only
- Content-Type: `application/json`
- Body: `CreateProductRequest`
- Success: `201 Created`
- Response: `ApiResponse<ProductResponse>`

Example:

```http
POST /api/products
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

```json
{
  "name": "iPhone 15 Pro",
  "slug": "iphone-15-pro",
  "price": 999.0,
  "categoryId": "550e8400-e29b-41d4-a716-446655440000",
  "brandId": "660e8400-e29b-41d4-a716-446655440000",
  "status": "PUBLISHED",
  "published": true,
  "featured": true,
  "stockQuantity": 50
}
```

### GET /api/products/{id}

- Auth: public
- Path variable: `id` as UUID
- Success: `200 OK`
- Response: `ApiResponse<ProductResponse>`

### GET /api/products/slug/{slug}

- Auth: public
- Path variable: `slug` as string
- Success: `200 OK`
- Response: `ApiResponse<ProductResponse>`

### GET /api/products

- Auth: public
- Query params:
  - `page` default `0`
  - `size` default `20`
  - `sortBy` default `createdAt`
  - `sortDirection` default `desc`
- Success: `200 OK`
- Response: `ApiResponse<PageResponse<ProductResponse>>`

### GET /api/products/published

- Auth: public
- Query params:
  - `page` default `0`
  - `size` default `20`
  - `sortBy` default `createdAt`
  - `sortDirection` default `desc`
- Success: `200 OK`
- Response: `ApiResponse<PageResponse<ProductResponse>>`

### GET /api/products/category/{categoryId}

- Auth: public
- Path variable: `categoryId` as UUID
- Query params:
  - `page` default `0`
  - `size` default `20`
- Success: `200 OK`
- Response: `ApiResponse<PageResponse<ProductResponse>>`

### GET /api/products/brand/{brandId}

- Auth: public
- Path variable: `brandId` as UUID
- Query params:
  - `page` default `0`
  - `size` default `20`
- Success: `200 OK`
- Response: `ApiResponse<PageResponse<ProductResponse>>`

### GET /api/products/featured

- Auth: public
- Query params:
  - `page` default `0`
  - `size` default `20`
- Success: `200 OK`
- Response: `ApiResponse<PageResponse<ProductResponse>>`

### GET /api/products/price-range

- Auth: public
- Query params:
  - `minPrice` required decimal
  - `maxPrice` required decimal
  - `page` default `0`
  - `size` default `20`
- Success: `200 OK`
- Response: `ApiResponse<PageResponse<ProductResponse>>`

### PUT /api/products/{id}

- Auth: ADMIN only
- Path variable: `id` as UUID
- Body: `CreateProductRequest`
- Success: `200 OK`
- Response: `ApiResponse<ProductResponse>`

### DELETE /api/products/{id}

- Auth: ADMIN only
- Path variable: `id` as UUID
- Success: `200 OK`
- Response: `ApiResponse<Void>`

### POST /api/products/{id}/images/upload

- Auth: ADMIN only
- Path variable: `id` as UUID
- Request: `multipart/form-data`
- Form field:
  - `file`: required `MultipartFile`
- Success: `200 OK`
- Response: `ApiResponse<String>` containing uploaded image URL

### POST /api/products/sync-elasticsearch

- Auth: ADMIN only
- No request body
- Success: `200 OK`
- Response: `ApiResponse<Void>`

## 3. Category Module

### Category Response Shape

`CategoryResponse` fields:

- `id`: UUID
- `name`: string
- `slug`: string
- `description`: string or null
- `parentId`: UUID or null
- `parentName`: string or null
- `children`: recursive array of `CategoryResponse`
- `active`: boolean or null
- `displayOrder`: integer or null
- `imageUrl`: string or null
- `metaTitle`: string or null
- `metaDescription`: string or null
- `metaKeywords`: string or null
- `createdAt`: date-time
- `updatedAt`: date-time

### Create / Update Category Request

`CreateCategoryRequest` fields:

- `name`: required string, max 100
- `slug`: optional string, max 120
- `description`: optional string
- `parentId`: optional UUID
- `active`: optional boolean
- `displayOrder`: optional integer
- `imageUrl`: optional string
- `metaTitle`: optional string, max 150
- `metaDescription`: optional string, max 300
- `metaKeywords`: optional string, max 200

### POST /api/categories

- Auth: ADMIN only
- Content-Type: `application/json`
- Body: `CreateCategoryRequest`
- Success: `201 Created`
- Response: `ApiResponse<CategoryResponse>`

### GET /api/categories

- Auth: public
- Success: `200 OK`
- Response: `ApiResponse<List<CategoryResponse>>`

### GET /api/categories/active

- Auth: public
- Success: `200 OK`
- Response: `ApiResponse<List<CategoryResponse>>`

### GET /api/categories/tree

- Auth: public
- Success: `200 OK`
- Response: `ApiResponse<List<CategoryResponse>>`

### GET /api/categories/root

- Auth: public
- Success: `200 OK`
- Response: `ApiResponse<List<CategoryResponse>>`

### GET /api/categories/{id}

- Auth: public
- Path variable: `id` as UUID
- Success: `200 OK`
- Response: `ApiResponse<CategoryResponse>`

### GET /api/categories/slug/{slug}

- Auth: public
- Path variable: `slug` as string
- Success: `200 OK`
- Response: `ApiResponse<CategoryResponse>`

### PUT /api/categories/{id}

- Auth: ADMIN only
- Path variable: `id` as UUID
- Body: `CreateCategoryRequest`
- Success: `200 OK`
- Response: `ApiResponse<CategoryResponse>`

### DELETE /api/categories/{id}

- Auth: ADMIN only
- Path variable: `id` as UUID
- Success: `200 OK`
- Response: `ApiResponse<Void>`

## 4. Brand Module

### Brand Response Shape

`BrandResponse` fields:

- `id`: UUID
- `name`: string
- `slug`: string
- `description`: string or null
- `logoUrl`: string or null
- `websiteUrl`: string or null
- `active`: boolean or null
- `metaTitle`: string or null
- `metaDescription`: string or null
- `createdAt`: date-time
- `updatedAt`: date-time

### Create / Update Brand Request

`CreateBrandRequest` fields:

- `name`: required string, max 100
- `slug`: optional string, max 120
- `description`: optional string
- `logoUrl`: optional string
- `websiteUrl`: optional string
- `active`: optional boolean
- `metaTitle`: optional string, max 150
- `metaDescription`: optional string, max 300

### POST /api/brands

- Auth: ADMIN only
- Content-Type: `application/json`
- Body: `CreateBrandRequest`
- Success: `201 Created`
- Response: `ApiResponse<BrandResponse>`

### GET /api/brands

- Auth: public
- Success: `200 OK`
- Response: `ApiResponse<List<BrandResponse>>`

### GET /api/brands/active

- Auth: public
- Success: `200 OK`
- Response: `ApiResponse<List<BrandResponse>>`

### GET /api/brands/{id}

- Auth: public
- Path variable: `id` as UUID
- Success: `200 OK`
- Response: `ApiResponse<BrandResponse>`

### GET /api/brands/slug/{slug}

- Auth: public
- Path variable: `slug` as string
- Success: `200 OK`
- Response: `ApiResponse<BrandResponse>`

### PUT /api/brands/{id}

- Auth: ADMIN only
- Path variable: `id` as UUID
- Body: `CreateBrandRequest`
- Success: `200 OK`
- Response: `ApiResponse<BrandResponse>`

### DELETE /api/brands/{id}

- Auth: ADMIN only
- Path variable: `id` as UUID
- Success: `200 OK`
- Response: `ApiResponse<Void>`

## 5. Search Module

### Availability

`GET /api/products/search` is only active when `app.search.enabled=true`.

### Search Response Shape

The search API returns `PageResponse<ProductDocument>`.

`ProductDocument` fields:

- `id`: string
- `name`: string
- `slug`: string
- `description`: string or null
- `shortDescription`: string or null
- `price`: decimal or null
- `categoryId`: string or null
- `categoryName`: string or null
- `categorySlug`: string or null
- `brandId`: string or null
- `brandName`: string or null
- `brandSlug`: string or null
- `status`: string or null
- `published`: boolean or null
- `featured`: boolean or null
- `stockQuantity`: integer or null
- `sku`: string or null
- `imageUrls`: array of strings
- `createdAt`: date-time or null
- `updatedAt`: date-time or null

### GET /api/products/search

- Auth: public
- Query params:
  - `keyword`: optional string, full-text search over name and description
  - `categoryId`: optional UUID string
  - `brandId`: optional UUID string
  - `minPrice`: optional decimal string
  - `maxPrice`: optional decimal string
  - `featured`: optional boolean
  - `page`: default `0`
  - `size`: default `20`
  - `sortBy`: default `createdAt`
  - `sortDirection`: default `desc`
- Success: `200 OK`
- Response: `ApiResponse<PageResponse<ProductDocument>>`

Notes:

- Invalid UUID or decimal query values will fail with `400 Bad Request`.
- The search response is an Elasticsearch document view, not `ProductResponse`.

## 6. Cart Module

### Cart Response Shape

`CartResponse` fields:

- `id`: UUID
- `userId`: string or null
- `sessionId`: string or null
- `status`: string
- `items`: array of `CartItemResponse`
- `totalItems`: integer or null
- `subtotal`: decimal or null
- `discount`: decimal or null
- `total`: decimal or null
- `createdAt`: date-time or null
- `updatedAt`: date-time or null

`CartItemResponse` fields:

- `id`: UUID
- `productId`: string
- `productVariantId`: string or null
- `productName`: string
- `productImageUrl`: string or null
- `quantity`: integer
- `price`: decimal
- `total`: decimal
- `createdAt`: date-time

`CartSummaryResponse` fields:

- `totalItems`: integer
- `subtotal`: decimal
- `discount`: decimal
- `shipping`: decimal
- `tax`: decimal
- `total`: decimal

### AddToCartRequest

- `productId`: required string, cannot be blank
- `quantity`: required integer, min `1`, max `100`
- `productVariantId`: optional string

### UpdateCartItemRequest

- `quantity`: required integer, min `0`, max `100`
- Note: `quantity = 0` removes the cart item in the current implementation.

### MergeCartRequest

- `guestSessionId`: required string, cannot be blank

### GET /api/cart

- Auth: JWT required
- Success: `200 OK`
- Response: `ApiResponse<CartResponse>`

### GET /api/cart/guest/{sessionId}

- Auth: JWT required in current security configuration
- Path variable: `sessionId` as string
- Success: `200 OK`
- Response: `ApiResponse<CartResponse>`

### POST /api/cart/items

- Auth: JWT required
- Content-Type: `application/json`
- Body: `AddToCartRequest`
- Success: `201 Created`
- Response: `ApiResponse<CartResponse>`

Business errors:

- `404 Not Found` when the product does not exist
- `400 Bad Request` when the product is out of stock
- `400 Bad Request` when requested quantity exceeds available stock

### PUT /api/cart/items/{itemId}

- Auth: JWT required
- Path variable: `itemId` as UUID
- Content-Type: `application/json`
- Body: `UpdateCartItemRequest`
- Success: `200 OK`
- Response: `ApiResponse<CartResponse>`

### DELETE /api/cart/items/{itemId}

- Auth: JWT required
- Path variable: `itemId` as UUID
- Success: `204 No Content`
- Response: no body

### DELETE /api/cart

- Auth: JWT required
- Success: `204 No Content`
- Response: no body

### GET /api/cart/summary

- Auth: JWT required
- Success: `200 OK`
- Response: `ApiResponse<CartSummaryResponse>`

### POST /api/cart/merge

- Auth: JWT required
- Content-Type: `application/json`
- Body: `MergeCartRequest`
- Success: `200 OK`
- Response: `ApiResponse<CartResponse>`

## 7. Contract Notes

- `GET /api/products/**` and `GET /api/categories/**` are public by security configuration.
- Product write endpoints use `@PreAuthorize("hasRole('ADMIN')")`, so ADMIN is the effective role requirement in the current code.
- Search is read-only and public, but depends on the `app.search.enabled` flag.
- Cart delete endpoints return `204 No Content` instead of `ApiResponse`.
- If you want, this contract can be converted into a formal OpenAPI/Swagger file next.