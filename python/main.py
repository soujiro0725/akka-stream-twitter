# -*- coding:utf-8 -*-

import os
from flask import send_from_directory
import boto3
import json
from boto3.dynamodb.conditions import Key, Attr
import datetime
from datetime import timedelta
from dateutil.relativedelta import relativedelta
import dash
import dash_core_components as dcc
import dash_html_components as html
import pandas as pd
import plotly.graph_objs as go

dynamodb = boto3.client('dynamodb', endpoint_url='http://localhost:7777')
# print(dynamodb.describe_table(TableName='twitter-sentiment3'))


app = dash.Dash()
app.css.config.serve_locally = True
app.scripts.config.serve_locally = True

dateparse = lambda dates : pd.datetime(dates, '%Y%m%dT%H:%M')
df = pd.read_csv('./data/test.csv', parse_dates=['datetime'])

epoch = datetime.datetime.utcfromtimestamp(0)
def unix_time_millis(dt):
    return (dt - epoch).total_seconds() #* 1000.0

def get_marks_from_start_end(start, end):
    ''' Returns dict with one item per month
    {1440080188.1900003: '2015-08',
    '''
    result = []
    current = start
    while current <= end:
        result.append(current)
        current += relativedelta(months=1)
    return {unix_time_millis(m):(str(m.strftime('%Y-%m'))) for m in result}

min=unix_time_millis(df['datetime'].min())
max=unix_time_millis(df['datetime'].max())

app.layout = html.Div([
    html.Link(
        rel='stylesheet',
        href='/static/stylesheet.css'
    ),
    html.Link(
        rel='stylesheet',
        href='/static/bootstrap.css'
    ),
    html.H1(children='dashboard',
            className='twelve columns'),
    
    html.Div([ # row
        html.Div([
            html.Div(id='example-graph'),
            dcc.Graph(
                id='example-graph',
                figure={
                    'data': [
                        {'x': [1, 2, 3], 'y': [4, 1, 2], 'type': 'bar', 'name': 'SF'},
                        {'x': [1, 2, 3], 'y': [2, 4, 5], 'type': 'bar', 'name': u'Montréal'},
                    ],
                    'layout': {
                        'title': 'Graph 1',
                        'xaxis' : dict(
                            title='x Axis',
                            titlefont=dict(
                                family='Courier New, monospace',
                                size=20,
                                color='#7f7f7f'
                            )),
                        'yaxis' : dict(
                            title='y Axis',
                            titlefont=dict(
                                family='Helvetica, monospace',
                                size=20,
                                color='#7f7f7f'
                            ))
                    }
                }
            )
        ], className='six columns'),
        
    html.Div([
        dcc.Graph(id='indicator-graphic'),
        dcc.RangeSlider(
            id='datetime--slider',
            min=min,
            max=max,
            value=[min, max],
            marks=get_marks_from_start_end(df['datetime'].min(), df['datetime'].max()),
        ),
        html.Div(id='rangeslider-output')
    ], className='six columns')
    ], className='row')
],
                      style={
                          # 'background-color': '#002b36'
                      }
)

@app.server.route('/static/<path:path>')
def static_file(path):
    static_folder = os.path.join(os.getcwd(), 'static')
    print(static_folder)
    return send_from_directory(static_folder, path)

@app.callback(
    dash.dependencies.Output('rangeslider-output', 'children'),
    [dash.dependencies.Input('datetime--slider', 'value')])
def update_output(value):
    return 'time range is  "{} - {}"'.format(datetime.datetime.fromtimestamp(value[0]),
                                             datetime.datetime.fromtimestamp(value[1]))

@app.callback(
    dash.dependencies.Output('indicator-graphic', 'figure'),
    [dash.dependencies.Input('datetime--slider', 'value')])
def update_graph(datetime_value_list):
    min = datetime.datetime.fromtimestamp(datetime_value_list[0])
    max = datetime.datetime.fromtimestamp(datetime_value_list[1])
    dff = df[(min <= df['datetime']) & (df['datetime'] <= max)]
    
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
