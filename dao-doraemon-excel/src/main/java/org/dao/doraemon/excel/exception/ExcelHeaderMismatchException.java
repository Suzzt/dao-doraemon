package org.dao.doraemon.excel.exception;

/**
 * Exception thrown when there is a mismatch in Excel headers during import.
 *
 * @author sucf
 * @since 1.0
 */
public class ExcelHeaderMismatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ExcelHeaderMismatchException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ExcelHeaderMismatchException(String message) {
        super(message);
    }
}