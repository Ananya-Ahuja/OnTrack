package com.igdtuw.ontrack

import javax.inject.Inject

class ProjectRepository @Inject constructor(private val dao: ProjectDao) {
    fun getProjects() = dao.getProjectsWithMilestones()

    suspend fun addProject(project: Project, initialMilestones: List<Milestone>) {
        val projectId = dao.insertProject(project)
        initialMilestones.forEach { dao.insertMilestone(it.copy(projectId = projectId)) }
    }

    suspend fun updateMilestone(milestone: Milestone) = dao.updateMilestone(milestone)

    suspend fun deleteProject(project: Project) = dao.deleteProject(project.id)
}
