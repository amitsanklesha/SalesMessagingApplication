package com.company.salesProcessingApp.sales;

public class SaleRecordPerProductType implements ISalesRecordPerProductType {
    public enum Operation {NONE, ADD, SUBTRACT, MULTIPLY};
    public double price;
    public int quantity;

    public SaleRecordPerProductType(int quantity, double price) {
        // Wanted to use Validate class from org.apache.commons.lang library but coz of max 2 dependencies restriction
        // the checks are written directly to throw exceptions if any issues.
        if (quantity <= 0) {
            throw new RuntimeException("Quantity of a product cannot be negative or zero.");
        }
        if (price <= 0.0) {
            throw new RuntimeException("Price of a product cannot be negative or zero.");
        }
        this.quantity = quantity;
        this.price = price;
    }

    /*
    This API assists in adding price of any product as well as update the total price per product
    depending on the quantity of products sold for the said type.
     */
    public SaleRecordPerProductType update(SaleRecordPerProductType saleRecordPerProductType) {
        this.quantity = this.quantity + saleRecordPerProductType.quantity;

        if (saleRecordPerProductType.quantity > 1) {
            this.price = this.price + (saleRecordPerProductType.quantity * saleRecordPerProductType.price);
        } else {
            this.price = this.price + saleRecordPerProductType.price;
        }

        return new SaleRecordPerProductType(this.quantity, this.price);
    }

    /*
    This API assists in updating price of all products per type that are already sold.
     */
    public SaleRecordPerProductType update(Operation operation, double price) {
        if (operation == Operation.ADD) {
            this.price = this.price + (this.quantity * price);
        } else if (operation == Operation.MULTIPLY) {
            this.price = this.price * price;
        } else if (operation == Operation.SUBTRACT) {
            this.price = this.price - (this.quantity * price);
        }

        return new SaleRecordPerProductType(this.quantity, this.price);
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
