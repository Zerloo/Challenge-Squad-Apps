package com.example.challenge_squad_apps.webclient.exceptions

import com.example.challenge_squad_apps.ChallengeSquadAppsApplication
import com.example.challenge_squad_apps.R

enum class HttpResponse(val code: Int) {
    HTTP_200_OK(200),
    HTTP_201_CREATED(201),
    HTTP_400_BAD_REQUEST(400),
    HTTP_401_UNAUTHORIZED(401),
    HTTP_403_FORBIDDEN(403),
    HTTP_404_NOT_FOUND(404),
    HTTP_409_CONFLICT(409),
    HTTP_500_INTERNAL_SERVER_ERROR(500),
    HTTP_503_SERVICE_UNVAILABLE(503);

    val message: String
        get() = ChallengeSquadAppsApplication.applicationContext().getString(messageResource)

    val messageResource: Int
        get() = when (this) {
            HTTP_200_OK -> R.string.HTTP_200_OK
            HTTP_201_CREATED -> R.string.HTTP_201_CREATED
            HTTP_400_BAD_REQUEST -> R.string.HTTP_400_BAD_REQUEST
            HTTP_401_UNAUTHORIZED -> R.string.HTTP_401_UNAUTHORIZED
            HTTP_403_FORBIDDEN -> R.string.HTTP_403_FORBIDDEN
            HTTP_404_NOT_FOUND -> R.string.HTTP_404_NOT_FOUND
            HTTP_409_CONFLICT -> R.string.HTTP_409_CONFLICT
            HTTP_500_INTERNAL_SERVER_ERROR -> R.string.HTTP_500_INTERNAL_SERVER_ERROR
            HTTP_503_SERVICE_UNVAILABLE -> R.string.HTTP_503_SERVICE_UNVAILABLE
        }

    companion object {

        fun fromCode(code: Int): HttpResponse? {
            return values().find { it.code == code }
        }
    }
}
