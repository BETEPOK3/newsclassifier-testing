package ru.gobar.classifier.util;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import org.assertj.core.error.AssertJMultipleFailuresError;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AllureStepUtil {

    private final List<Throwable> errors = new ArrayList<>();
    private final List<String> errorSteps = new ArrayList<>();

    public void runStep(String name, Runnable runnable) {
        String uuid = UUID.randomUUID().toString();
        List<Throwable> errors = new ArrayList<>();
        List<String> errorSteps = new ArrayList<>();

        try {
            Allure.getLifecycle().startStep(uuid, new StepResult().setName(name));
            runnable.run();
        } catch (Throwable e) {
            errors.add(e);
            errorSteps.add("Failed - " + name);
        }

        if (!errors.isEmpty()) {
            Allure.getLifecycle().updateStep(uuid, (stepResult -> stepResult.setStatus(Status.FAILED)));
            this.errors.addAll(errors);
            this.errorSteps.addAll(errorSteps);
        } else {
            Allure.getLifecycle().updateStep(uuid, (stepResult -> stepResult.setStatus(Status.PASSED)));
        }

        Allure.getLifecycle().updateStep(stepResult -> {
            List<StepResult> stepResults = stepResult.getSteps();
            if (stepResults.stream().anyMatch(res -> res.getStatus().equals(Status.FAILED))) {
                stepResult.setStatus(Status.FAILED);
            } else if (stepResults.stream().anyMatch(res -> res.getStatus().equals(Status.BROKEN))) {
                stepResult.setStatus(Status.FAILED);
            }
        });
        Allure.getLifecycle().stopStep(uuid);
    }

    public void check() {
        if (!errors.isEmpty()) {
            throw new AssertJMultipleFailuresError(errorSteps.toString(), errors);
        }
    }
}
