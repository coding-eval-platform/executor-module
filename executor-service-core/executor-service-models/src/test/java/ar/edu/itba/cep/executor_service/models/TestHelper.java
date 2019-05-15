package ar.edu.itba.cep.executor_service.models;

import ar.edu.itba.cep.executor_service.models.Language;
import com.github.javafaker.Faker;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class for testing.
 */
class TestHelper {

    /**
     * Indicates how many elements must have the created {@link String} {@link List}s used for testing
     * (i.e to be used as inputs and expected outputs).
     */
    private static final int STRING_LISTS_SIZE = 10;


    // ================================================================================================================
    // Valid values
    // ================================================================================================================

    /**
     * @return A valid code {@link String}.
     */
    /* package */
    static String validCode() {
        return Faker.instance()
                .lorem()
                .paragraph();
    }

    /**
     * @return A valid timeout.
     */
    /* package */
    static Long validTimeout() {
        final List<Long> validValues = new LinkedList<>();
        validValues.add(null);
        validValues.add(Faker.instance().number().numberBetween(1, Long.MAX_VALUE));
        final var index = Faker.instance().number().numberBetween(0, validValues.size());
        return validValues.get(index);
    }

    /**
     * @return a valid {@link Language}.
     */
    /* package */
    static Language validLanguage() {
        final var languages = Language.values();
        final var index = Faker.instance().number().numberBetween(0, languages.length);
        return languages[index];
    }


    /**
     * @return A valid exit code.
     */
    /* package */
    static int validExitCode() {
        return Faker.instance()
                .number()
                .numberBetween(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Creates a valid {@link List} of {@link String} to be used as inputs or outputs (stdout/stderr).
     *
     * @return A valid {@link List} of inputs/outputs.
     */
    /* package */
    static List<String> validInputOutputList() {
        return Faker.instance()
                .lorem()
                .words(STRING_LISTS_SIZE);
    }


    // ================================================================================================================
    // Invalid values
    // ================================================================================================================

    /**
     * Creates an invalid {@link List} of {@link String} to be used as inputs or outputs (stdout/stderr).
     * It is invalid because it contains a {@code null} element.
     *
     * @return An invalid (contains a {@code null} element) {@link List} of inputs/outputs.
     */
    /* package */
    static List<String> inputOutputListWithNullElement() {
        final var list = Faker.instance().lorem().words(STRING_LISTS_SIZE - 1);
        list.add(null);
        Collections.shuffle(list);
        return list;
    }

}
