'use strict';

describe('Controller Tests', function() {

    describe('Bankaccount Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBankaccount;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBankaccount = jasmine.createSpy('MockBankaccount');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Bankaccount': MockBankaccount
            };
            createController = function() {
                $injector.get('$controller')("BankaccountDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hipsterBankGatewayApp:bankaccountUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
