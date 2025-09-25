package org.careerseekers.csbackupmanager.services

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.careerseekers.csbackupmanager.annotations.Refreshable
import org.careerseekers.csbackupmanager.config.properties.UserServiceDatabaseConfigurationProperties
import org.careerseekers.csbackupmanager.enums.DatabaseNames
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Service
import java.io.File

@Service
@Refreshable
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class UsersServicePostgresBackupService(
    override val databaseProperties: UserServiceDatabaseConfigurationProperties,
    override val yandexDiskService: YandexDiskService,
) : IPostgresBackupService, DisposableBean {

    @Value("\${storage-dir}")
    private lateinit var storagePath: String

    override val database = DatabaseNames.USERS_SERVICE_PG
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @PostConstruct
    fun init() {
        val dumpsDir = File("${storagePath}/db/us-postgres")
        val backupResponse = createBackup(dumpsDir)

        coroutineScope.launch {
            yandexDiskService.uploadFileToDisk(
                filePath = backupResponse.absolutePath,
                filename = backupResponse.fileName,
                database = database
            ).awaitSingleOrNull()
        }
    }

    override fun destroy() {
        coroutineScope.cancel()
    }
}