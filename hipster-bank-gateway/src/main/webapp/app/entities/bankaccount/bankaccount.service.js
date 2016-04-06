(function() {
    'use strict';
    angular
        .module('hipsterBankGatewayApp')
        .factory('Bankaccount', Bankaccount);

    Bankaccount.$inject = ['$resource'];

    function Bankaccount ($resource) {
        var resourceUrl =  'accountsmicroservice/' + 'api/bankaccounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
