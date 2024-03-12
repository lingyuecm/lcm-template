import re

from django.utils.deprecation import MiddlewareMixin

from backend.utils.jwt_utils import parse_access_token


class TokenMiddleware(MiddlewareMixin):
    no_auth_pattern = '^/user/captcha$|^/user/login$'

    def process_request(self, request):
        print(request.path)
        if re.match(self.no_auth_pattern, request.path):
            return

        token_verification_dto = parse_access_token(request.headers.get('Access-Token'))
        print(token_verification_dto.userId)

        request.GET._mutable = True
        request.GET['userId'] = token_verification_dto.userId
        pass
