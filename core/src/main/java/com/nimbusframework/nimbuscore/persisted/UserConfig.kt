package com.nimbusframework.nimbuscore.persisted

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class UserConfig(val projectName: String = "nimbus-project", val assemble: Boolean = false)