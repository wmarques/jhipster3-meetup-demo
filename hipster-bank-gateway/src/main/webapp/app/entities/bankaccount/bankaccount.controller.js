(function() {
    'use strict';

    angular
        .module('hipsterBankGatewayApp')
        .controller('BankaccountController', BankaccountController);

    BankaccountController.$inject = ['$scope', '$state', 'Bankaccount','$http','BankaccountSearch'];

    function BankaccountController ($scope, $state, Bankaccount, $http, BankaccountSearch) {
        var vm = this;
        vm.bankaccounts = [];
        vm.loadAll = function() {
            Bankaccount.query(function(result) {
                vm.bankaccounts = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            BankaccountSearch.query({query: vm.searchQuery}, function(result) {
                vm.bankaccounts = result;
            });
        };

        vm.loadAll();

        $http.get("feature-toggle").then(function(response){
            vm.bankaccountsVersion = response.data.account;
        });
    }
})();
