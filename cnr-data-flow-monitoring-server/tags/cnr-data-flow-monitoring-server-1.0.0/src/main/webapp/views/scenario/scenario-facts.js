var module = angular.module('scenarioFacts', ['ngRoute', 'jlareau.pnotify']);

module.controller('ScenarioFactsController', function ($scope, $http, $routeParams, notificationService) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	$http.get('ajax/scenarios/' + $scope.dashboard.currentScenario)
	.success(
		function(data) {
			$scope.dashboard.scenarioFacts = data;
		})
	.error(
		function(data) {
			notificationService.error('Error..');
		}		
	);
});
