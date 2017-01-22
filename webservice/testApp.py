import unittest

from unittest import mock

from eveapimongo import MongoProvider

from webservice.app import get_pending


class AppTest(unittest.TestCase):
    def setUp(self):
        pass

    def test_get_pending(self):
        mocked_data = [item_exchange_deleted, courier, outstanding_with_pricing, outstanding_without_pricing]
        find_method = mock.patch.object(MongoProvider, 'find', return_value=mocked_data).start()
        expected = [outstanding_with_pricing, {
            "_id": "5883743cc846b40001d9f81c",
            "sell": "pending",
            "buy": "pending",
            "link": "pending",
            "status": "Outstanding",
            "dateCompleted": "2017-01-11 20:12:37",
            "reward": 0,
            "title": "1 Tower's Fuel @ 21k ea",
            "assigneeId": 99007072,
            "items": [
                {
                    "typeId": 4051,
                    "typeName": "Nitrogen Fuel Block",
                    "quantity": 21600
                }
            ],
            "acceptorId": 98171958,
            "volume": 108000,
            "endStationId": 1022847969432,
            "price": 453600000,
            "type": "ItemExchange",
            "apiId": "588333d2623d5f6f694b6ec7",
            "numDays": 0,
            "contractId": 113085803
        }]

        result = get_pending()

        for expect in expected:
            self.assertIn(expect, result)
        find_method.assert_called_with('contracts')


item_exchange_deleted = {
    "_id": "5883743cc846b40001d9f824",
    "status": "Deleted",
    "dateCompleted": "",
    "acceptorId": 0,
    "volume": 108000,
    "numDays": 0,
    "contractId": 113085795,
    "title": "1 Tower's Fuel @ 21k ea",
    "assigneeId": 99007072,
    "items": [
        {
            "typeId": 4051,
            "quantity": 21600
        }
    ],
    "apiId": "588333d2623d5f6f694b6ec7",
    "endStationId": 1022847969432,
    "reward": 0,
    "type": "ItemExchange",
    "price": 453600000
}

outstanding_with_pricing = {
    "_id": "5883743cc846b40001d9f81c",
    "status": "Outstanding",
    "sell": 477337536,
    "dateCompleted": "2017-01-11 20:12:37",
    "reward": 0,
    "title": "1 Tower's Fuel @ 21k ea",
    "assigneeId": 99007072,
    "items": [
        {
            "typeId": 4051,
            "typeName": "Nitrogen Fuel Block",
            "quantity": 21600
        }
    ],
    "acceptorId": 98171958,
    "volume": 108000,
    "buy": 467640000,
    "link": "http://evepraisal.com/e/15031439",
    "endStationId": 1022847969432,
    "price": 453600000,
    "type": "ItemExchange",
    "apiId": "588333d2623d5f6f694b6ec7",
    "numDays": 0,
    "contractId": 113085803
}

outstanding_without_pricing = {
    "_id": "5883743cc846b40001d9f81c",
    "status": "Outstanding",
    "dateCompleted": "2017-01-11 20:12:37",
    "reward": 0,
    "title": "1 Tower's Fuel @ 21k ea",
    "assigneeId": 99007072,
    "items": [
        {
            "typeId": 4051,
            "typeName": "Nitrogen Fuel Block",
            "quantity": 21600
        }
    ],
    "acceptorId": 98171958,
    "volume": 108000,
    "endStationId": 1022847969432,
    "price": 453600000,
    "type": "ItemExchange",
    "apiId": "588333d2623d5f6f694b6ec7",
    "numDays": 0,
    "contractId": 113085803
}

courier = {
    "_id": "5883743cc846b40001d9f81c",
    "status": "Outstanding",
    "dateCompleted": "2017-01-11 20:12:37",
    "reward": 0,
    "title": "1 Tower's Fuel @ 21k ea",
    "assigneeId": 99007072,
    "items": [
        {
            "typeId": 4051,
            "typeName": "Nitrogen Fuel Block",
            "quantity": 21600
        }
    ],
    "acceptorId": 98171958,
    "volume": 108000,
    "endStationId": 1022847969432,
    "price": 453600000,
    "type": "Courier",
    "apiId": "588333d2623d5f6f694b6ec7",
    "numDays": 0,
    "contractId": 113085803
}
