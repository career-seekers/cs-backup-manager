package org.careerseekers.csbackupmanager.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.careerseekers.csbackupmanager.config.properties.FilesServiceDatabaseConfigurationProperties
import org.careerseekers.csbackupmanager.enums.DatabaseNames
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File

@Service
class FilesServicePostgresBackupService(
    override val databaseProperties: FilesServiceDatabaseConfigurationProperties,
    override val yandexDiskService: YandexDiskService,
) : IPostgresBackupService, IRefreshable {

    @Value("\${storage-dir}")
    private lateinit var storagePath: String

    override val database = DatabaseNames.FILES_SERVICE_PG
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @Scheduled(fixedDelay = 900_000)
    override fun invokeRefreshableAction() {
        val dumpsDir = File("${storagePath}/db/fs-postgres")
        val backupResponse = createBackup(dumpsDir)

        coroutineScope.launch {
            yandexDiskService.uploadFileToDisk(
                filePath = backupResponse.absolutePath,
                filename = backupResponse.fileName,
                database = database
            ).awaitSingleOrNull()
        }
    }
}