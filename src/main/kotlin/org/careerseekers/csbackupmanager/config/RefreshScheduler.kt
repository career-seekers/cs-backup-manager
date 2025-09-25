package org.careerseekers.csbackupmanager.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RefreshScheduler(
    private val context: ApplicationContext,
    private val refreshableBeanPostProcessor: RefreshableClassesBeanPostProcessor
) {
    private val logger = LoggerFactory.getLogger(RefreshScheduler::class.java)
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    @Scheduled(fixedRate = 60_000)
    fun refreshBeans() {
        val now = System.currentTimeMillis()

        refreshableBeanPostProcessor.refreshableBeans.forEach { (beanName, info) ->
            val oldInstance = info.currentBeanInstance
            if (now - info.lastRefreshTime > info.refreshPeriod) {
                if (oldInstance is DisposableBean) {
                    oldInstance.destroy()
                }

                coroutineScope.launch {
                    val newInstance = context.getBean(beanName)
                    refreshableBeanPostProcessor.refreshableBeans[beanName] = info.copy(
                        lastRefreshTime = System.currentTimeMillis(),
                        currentBeanInstance = newInstance
                    )
                    logger.info("Bean $beanName was refreshed in coroutine")
                }
            }
        }
    }
}