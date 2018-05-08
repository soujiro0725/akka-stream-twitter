import json
import pprint
import csv
import boto3
from boto3.dynamodb.conditions import Key, Attr

dynamodb = boto3.client('dynamodb', endpoint_url='http://localhost:7777')
# print(dynamodb.describe_table(TableName='twitter-sentiment3'))
pp = pprint.PrettyPrinter(indent=4)

class TweetSentiment(object):
    def __init__(self, client):
        self.client = client
        self.table_name = 'twitter-sentiment3'

    """
    @return list[Item]
    """
    def get(self):
        scan_result = self.client.scan(TableName=self.table_name)
        return scan_result['Items']

    """
    row structure
    (index),userId,statusId,datetime,sentiment

    return scan_result["Items"][5]["userId"].get('S')
    """
    def save_csv(self):
        arry = self.get()
        with open('./data/result.csv', 'w') as f:
            writer = csv.writer(f, lineterminator='\n')
            for item in arry:
                # TODO make conditional if each attribute is stored.
                writer.writerow([item['userId'].get('S'),
                                 item['statusId'].get('S'),
                                 item['datetime'].get('S'),
                                 item['rt'].get('S'),                                 
                                 item['sentiment'].get('S')
                ])

if __name__ == '__main__':
    client = TweetSentiment(dynamodb)
    print(client.save_csv())
