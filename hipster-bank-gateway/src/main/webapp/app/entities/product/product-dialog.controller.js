(function() {
    'use strict';

    angular
        .module('hipsterBankGatewayApp')
        .controller('ProductDialogController', ProductDialogController);

    ProductDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Product'];

    function ProductDialogController ($scope, $stateParams, $uibModalInstance, entity, Product) {
        var vm = this;
        vm.product = entity;

        var onSaveSuccess = function (result) {
            $scope.$emit('hipsterBankGatewayApp:productUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.product.id !== null) {
                Product.update(vm.product, onSaveSuccess, onSaveError);
            } else {
                Product.save(vm.product, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
