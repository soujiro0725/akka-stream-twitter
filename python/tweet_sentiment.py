import boto3
from boto3.dynamodb.conditions import Key, Attr

dynamodb = boto3.client('dynamodb', endpoint_url='http://localhost:7777')
# print(dynamodb.describe_table(TableName='twitter-sentiment3'))

class TweetSentiment(object):
    def __init__(self, client):
        self.client = client
        self.table_name = 'twitter-sentiment3'

    def get(self):
        return self.client.scan(TableName=self.table_name)

    def save_csv(self):
        print("saving csv file...")

if __name__ == '__main__':
    client = TweetSentiment(dynamodb)
    print(client.get())
