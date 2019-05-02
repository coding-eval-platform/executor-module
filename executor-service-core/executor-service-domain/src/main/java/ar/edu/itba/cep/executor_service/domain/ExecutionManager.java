package ar.edu.itba.cep.executor_service.domain;

import ar.edu.itba.cep.executor_service.models.ExecutionRequest;
import ar.edu.itba.cep.executor_service.models.ExecutionResult;
import ar.edu.itba.cep.executor_service.models.Language;
import ar.edu.itba.cep.executor_service.runner.CodeRunner;
import ar.edu.itba.cep.executor_service.services.ExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Juan Marcos Bellini on 2019-04-12.
 */
@Service
public class ExecutionManager implements ExecutorService {

    private final CodeRunner codeRunner;

    @Autowired
    public ExecutionManager(
            final CodeRunner codeRunner) {
        this.codeRunner = codeRunner;
    }

    @Override
    public ExecutionResult runCode(
            final String code,
            final List<String> inputs,
            final Long timeout,
            final Language language) throws IllegalArgumentException {
        return codeRunner.processExecutionRequest(new ExecutionRequest(code, inputs, timeout, language));
    }
}
