package org.careerseekers.csbackupmanager.services

import jakarta.annotation.PostConstruct
import org.careerseekers.csbackupmanager.annotations.Refreshable
import org.careerseekers.csbackupmanager.config.properties.FilesServiceDatabaseConfigurationProperties
import org.springframework.stereotype.Service
import java.io.File

@Service
@Refreshable(900_000)
class FilesServicePostgresBackupService(private val properties: FilesServiceDatabaseConfigurationProperties) :
    IPostgresBackupService {


    @PostConstruct
    fun init() {
        val dumpsDir = File("./db/fs-pg")

        createBackup(dumpsDir, properties)
    }
}