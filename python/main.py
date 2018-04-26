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
    html.Div([

        html.Div([
            dcc.RadioItems(
                id='xaxis-type',
                options=[{'label': i, 'value': i} for i in ['Linear', 'Log']],
                value='Linear',
                labelStyle={'display': 'inline-block'}
            )
        ],
                 style={'width': '48%', 'display': 'inline-block'}),

        html.Div([
            dcc.RadioItems(
                id='yaxis-type',
                options=[{'label': i, 'value': i} for i in ['Linear', 'Log']],
                value='Linear',
                labelStyle={'display': 'inline-block'}
            )
        ],style={'width': '48%', 'float': 'right', 'display': 'inline-block'})
    ]),

    dcc.Graph(id='indicator-graphic'),

    dcc.Slider(
        id='datetime--slider',
        min=df['datetime'].min(),
        max=df['datetime'].max(),
        value=df['datetime'].max(),
        step=timedelta(minutes=1),
        marks={str(dt): str(dt) for dt in df['datetime'].unique()}
    )
])

@app.callback(
    dash.dependencies.Output('indicator-graphic', 'figure'),
    [dash.dependencies.Input('xaxis-column', 'value'),
     dash.dependencies.Input('yaxis-column', 'value'),
     dash.dependencies.Input('xaxis-type', 'value'),
     dash.dependencies.Input('yaxis-type', 'value'),
     dash.dependencies.Input('datetime--slider', 'value')])
def update_graph(xaxis_column_name, yaxis_column_name,
                 xaxis_type, yaxis_type,
                 datetime_value):
    dff = df[df['datetime'] == datetime_value]

    return {
        'data': [go.Scatter(
            x=dff[dff['statusId'] == xaxis_column_name]['Value'],
            y=dff[dff['sentiment'] == yaxis_column_name]['Value'],
            text=dff[dff['userId'] == yaxis_column_name]['userId'],
            mode='markers',
            marker={
                'size': 15,
                'opacity': 0.5,
                'line': {'width': 0.5, 'color': 'white'}
            }
        )],
        'layout': go.Layout(
            xaxis={
                'title': xaxis_column_name,
                'type': 'linear' if xaxis_type == 'Linear' else 'log'
            },
            yaxis={
                'title': yaxis_column_name,
                'type': 'linear' if yaxis_type == 'Linear' else 'log'
            },
            margin={'l': 40, 'b': 40, 't': 10, 'r': 0},
            hovermode='closest'
        )
    }


if __name__ == '__main__':
    app.run_server(debug=True)
