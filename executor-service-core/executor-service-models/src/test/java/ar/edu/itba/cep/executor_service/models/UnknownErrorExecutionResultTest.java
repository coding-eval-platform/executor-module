package ar.edu.itba.cep.executor_service.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for {@link UnknownErrorExecutionResult}.
 */
@ExtendWith(MockitoExtension.class)
class UnknownErrorExecutionResultTest {

    // ================================================================================================================
    // Acceptable arguments
    // ================================================================================================================

    /**
     * Tests that the constructor does not fail when invoking it with valid arguments.
     *
     * @param mockedRequest A mocked {@link ExecutionRequest} to be passed to the
     *                      {@link UnknownErrorExecutionResult#UnknownErrorExecutionResult(ExecutionRequest)}
     *                      constructor.
     */
    @Test
    void testConstructorWithValidArguments(@Mock final ExecutionRequest mockedRequest) {
        Assertions.assertDoesNotThrow(
                () -> new UnknownErrorExecutionResult(mockedRequest),
                "An unknown error execution result is not being created with acceptable arguments."
        );
    }


    // ================================================================================================================
    // Constraint testing
    // ================================================================================================================

    /**
     * Tests that creating a {@link UnknownErrorExecutionResult} with a {@code null} {@link ExecutionRequest}
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void testNullExecutionRequest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new UnknownErrorExecutionResult(null),
                "Creating an unknown error execution result with a null execution request is being allowed."
        );
    }
}
