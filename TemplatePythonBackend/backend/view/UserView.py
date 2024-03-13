from django.http import HttpRequest, HttpResponse
from rest_framework.decorators import api_view

from backend.models import wrap_paged_list_to_json
from backend.utils import cursor


@api_view(['GET'])
def get_users(request: HttpRequest) -> HttpResponse:
    page_no = int(request.query_params['pageNo'])
    page_size = int(request.query_params['pageSize'])

    cursor.execute('SELECT USER_ID AS userId, PHONE_NO AS phoneNo, FIRST_NAME AS firstName, LAST_NAME AS lastName'
                   ' FROM BIZ_USER LIMIT %s, %s', [(page_no - 1) * page_size, page_size])
    users = cursor.fetchall()

    cursor.execute('SELECT COUNT(USER_ID) AS totalCount FROM BIZ_USER')
    user_count = cursor.fetchone()

    return HttpResponse(wrap_paged_list_to_json(user_count['totalCount'], users))
