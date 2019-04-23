package com.nimbusframework.nimbuscore.clients

import com.nimbusframework.nimbuscore.clients.document.DocumentStoreClient
import com.nimbusframework.nimbuscore.clients.document.DocumentStoreClientDynamo
import com.nimbusframework.nimbuscore.clients.document.DocumentStoreClientLocal
import com.nimbusframework.nimbuscore.clients.file.FileStorageClient
import com.nimbusframework.nimbuscore.clients.file.FileStorageClientLocal
import com.nimbusframework.nimbuscore.clients.file.FileStorageClientS3
import com.nimbusframework.nimbuscore.clients.function.*
import com.nimbusframework.nimbuscore.clients.keyvalue.KeyValueStoreClient
import com.nimbusframework.nimbuscore.clients.keyvalue.KeyValueStoreClientDynamo
import com.nimbusframework.nimbuscore.clients.keyvalue.KeyValueStoreClientLocal
import com.nimbusframework.nimbuscore.clients.notification.NotificationClient
import com.nimbusframework.nimbuscore.clients.notification.NotificationClientLocal
import com.nimbusframework.nimbuscore.clients.notification.NotificationClientSNS
import com.nimbusframework.nimbuscore.clients.queue.QueueClient
import com.nimbusframework.nimbuscore.clients.queue.QueueClientSQS
import com.nimbusframework.nimbuscore.clients.queue.QueueClientLocal
import com.nimbusframework.nimbuscore.clients.rdbms.DatabaseClient
import com.nimbusframework.nimbuscore.clients.rdbms.DatabaseClientLocal
import com.nimbusframework.nimbuscore.clients.rdbms.DatabaseClientRds
import com.nimbusframework.nimbuscore.clients.websocket.ServerlessFunctionWebSocketClient
import com.nimbusframework.nimbuscore.clients.websocket.ServerlessFunctionWebSocketClientApiGateway
import com.nimbusframework.nimbuscore.clients.websocket.ServerlessFunctionWebsocketClientLocal

object ClientBuilder {

    var isLocalDeployment = false

    @JvmStatic
    fun <K, V> getKeyValueStoreClient(key: Class<K>, value: Class<V>): KeyValueStoreClient<K, V> {
        return if (isLocalDeployment) {
            KeyValueStoreClientLocal(value)
        } else {
            KeyValueStoreClientDynamo(key, value, getStage())
        }
    }

    @JvmStatic
    fun <T> getDocumentStoreClient(document: Class<T>): DocumentStoreClient<T> {
        return if (isLocalDeployment) {
            DocumentStoreClientLocal(document)
        } else {
            DocumentStoreClientDynamo(document, getStage())
        }
    }

    @JvmStatic
    fun getQueueClient(id: String): QueueClient {
        return if (isLocalDeployment) {
            QueueClientLocal(id)
        } else {
            QueueClientSQS(id)
        }
    }

    @JvmStatic
    fun <T> getDatabaseClient(databaseObject: Class<T>): DatabaseClient {
        return if (isLocalDeployment) {
            DatabaseClientLocal(databaseObject)
        } else {
            DatabaseClientRds(databaseObject)
        }
    }

    @JvmStatic
    fun <T> getEnvironmentVariableClient(): EnvironmentVariableClient {
        return if (isLocalDeployment) {
            EnvironmentVariableClientLocal()
        } else {
            EnvironmentVariableClientLambda()
        }
    }


    @JvmStatic
    fun getNotificationClient(topic: String): NotificationClient {
        return if (isLocalDeployment) {
            NotificationClientLocal(topic)
        } else {
            NotificationClientSNS(topic)
        }
    }

    @JvmStatic
    fun getBasicServerlessFunctionClient(): BasicServerlessFunctionClient {
        return if (isLocalDeployment) {
            BasicServerlessFunctionClientLocal()
        } else {
            BasicServerlessFunctionClientLambda()
        }
    }

    @JvmStatic
    fun getFileStorageClient(bucketName: String): FileStorageClient {
        return if (isLocalDeployment) {
            FileStorageClientLocal(bucketName)
        } else {
            FileStorageClientS3(bucketName + getStage())
        }
    }

    @JvmStatic
    fun getServerlessFunctionWebSocketClient(): ServerlessFunctionWebSocketClient {
        return if (isLocalDeployment) {
            ServerlessFunctionWebsocketClientLocal()
        } else {
            ServerlessFunctionWebSocketClientApiGateway()
        }
    }

    private fun getStage(): String {
        return if (System.getenv().containsKey("NIMBUS_STAGE")) {
            System.getenv("NIMBUS_STAGE")
        } else {
            "dev"
        }
    }
}
