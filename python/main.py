# python script to get data from dynmadb

import boto3
import json
from boto3.dynamodb.conditions import Key, Attr

dynamodb = boto3.client('dynamodb', endpoint_url='http://localhost:7777')
print(dynamodb.describe_table(TableName='twitter-sentiment3'))

