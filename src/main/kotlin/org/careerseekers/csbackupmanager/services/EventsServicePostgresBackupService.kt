package org.careerseekers.csbackupmanager.services

import jakarta.annotation.PostConstruct
import org.careerseekers.csbackupmanager.annotations.Refreshable
import org.careerseekers.csbackupmanager.config.properties.EventsServiceDatabaseConfigurationProperties
import org.springframework.stereotype.Service
import java.io.File

@Service
@Refreshable(900_000)
class EventsServicePostgresBackupService(private val properties: EventsServiceDatabaseConfigurationProperties) :
    IPostgresBackupService {

    @PostConstruct
    fun init() {
        val dumpsDir = File("./db/es-pg")

        createBackup(dumpsDir, properties)
    }
}