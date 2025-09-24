package org.careerseekers.csbackupmanager.services

import jakarta.annotation.PostConstruct
import org.careerseekers.csbackupmanager.annotations.Refreshable
import org.careerseekers.csbackupmanager.config.properties.UserServiceDatabaseConfigurationProperties
import org.springframework.stereotype.Service
import java.io.File

@Service
@Refreshable
class UsersServicePostgresBackupService(private val properties: UserServiceDatabaseConfigurationProperties) : IPostgresBackupService {

    @PostConstruct
    fun init() {
        val dumpsDir = File("./db/us-pg")

        createBackup(dumpsDir, properties)
    }
}