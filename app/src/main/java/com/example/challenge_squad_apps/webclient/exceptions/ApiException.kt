package com.example.challenge_squad_apps.webclient.exceptions

class ApiException(var error: HttpResponse) : BaseException(error.message, error.messageResource)