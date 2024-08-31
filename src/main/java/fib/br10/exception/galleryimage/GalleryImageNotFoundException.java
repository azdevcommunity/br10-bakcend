package fib.br10.exception.galleryimage;

import fib.br10.core.exception.NotFoundException;
import fib.br10.utility.Messages;

public class GalleryImageNotFoundException extends NotFoundException {
    public GalleryImageNotFoundException() {
        super(Messages.GALLERY_IMAGE_NOT_FOUND);
    }
}
