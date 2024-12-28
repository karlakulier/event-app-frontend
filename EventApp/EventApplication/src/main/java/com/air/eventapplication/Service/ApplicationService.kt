package com.air.eventapplication.Service

import com.air.eventapplication.API.IApplicationApi
import com.air.eventapplication.models.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApplicationService (private val api: IApplicationApi) : IApplicationService{
    override suspend fun CereateApplication(application: Application): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.CreateApplication(application)
                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Greška kod slanja prijave: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun UpdateApplicationStatus(
        applicationId: Int,
        newStatus: String
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    /*
    override suspend fun azurirajStatusPrijave(idPrijave: String, noviStatus: String): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.azurirajStatus(idPrijave, StatusRequest(noviStatus))
                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Greška kod ažuriranja statusa: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }*/
}