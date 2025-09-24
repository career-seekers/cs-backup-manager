package org.careerseekers.csbackupmanager.services

import org.careerseekers.csbackupmanager.config.properties.IDatabaseProperties
import java.io.File
import java.time.LocalDateTime

interface IPostgresBackupService {

    fun createBackup(dumpsDir: File, properties: IDatabaseProperties): String {
        if (!dumpsDir.exists()) {
            dumpsDir.mkdirs()
        }
        val backupFile = File(dumpsDir, "${properties.backupName}_${LocalDateTime.now()}.sql.gz").absolutePath
        val containerName = properties.containerName

        val command = listOf(
            "bash", "-c",
            "docker exec -t $containerName pg_dump -U ${properties.username} -d ${properties.dbName} | gzip -9 > $backupFile"
        )

        val process = ProcessBuilder(command)
            .inheritIO()
            .start()

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("Backup process failed with code $exitCode")
        }

        return backupFile
    }
}