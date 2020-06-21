package com.company.salesProcessingApp.sales;

public interface IAdjustmentRecord {
    public SalesRecord.ProductType getProductType();

    public SaleRecordPerProductType.Operation getOperation();

    public double getPrice();
}
