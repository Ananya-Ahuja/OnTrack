package com.igdtuw.ontrack

import java.time.LocalDate

data class MilestoneTemplate(
    val name: String = "",
    val date: LocalDate = LocalDate.now()
)
