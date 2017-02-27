var dashboard = angular.module('dfmDashboard', ['ngRoute', 'metricsExplorer', 'metricsGraphsExpose', 'controlsExplorer', 'configurationsExplorer']);

dashboard.config(function($routeProvider) {
		$routeProvider
			.when('/home', {templateUrl: './views/home.html', controller: 'HomeController'})
			.when('/scenarios/:scenario', {templateUrl: './views/scenario-home.html', controller: 'ScenarioHomeController'})
			.when('/scenarios/:scenario/metrics/',  { templateUrl: './views/metrics-explorer.html', controller: 'MetricsExplorerController' })
			.when('/scenarios/:scenario/metrics/:metric',  { templateUrl: './views/metrics-explorer.html', controller: 'MetricsExplorerController' })
			.when('/scenarios/:scenario/expose',  { templateUrl: './views/metrics-graphs-expose.html', controller: 'MetricsExposeController' })
			.when('/scenarios/:scenario/controls',  { templateUrl: './views/controls-explorer.html', controller: 'ControlsExplorerController' })
			.when('/scenarios/:scenario/configurations',  { templateUrl: './views/configurations-explorer.html', controller: 'ConfigurationsExplorerController' })
			.otherwise({ redirectTo: '/home' });
	}
);

dashboard.controller('HomeController', function ($scope, $http) {
	$scope.dashboard.currentScenario = '';
	$http.get('ajax/scenarios')
	.success(
		function(data) {
			$scope.dashboard.scenarios = data;
		})
	.error(
		function(data) {
			
		}		
	);
});

dashboard.controller('NavController', function ($scope) {
	$scope.dashboard = {};
	$scope.dashboard.currentScenario = '';
});

dashboard.controller('ScenarioHomeController', function ($scope, $http, $routeParams) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	$http.get('ajax/scenarios/' + $scope.dashboard.currentScenario)
	.success(
		function(data) {
			$scope.dashboard.scenarioFacts = data;
		})
	.error(
		function(data) {
			
		}		
	);
});