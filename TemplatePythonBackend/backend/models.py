import json
# Create your models here.


class LcmWebResult:
    resultCode: int
    resultMessage: str

    def __init__(self, result_body: any, result_code: int = 0, result_message: str = 'OK'):
        self.resultCode = result_code
        self.resultMessage = result_message
        self.resultBody = result_body


class CaptchaDto:
    token: str
    captchaImage: str


class AccessTokenDto:
    token: str
    jwtId: str


class LoginDto:
    token: str


class TokenVerificationDto:
    userId: int


def wrap_result_to_json(result_body: any, result_code: int = 0, result_message: str = 'OK') -> str:
    return json.dumps(LcmWebResult(result_body, result_code, result_message).__dict__, default=lambda o: o.__dict__)


def wrap_paged_list_to_json(total_count: int, data_list: list, result_code: int = 0, result_message: str = 'OK'):
    return json.dumps(LcmWebResult({
        'totalCount': total_count,
        'dataList': data_list
    }, result_code, result_message).__dict__, default=lambda o: o.__dict__)
