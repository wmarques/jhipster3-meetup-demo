(function() {
    'use strict';

    angular
        .module('hipsterBankGatewayApp')
        .controller('BankaccountDialogController', BankaccountDialogController);

    BankaccountDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Bankaccount'];

    function BankaccountDialogController ($scope, $stateParams, $uibModalInstance, entity, Bankaccount) {
        var vm = this;
        vm.bankaccount = entity;

        var onSaveSuccess = function (result) {
            $scope.$emit('hipsterBankGatewayApp:bankaccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.bankaccount.id !== null) {
                Bankaccount.update(vm.bankaccount, onSaveSuccess, onSaveError);
            } else {
                Bankaccount.save(vm.bankaccount, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
