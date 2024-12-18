package fib.br10.utility;

import java.util.Objects;

public class PaginationUtil {

    public static final Long DEFAULT_PAGE_SIZE = 10L;
    public static final Long DEFAULT_PAGE_NUMBER = 0L;

    public static Long getPageSize(Long pageSize) {
        return Objects.isNull(pageSize) ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public static Long getPageNumber(Long pageNumber) {
        return Objects.isNull(pageNumber) ? DEFAULT_PAGE_NUMBER : pageNumber;
    }

    public static Long getOffset(Long lageSize, Long pageNumber) {
        return getPageNumber(pageNumber) * getPageSize(lageSize);
    }
}
