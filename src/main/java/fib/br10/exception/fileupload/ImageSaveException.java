package fib.br10.exception.fileupload;

import fib.br10.core.exception.BaseException;
import fib.br10.utility.Messages;

public class ImageSaveException extends BaseException {
    public ImageSaveException() {
        super(Messages.IMAGE_SAVE_EXCEPTION);
    }
}
