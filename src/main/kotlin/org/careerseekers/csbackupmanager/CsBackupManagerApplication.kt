package org.careerseekers.csbackupmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CsBackupManagerApplication

fun main(args: Array<String>) {
    runApplication<CsBackupManagerApplication>(*args)
}
