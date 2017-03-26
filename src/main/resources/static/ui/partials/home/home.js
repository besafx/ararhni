app.controller("homeCtrl", ['$scope', '$rootScope', '$timeout', 'TaskService', 'TaskOperationService', 'TaskCloseRequestService', 'PersonService', 'ModalProvider', 'ReportModelService',
    function ($scope, $rootScope, $timeout, TaskService, TaskOperationService, TaskCloseRequestService, PersonService, ModalProvider, ReportModelService) {

        PersonService.findActivePerson().then(function (data) {
            $scope.me = data;
        });

        ReportModelService.findAll().then(function (data) {
            $scope.reportModels = data;
            $rootScope.showNotify("الرئيسية", "تم تحميل نماذج الطباعة بنجاح", "success", "fa-dashboard");
        });

        $scope.delete = function (task) {
            if (task) {
                TaskService.remove(task.id);
                return;
            }
            TaskService.remove($scope.selected);
        };

        $scope.openCreateModel = function () {
            ModalProvider.openTaskCreateModel();
        };

        $scope.openUpdateModel = function (task) {
            if (task) {
                ModalProvider.openTaskUpdateModel(task);
                return;
            }
            ModalProvider.openTaskUpdateModel($scope.selected);
        };

        $scope.openCreateOperationModel = function (task) {
            if (task) {
                ModalProvider.openTaskOperationCreateModel(task);
                return;
            }
            ModalProvider.openTaskOperationCreateModel($scope.selected);
        };

        $scope.rowMenu = [];

        if ($rootScope.contains($rootScope.authorities, ['ROLE_TASK_CREATE'])) {
            $scope.rowMenu.push(
                {
                    html: '<div style="cursor: pointer;padding: 10px; text-align: right"><span>اضافة مهمة</span><span style="margin-left: 10px;" class="fa fa-plus fa-lg"></span></div>',
                    enabled: function () {
                        return true
                    },
                    click: function ($itemScope, $event, value) {
                        $scope.openCreateModel();
                    }
                });
        }

        if ($rootScope.contains($rootScope.authorities, ['ROLE_TASK_UPDATE'])) {
            $scope.rowMenu.push(
                {
                    html: '<div style="cursor: pointer;padding: 10px;text-align: right"><span>تعديل بيانات مهمة</span><span style="margin-left: 10px;" class="fa fa-edit fa-lg"></span></div>',
                    enabled: function () {
                        return true
                    },
                    click: function ($itemScope, $event, value) {
                        $scope.openUpdateModel($itemScope.task);
                    }
                });
        }

        if ($rootScope.contains($rootScope.authorities, ['ROLE_TASK_DELETE'])) {
            $scope.rowMenu.push(
                {
                    html: '<div style="cursor: pointer;padding: 10px;text-align: right"><span>حذف مهمة</span><span style="margin-left: 10px;" class="fa fa-minus-square-o fa-lg"></span></div>',
                    enabled: function () {
                        return true
                    },
                    click: function ($itemScope, $event, value) {
                        $scope.delete($itemScope.task);
                    }
                });
        }

        if ($rootScope.contains($rootScope.authorities, ['ROLE_TASK_OPERATION_CREATE'])) {
            $scope.rowMenu.push(
                {
                    html: '<div style="cursor: pointer;padding: 10px;text-align: right"><span>اضافة حركة</span><span style="margin-left: 10px;" class="fa fa-plus fa-lg"></span></div>',
                    enabled: function () {
                        return true
                    },
                    click: function ($itemScope, $event, value) {
                        $scope.openCreateOperationModel($itemScope.task);
                    }
                });
        }

        $scope.printTask = function (id, reportModel) {
            $rootScope.showNotify("الرئيسية", "جاري إعداد التقرير، فضلاً انتظر قليلاً", "warning", "fa-dashboard");
            TaskService.reportTask(id, JSON.parse(reportModel.template));
        };


    }]);
