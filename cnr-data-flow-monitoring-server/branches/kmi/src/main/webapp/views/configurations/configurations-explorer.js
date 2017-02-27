var module = angular.module('configurationsExplorer', ['ngRoute', 'jlareau.pnotify', 'angular-json-editor']);

module.controller('ConfigurationsExplorerController', function ($scope, $http, $routeParams, $timeout, notificationService) {
	$scope.dashboard.currentScenario = $routeParams.scenario;
	
	$scope.configurationsExplorer = {};
	$scope.configurationsExplorer.stringParams = {"text" : "{}", "valid" : true};
	$scope.configurationsExplorer.editorOptions = {
		"mode": "code",
		"modes": ["tree", "form", "code"],
		"history": true
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
				// $scope.configurationsExplorer.stringParams.text = angular.toJson(configuration.params, true);
				delete $scope.configurationsExplorer.tmpConfiguration.$index;
			} else {
				//new
				$scope.configurationsExplorer.selectedConfigurationIndex = null;
				$scope.configurationsExplorer.tmpConfiguration = {
					monitoringScenario: $scope.dashboard.currentScenario,
					status: 'inactive',
					params: {"monitoringScenario": $scope.dashboard.currentScenario}
				};
				// $scope.configurationsExplorer.stringParams.text = "{\"monitoringScenario\" : \"" + $routeParams.scenario + "\"}";
			}
		}, 250);
	};
	
	$scope.configurationsExplorer.remove = function(configuration) {
		var result = confirm("Want to delete?");
		if (result) {
		    //Logic to delete the item
			$scope.configurationsExplorer.selectedConfigurationIndex = $scope.configurationsExplorer.configurations.indexOf(configuration);
			$http['delete']('ajax/scenarios/' + $routeParams.scenario + '/configurations/' + configuration.id)
			.success(
				function(data) {
					$scope.configurationsExplorer.configurations.splice($scope.configurationsExplorer.selectedConfigurationIndex, 1);
					notificationService.success('Success!!!');
				}
			)
			.error(
				function(data) {
					notificationService.error('Error..');
				}
			);
		}
	};
	
	$scope.configurationsExplorer.save = function() {
		$scope.configurationsExplorer.tmpConfiguration.lastModified = new Date().getTime();
		// $scope.configurationsExplorer.tmpConfiguration.params = angular.fromJson($scope.configurationsExplorer.stringParams.text);
		if ($scope.configurationsExplorer.selectedConfigurationIndex != null) {
			// update
			$http.put('ajax/scenarios/' + $routeParams.scenario + '/configurations', $scope.configurationsExplorer.tmpConfiguration)
			.success(
				function(data) {
					$scope.configurationsExplorer.configurations[$scope.configurationsExplorer.selectedConfigurationIndex] = $scope.configurationsExplorer.tmpConfiguration;
					notificationService.success('Success!!!');
				}
			)
			.error(
				function(data) {
					notificationService.error('Error..');
				}
			);
		} else {
			// new
			$http.post('ajax/scenarios/' + $routeParams.scenario + '/configurations', $scope.configurationsExplorer.tmpConfiguration)
			.success(
				function(data) {
					$scope.configurationsExplorer.tmpConfiguration.id = data;
					$scope.configurationsExplorer.configurations.push($scope.configurationsExplorer.tmpConfiguration);
					notificationService.success('Success!!!');
				}
			)
			.error(
				function(data) {
					notificationService.error('Error..');
				}
			);
		}
		$('#configurationModal').modal('hide');
	};
	
	$scope.configurationsExplorer.setStatus = function(status) {
		$scope.configurationsExplorer.tmpConfiguration.status = status;
	};

	// $scope.configurationsExplorer.validate = function() {
	// 	try {
	// 		JSON.parse($scope.configurationsExplorer.stringParams.text);
	// 		$scope.configurationsExplorer.stringParams.valid = true;
	// 	} catch(err) {
	// 		$scope.configurationsExplorer.stringParams.valid = false;
	// 	}
	// };

});