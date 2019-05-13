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
}
