import json
import xml
from datetime import datetime
from html.parser import HTMLParser

import requests
from eveapimongo import MongoProvider


class EvepraisalParser(HTMLParser):
    def __init__(self):
        HTMLParser.__init__(self)
        self.data = []

    def handle_data(self, data):
        if "Result #" in data:
            # splitting "Result #5904 [Assets]"
            data = data.split('#')[1].split(' ')[0]
            self.data.append(data)


def lambda_handler(event, context):
    AppraisalParser().main()
    return "done"


class AppraisalParser:
    def main(self):
        result = []
        print("loading contracts")
        contracts = self.get_contracts()
        print("processing %d contracts" % len(contracts))
        for i in range(0, len(contracts) - 1):
            contract = contracts[i]
            if 'link' not in contract:
                print("processing contract %d/%d" % (i + 1, len(contracts)))
                result.append(self.extend_contract(contract))
        print("updating %d contracts" % len(result))
        if len(result) > 0:
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
        contract['client'] = self.get_character_name(contract['issuerId'])
        return contract

    def get_character_name(self, character_id):
        response = requests.get('https://api.eveonline.com/eve/CharacterName.xml.aspx?ids=%d' % character_id)
        rows = self.get_rows(response.text)
        return rows[0].get('name')

    def get_rows(self, xml_string):
        e = xml.etree.ElementTree.fromstring(xml_string)
        xml_result = e[1]

        if xml_result.tag == 'error':
            print("ERROR: " + xml_result.text)
            return None

        result = []
        for row in xml_result[0]:
            result.append(row)
        return result

    def get_appraisal_link(self, items):
        items_with_names = self.get_items_with_names(items)
        print("types has %d entries" % len(self.types))
        items_as_string = self.concatenate_items(items_with_names)
        payload = {'raw_paste': items_as_string, 'hide_buttons': 'false', 'paste_autosubmit': 'false',
                   'market': '30000142', 'save': 'true'}
        # this may fail on OS X https://github.com/kennethreitz/requests/issues/2022
        estimate = requests.post("https://skyblade.de/estimate", payload, verify=False)

        parser = EvepraisalParser()
        parser.feed(estimate.text)
        number = parser.data[0]
        link = "https://skyblade.de/e/" + number

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

    types = {}

    def get_items_with_names(self, items):
        new_items_count = 0
        before = datetime.now()
        for item in items:
            type_id = item['typeId']
            if type_id in self.types:
                item['typeName'] = self.types[type_id]
            else:
                url = "https://crest-tq.eveonline.com/inventory/types/%d/" % type_id
                response = requests.get(url)
                data = json.loads(response.text)
                item['typeName'] = data['name']
                self.types[type_id] = data['name']
                new_items_count += 1
        after = datetime.now()
        delta = after - before
        print("loading %d new items took %ds" % (new_items_count, delta.seconds))
        return items


if __name__ == '__main__':
    total_before = datetime.now()
    AppraisalParser().main()
    total_after = datetime.now()
    total_delta = total_after - total_before
    print("total duration: %ds" % total_delta.seconds)
