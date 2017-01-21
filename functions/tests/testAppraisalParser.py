import json
import unittest
from unittest import mock

import requests
from pprint import pprint

from eveapimongo import MongoProvider

from functions.appraisalParser.appraisalParser import AppraisalParser, lambda_handler, EvepraisalParser


class Response:
    def __init__(self, status_code, url, text):
        self.status_code = status_code
        self.url = url
        self.text = text


class AppraisalParserTest(unittest.TestCase):
    def setUp(self):
        self.sut = AppraisalParser()

    def tearDown(self):
        mock.patch.stopall()

    def test_lambda(self):
        main_method = mock.patch.object(AppraisalParser, 'main').start()

        lambda_handler('event', 'context')

        self.assertEqual(main_method.call_count, 1)

    def test_main(self):
        contracts_in_db = [{
            "apiId": "588333d2623d5f6f694b6ec7",
            "type": "ItemExchange",
            "price": 453600000,
            "items": [
                {
                    "quantity": 21600,
                    "typeId": 4051
                }
            ],
            "reward": 0,
            "status": "Completed",
            "contractId": 113085803,
            "dateCompleted": "2017-01-11 20:12:37",
            "title": "1 Tower's Fuel @ 21k ea",
            "endStationId": 1022847969432,
            "assigneeId": 99007072,
            "volume": 108000,
            "numDays": 0,
            "acceptorId": 98171958
        }]
        expected = [{
            "apiId": "588333d2623d5f6f694b6ec7",
            "type": "ItemExchange",
            "price": 453600000,
            "items": [
                {
                    "quantity": 21600,
                    "typeId": 4051
                }
            ],
            "reward": 0,
            "status": "Completed",
            "contractId": 113085803,
            "dateCompleted": "2017-01-11 20:12:37",
            "title": "1 Tower's Fuel @ 21k ea",
            "endStationId": 1022847969432,
            "assigneeId": 99007072,
            "volume": 108000,
            "numDays": 0,
            "acceptorId": 98171958,
            'buy': 4.94,
            'sell': 5.06,
            'link': 'http://evepraisal.com/e/12345'
        }]
        find_contracts_method = mock.patch.object(self.sut, 'get_contracts', return_value=contracts_in_db).start()
        items_method = mock.patch.object(self.sut, 'get_items_with_names', return_value="items").start()
        appraisal_result = {'buy': 4.94, 'sell': 5.06, 'link': 'http://evepraisal.com/e/12345'}
        appraisal_method = mock.patch.object(self.sut, 'get_appraisal_details', return_value=appraisal_result).start()
        update_method = mock.patch.object(self.sut, 'update_contracts').start()

        self.sut.main()

        self.assertEqual(find_contracts_method.call_count, 1)
        self.assertEqual(appraisal_method.call_count, len(contracts_in_db))
        appraisal_method.assert_called_with("items")
        self.assertEqual(update_method.call_count, 1)
        self.assertEqual(items_method.call_count, 1)

    def test_get_contracts(self):
        expected = [{'type': 'ItemExchange', 'status': 'Outstanding'}]
        find_result = [{'type': 'ItemExchange', 'status': 'Outstanding'},
                       {'type': 'Courier'},
                       {'type': 'ItemExchange', 'status': 'Deleted'}]
        find_method = mock.patch.object(MongoProvider, 'find',
                                        return_value=find_result).start()

        result = self.sut.get_contracts()

        self.assertEqual(expected, result)
        self.assertEqual(find_method.call_count, 1)
        find_method.assert_called_with('contracts')

    def test_get_appraisal_details(self):
        link = "someLink"
        details = {'totals': {'buy': 5, 'sell': 7}}
        link_method = mock.patch.object(self.sut, 'get_appraisal_link', return_value=link).start()
        details_method = mock.patch.object(self.sut, 'get_appraisal', return_value=details).start()
        items = [{'Tritanium': 1}]
        expected = {'link': link, 'buy': 5, 'sell': 7}

        result = self.sut.get_appraisal_details(items)

        self.assertEqual(link_method.call_count, 1)
        link_method.assert_called_with(items)
        self.assertEqual(details_method.call_count, 1)
        details_method.assert_called_with(link + ".json")
        self.assertEqual(result, expected)

    def test_get_appraisal_link(self):
        expected_link = "http://evepraisal.com/e/15028987"
        items_with_names = [{'typeId': 37, 'typeName': 'Tritanium', 'quantity': 1}]
        expected_payload = {'raw_paste': "Tritanium x1", 'hide_buttons': 'false',
                            'paste_autosubmit': 'false',
                            'market': '30000142', 'save': 'true'}

        item_names_method = mock.patch.object(self.sut, 'get_items_with_names', return_value=items_with_names).start()
        request_method = mock.patch.object(requests, 'post',
                                           return_value=Response(200, 'estimateUrl',
                                                                 '<script>  document.title = "Evepraisal - Result #15028987 [Listing]";</script>')).start()

        result = self.sut.get_appraisal_link("dummy")

        item_names_method.assert_called_with("dummy")
        self.assertEqual(request_method.call_count, 1)
        request_method.assert_called_with("http://evepraisal.com/estimate", expected_payload)
        self.assertEqual(result, expected_link)

    def test_concatenate_items(self):
        items = [{'quantity': 1, 'typeName': 'Tritanium'}, {'quantity': 5, 'typeName': 'Helium Isotopes'}]
        expected = "Tritanium x1\nHelium Isotopes x5"

        result = self.sut.concatenate_items(items)

        self.assertEqual(result, expected)

    def test_get_items_with_names(self):
        items = [{'typeId': 16274, 'quantity': 5}]
        expected = [{'typeId': 16274, 'quantity': 5, 'typeName': 'Helium Isotopes'}]
        request_method = mock.patch.object(requests, 'get', return_value=Response(200, 'crestUrl',
                                                                                  '{"name": "Helium Isotopes"}')).start()
        result = self.sut.get_items_with_names(items)

        request_method.assert_called_with('https://crest-tq.eveonline.com/inventory/types/16274/')
        self.assertEqual(result, expected)

    def request(self):
        parser = EvepraisalParser()
        requested_items = "Tritanium x1\nHelium Isotopes x5"
        payload = {'raw_paste': requested_items, 'hide_buttons': 'false', 'paste_autosubmit': 'false',
                   'market': '30000142', 'save': 'true'}

        estimate = requests.post("http://evepraisal.com/estimate", payload)

        parser.feed(estimate.text)
        number = parser.data[0]
        link = "http://evepraisal.com/e/" + number

        things = requests.get(link + ".json")

        data = json.loads(things.text)
        pprint(data)
        pprint(data['totals'])

    def test_get_appraisal(self):
        link = 'appraisalUrl'
        request_method = mock.patch.object(requests, 'get', return_value=Response(200, link,
                                                                                  '{"totals": {"buy": 5, "sell": 7}}')).start()

        result = self.sut.get_appraisal(link)

        self.assertEqual({"totals": {"buy": 5, "sell": 7}}, result)
        request_method.assert_called_with(link)

    def test_get_contract_ids(self):
        contracts = [{'status': 'Completed', 'title': 'MWD Scimitar + Kin Hardener - Rigs in cargo', 'type': 'ItemExchange',
                      'assigneeId': 386292982, 'reward': 0.0, 'acceptorId': 258695360, 'endStationId': 60015108,
                      'price': 220000000.0, 'volume': 89000.0, 'dateCompleted': '2015-10-16 04:36:30', 'contractId': 97809127,
                      'numDays': 0},
                     {'status': 'Completed', 'title': '', 'type': 'ItemExchange', 'assigneeId': 386292982, 'reward': 0.0,
                      'acceptorId': 258695360, 'endStationId': 60015108, 'price': 149000000.0, 'volume': 216000.0,
                      'dateCompleted': '2015-10-16 04:39:27', 'contractId': 97884327, 'numDays': 0}]
        expected = [97809127, 97884327]

        result = self.sut.get_contract_ids(contracts)

        self.assertEqual(result, expected)

