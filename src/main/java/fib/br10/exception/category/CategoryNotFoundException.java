package fib.br10.exception.category;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class CategoryNotFoundException extends BaseException {
    public CategoryNotFoundException(){
        super(Messages.CATEGORY_NOT_FOUND);
    }
}
