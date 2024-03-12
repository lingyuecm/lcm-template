import uuid
from datetime import datetime, timedelta

import jwt

from backend.models import AccessTokenDto


def generate_login_token(login_captcha_id: str) -> str:
    headers = {
        'type': 'jwt',
        'alg': 'HS256'
    }
    payload = {
        'loginCaptchaId': login_captcha_id
    }
    return jwt.encode(payload=payload, key='lingyuecm123', algorithm='HS256', headers=headers)


def parse_login_token(token: str) -> str:
    decoded_jwt = jwt.decode(jwt=token, key='lingyuecm123', algorithms=['HS256'])
    return decoded_jwt['loginCaptchaId']


def generate_access_token(user_id) -> AccessTokenDto:
    headers = {
        'type': 'jwt',
        'alg': 'HS512'
    }
    payload = {
        'jti': str(uuid.uuid4()),
        'nbf': datetime.utcnow(),
        'exp': datetime.utcnow() + timedelta(milliseconds=86400000),
        'userId': user_id
    }
    result = AccessTokenDto()
    result.jwtId = payload['jti']
    result.token = jwt.encode(payload=payload, key='lingyuecm123', algorithm='HS512', headers=headers)
    return result
