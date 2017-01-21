import json

import requests
from eveapimongo import MongoProvider

from HTMLParser import HTMLParser


class EvepraisalParser(HTMLParser):
    def __init__(self):
        HTMLParser.__init__(self)
        self.data = []

    def handle_data(self, data):
        if "Result #" in data:
            data = data.split('#')[1].split(' ')[0]
            self.data.append(data)


def lambda_handler(event, context):
    AppraisalParser().main()
    return "done"


class AppraisalParser:
    def main(self):
        result = []
        for contract in self.get_contracts():
            result.append(self.extend_contract(contract))
        self.update_contracts(result)

    def get_contracts(self):
        result = []
        for row in MongoProvider().find('contracts'):
            if row.get('type') != 'Courier' and row.get('status') != 'Deleted':
                result.append(row)
        return result

    def get_appraisal_details(self, items):
        link = self.get_appraisal_link(items)
        appraisal = self.get_appraisal(link + ".json")
        return {'link': link, 'buy': appraisal['totals']['buy'], 'sell': appraisal['totals']['sell']}

    def update_contracts(self, contracts):
        contract_ids = self.get_contract_ids(contracts)
        MongoProvider().cursor('contracts').delete_many({'contractId': {'$in': contract_ids}})
        MongoProvider().cursor('contracts').insert_many(contracts)

    def get_contract_ids(self, contracts):
        result = []
        for contract in contracts:
            result.append(contract['contractId'])
        return result

    def extend_contract(self, contract):
        items = self.get_items_with_names(contract['items'])
        details = self.get_appraisal_details(items)
        contract['buy'] = details['buy']
        contract['sell'] = details['sell']
        contract['link'] = details['link']
        return contract

    def get_appraisal_link(self, items):
        items_with_names = self.get_items_with_names(items)
        items_as_string = self.concatenate_items(items_with_names)
        payload = {'raw_paste': items_as_string, 'hide_buttons': 'false', 'paste_autosubmit': 'false',
                   'market': '30000142', 'save': 'true'}
        estimate = requests.post("http://evepraisal.com/estimate", payload)

        parser = EvepraisalParser()
        parser.feed(estimate.text)
        number = parser.data[0]
        link = "http://evepraisal.com/e/" + number

        return link

    def get_appraisal(self, link):
        response = requests.get(link)
        text = response.text
        return json.loads(text)

    def concatenate_items(self, items):
        quantified_items = []
        for item in items:
            quantified_items.append(item['typeName'] + " x" + str(item['quantity']))
        return "\n".join(quantified_items)

    def get_items_with_names(self, items):
        for item in items:
            type_id = item['typeId']
            response = requests.get("https://crest-tq.eveonline.com/inventory/types/%d/" % type_id)
            data = json.loads(response.text)
            item['typeName'] = data['name']
        return items


if __name__ == '__main__':
    AppraisalParser().main()
