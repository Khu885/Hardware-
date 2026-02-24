package com.hardwareaplications.hardware.examples;

/**
 * VALIDATION USAGE EXAMPLES
 *
 * This file demonstrates how the custom validation annotations work in practice.
 * These are examples - not actual test code.
 */

public class ValidationExamples {

    /*
     * EXAMPLE 1: Adding Inventory with Valid Product ID
     * ================================================
     *
     * Scenario: Product with ID 1 exists in database
     *
     * REQUEST:
     * POST /inventories
     * {
     *   "inventoryId": 5,
     *   "productId": 1,        // Product exists
     *   "quantity": 10,
     *   "location": "Warehouse A"
     * }
     *
     * RESULT: ✓ SUCCESS
     * - @Positive validates inventoryId = 5 (valid)
     * - @Positive validates productId = 1 (valid)
     * - @ExistsProduct checks database: productRepo.existsById(1) → true
     * - @PositiveOrZero validates quantity = 10 (valid)
     * - @NotBlank validates location = "Warehouse A" (valid)
     *
     * Response: 200 OK with inventory object
     */

    /*
     * EXAMPLE 2: Adding Inventory with Invalid Product ID
     * ==================================================
     *
     * Scenario: Product with ID 999 does NOT exist in database
     *
     * REQUEST:
     * POST /inventories
     * {
     *   "inventoryId": 6,
     *   "productId": 999,      // Product does NOT exist
     *   "quantity": 5,
     *   "location": "Store B"
     * }
     *
     * RESULT: ✗ VALIDATION FAILURE
     * - @Positive validates inventoryId = 6 (valid)
     * - @Positive validates productId = 999 (valid)
     * - @ExistsProduct checks database: productRepo.existsById(999) → false
     * - Validation FAILS
     *
     * Response: 400 Bad Request
     * {
     *   "productId": "Product with this ID does not exist in the database"
     * }
     */

    /*
     * EXAMPLE 3: Adding Order with Valid Customer ID
     * ==============================================
     *
     * Scenario: Customer with ID 1 exists in database
     *
     * REQUEST:
     * POST /orders
     * {
     *   "orderId": 10,
     *   "customerId": 1,       // Customer exists
     *   "orderDate": "2026-02-09",
     *   "totalAmount": 1500.00,
     *   "Quantity": 5
     * }
     *
     * RESULT: ✓ SUCCESS
     * - @Positive validates orderId = 10 (valid)
     * - @Positive validates customerId = 1 (valid)
     * - @ExistsCustomer checks database: customerRepo.existsById(1) → true
     * - @NotBlank validates orderDate (valid)
     * - @PositiveOrZero validates totalAmount = 1500.00 (valid)
     * - @Positive validates Quantity = 5 (valid)
     *
     * Response: 200 OK with order object
     */

    /*
     * EXAMPLE 4: Adding Order with Invalid Customer ID
     * ===============================================
     *
     * Scenario: Customer with ID 777 does NOT exist in database
     *
     * REQUEST:
     * POST /orders
     * {
     *   "orderId": 11,
     *   "customerId": 777,     // Customer does NOT exist
     *   "orderDate": "2026-02-10",
     *   "totalAmount": 2000.00,
     *   "Quantity": 3
     * }
     *
     * RESULT: ✗ VALIDATION FAILURE
     * - @Positive validates orderId = 11 (valid)
     * - @Positive validates customerId = 777 (valid)
     * - @ExistsCustomer checks database: customerRepo.existsById(777) → false
     * - Validation FAILS
     *
     * Response: 400 Bad Request
     * {
     *   "customerId": "Customer with this ID does not exist in the database"
     * }
     */

    /*
     * EXAMPLE 5: Updating Inventory with Invalid Product ID
     * ====================================================
     *
     * Scenario: Trying to update inventory to reference non-existent product
     *
     * REQUEST:
     * PUT /inventories
     * {
     *   "inventoryId": 1,
     *   "productId": 500,      // Product does NOT exist
     *   "quantity": 20,
     *   "location": "Updated Location"
     * }
     *
     * RESULT: ✗ VALIDATION FAILURE
     * - @ExistsProduct checks database: productRepo.existsById(500) → false
     * - Validation FAILS before update
     *
     * Response: 400 Bad Request
     * {
     *   "productId": "Product with this ID does not exist in the database"
     * }
     */

    /*
     * EXAMPLE 6: Multiple Validation Failures
     * ======================================
     *
     * Scenario: Multiple fields fail validation
     *
     * REQUEST:
     * POST /inventories
     * {
     *   "inventoryId": -1,     // Negative (invalid)
     *   "productId": 888,      // Does not exist (invalid)
     *   "quantity": -5,        // Negative (invalid)
     *   "location": ""         // Blank (invalid)
     * }
     *
     * RESULT: ✗ MULTIPLE VALIDATION FAILURES
     * - @Positive fails for inventoryId = -1
     * - @Positive fails for productId = -1 (never checks @ExistsProduct)
     * - @PositiveOrZero fails for quantity = -5
     * - @NotBlank fails for location = ""
     *
     * Response: 400 Bad Request
     * {
     *   "inventoryId": "inventoryId must be a positive number",
     *   "productId": "productId must be a positive number",
     *   "quantity": "quantity must be zero or positive",
     *   "location": "location must not be blank"
     * }
     */

    /*
     * VALIDATION EXECUTION ORDER
     * =========================
     *
     * 1. Standard validations run first (@Positive, @NotBlank, etc.)
     * 2. If standard validations pass, custom validators run (@ExistsProduct, @ExistsCustomer)
     * 3. Custom validators query the database via repositories
     * 4. If any validation fails, the method is NOT executed
     * 5. Error messages are returned to the client
     *
     * IMPORTANT NOTES
     * ==============
     *
     * - Validators are Spring beans, so they can inject repositories
     * - Validation occurs automatically when @Valid is used in the controller
     * - The validation happens BEFORE the service method is called
     * - This prevents invalid data from reaching your business logic
     * - Database queries in validators are minimal (just existsById checks)
     */

    /*
     * HOW TO TEST THIS IN POSTMAN/CURL
     * ================================
     *
     * Step 1: Add a Product first
     * POST http://localhost:8080/products
     * {
     *   "ProductId": 1,
     *   "ProductName": "Hammer",
     *   "ProductBrand": "Stanley",
     *   "ProductColour": "Red",
     *   "ProductSize": "Medium",
     *   "ProductType": "Tool"
     * }
     *
     * Step 2: Add Inventory with valid productId
     * POST http://localhost:8080/inventories
     * {
     *   "inventoryId": 1,
     *   "productId": 1,
     *   "quantity": 100,
     *   "location": "Main Warehouse"
     * }
     * Result: SUCCESS ✓
     *
     * Step 3: Try to add Inventory with invalid productId
     * POST http://localhost:8080/inventories
     * {
     *   "inventoryId": 2,
     *   "productId": 999,
     *   "quantity": 50,
     *   "location": "Store Front"
     * }
     * Result: FAILURE ✗ - "Product with this ID does not exist in the database"
     *
     * Step 4: Add a Customer first
     * POST http://localhost:8080/customers
     * {
     *   "CustomerId": 1,
     *   "Customername": "John Doe",
     *   "PhoneNo": 1234567890,
     *   "address": "123 Main St",
     *   "due_amt": 0
     * }
     *
     * Step 5: Add Order with valid customerId
     * POST http://localhost:8080/orders
     * {
     *   "orderId": 1,
     *   "customerId": 1,
     *   "orderDate": "2026-02-09",
     *   "totalAmount": 500.00,
     *   "Quantity": 10
     * }
     * Result: SUCCESS ✓
     *
     * Step 6: Try to add Order with invalid customerId
     * POST http://localhost:8080/orders
     * {
     *   "orderId": 2,
     *   "customerId": 555,
     *   "orderDate": "2026-02-10",
     *   "totalAmount": 300.00,
     *   "Quantity": 5
     * }
     * Result: FAILURE ✗ - "Customer with this ID does not exist in the database"
     */
}
