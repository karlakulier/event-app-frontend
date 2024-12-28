package com.air.eventapplication

import android.content.Context
import android.widget.Toast
import com.air.eventapplication.API.ApiClient
import com.air.eventapplication.API.IApplicationApi
import com.air.eventapplication.Service.ApplicationService
import com.air.eventapplication.Service.IApplicationService
import com.air.eventapplication.models.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ApplicationModul {
    private val api: IApplicationApi = ApiClient.retrofit.create(IApplicationApi::class.java)
    private val applicationService: IApplicationService = ApplicationService(api)

    fun SendApplication(context: Context,eventId : Int, musicianId : Int){

            val application = Application("active", eventId, musicianId)
            CoroutineScope(Dispatchers.Main).launch {
                val rezultat = withContext(Dispatchers.IO) {
                    applicationService.CereateApplication(application)
                }
                if (rezultat.isSuccess) {
                    Toast.makeText(context, "Prijava uspješna!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Greška: ${rezultat.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }
}