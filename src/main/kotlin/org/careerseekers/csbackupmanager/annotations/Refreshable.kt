package org.careerseekers.csbackupmanager.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Refreshable(val periodMillis: Long = 1_800_000)