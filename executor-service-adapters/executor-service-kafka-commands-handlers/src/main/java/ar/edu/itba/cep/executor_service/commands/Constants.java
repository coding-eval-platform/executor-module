package ar.edu.itba.cep.executor_service.commands;

/**
 * Class with several constants to be used across the Kafka Command Handlers module.
 */
/* package */ class Constants {

    // ================================================================================================================
    // Headers
    // ================================================================================================================

    /**
     * The Reply-Channel header key. Is used to indicate where a response with an execution result must be sent.
     */
    /* package */ static final String REPLY_CHANNEL_HEADER = "Reply-Channel";


    // ================================================================================================================
    // Kafka Listening Topics
    // ================================================================================================================

    /**
     * Topic in which the {@link ar.edu.itba.cep.executor_service.models.ExecutionRequest}s commands are received.
     */
    /* package */ static final String EXECUTION_REQUEST_CHANNEL = "ExecutorService-Commands";
}
