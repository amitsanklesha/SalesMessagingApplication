package com.company.salesProcessingApp.sales;

/**
 * SalesRecord details class
 */
public class SalesRecord extends SaleRecordPerProductType implements ISalesRecord {
    public enum ProductType {MICROWAVE, MOBILE};
    private Operation operation = Operation.NONE;
    private ProductType productType;

    /**
     * For Message Type 1
     * Use this constructor to specify the product sold (1 piece) and at what price.
     *
     * @param productType
     * @param price
     * @see ProductType
     */
    public SalesRecord(ProductType productType, double price) {
        this(productType, price, 1);
    }

    /**
     * For Message Type 2
     * Use this constructor to specify the quantity of the products sold (mentioned in ProductType) and at what price.
     *
     * @param productType
     * @param price
     * @param quantity
     */
    public SalesRecord(ProductType productType, double price, int quantity) {
        super(quantity, price);
        // Wanted to use Validate class from org.apache.commons.lang library but coz of max 2 dependencies restriction
        // the checks are written directly to throw exceptions if any issues.
        if (productType == null) {
            throw new RuntimeException("ProductType cannot be null");
        }
        this.productType = productType;
    }

    /**
     * For Message Type 3
     * Use this contructor to specify the adjustments to be done for a product and by what value/price?
     * Operation cannot be NONE.
     *
     * @param productType
     * @param operation
     * @param price
     * @see Operation
     */
    public SalesRecord(ProductType productType, Operation operation, double price) {
        this(productType, price);
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalesRecord salesRecord = (SalesRecord) o;
        return Double.compare(salesRecord.price, price) == 0 &&
                quantity == salesRecord.quantity &&
                operation == salesRecord.operation &&
                productType == salesRecord.productType;
    }

    @Override
    public int hashCode() {
        long temp;
        final int prime = 31;
        int result = 1;

        result = prime * result + ((productType == null) ? 0 : productType.hashCode());
        result = prime * result + ((operation == null) ? 0 : operation.hashCode());
        result = prime * result + quantity;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;
    }

    @Override
    public String toString() {
        return String.format("Sales details: ProductType: %s, Price: %f, Quantity: %d, Operation: %s", productType, price, quantity, operation);
    }

    public Operation getOperation() {
        return operation;
    }

    public ProductType getProductType() {
        return productType;
    }
}
