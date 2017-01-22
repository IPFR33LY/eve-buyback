import json
from datetime import datetime

from eveapimongo import MongoProvider


def is_favicon(environ):
    return environ.get('PATH_INFO', '').lstrip('/') == "favicon.ico"


def get_pending():
    contracts = []
    for row in get_pending_contracts():
        contracts.append(row)
    for contract in contracts:
        del contract['_id']
        del contract['apiId']
        if 'buy' not in contract or 'sell' not in contract or 'link' not in contract or 'client' not in contract:
            contract['buy'] = 0
            contract['sell'] = 0
            contract['link'] = 'pending'
            contract['client'] = 'pending'
    return contracts


def get_pending_contracts():
    return MongoProvider().find_filtered('contracts', {'status': 'Outstanding', 'type': 'ItemExchange'})


def process_request(environ):
    url_path = environ.get('PATH_INFO', '').lstrip('/')
    print(url_path)
    if url_path == "pending":
        return get_pending()
    return {'status': 'path ' + url_path + ' not found'}


def app(environ, start_response):
    # skip favicon
    if is_favicon(environ):
        start_response('200 OK', [('Content-Type', 'text/html')])
        return [""]

    # parameters = parse_qs(environ.get('QUERY_STRING', ''))

    before = datetime.now()
    response_data = process_request(environ)
    start_response('200 OK', [('Content-Type', 'application/json'), ("Access-Control-Allow-Origin", "*")])
    after = datetime.now()
    delta = after - before
    print("processing took %d seconds" % delta.seconds)

    return json_to_response(response_data)

def json_to_response(some_json):
    return [str.encode(json.dumps(some_json))]


if __name__ == '__main__':
    from wsgiref.simple_server import make_server

    port = 9000
    srv = make_server('0.0.0.0', port, app)

    print("listening on port " + str(port))
    srv.serve_forever()
