package com.nimbusframework.nimbuscore.annotations.queue;

import com.nimbusframework.nimbuscore.annotations.NimbusConstants;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UsesQueues.class)
public @interface UsesQueue {
    String id();
    String[] stages() default {NimbusConstants.stage};
}
