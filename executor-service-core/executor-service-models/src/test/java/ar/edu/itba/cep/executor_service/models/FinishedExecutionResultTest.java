package ar.edu.itba.cep.executor_service.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Test class for {@link FinishedExecutionResult}
 */
@ExtendWith(MockitoExtension.class)
class FinishedExecutionResultTest {

    // ================================================================================================================
    // Acceptable arguments
    // ================================================================================================================

    /**
     * Tests that the constructor does not fail when invoking it with valid arguments.
     *
     * @param mockedExecutionRequest A mocked {@link ExecutionRequest} to be passed to the
     *                               {@link FinishedExecutionResult#FinishedExecutionResult(int, List, List, ExecutionRequest)}
     *                               constructor.
     */
    @Test
    void testConstructorWithValidArguments(@Mock final ExecutionRequest mockedExecutionRequest) {
        Assertions.assertDoesNotThrow(
                () -> new FinishedExecutionResult(
                        TestHelper.validExitCode(),
                        TestHelper.validInputOutputList(),
                        TestHelper.validInputOutputList(),
                        mockedExecutionRequest
                ),
                "A finished execution result is not being created with acceptable arguments."
        );
        Mockito.verifyZeroInteractions(mockedExecutionRequest);
    }


    // ================================================================================================================
    // Constraint testing
    // ================================================================================================================

    /**
     * Tests that creating an {@link ExecutionResult} with a {@code null} {@code stdout} {@link java.util.List}
     * throws an {@link IllegalArgumentException}.
     *
     * @param mockedExecutionRequest A mocked {@link ExecutionRequest} to be passed to the
     *                               {@link FinishedExecutionResult#FinishedExecutionResult(int, List, List, ExecutionRequest)}
     *                               constructor.
     */
    @Test
    void testNullStdout(@Mock final ExecutionRequest mockedExecutionRequest) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FinishedExecutionResult(
                        TestHelper.validExitCode(),
                        null,
                        TestHelper.validInputOutputList(),
                        mockedExecutionRequest
                ),
                "Creating a finished execution result with a null stdout list is being allowed."
        );
        Mockito.verifyZeroInteractions(mockedExecutionRequest);
    }

    /**
     * Tests that creating an {@link FinishedExecutionResult} with a {@code null} element
     * in the {@code stdout} {@link java.util.List} throws an {@link IllegalArgumentException}.
     *
     * @param mockedExecutionRequest A mocked {@link ExecutionRequest} to be passed to the
     *                               {@link FinishedExecutionResult#FinishedExecutionResult(int, List, List, ExecutionRequest)}
     *                               constructor.
     */
    @Test
    void testNullElementInStdout(@Mock final ExecutionRequest mockedExecutionRequest) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FinishedExecutionResult(
                        TestHelper.validExitCode(),
                        TestHelper.inputOutputListWithNullElement(),
                        TestHelper.validInputOutputList(),
                        mockedExecutionRequest
                ),
                "Creating a finished execution result with a null element in the stdout list is being allowed."
        );
        Mockito.verifyZeroInteractions(mockedExecutionRequest);
    }

    /**
     * Tests that creating an {@link FinishedExecutionResult} with a {@code null} {@code stderr} {@link java.util.List}
     * throws an {@link IllegalArgumentException}.
     *
     * @param mockedExecutionRequest A mocked {@link ExecutionRequest} to be passed to the
     *                               {@link FinishedExecutionResult#FinishedExecutionResult(int, List, List, ExecutionRequest)}
     *                               constructor.
     */
    @Test
    void testNullStderr(@Mock final ExecutionRequest mockedExecutionRequest) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FinishedExecutionResult(
                        TestHelper.validExitCode(),
                        TestHelper.validInputOutputList(),
                        null,
                        mockedExecutionRequest
                ),
                "Creating a finished execution result with a null stderr list is being allowed."
        );
        Mockito.verifyZeroInteractions(mockedExecutionRequest);
    }

    /**
     * Tests that creating an {@link FinishedExecutionResult} with a {@code null} element
     * in the {@code stderr} {@link java.util.List} throws an {@link IllegalArgumentException}.
     *
     * @param mockedExecutionRequest A mocked {@link ExecutionRequest} to be passed to the
     *                               {@link FinishedExecutionResult#FinishedExecutionResult(int, List, List, ExecutionRequest)}
     *                               constructor.
     */
    @Test
    void testNullElementInStderr(@Mock final ExecutionRequest mockedExecutionRequest) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FinishedExecutionResult(
                        TestHelper.validExitCode(),
                        TestHelper.validInputOutputList(),
                        TestHelper.inputOutputListWithNullElement(),
                        mockedExecutionRequest
                ),
                "Creating a finished execution result with a null element in the stderr list is being allowed."
        );
        Mockito.verifyZeroInteractions(mockedExecutionRequest);
    }

    /**
     * Tests that creating a {@link FinishedExecutionResult} with a {@code null} {@link ExecutionRequest}
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void testNullExecutionRequest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FinishedExecutionResult(
                        TestHelper.validExitCode(),
                        TestHelper.validInputOutputList(),
                        TestHelper.validInputOutputList(),
                        null
                ),
                "Creating a finished execution result with a null execution request is being allowed."
        );
    }
}
