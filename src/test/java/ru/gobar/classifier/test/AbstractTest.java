package ru.gobar.classifier.test;

import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({AllureJunit5.class, SuiteWrapper.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractTest {
}
