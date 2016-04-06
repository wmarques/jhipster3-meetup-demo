(function() {
    'use strict';
    angular
        .module('hipsterBankGatewayApp')
        .factory('Product', Product);

    Product.$inject = ['$resource'];

    function Product ($resource) {
        var resourceUrl =  'insurances/' + 'api/products/:id';

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
