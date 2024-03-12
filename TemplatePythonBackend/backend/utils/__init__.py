import os

import pymysql
from pymysql.cursors import DictCursor

conn = pymysql.connect(host=os.environ.get('DB_HOST'), port=int(os.environ.get('DB_PORT')),
                       user=os.environ.get('DB_USERNAME'),
                       password=os.environ.get('DB_PASSWORD'), database=os.environ.get('DB_NAME'))
cursor = conn.cursor(cursor=DictCursor)
