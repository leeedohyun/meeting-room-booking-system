package com.example.demo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PaymentProviderTypeTest {

    @ParameterizedTest
    @CsvSource({
            "A, CARD",
            "B, SIMPLE",
            "C, VIRTUAL_ACCOUNT"
    })
    void fromProviderCode_Success(String inputCode, PaymentProviderType expectedType) {
        assertThat(PaymentProviderType.fromProviderCode(inputCode)).isEqualTo(expectedType);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    void fromProviderCode_CaseInsensitive(String inputCode) {
        PaymentProviderType actualType = PaymentProviderType.fromProviderCode(inputCode);

        if ("a".equalsIgnoreCase(inputCode)) {
            assertThat(actualType).isEqualTo(PaymentProviderType.CARD);
        } else if ("b".equalsIgnoreCase(inputCode)) {
            assertThat(actualType).isEqualTo(PaymentProviderType.SIMPLE);
        } else if ("c".equalsIgnoreCase(inputCode)) {
            assertThat(actualType).isEqualTo(PaymentProviderType.VIRTUAL_ACCOUNT);
        }
    }

    @Test
    void testFromProviderCode_Failure_UnknownCode() {
        // given
        String unknownCode = "D";

        // when & then
        assertThatThrownBy(() -> PaymentProviderType.fromProviderCode(unknownCode))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Unknown provider code: " + unknownCode);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void testFromProviderCode_Failure_EmptyOrBlank(String inputCode) {
        assertThatThrownBy(() -> PaymentProviderType.fromProviderCode(inputCode))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testFromProviderCode_Failure_Null() {
        assertThatThrownBy(() -> PaymentProviderType.fromProviderCode(null))
                .isInstanceOfAny(NoSuchElementException.class, NullPointerException.class);
    }
}
