import base64
import io
import json
import os
import random
import uuid

import bcrypt
import pymysql
from PIL import Image, ImageDraw
from django.core.cache import cache

from django.http import HttpRequest, HttpResponse
from pymysql.cursors import DictCursor
from rest_framework.decorators import api_view

from backend.models import LcmWebResult, CaptchaDto, LoginDto
from backend.utils.jwt_utils import generate_login_token, parse_login_token, generate_access_token


# Create your views here.

@api_view(["POST"])
def captcha(request: HttpRequest) -> HttpResponse:
    body_dict = json.loads(request.body)
    image: Image = Image.new(mode='RGB', size=(body_dict['captchaWidth'], body_dict['captchaHeight']))
    draw: ImageDraw = ImageDraw.Draw(image)

    captcha_text: str = ''.join(random.choices('0123456789', k=6))
    print(captcha_text)
    draw.text((10, 10), captcha_text, fill='white')

    byte_io = io.BytesIO()
    image.save(byte_io, format='PNG')

    result: CaptchaDto = CaptchaDto()

    login_captcha_id: str = uuid.uuid4().__str__()
    result.token = generate_login_token(login_captcha_id)
    result.captchaImage = base64.b64encode(byte_io.getvalue()).decode('utf-8')

    cache.set(login_captcha_id, captcha_text, timeout=300000)

    return HttpResponse(json.dumps(LcmWebResult(result_body=result).__dict__, default=lambda o: o.__dict__))


@api_view(["POST"])
def login(request: HttpRequest) -> HttpResponse:
    body_dict = json.loads(request.body)
    login_captcha_id = parse_login_token(body_dict['token'])
    if not (login_captcha_id in cache):
        return HttpResponse(json.dumps(LcmWebResult(None, result_code=-4, result_message='Failed to login').__dict__,
                                       default=lambda o: o.__dict__))

    captcha_text = cache.get(login_captcha_id)
    if not (body_dict['captcha'] == captcha_text):
        return HttpResponse(json.dumps(LcmWebResult(None, result_code=-4, result_message='Failed to login').__dict__,
                                       default=lambda o: o.__dict__))

    conn = pymysql.connect(host=os.environ.get('DB_HOST'), port=int(os.environ.get('DB_PORT')),
                           user=os.environ.get('DB_USERNAME'), password=os.environ.get('DB_PASSWORD'),
                           database=os.environ.get('DB_NAME'))
    cursor = conn.cursor(cursor=DictCursor)
    cursor.execute('SELECT USER_ID, LOGIN_PWD FROM BIZ_USER WHERE PHONE_NO = %s',
                   body_dict['phoneNo'])
    user_dto = cursor.fetchone()
    if not bcrypt.checkpw(bytes(body_dict['password'], encoding='utf8'),
                          bytes(user_dto['LOGIN_PWD'], encoding='utf8')):
        return HttpResponse(json.dumps(LcmWebResult(None, result_code=-4, result_message='Failed to login').__dict__,
                                       default=lambda o: o.__dict__))

    login_dto = LoginDto()

    access_token_dto = generate_access_token(user_dto['USER_ID'])
    login_dto.token = access_token_dto.token

    conn.close()
    return HttpResponse(json.dumps(LcmWebResult(login_dto).__dict__, default=lambda o: o.__dict__))
