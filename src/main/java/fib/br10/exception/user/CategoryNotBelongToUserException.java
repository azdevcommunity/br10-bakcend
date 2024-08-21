package fib.br10.exception.user;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class CategoryNotBelongToUserException extends BaseException {
    public CategoryNotBelongToUserException() {
        super(Messages.CATEGORY_NOT_BELONG_TO_USER);
    }
}
