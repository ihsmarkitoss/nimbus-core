package com.nimbusframework.nimbuscore.testing.webserver.webconsole

import com.nimbusframework.nimbuscore.annotation.annotations.function.HttpMethod
import com.nimbusframework.nimbuscore.testing.http.HttpMethodIdentifier
import com.nimbusframework.nimbuscore.testing.webserver.WebserverHandler
import com.nimbusframework.nimbuscore.testing.webserver.resources.FileResource
import com.nimbusframework.nimbuscore.testing.webserver.resources.WebResource
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import java.io.File
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import javax.servlet.http.HttpServletResponse.SC_OK

class WebConsole(stage: String) : WebserverHandler("", "", "") {

    private val apis: MutableMap<HttpMethodIdentifier, WebResource> = mutableMapOf()

    init {
        apis[HttpMethodIdentifier("/FileBucketAPI", HttpMethod.POST)] = FileBucketApiResource(HttpMethod.POST)
        apis[HttpMethodIdentifier("/FileBucketAPI", HttpMethod.GET)] = FileBucketApiResource(HttpMethod.GET)
        apis[HttpMethodIdentifier("/FileBucketAPI", HttpMethod.OPTIONS)] = FileBucketApiResource(HttpMethod.OPTIONS)
        apis[HttpMethodIdentifier("/DocumentStoreAPI", HttpMethod.POST)] = DocumentStoreApiResource(HttpMethod.POST, stage)
        apis[HttpMethodIdentifier("/DocumentStoreAPI", HttpMethod.GET)] = DocumentStoreApiResource(HttpMethod.GET, stage)
        apis[HttpMethodIdentifier("/DocumentStoreAPI", HttpMethod.OPTIONS)] = DocumentStoreApiResource(HttpMethod.OPTIONS, stage)
        apis[HttpMethodIdentifier("/KeyValueStoreAPI", HttpMethod.POST)] = KeyValueStoreApiResource(HttpMethod.POST, stage)
        apis[HttpMethodIdentifier("/KeyValueStoreAPI", HttpMethod.GET)] = KeyValueStoreApiResource(HttpMethod.GET, stage)
        apis[HttpMethodIdentifier("/KeyValueStoreAPI", HttpMethod.OPTIONS)] = KeyValueStoreApiResource(HttpMethod.OPTIONS, stage)
        apis[HttpMethodIdentifier("/QueueAPI", HttpMethod.POST)] = QueueApiResource(HttpMethod.POST)
        apis[HttpMethodIdentifier("/QueueAPI", HttpMethod.GET)] = QueueApiResource(HttpMethod.GET)
        apis[HttpMethodIdentifier("/QueueAPI", HttpMethod.OPTIONS)] = QueueApiResource(HttpMethod.OPTIONS)
        apis[HttpMethodIdentifier("/NotificationAPI", HttpMethod.POST)] = NotificationApiResource(HttpMethod.POST)
        apis[HttpMethodIdentifier("/NotificationAPI", HttpMethod.GET)] = NotificationApiResource(HttpMethod.GET)
        apis[HttpMethodIdentifier("/NotificationAPI", HttpMethod.OPTIONS)] = NotificationApiResource(HttpMethod.OPTIONS)
        apis[HttpMethodIdentifier("/FunctionAPI", HttpMethod.POST)] = FunctionApiResource(HttpMethod.POST)
        apis[HttpMethodIdentifier("/FunctionAPI", HttpMethod.GET)] = FunctionApiResource(HttpMethod.GET)
        apis[HttpMethodIdentifier("/FunctionAPI", HttpMethod.OPTIONS)] = FunctionApiResource(HttpMethod.OPTIONS)
    }

    override fun handle(
            target: String,
            baseRequest: Request,
            request: HttpServletRequest,
            response: HttpServletResponse
    ) {
        val handler = apis[HttpMethodIdentifier(target, HttpMethod.valueOf(request.method))]
        if (handler != null) {
            handler.writeResponse(request, response, target)
            response.status = SC_OK
            response.setHeader("Access-Control-Allow-Origin", "*")
            response.outputStream.close()
        } else {
            response.status = SC_NOT_FOUND
            response.outputStream.close()
        }

    }
}