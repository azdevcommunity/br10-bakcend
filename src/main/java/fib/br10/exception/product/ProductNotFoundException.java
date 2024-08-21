package fib.br10.exception.product;

import fib.br10.core.exception.NotFoundException;
import fib.br10.utility.Messages;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException() {
        super(Messages.PRODUCT_NOT_FOUND);
    }
}
