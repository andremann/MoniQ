var module = angular.module('configurationsExplorer', ['ngRoute']);

module.controller('ConfigurationsExplorerController', function ($scope, $http, $routeParams) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	
	$scope.configurationsExplorer = {};
	
	$http.get('ajax/scenarios/'+ $routeParams.scenario +'/configurations/')
	.success(
		function(data) {
			$scope.configurationsExplorer.configuration = data;
		}
	)
	.error(
		function(data) {
			
		}
	);
});