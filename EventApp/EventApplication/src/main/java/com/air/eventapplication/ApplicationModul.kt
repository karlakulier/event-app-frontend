package com.air.eventapplication

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.air.eventapplication.API.ApiClient
import com.air.eventapplication.API.IApplicationApi
import com.air.eventapplication.Service.ApplicationService
import com.air.eventapplication.Service.IApplicationService
import com.air.eventapplication.models.Application
import com.air.eventapplication.models.ApplicationGet
import com.air.eventapplication.models.ApplicationMusician
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ApplicationModul {
    private val api: IApplicationApi = ApiClient.retrofit.create(IApplicationApi::class.java)
    private val applicationService: IApplicationService = ApplicationService(api)

    fun SendApplication(context: Context, eventId: Int, musicianId: Int) {

        val application = Application("waiting", eventId, musicianId)
        CoroutineScope(Dispatchers.Main).launch {
            val rezultat = withContext(Dispatchers.IO) {
                applicationService.CereateApplication(application)
            }
            if (rezultat.isSuccess) {
                Toast.makeText(context, "Prijava uspješna!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Greška: ${rezultat.exceptionOrNull()?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    fun UpdateApplicationStatus(context: Context, applicationId: Int, newStatus: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val rezultat = withContext(Dispatchers.IO) {
                applicationService.UpdateApplicationStatus(applicationId, newStatus)
            }
            if (rezultat.isSuccess) {
                Toast.makeText(context, "Promjena statusa prijave uspješna!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(
                    context,
                    "Greška: ${rezultat.exceptionOrNull()?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    suspend fun GetApplicationsByEvent(context: Context, eventId: Int): List<ApplicationGet> {
        return withContext(Dispatchers.IO) {
            val rezultat = applicationService.GetApplicationsByEvent(eventId)
            if (rezultat.isSuccess) {
                val applications = rezultat.getOrNull()
                return@withContext applications ?: emptyList()
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Greška: ${rezultat.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@withContext emptyList() // Ako je greška, vratite praznu listu
            }
        }
    }
    suspend fun GetApplicationsForMusician(context: Context, eventId: Int): List<ApplicationMusician> {
        return withContext(Dispatchers.IO) {
            val rezultat = applicationService.GetApplicationsForMusician(eventId)
            if (rezultat.isSuccess) {
                val applications = rezultat.getOrNull()
                return@withContext applications ?: emptyList()
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Greška: ${rezultat.exceptionOrNull()?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@withContext emptyList() // Ako je greška, vratite praznu listu
            }
        }
    }
}
