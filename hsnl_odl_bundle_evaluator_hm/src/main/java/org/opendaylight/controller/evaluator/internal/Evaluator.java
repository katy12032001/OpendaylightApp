package org.opendaylight.controller.evaluator.internal;

import org.opendaylight.controller.evaluator.EvaluatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Evaluator {
    private static final Logger log = LoggerFactory.getLogger(Evaluator.class);

    public Evaluator() {
    }

    public void init() {
        System.out.println("evaluator init");
    }

    public void destroy() {
        System.out.println("evaluator destroy");
    }

    public void start() {
        System.out.println("evaluator start");
        EvaluatorService evaluatorService = new EvaluatorService();
        evaluatorService.start();
    }

    public void stop() {
        System.out.println("evaluator stop");
    }
}
