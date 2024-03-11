import base64
import io
import json
import random
import uuid

from PIL import Image, ImageDraw
from django.core.cache import cache

from django.http import HttpRequest, HttpResponse
from rest_framework.decorators import api_view

from backend.models import LcmWebResult, CaptchaDto
from backend.utils.jwt_utils import generate_login_token


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

    cache.set(login_captcha_id, result, timeout=300000)

    return HttpResponse(json.dumps(LcmWebResult(result).__dict__, default=lambda o: o.__dict__))
