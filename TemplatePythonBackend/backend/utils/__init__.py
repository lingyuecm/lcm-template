import os

import pymysql
from dbutils.pooled_db import PooledDB
from pymysql.cursors import DictCursor

pool = PooledDB(creator=pymysql, mincached=5, maxconnections=10, host=os.environ.get('DB_HOST'),
                port=int(os.environ.get('DB_PORT')), user=os.environ.get('DB_USERNAME'),
                password=os.environ.get('DB_PASSWORD'), database=os.environ.get('DB_NAME'))
conn = pool.connection()
cursor = conn.cursor(cursor=DictCursor)
