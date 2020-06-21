package com.company.salesProcessingApp.sales;

import com.company.salesProcessingApp.sales.SaleRecordPerProductType.Operation;
import com.company.salesProcessingApp.sales.SalesRecord.ProductType;

/**
 * Adjustment Record details class
 */
public class AdjustmentRecord implements IAdjustmentRecord {
    private ProductType productType;
    private double price;
    private Operation operation;

    public AdjustmentRecord(ProductType productType, Operation operation, double price) {
        this.productType = productType;
        this.operation = operation;
        this.price = price;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Operation getOperation() {
        return operation;
    }

    public double getPrice() {
        return price;
    }
}
