package com.air.eventapplication.Service

import com.air.eventapplication.models.Application
import com.air.eventapplication.models.ApplicationGet
import com.air.eventapplication.models.ApplicationMusician

interface IApplicationService {
    suspend fun CereateApplication(application: Application): Result<Boolean>
    suspend fun UpdateApplicationStatus(applicationId: Int, newStatus: String): Result<Boolean>
    suspend fun GetApplicationsByEvent(EventId: Int): Result<List<ApplicationGet>>
    suspend fun GetApplicationsForMusician(EventId: Int): Result<List<ApplicationMusician>>
}