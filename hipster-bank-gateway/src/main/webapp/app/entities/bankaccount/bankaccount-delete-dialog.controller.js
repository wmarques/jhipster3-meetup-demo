(function() {
    'use strict';

    angular
        .module('hipsterBankGatewayApp')
        .controller('BankaccountDeleteController',BankaccountDeleteController);

    BankaccountDeleteController.$inject = ['$uibModalInstance', 'entity', 'Bankaccount'];

    function BankaccountDeleteController($uibModalInstance, entity, Bankaccount) {
        var vm = this;
        vm.bankaccount = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Bankaccount.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
