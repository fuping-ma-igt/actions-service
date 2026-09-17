package com.actions.service.retry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FailureCategoryParserTest {

    private final FailureCategoryParser parser = new FailureCategoryParser();

    @Test
    void parse_lowercaseValue_returnsCategory() {
        assertThat(parser.parse("transient"))
                .isEqualTo(FailureCategory.TRANSIENT);
    }

    @Test
    void parse_enumValueWithWhitespace_returnsCategory() {
        assertThat(parser.parse("  RATE_LIMIT  "))
                .isEqualTo(FailureCategory.RATE_LIMIT);
    }
}
