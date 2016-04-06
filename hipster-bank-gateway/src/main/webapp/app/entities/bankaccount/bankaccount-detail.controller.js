(function() {
    'use strict';

    angular
        .module('hipsterBankGatewayApp')
        .controller('BankaccountDetailController', BankaccountDetailController);

    BankaccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Bankaccount'];

    function BankaccountDetailController($scope, $rootScope, $stateParams, entity, Bankaccount) {
        var vm = this;
        vm.bankaccount = entity;
        
        var unsubscribe = $rootScope.$on('hipsterBankGatewayApp:bankaccountUpdate', function(event, result) {
            vm.bankaccount = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
