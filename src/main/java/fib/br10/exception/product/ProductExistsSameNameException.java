package fib.br10.exception.product;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class ProductExistsSameNameException extends BaseException {
    public ProductExistsSameNameException() {
        super(Messages.PRODUCT_EXISTS_SAME_NAME);
    }
}
