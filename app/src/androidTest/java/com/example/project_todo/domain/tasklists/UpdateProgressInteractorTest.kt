package com.example.project_todo.domain.tasklists

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.project_todo.core.TaskCollectionDataCore
import com.example.project_todo.currentValue
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskCollection
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.junit.Assert.*
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UpdateProgressInteractorTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var taskCollectionDataCoreMock: TaskCollectionDataCore
    @Mock private lateinit var taskMock: Task
    @Mock private lateinit var taskCollectionMock: TaskCollection

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        taskMock.priority = 2
    }

    @Test
    fun testOnCompleteTask() {
        runBlocking {
            UpdateProgressInteractor(taskMock, UpdateProgressInteractor.TaskAction.TASK_COMPLETED,
                taskCollectionMock.apply { progressValue = 0F; fullValue = 2F },
                taskCollectionDataCoreMock)
                .test().currentValue { resource ->
                    assertTrue(resource is Resource.Success)
                    resource.inspect({updatedProgress ->
                        assertTrue(updatedProgress == 100F)
                        assertTrue(taskCollectionMock.progressValue == taskCollectionMock.fullValue)
                    })
            }
        }
    }

    @Test
    fun testOnUndoComplete() {
        runBlocking {
            UpdateProgressInteractor(taskMock, UpdateProgressInteractor.TaskAction.TASK_UNDO_COMPLETED,
                taskCollectionMock.apply { progressValue = 2F; fullValue = 2F },
                taskCollectionDataCoreMock)
                .test().currentValue {resource ->
                    assertTrue(resource is Resource.Success)
                    resource.inspect({updatedProgress ->
                        assertTrue(updatedProgress == 0F)
                        assertTrue(taskCollectionMock.progressValue == 0F)
                        assertTrue(taskCollectionMock.fullValue == 2F)
                    })
                }
        }
    }

    @Test
    fun testOnSaveAction() {
        runBlocking {
            UpdateProgressInteractor(taskMock, UpdateProgressInteractor.TaskAction.TASK_SAVED,
                taskCollectionMock.apply { progressValue = 0F; fullValue = 2F },
                taskCollectionDataCoreMock)
                .test().currentValue {resource ->
                    assertTrue(resource is Resource.Success)
                    resource.inspect({updatedProgress ->
                        assertTrue(updatedProgress == 0F)
                        assertTrue(taskCollectionMock.fullValue == 4F)
                    })
                }
        }
    }

    @Test
    fun testOnDeleteAction() {
        runBlocking {
            UpdateProgressInteractor(taskMock, UpdateProgressInteractor.TaskAction.TASK_DELETED,
                taskCollectionMock.apply { progressValue = 0F; fullValue = 4F },
                taskCollectionDataCoreMock)
                .test().currentValue {resource ->
                    assertTrue(resource is Resource.Success)
                    resource.inspect({updatedProgress ->
                        assertTrue(updatedProgress == 0F)
                        assertTrue(taskCollectionMock.fullValue == 2F)
                    })
                }
        }
    }

    @Test
    fun testOnErrorAction() {
        runBlocking {

            Mockito.`when`(taskCollectionDataCoreMock.updateTaskCollection(taskCollectionMock))
                .thenThrow(IllegalThreadStateException::class.java)

            UpdateProgressInteractor(taskMock, UpdateProgressInteractor.TaskAction.TASK_COMPLETED,
                taskCollectionMock.apply { progressValue = 0F; fullValue = 2F },
                taskCollectionDataCoreMock)
                .test().currentValue { resource ->
                    assertTrue(resource is Error)
                    taskCollectionMock.apply {
                        assertTrue(progressValue == 0F && fullValue == 2F)
                    }
                }
        }
    }



}