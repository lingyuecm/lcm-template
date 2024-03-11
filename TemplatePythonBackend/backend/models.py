from django.db import models


# Create your models here.

class LcmWebResult:
    resultCode: int
    resultMessage: str

    def __init__(self, result_body):
        self.resultCode = 0
        self.resultMessage = "OK"
        self.resultBody = result_body


class CaptchaDto:
    token: str
    captchaImage: str
