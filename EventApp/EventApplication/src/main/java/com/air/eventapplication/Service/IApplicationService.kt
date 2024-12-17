package com.air.eventapplication.Service

import com.air.eventapplication.models.Application

interface IApplicationService {
    suspend fun CereateApplication(application: Application): Result<Boolean>
    suspend fun UpdateApplicationStatus(applicationId: Int, newStatus: String): Result<Boolean>
}