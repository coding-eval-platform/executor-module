package ar.edu.itba.cep.executor_service.services;

import ar.edu.itba.cep.executor_service.models.ExecutionResult;
import ar.edu.itba.cep.executor_service.models.Language;

import java.util.List;

/**
 * A port into the application that allows requesting an execution.
 */
public interface ExecutorService {

    /**
     * Runs the given {@code code}, passing the given {@code inputs} as arguments.
     *
     * @param code     The code to be run.
     * @param inputs   The input arguments to be passed to the execution.
     * @param timeout  The time given to execute, in milliseconds..
     * @param language The programming language in which the {@code code} is written.
     * @return The {@link ExecutionResult} that came up of after executing the given {@code code}
     * with the given {@code inputs}.
     * @throws IllegalArgumentException If any argument is not valid.
     */
    ExecutionResult runCode(final String code, final List<String> inputs, final Long timeout, final Language language)
            throws IllegalArgumentException;
}
