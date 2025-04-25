package com.igdtuw.ontrack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {
    val projects = repository.getProjects().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addProject(title: String, milestones: List<Milestone>) = viewModelScope.launch {
        repository.addProject(Project(title = title), milestones)
    }

    fun updateMilestone(milestone: Milestone) = viewModelScope.launch {
        repository.updateMilestone(milestone)
    }

    fun deleteProject(project: Project) = viewModelScope.launch {
        repository.deleteProject(project)
    }
}
