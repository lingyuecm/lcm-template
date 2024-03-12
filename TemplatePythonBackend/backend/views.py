import base64
import io
import json
import random
import uuid

import bcrypt
from PIL import Image, ImageDraw
from django.core.cache import cache

from django.http import HttpRequest, HttpResponse
from rest_framework.decorators import api_view

from backend.models import CaptchaDto, LoginDto, wrap_result_to_json
from backend.utils import cursor
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

    return HttpResponse(wrap_result_to_json(result))


@api_view(["POST"])
def login(request: HttpRequest) -> HttpResponse:
    body_dict = json.loads(request.body)
    login_captcha_id = parse_login_token(body_dict['token'])
    if not (login_captcha_id in cache):
        return HttpResponse(wrap_result_to_json(None, -4, 'Failed to login'))

    captcha_text = cache.get(login_captcha_id)
    if not (body_dict['captcha'] == captcha_text):
        return HttpResponse(wrap_result_to_json(None, -4, 'Failed to login'))

    cursor.execute('SELECT USER_ID, LOGIN_PWD FROM BIZ_USER WHERE PHONE_NO = %s',
                   body_dict['phoneNo'])
    user_dto = cursor.fetchone()
    if not bcrypt.checkpw(bytes(body_dict['password'], encoding='utf8'),
                          bytes(user_dto['LOGIN_PWD'], encoding='utf8')):
        return HttpResponse(wrap_result_to_json(None, -4, 'Failed to login'))

    login_dto = LoginDto()

    access_token_dto = generate_access_token(user_dto['USER_ID'])
    login_dto.token = access_token_dto.token

    return HttpResponse(wrap_result_to_json(login_dto))


@api_view(['GET'])
def metadata(request: HttpRequest) -> HttpResponse:
    cursor.execute('SELECT FIRST_NAME AS firstName, LAST_NAME AS lastName FROM BIZ_USER WHERE USER_ID = %s',
                   request.query_params['userId'])
    person_name = cursor.fetchone()
    return HttpResponse(wrap_result_to_json({
        'firstName': person_name['firstName'],
        'lastName': person_name['lastName']}))
