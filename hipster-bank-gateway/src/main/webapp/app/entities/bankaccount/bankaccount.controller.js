(function() {
    'use strict';

    angular
        .module('hipsterBankGatewayApp')
        .controller('BankaccountController', BankaccountController);

    BankaccountController.$inject = ['$scope', '$state', 'Bankaccount'];

    function BankaccountController ($scope, $state, Bankaccount) {
        var vm = this;
        vm.bankaccounts = [];
        vm.loadAll = function() {
            Bankaccount.query(function(result) {
                vm.bankaccounts = result;
            });
        };

        vm.loadAll();
        
    }
})();
