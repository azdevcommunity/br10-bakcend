package fib.br10.exception.category;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class CategoryHaveProductException extends BaseException {
    public CategoryHaveProductException(){
        super(Messages.CATEGORY_HAVE_PRODUCT);
    }
}
