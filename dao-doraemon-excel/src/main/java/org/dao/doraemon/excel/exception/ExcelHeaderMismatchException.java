package org.dao.doraemon.excel.exception;

/**
 * Exception thrown when there is a mismatch in Excel headers during import.
 *
 * @author sucf
 * @create_time 2024/12/31 20:51
 */
public class ExcelHeaderMismatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String expectedHeaders;
    private final String actualHeaders;

    /**
     * Constructs a new ExcelHeaderMismatchException with the specified detail message.
     *
     * @param message         the detail message.
     * @param expectedHeaders the expected headers.
     * @param actualHeaders   the actual headers.
     */
    public ExcelHeaderMismatchException(String message, String expectedHeaders, String actualHeaders) {
        super(message);
        this.expectedHeaders = expectedHeaders;
        this.actualHeaders = actualHeaders;
    }

    /**
     * Constructs a new ExcelHeaderMismatchException with the specified cause.
     *
     * @param expectedHeaders the expected headers.
     * @param actualHeaders   the actual headers.
     */
    public ExcelHeaderMismatchException(String expectedHeaders, String actualHeaders) {
        super(String.format("Excel header mismatch: expected [%s], but found [%s]", expectedHeaders, actualHeaders));
        this.expectedHeaders = expectedHeaders;
        this.actualHeaders = actualHeaders;
    }

    public String getExpectedHeaders() {
        return expectedHeaders;
    }

    public String getActualHeaders() {
        return actualHeaders;
    }
}