package com.nimbusframework.nimbuscore.clients.store.conditions.bool

import com.nimbusframework.nimbuscore.clients.store.conditions.Condition

class BooleanComparisonCondition(val value1: Condition, val operator: BooleanOperator, val value2: Condition): Condition