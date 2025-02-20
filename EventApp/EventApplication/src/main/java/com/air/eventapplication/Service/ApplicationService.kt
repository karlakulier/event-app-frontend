package com.air.eventapplication.Service

import com.air.eventapplication.API.IApplicationApi
import com.air.eventapplication.models.Application
import com.air.eventapplication.models.ApplicationGet
import com.air.eventapplication.models.ApplicationMusician
import com.air.eventapplication.models.ApplicationUpdateStatus
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
        return withContext(Dispatchers.IO) {
            try {
                var newStatus = ApplicationUpdateStatus(applicationId,newStatus)
                val response = api.UpdateApplicationStatus(newStatus)
                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(Exception("Greška kod promjene statusa prijave: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun GetApplicationsByEvent(EventId: Int): Result<List<ApplicationGet>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.GetApplicationsByEvent(EventId)
                if (response.isSuccessful) {
                    val applications = response.body()
                    if (applications != null) {
                        Result.success(applications)
                    } else {
                        Result.failure(Exception("Prazan odgovor od servera."))
                    }
                } else {
                    Result.failure(Exception("Greška kod dohvaćanja prijava: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    override suspend fun GetApplicationsForMusician(EventId: Int): Result<List<ApplicationMusician>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.GetApplicationsForMusician(EventId)
                if (response.isSuccessful) {
                    val applications = response.body()
                    if (applications != null) {
                        Result.success(applications)
                    } else {
                        Result.failure(Exception("Prazan odgovor od servera."))
                    }
                } else {
                    Result.failure(Exception("Greška kod dohvaćanja prijava: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }


}