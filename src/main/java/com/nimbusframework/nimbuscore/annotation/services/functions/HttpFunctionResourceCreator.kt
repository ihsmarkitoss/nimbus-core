package com.nimbusframework.nimbuscore.annotation.services.functions

import com.nimbusframework.nimbuscore.annotation.annotations.function.HttpServerlessFunction
import com.nimbusframework.nimbuscore.annotation.annotations.function.repeatable.HttpServerlessFunctions
import com.nimbusframework.nimbuscore.persisted.NimbusState
import com.nimbusframework.nimbuscore.cloudformation.resource.function.FunctionConfig
import com.nimbusframework.nimbuscore.annotation.processor.FunctionInformation
import com.nimbusframework.nimbuscore.annotation.services.FunctionEnvironmentService
import com.nimbusframework.nimbuscore.cloudformation.CloudFormationDocuments
import com.nimbusframework.nimbuscore.wrappers.http.HttpServerlessFunctionFileBuilder
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

class HttpFunctionResourceCreator(
        cfDocuments: MutableMap<String, CloudFormationDocuments>,
        nimbusState: NimbusState,
        processingEnv: ProcessingEnvironment
) : FunctionResourceCreator(
        cfDocuments,
        nimbusState,
        processingEnv,
        HttpServerlessFunction::class.java,
        HttpServerlessFunctions::class.java
) {

    override fun handleElement(type: Element, functionEnvironmentService: FunctionEnvironmentService, results: MutableList<FunctionInformation>) {
        val httpFunctions = type.getAnnotationsByType(HttpServerlessFunction::class.java)

        val methodInformation = extractMethodInformation(type)


        val fileBuilder = HttpServerlessFunctionFileBuilder(
                processingEnv,
                methodInformation,
                type
        )

        fileBuilder.createClass()

        for (httpFunction in httpFunctions) {
            for (stage in httpFunction.stages) {
                val handler = fileBuilder.getHandler()

                val config = FunctionConfig(httpFunction.timeout, httpFunction.memory, stage)
                val functionResource = functionEnvironmentService.newFunction(handler, methodInformation, config)

                functionEnvironmentService.newHttpMethod(httpFunction, functionResource)

                results.add(FunctionInformation(type, functionResource))
            }
        }
    }
}