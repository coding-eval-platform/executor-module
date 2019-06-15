package ar.edu.itba.cep.executor_service.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link ExecutionRequest}.
 */
class ExecutionRequestTest {


    // ================================================================================================================
    // Acceptable arguments
    // ================================================================================================================

    /**
     * Tests that the constructor does not fail when invoking it with valid arguments.
     */
    @Test
    void testConstructorWithValidArguments() {
        Assertions.assertDoesNotThrow(
                () -> new ExecutionRequest(
                        TestHelper.validCode(),
                        TestHelper.validInputOutputList(),
                        TestHelper.validTimeout(),
                        TestHelper.validLanguage()
                ),
                "An execution request is not being created with acceptable arguments."
        );
    }


    // ================================================================================================================
    // Constraint testing
    // ================================================================================================================

    /**
     * Tests that creating an {@link ExecutionRequest} with a {@code null} {@code code} {@link String}
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void testNullCode() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ExecutionRequest(
                        null,
                        TestHelper.validInputOutputList(),
                        TestHelper.validTimeout(),
                        TestHelper.validLanguage()
                ),
                "Creating an execution request with a null code is being allowed."
        );
    }

    /**
     * Tests that creating an {@link ExecutionRequest} with a {@code null} {@code inputs} {@link java.util.List}
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void testNullInputs() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ExecutionRequest(
                        TestHelper.validCode(),
                        null,
                        TestHelper.validTimeout(),
                        TestHelper.validLanguage()
                ),
                "Creating an execution request with a null inputs list is being allowed."
        );
    }

    /**
     * Tests that creating an {@link ExecutionRequest} with a {@code null} element
     * in the {@code inputs} {@link java.util.List} throws an {@link IllegalArgumentException}.
     */
    @Test
    void testNullElementInInputsList() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ExecutionRequest(
                        TestHelper.validCode(),
                        TestHelper.inputOutputListWithNullElement(),
                        TestHelper.validTimeout(),
                        TestHelper.validLanguage()
                ),
                "Creating an execution request with a null element in the inputs list is being allowed."
        );
    }

    /**
     * Tests that creating an {@link ExecutionRequest} with a non positive timeout
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void testNonPositiveTimeout() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ExecutionRequest(
                        TestHelper.validCode(),
                        TestHelper.validInputOutputList(),
                        TestHelper.nonPositiveTimeout(),
                        TestHelper.validLanguage()
                ),
                "Creating an execution request with a null language is being allowed."
        );
    }

    /**
     * Tests that creating an {@link ExecutionRequest} with a {@code null} {@link Language}
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void testNullLanguage() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new ExecutionRequest(
                        TestHelper.validCode(),
                        TestHelper.validInputOutputList(),
                        TestHelper.validTimeout(),
                        null
                ),
                "Creating an execution request with a null language is being allowed."
        );
    }
}
