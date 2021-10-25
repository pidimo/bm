angular.module('starter.scheduleroutes', ['starter.schedulecontrollers','starter.constantsSchedule'])

.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider
  
  
  .state('app.schedulerDay', {
    url: "/schedulerDay",
    cache: false,
    views: {
      'menuContent': {
        templateUrl: "templates/views_schedule/scheduler_day.html",
        controller: "ControlSchedule"
      }
    }
  })
  
  .state('app.schedulerDetail', {
    url: "/schedulerDetail?appointmentId",
    cache: false,
    views: {
      'menuContent': {
        templateUrl: "templates/views_schedule/appointmentDetail.html",
        controller: "ControlScheduleDetail"
      }
    }
  })

  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/login');
});