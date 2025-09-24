package org.careerseekers.csbackupmanager.services

import org.careerseekers.csbackupmanager.config.properties.IDatabaseProperties
import org.careerseekers.csbackupmanager.dto.DatabaseBackupResponseDto
import org.careerseekers.csbackupmanager.enums.DatabaseNames
import java.io.File
import java.time.LocalDateTime

interface IPostgresBackupService {
    val database: DatabaseNames
    val databaseProperties: IDatabaseProperties
    val yandexDiskService: YandexDiskService

    fun createBackup(dumpsDir: File): DatabaseBackupResponseDto {
        if (!dumpsDir.exists()) {
            dumpsDir.mkdirs()
        }
        val backupFile = File(dumpsDir, "${databaseProperties.backupName}_${LocalDateTime.now().withNano(0).toString().replace(":", "-")}.sql.gz")
        val containerName = databaseProperties.containerName

        val command = listOf(
            "bash", "-c",
            "docker exec -t $containerName pg_dump -U ${databaseProperties.username} -d ${databaseProperties.dbName} | gzip -9 > ${backupFile.absolutePath}"
        )

        val process = ProcessBuilder(command)
            .inheritIO()
            .start()

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("Backup process failed with code $exitCode")
        }

        return DatabaseBackupResponseDto(backupFile.absolutePath, backupFile.name)
    }
}