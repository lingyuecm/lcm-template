import os
import uuid
from datetime import datetime, timedelta

import jwt

from backend.models import AccessTokenDto, TokenVerificationDto


def generate_login_token(login_captcha_id: str) -> str:
    headers = {
        'type': 'jwt',
        'alg': 'HS256'
    }
    payload = {
        'loginCaptchaId': login_captcha_id
    }
    return jwt.encode(payload=payload, key=os.environ.get('LOGIN_TOKEN_SECRET'), algorithm='HS256', headers=headers)


def parse_login_token(token: str) -> str:
    decoded_jwt = jwt.decode(jwt=token, key=os.environ.get('LOGIN_TOKEN_SECRET'), algorithms=['HS256'])
    return decoded_jwt['loginCaptchaId']


def generate_access_token(user_id) -> AccessTokenDto:
    headers = {
        'type': 'jwt',
        'alg': 'HS512'
    }
    nbf = datetime.utcnow()
    exp = datetime.utcnow() + timedelta(milliseconds=86400000)
    payload = {
        'jti': str(uuid.uuid4()),
        'nbf': nbf,
        'exp': exp,
        'userId': user_id
    }
    result = AccessTokenDto()
    result.jwtId = payload['jti']
    result.token = jwt.encode(payload=payload, key=os.environ.get('ACCESS_TOKEN_SECRET'), algorithm='HS512', headers=headers)
    return result


def parse_access_token(token: str) -> TokenVerificationDto:
    decoded_jwt = jwt.decode(jwt=token, key=os.environ.get('ACCESS_TOKEN_SECRET'), algorithms=['HS512'])

    result = TokenVerificationDto()

    result.userId = decoded_jwt['userId']

    return result
