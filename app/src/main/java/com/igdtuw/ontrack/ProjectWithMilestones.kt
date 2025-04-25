package com.igdtuw.ontrack

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectWithMilestones(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "id",
        entityColumn = "projectId"
    )
    val milestones: List<Milestone>
)
