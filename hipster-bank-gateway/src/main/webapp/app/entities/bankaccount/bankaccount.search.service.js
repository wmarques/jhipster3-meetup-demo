(function() {
    'use strict';

    angular
        .module('hipsterBankGatewayApp')
        .factory('BankaccountSearch', BankaccountSearch);

    BankaccountSearch.$inject = ['$resource'];

    function BankaccountSearch($resource) {
        var resourceUrl =  'accountsmicroservice/' + 'api/_search/bankaccounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
