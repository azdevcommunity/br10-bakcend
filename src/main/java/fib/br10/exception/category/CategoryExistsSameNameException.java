package fib.br10.exception.category;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class CategoryExistsSameNameException extends BaseException {

    public CategoryExistsSameNameException(){
        super(Messages.CATEGORY_EXISTS_SAME_NAME);
    }
}
