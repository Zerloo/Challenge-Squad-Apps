package com.example.challenge_squad_apps.webclient.exceptions

import com.example.challenge_squad_apps.R

enum class HttpResponse(val code: Int, val message: Int) {
    HTTP_200_OK(200, R.string.HTTP_200_OK),
    HTTP_201_CREATED(201, R.string.HTTP_201_CREATED),
    HTTP_400_BAD_REQUEST(400, R.string.HTTP_400_BAD_REQUEST),
    HTTP_401_UNAUTHORIZED(401, R.string.HTTP_401_UNAUTHORIZED),
    HTTP_403_FORBIDDEN(403, R.string.HTTP_403_FORBIDDEN),
    HTTP_404_NOT_FOUND(404, R.string.HTTP_404_NOT_FOUND),
    HTTP_409_CONFLICT(409,R.string.HTTP_409_CONFLICT),
    HTTP_500_INTERNAL_SERVER_ERROR(500, R.string.HTTP_500_INTERNAL_SERVER_ERROR),
    HTTP_503_SERVICE_UNVAILABLE(503,R.string.HTTP_503_SERVICE_UNVAILABLE);

    companion object {
        fun fromCode(code: Int): HttpResponse? {
            return values().find { it.code == code }
        }
    }
}
