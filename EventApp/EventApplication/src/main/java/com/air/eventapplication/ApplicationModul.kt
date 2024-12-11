package com.air.eventapplication

import com.air.eventapplication.API.ApiClient
import com.air.eventapplication.API.IApplicationApi
import com.air.eventapplication.Service.ApplicationService
import com.air.eventapplication.Service.IApplicationService

object ApplicationModul {
    private val api: IApplicationApi = ApiClient.retrofit.create(IApplicationApi::class.java)
    val applicationService: IApplicationService = ApplicationService(api)
}