package com.kvrae.easykitchen.data.util

import com.kvrae.easykitchen.domain.util.DateProvider
import java.time.LocalDate

class SystemDateProvider : DateProvider {
    override fun todayKey(): String {
        return LocalDate.now().toString()
    }
}
