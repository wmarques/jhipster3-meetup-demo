(function() {
    'use strict';

    angular
        .module('hipsterBankGatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bankaccount', {
            parent: 'entity',
            url: '/bankaccount',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Bankaccounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bankaccount/bankaccounts.html',
                    controller: 'BankaccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('bankaccount-detail', {
            parent: 'entity',
            url: '/bankaccount/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Bankaccount'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bankaccount/bankaccount-detail.html',
                    controller: 'BankaccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Bankaccount', function($stateParams, Bankaccount) {
                    return Bankaccount.get({id : $stateParams.id});
                }]
            }
        })
        .state('bankaccount.new', {
            parent: 'bankaccount',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bankaccount/bankaccount-dialog.html',
                    controller: 'BankaccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                clientname: null,
                                balance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bankaccount', null, { reload: true });
                }, function() {
                    $state.go('bankaccount');
                });
            }]
        })
        .state('bankaccount.edit', {
            parent: 'bankaccount',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bankaccount/bankaccount-dialog.html',
                    controller: 'BankaccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bankaccount', function(Bankaccount) {
                            return Bankaccount.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bankaccount', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bankaccount.delete', {
            parent: 'bankaccount',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bankaccount/bankaccount-delete-dialog.html',
                    controller: 'BankaccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bankaccount', function(Bankaccount) {
                            return Bankaccount.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bankaccount', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
