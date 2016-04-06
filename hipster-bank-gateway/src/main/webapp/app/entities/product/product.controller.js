(function() {
    'use strict';

    angular
        .module('hipsterBankGatewayApp')
        .controller('ProductController', ProductController);

    ProductController.$inject = ['$scope', '$state', 'Product'];

    function ProductController ($scope, $state, Product) {
        var vm = this;
        vm.products = [];
        vm.loadAll = function() {
            Product.query(function(result) {
                vm.products = result;
            });
        };

        vm.loadAll();
        
    }
})();
