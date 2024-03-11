import jwt


def generate_login_token(login_captcha_id: str) -> str:
    headers = {
        'type': 'jwt',
        'alg': 'HS256'
    }
    payload = {
        'loginCaptchaId': login_captcha_id
    }
    return jwt.encode(payload=payload, key='lingyuecm123', algorithm='HS256', headers=headers)
