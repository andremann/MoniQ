var module = angular.module('configurationsExplorer', ['ui.codemirror', 'ngRoute']);

module.controller('ConfigurationsExplorerController', function ($scope, $http, $routeParams, $timeout) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	
	$scope.configurationsExplorer = {};
	$scope.configurationsExplorer.stringParams = {"text" : "{}", "valid" : true};
	$scope.configurationsExplorer.editorOptions = {
	        lineWrapping : true,
	        lineNumbers: true,
	        readOnly: false,
	        mode: 'application/json',
	    };
	
	$http.get('ajax/scenarios/'+ $routeParams.scenario +'/configurations/')
	.success(
		function(data) {
			$scope.configurationsExplorer.configurations = data;
		}
	)
	.error(
		function(data) {
			
		}
	);
	
	$scope.configurationsExplorer.open = function(configuration) {
		$('#configurationModal').modal('show');
		$timeout(function() {
			if (configuration) {
				//open
				$scope.configurationsExplorer.selectedConfigurationIndex = $scope.configurationsExplorer.configurations.indexOf(configuration);
				$scope.configurationsExplorer.tmpConfiguration = angular.copy(configuration);
				$scope.configurationsExplorer.stringParams.text = angular.toJson(configuration.params, true);
				delete $scope.configurationsExplorer.tmpConfiguration.$index;
			} else {
				//new
				$scope.configurationsExplorer.selectedConfigurationIndex = null;
				$scope.configurationsExplorer.tmpConfiguration = {monitoringScenario : $scope.dashboard.currentScenario, status: 'inactive'};
				$scope.configurationsExplorer.stringParams.text = "{\"monitoringScenario\" : \"" + $routeParams.scenario + "\"}";
			}
		}, 500);
	}
	
	$scope.configurationsExplorer.remove = function(configuration) {
		var result = confirm("Want to delete?");
		if (result) {
		    //Logic to delete the item
			$scope.configurationsExplorer.selectedConfigurationIndex = $scope.configurationsExplorer.configurations.indexOf(configuration);
			$http['delete']('ajax/scenarios/' + $routeParams.scenario + '/configurations/' + configuration.id)
			.success(
				function(data) {
					$scope.configurationsExplorer.configurations.splice($scope.configurationsExplorer.selectedConfigurationIndex, 1);
				}
			)
			.error(
				function(data) {
					alert("error");
				}
			);
		}
	}
	
	$scope.configurationsExplorer.save = function() {
		$scope.configurationsExplorer.tmpConfiguration.lastModified = new Date().getTime();
		$scope.configurationsExplorer.tmpConfiguration.params = angular.fromJson($scope.configurationsExplorer.stringParams.text);
		if ($scope.configurationsExplorer.selectedConfigurationIndex != null) {
			// update
			$http.put('ajax/scenarios/' + $routeParams.scenario + '/configurations', $scope.configurationsExplorer.tmpConfiguration)
			.success(
				function(data) {
					$scope.configurationsExplorer.configurations[$scope.configurationsExplorer.selectedConfigurationIndex] = $scope.configurationsExplorer.tmpConfiguration;
				}
			)
			.error(
				function(data) {
					alert("error");
				}
			);
		} else {
			// new
			$http.post('ajax/scenarios/' + $routeParams.scenario + '/configurations', $scope.configurationsExplorer.tmpConfiguration)
			.success(
				function(data) {
					$scope.configurationsExplorer.tmpConfiguration.id = data;
					$scope.configurationsExplorer.configurations.push($scope.configurationsExplorer.tmpConfiguration);
				}
			)
			.error(
				function(data) {
					alert("error");
				}
			);
		}
		$('#configurationModal').modal('hide');
	}
	
	$scope.configurationsExplorer.setStatus = function(status) {
		$scope.configurationsExplorer.tmpConfiguration.status = status;
	}
	
	$scope.configurationsExplorer.validate = function() {
		try {
			JSON.parse($scope.configurationsExplorer.stringParams.text);
			$scope.configurationsExplorer.stringParams.valid = true;
		} catch(err) {
			$scope.configurationsExplorer.stringParams.valid = false;
		}
	}
});