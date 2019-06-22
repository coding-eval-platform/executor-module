package ar.edu.itba.cep.executor_service.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

/**
 * Test class for {@link CompileErrorExecutionResult}
 */
@ExtendWith(MockitoExtension.class)
class CompiledErrorExecutionResultTest {

    // ================================================================================================================
    // Acceptable arguments
    // ================================================================================================================

    /**
     * Tests that the constructor does not fail when invoking it with valid arguments.
     *
     * @param mockedRequest A mocked {@link ExecutionRequest} to be passed to the
     *                      {@link CompileErrorExecutionResult#CompileErrorExecutionResult(List, ExecutionRequest)}
     *                      constructor.
     */
    @Test
    void testConstructorWithValidArguments(@Mock final ExecutionRequest mockedRequest) {
        Mockito.when(mockedRequest.getLanguage()).thenReturn(TestHelper.compiledLanguage());
        Assertions.assertDoesNotThrow(
                () -> new CompileErrorExecutionResult(
                        TestHelper.validInputOutputList(),
                        mockedRequest
                ),
                "A compile error execution result is not being created with acceptable arguments."
        );
        Mockito.verify(mockedRequest, Mockito.only()).getLanguage();
    }


    // ================================================================================================================
    // Constraint testing
    // ================================================================================================================


    /**
     * Tests that creating an {@link CompileErrorExecutionResult} with a {@code null} {@code compilerErrors}
     * {@link List} throws an {@link IllegalArgumentException}.
     *
     * @param mockedRequest A mocked {@link ExecutionRequest} to be passed to the
     *                      {@link CompileErrorExecutionResult#CompileErrorExecutionResult(List, ExecutionRequest)}
     *                      constructor.
     */
    @Test
    void testNullCompilerErrorList(@Mock final ExecutionRequest mockedRequest) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CompileErrorExecutionResult(
                        null,
                        mockedRequest
                ),
                "Creating a compile error execution result with a null compiler errors list is being allowed."
        );
        Mockito.verifyZeroInteractions(mockedRequest);
    }

    /**
     * Tests that creating an {@link CompileErrorExecutionResult} with a {@code null} element
     * in the {@code compilerErrors} {@link List} throws an {@link IllegalArgumentException}.
     *
     * @param mockedRequest A mocked {@link ExecutionRequest} to be passed to the
     *                      {@link CompileErrorExecutionResult#CompileErrorExecutionResult(List, ExecutionRequest)}
     *                      constructor.
     */
    @Test
    void testNullElementInCompilerErrorList(@Mock final ExecutionRequest mockedRequest) {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CompileErrorExecutionResult(
                        TestHelper.inputOutputListWithNullElement(),
                        mockedRequest
                ),
                "Creating a compile error execution result with a null element in the compiler errors list" +
                        " is being allowed."
        );
        Mockito.verifyZeroInteractions(mockedRequest);
    }

    /**
     * Tests that creating a {@link CompileErrorExecutionResult} with a {@code null} {@link ExecutionRequest}
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void testNullExecutionRequest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CompileErrorExecutionResult(
                        TestHelper.validInputOutputList(),
                        null
                ),
                "Creating a compile error execution result with a null execution request is being allowed."
        );
    }

    /**
     * Tests that creating a {@link CompileErrorExecutionResult} with an {@link ExecutionRequest}
     * containing a non compiled {@link Language} throws an {@link IllegalArgumentException}.
     *
     * @param mockedRequest A mocked {@link ExecutionRequest} to be passed to the
     *                      {@link CompileErrorExecutionResult#CompileErrorExecutionResult(List, ExecutionRequest)}
     *                      constructor.
     */
    @Test
    void testNonCompiledLanguage(@Mock final ExecutionRequest mockedRequest) {
        Mockito.when(mockedRequest.getLanguage()).thenReturn(TestHelper.nonCompiledLanguage());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new CompileErrorExecutionResult(
                        TestHelper.validInputOutputList(),
                        mockedRequest
                ),
                "Creating a compile error execution result with an execution request" +
                        " for a non-compiled language is being allowed."
        );
        Mockito.verify(mockedRequest, Mockito.only()).getLanguage();
    }
}
