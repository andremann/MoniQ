var dashboard = angular.module('dfmDashboard', ['ngRoute', 'home', 'scenarioFacts', 'reportExplorer', 'metricsExplorer', 'metricsGraphsExpose', 'controlsExplorer', 'configurationsExplorer']);

dashboard.config(function($routeProvider) {
		$routeProvider
			.when('/home', {templateUrl: './views/home/home.html', controller: 'HomeController'})
			.when('/scenarios/:scenario', {templateUrl: './views/scenario/scenario-facts.html', controller: 'ScenarioFactsController'})
			.when('/scenarios/:scenario/metrics/',  { templateUrl: './views/metrics/metrics-explorer.html', controller: 'MetricsExplorerController' })
			.when('/scenarios/:scenario/metrics/:metric',  { templateUrl: './views/metrics/metrics-explorer.html', controller: 'MetricsExplorerController' })
			.when('/scenarios/:scenario/expose',  { templateUrl: './views/expose/metrics-graphs-expose.html', controller: 'MetricsExposeController' })
			.when('/scenarios/:scenario/expose/:widgetConf',  { templateUrl: './views/expose/metrics-graphs-expose.html', controller: 'MetricsExposeController' })
			.when('/scenarios/:scenario/controls',  { templateUrl: './views/controls/controls-explorer.html', controller: 'ControlsExplorerController' })
			.when('/scenarios/:scenario/configurations',  { templateUrl: './views/configurations/configurations-explorer.html', controller: 'ConfigurationsExplorerController' })
			.when('/scenarios/:scenario/report',  { templateUrl: './views/report/global-report.html', controller: 'GlobalReportController' })
			.otherwise({ redirectTo: '/home' });
	}
);

dashboard.controller('NavController', function ($scope) {
	$scope.dashboard = {};
	$scope.dashboard.currentScenario = '';
});