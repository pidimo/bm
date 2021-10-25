angular.module('starter.schedulecontrollers', ['starter.scheduleservices'])


.controller('ControlScheduleDetail', function(PopupFactory,$scope, $stateParams,scheduleService,pathSchedule,$ionicActionSheet) {
    $scope.pathSchedule = pathSchedule;

    

    console.log("==CONTROLLER SCHEDULE==", $stateParams.appointmentId);
    
    $scope.taskcall = scheduleService.query({'dto(appointmentId)':$stateParams.appointmentId});

    $scope.taskcall.$promise.then(function (results){
        // call factory 
        PopupFactory.getPopup($scope,results);

        $scope.tareas = results;
        $scope.prioridades = (results['mainData'])['priorityArray'];
        $scope.prid = parseInt((results['mainData'])['entity']['priorityId']);
        $scope.tipolist = (results['mainData'])['appointmentTypeArray'];
        $scope.appoid = parseInt((results['mainData'])['entity']['appointmentTypeId']);
          
        console.log("==CONTROLLER SCHEDULE== results detail:",results['mainData']);

    })
    
    $scope.conf = function(){
      $ionicActionSheet.show({
         buttons: [
         
         { text: 'Edit' }
          ],
          destructiveText: 'Delete ',
          cancelText: 'Cancel',
          destructiveButtonClicked: function(){
          }
      });
    };
    
})
// 
// CONTROLLER SCHEDULE
// 
.controller('ControlSchedule',function(PopupFactory,getAppointments,$http,apiUrlLocal,pathSchedule,$ionicScrollDelegate,$state,$window,COLOR_VIEW,COLOR_2,$scope,Load_variable_date,schedule_calculate_Next_Ant,$q,scheduleService,$localstorage,SCHEDULE_TYPE_MONTH,SCHEDULE_TYPE_WEEK,SCHEDULE_TYPE_DAY,SCHEDULE_TYPE_MONTH_STRING,SCHEDULE_TYPE_WEEK_STRING,SCHEDULE_TYPE_DAY_STRING){
  
  // COLOR DEFAULT
  $scope.colorFont = COLOR_VIEW;

  $scope.calendar;

  // GET USER INFO FOR LANGUAGE CALENDAR
  var userInfo = $localstorage.getObject('userInfo');
  $scope.languageCalendar = "";
  switch(userInfo.locale) {
    case 'es':
        $scope.languageCalendar = 'es-ES';
        break;
    case 'de':
        $scope.languageCalendar = 'de-DE';
        break;
    case 'fr':
        $scope.languageCalendar = 'fr-FR';
        break;    
  } 
  
  // change text colour 
  $('button').css({"color":COLOR_2});
  $('view-title').css({"color":COLOR_2});
  
  //  LOAD OBJECT IN LOCAL STORAGE
  Load_variable_date.setData();

  //  GET OBJECT OF LOCAL STORAGE
  var _data_date = $localstorage.getObject('dataDate');

  var date = new Date();

      var yyyy = date.getFullYear().toString();
      var ww = (date.getWeek()).toString().length == 1 ? "0"+(date.getWeek()).toString() : (date.getWeek()).toString();       
      var mm = (date.getMonth()+1).toString().length == 1 ? "0"+(date.getMonth()+1).toString() : (date.getMonth()+1).toString();
      var dd = (date.getDate()).toString().length == 1 ? "0"+(date.getDate()).toString() : (date.getDate()).toString();

  //  HELP TO SEE DATE IN VIEW
  $scope.real_date_view = yyyy+"-"+mm+"-"+dd;

  var options = {
    events_source: function () {       
        return [];
    
    }, 
    tmpl_path: 'lib/bootstrap-calendar/tmpls/',
    view: 'month',
    language: $scope.languageCalendar,
    tmpl_cache: false,
    day: yyyy+"-"+mm+"-"+dd,
    time_split: '60',
    width: '100%',   

    onClickADate: function(event) {
    	var aux = this.innerHTML;
      	var pos = aux.indexOf("data-cal-date=");
      	var res = aux.substring(pos+15, pos+25);     
		setTimeout(function(){
		console.log('First name being reset');
			$scope.$apply(function(){
				$scope.real_date_view = res;
				}
			)
		}, 0);
    },
    onAfterViewLoad: function(view)
    {     
      if ($scope.calendar != undefined) {
        console.log("calendar",$scope.calendar);

        var yyyy = $scope.calendar.options.position.start.getFullYear();
        var mmm = ($scope.calendar.options.position.start.getMonth()+1);
        var ddd = ($scope.calendar.options.position.start.getDate());  
        var www = ($scope.calendar.options.position.start.getWeek());

        var mm = (mmm).toString().length == 1 ? "0"+(mmm).toString() : (mmm).toString();
        var dd = (ddd).toString().length == 1 ? "0"+(ddd).toString() : (ddd).toString();
        var ww = (www).toString().length == 1 ? "0"+(www).toString() : (www).toString();

        // load date in calendar view
        var realDate = $scope.calendar.options.day;
        $scope.real_date_view = realDate;

        Load_variable_date.newValue(yyyy,mm,ww,dd,$scope.calendar.options.view);

        var _data_date = $localstorage.getObject('dataDate');
      };
      
      $scope.appointments =[];
      var _data_date = $localstorage.getObject('dataDate');
      
      // -------------------------------------
      $scope.newAppointments = scheduleService.query({type: _data_date.type,calendar: _data_date.data});

      //  PROMISE
      $scope.newAppointments.$promise.then(function (results){

        // call factory 
        PopupFactory.getPopup($scope,results);

        console.log("==CONTROLLER SCHEDULE== get query list appointments success OK");
        $scope.listAppointments = (results['mainData'])['appointmentsList'];
    
        //parse to variables PROVISIONAL
        $scope.appointments = [];
        angular.forEach($scope.listAppointments, function (appointment) {

          // APPOINTMENT RECURRENT GET ID WITHOUT "-"
          var str = appointment.virtualAppointmentId;
          var pos = str.indexOf("-"); 
          var idAppointment = appointment.virtualAppointmentId;
          if (pos > -1) {
            idAppointment = str.substring(0, pos);   
          };
          console.log("==CONTROLLER SCHEDULE== id appointment without '-' IN RECURRENT", idAppointment);
          
          // load appointment and push in list
          var change = {id: appointment.virtualAppointmentId, title: appointment.title, start: appointment.startMillis, end: appointment.endMillis ,body: appointment.location,url:'#app/schedulerDetail'+'?appointmentId=' +idAppointment};
          $scope.appointments.push(change);
      
        });

        console.log("==CONTROLLER SCHEDULE== list appointments: ",$scope.appointments);

        $scope.calendar.options.events = $scope.appointments;
        $scope.calendar._render();

        // get month string to show in view
        var realDate = $scope.calendar.options.day;
        var res = realDate.substring(5, 7); 
        var resInt = parseInt(res);

        var monthString = $scope.calendar.locale['m'+(resInt-1)];
        $scope.realMonth = monthString;
        
    });//END PROMISE
    
    },
    onAfterEventsLoad: function(events)
    {
      if(!events)
      {
        return;
      }
      var list = $('#eventlist');
      list.html('');
      $.each(events, function(key, val)
      {
        $(document.createElement('li'))
        .html('<a href="' + val.url + '">' + val.title + '</a>')
        .appendTo(list);
      });
    },
  };

 

  //LOAD OPTIONS TO CALENDAR
  $scope.calendar = $('#calendar').calendar(options);    

  // FUNCTION NEXT FOR DAY WEEK AND MONTH  
  $scope.scheduleLoadAppointments = function(){
     return [];
  };   

  // FUNCTION NEXT FOR DAY WEEK AND MONTH  
  $scope.scheduleNext  = function(){
    $scope.calendar.navigate('next');
    // var mmm = ($scope.calendar.options.position.start.getMonth()+1);
    // var ddd = ($scope.calendar.options.position.start.getDate());  
    // var mm = (mmm).toString().length == 1 ? "0"+(mmm).toString() : (mmm).toString();
    // var dd = (ddd).toString().length == 1 ? "0"+(ddd).toString() : (ddd).toString();
    // var stringDate = $scope.calendar.options.position.start.getFullYear()+"-"+mm+"-"+dd;
    // $scope.real_date_view = stringDate;
  };

  $scope.schedulePrev  = function(){
    $scope.calendar.navigate('prev');
    // var mmm = ($scope.calendar.options.position.start.getMonth()+1);
    // var ddd = ($scope.calendar.options.position.start.getDate());  
    // var mm = (mmm).toString().length == 1 ? "0"+(mmm).toString() : (mmm).toString();
    // var dd = (ddd).toString().length == 1 ? "0"+(ddd).toString() : (ddd).toString();
    // var stringDate = $scope.calendar.options.position.start.getFullYear()+"-"+mm+"-"+dd;
    // $scope.real_date_view = stringDate;
  };

  $scope.scheduleToday  = function(){
    $scope.calendar.navigate('today');
    // var date = new Date();
    // var mmm = (date.getMonth()+1);
    // var ddd = (date.getDate());  
    // var mm = (mmm).toString().length == 1 ? "0"+(mmm).toString() : (mmm).toString();
    // var dd = (ddd).toString().length == 1 ? "0"+(ddd).toString() : (ddd).toString();
    // var stringDate = date.getFullYear()+"-"+mm+"-"+dd;
    // $scope.real_date_view = stringDate;
  };

  $scope.dataScheduleMonth = function(){
    var _data_date = $localstorage.getObject('dataDate');
    _data_date.type = SCHEDULE_TYPE_MONTH;
    $localstorage.setObject('dataDate',_data_date);    

    $scope.calendar.view('month');
    // var mmm = ($scope.calendar.options.position.start.getMonth()+1);
    // var ddd = ($scope.calendar.options.position.start.getDate());  
    // var mm = (mmm).toString().length == 1 ? "0"+(mmm).toString() : (mmm).toString();
    // var dd = (ddd).toString().length == 1 ? "0"+(ddd).toString() : (ddd).toString();
    // var stringDate = $scope.calendar.options.position.start.getFullYear()+"-"+mm+"-"+dd;
    // $scope.real_date_view = stringDate;


  };

  $scope.dataScheduleDay = function()    {
    var _data_date = $localstorage.getObject('dataDate');
    _data_date.type = SCHEDULE_TYPE_DAY;
    $localstorage.setObject('dataDate',_data_date);    

    $scope.calendar.view('day');
    // var mmm = ($scope.calendar.options.position.start.getMonth()+1);
    // var ddd = ($scope.calendar.options.position.start.getDate());  
    // var mm = (mmm).toString().length == 1 ? "0"+(mmm).toString() : (mmm).toString();
    // var dd = (ddd).toString().length == 1 ? "0"+(ddd).toString() : (ddd).toString();
    // var stringDate = $scope.calendar.options.position.start.getFullYear()+"-"+mm+"-"+dd;
    // $scope.real_date_view = stringDate;
  };

  $scope.dataScheduleWekk = function(){
    var _data_date = $localstorage.getObject('dataDate');
    _data_date.type = SCHEDULE_TYPE_WEEK;
    $localstorage.setObject('dataDate',_data_date);    
    
    $scope.calendar.view('week');
    // var mmm = ($scope.calendar.options.position.start.getMonth()+1);
    // var ddd = ($scope.calendar.options.position.start.getDate());  
    // var mm = (mmm).toString().length == 1 ? "0"+(mmm).toString() : (mmm).toString();
    // var dd = (ddd).toString().length == 1 ? "0"+(ddd).toString() : (ddd).toString();
    // var stringDate = $scope.calendar.options.position.start.getFullYear()+"-"+mm+"-"+dd;
    // $scope.real_date_view = stringDate;
    // $ionicScrollDelegate.scrollTop();
  };

  $scope.doRefresh = function() {
      // GET OBJECT OF LOCAL STORAGE
      var _data_date = $localstorage.getObject('dataDate');

      //CALL SERVICES WITH (TYPE AND DATA)
      console.log("==CONTROLLER SCHEDULE== get query list doRefresh appointments");  
      $scope.newAppointments = scheduleService.query({type: _data_date.type,calendar: _data_date.data});

      //  PROMISE
      $scope.newAppointments.$promise.then(function (results){
        // call factory 
        PopupFactory.getPopup($scope,results);
        
        console.log("==CONTROLLER SCHEDULE== get query list doRefresh appointmentssuccess OK",results['mainData']);
        $scope.listAppointments = (results['mainData'])['appointmentsList'];

          //parse to variables
          $scope.appointments = [];
          angular.forEach($scope.listAppointments, function (appointment) {

            // APPOINTMENT RECURRENT GET ID WITHOUT "-"
            var str = appointment.virtualAppointmentId;
            var pos = str.indexOf("-"); 
            var idAppointment = appointment.virtualAppointmentId;
            if (pos > -1) {
              idAppointment = str.substring(0, pos);   
            };
            console.log("==CONTROLLER SCHEDULE== id appointment without '-'", idAppointment);

            var change = {id: appointment.virtualAppointmentId, title: appointment.title, start: appointment.startMillis, end: appointment.endMillis ,body: appointment.location,url:'#app/schedulerDetail'+'?appointmentId=' +idAppointment};
            $scope.appointments.push(change);
          });

          console.log("==CONTROLLER SCHEDULE== list appointments: ",$scope.appointments);

          $scope.$broadcast('scroll.refreshComplete');  

          //LOAD OPTIONS TO CALENDAR
          $scope.calendar._render();          
      });//END PROMISE

  };
});
