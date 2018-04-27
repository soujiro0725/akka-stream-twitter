# -*- coding:utf-8 -*-

import boto3
import json
from boto3.dynamodb.conditions import Key, Attr
import datetime
from datetime import timedelta
import dash
import dash_core_components as dcc
import dash_html_components as html
import pandas as pd
import plotly.graph_objs as go

dynamodb = boto3.client('dynamodb', endpoint_url='http://localhost:7777')
# print(dynamodb.describe_table(TableName='twitter-sentiment3'))


app = dash.Dash()
dateparse = lambda dates : pd.datetime(dates, '%Y%m%dT%H:%M')
df = pd.read_csv('./data/test.csv', parse_dates=['datetime'])


app.layout = html.Div([
    dcc.Graph(id='indicator-graphic'),

    dcc.Slider(
        id='datetime--slider',
        min=df['datetime'].min(),
        max=df['datetime'].max(),
        value=df['datetime'].max(),
        step=None, #timedelta(minutes=1),
        marks={str(dt): str(dt) for dt in df['datetime'].unique()}
    )
])

@app.callback(
    dash.dependencies.Output('indicator-graphic', 'figure'),
    [dash.dependencies.Input('datetime--slider', 'value')])
def update_graph(datetime_value):
    dff = df[df['datetime'] == datetime_value]

    print("printing logs...")
    print(dff)
    return {
        'data': [go.Scatter(
            x=dff['statusId'],
            y=dff['sentiment'],
            text=dff['userId'],
            mode='markers',
            marker={
                'size': 15,
                'opacity': 0.5,
                'line': {'width': 0.5, 'color': 'white'}
            }
        )],
        'layout': go.Layout(
            xaxis={
                'title': 'user',
                'type': 'linear'
            },
            yaxis={
                'title': 'sentiment',
                'type': 'linear'
            },
            margin={'l': 40, 'b': 40, 't': 10, 'r': 0},
            hovermode='closest'
        )
    }


if __name__ == '__main__':
    app.run_server(debug=True)
