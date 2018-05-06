# -*- coding:utf-8 -*-

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
css_url = "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
app.css.append_css({
    "external_url": css_url
})

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

# colors = {
#     'background': '#111111',
#     'text': '#ffffff'
# }

app.layout = html.Div(
    html.Div([html.H3("Neat H3")], className='col-sm-4' ),
    html.Div([
        dcc.Graph(id='indicator-graphic'),
        dcc.RangeSlider(
            id='datetime--slider',
            min=min,
            max=max,
            value=[min, max],
            marks=get_marks_from_start_end(df['datetime'].min(), df['datetime'].max()),
        ),
        html.Div(id='rangeslider-output'),
    ],
    # style={
    #      'width':'50%',
    #      'margin':'auto'
    #      #     'background': colors['background'],
    #      #     'text':colors['text']
    #  }
    )
)

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
