package org.careerseekers.csbackupmanager.services

import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.careerseekers.csbackupmanager.annotations.Refreshable
import org.careerseekers.csbackupmanager.config.properties.FilesServiceDatabaseConfigurationProperties
import org.careerseekers.csbackupmanager.enums.DatabaseNames
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File

@Service
@Refreshable
class FilesServicePostgresBackupService(
    override val databaseProperties: FilesServiceDatabaseConfigurationProperties,
    override val yandexDiskService: YandexDiskService,
) : IPostgresBackupService {

    @Value("\${storage-dir}")
    private lateinit var storagePath: String

    override val database = DatabaseNames.FILES_SERVICE_PG
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @PostConstruct
    fun init() {
        val dumpsDir = File("${storagePath}/db/fs-postgres")
        val backupResponse = createBackup(dumpsDir)

        coroutineScope.launch {
            yandexDiskService.uploadFileToDisk(
                filePath = backupResponse.absolutePath,
                filename = backupResponse.fileName,
                database = database
            ).subscribe()
        }
    }
}