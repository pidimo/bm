angular.module('starter.contactcontrollers',['starter.contactservices','starter.constantscontact'] )
 
.controller('editPersonCtrl', function(PopupFactory,apiUrlLocal,$http,transformRequestAsFormPost,bridgeService,$scope,COLOR_VIEW, $stateParams,apiUrlLocal,$localstorage) {
    $scope.apiUrlLocal = apiUrlLocal;
    $scope.colorFont = COLOR_VIEW;

    // get contact for edit
    var mainData = bridgeService.getContact();
    // $scope.entity = mainData.entity;

    $scope.entity = mainData.entity;

    // $scope.entity = { 'dto(name1)': aux.name1};

    $scope.description = "Edit person"


    console.log("==CONTACTS CONTROLLER==  get contact data:-----",mainData);

    var salutationArray = mainData.salutationArray;  
    $scope.salutations = []; 
    salutationArray.forEach(function(salutation) {           
        $scope.salutations.push({
          name: salutation.name,
          value:salutation.salutationId
        });       
        if($scope.entity.salutationId == salutation.salutationId) {             
           $scope.salutation = $scope.salutations[$scope.salutations.length-1];  
        } 
    });

    var titleArray = mainData.titleArray;  
    $scope.titles = [];    
    titleArray.forEach(function(title) {           
        $scope.titles.push({
          name: title.name,
          value:title.titleId
        });       
        if($scope.entity.titleId == title.titleId) {             
           $scope.title = $scope.titles[$scope.titles.length-1];  
        } 
    });

    var countryArray = mainData.countryArray;  
    $scope.countries = [];    
    countryArray.forEach(function(country) {           
        $scope.countries.push({
          name: country.name,
          value:country.countryId
        });       
        if($scope.entity.countryId == country.countryId) {             
           $scope.country = $scope.countries[$scope.countries.length-1];  
        } 
    });

    var languageArray = mainData.languageArray;
    $scope.languages = [];    
    languageArray.forEach(function(language) {           
        $scope.languages.push({
          name: language.name,
          value:language.languageId
        });       
        if($scope.entity.languageId == language.languageId) {             
           $scope.language = $scope.languages[$scope.languages.length-1];  
        } 
    });    

    $scope.birthday = new Date ( [$scope.entity.birthday.slice(0, 4), "/",$scope.entity.birthday.slice(4,6),"/",$scope.entity.birthday.slice(6)].join('') ).getTime();

    
    $scope.updateSalutation = function (nsalutation)
    {
      $scope.salutation = nsalutation;     
    }

    $scope.updateTitle = function (ntitle)
    {
      $scope.title = ntitle;     
    }

    $scope.updateCountry = function (ncountry)
    {
      $scope.country = ncountry;     
    }

    $scope.updateLanguage = function (nlanguage)
    {
      $scope.language = nlanguage;     
    }

    $scope.saveChangePerson = function() {
  
       console.log("==CONTROLLER CONTACT== save changes person, data:",$scope.entity);

       var aux = $scope.entity;

       var newEntity = {'dto(version)': aux.recordUserId, 
                        'dto(recordUserId)': aux.version,
                        'dto(addressId)': aux.addressId, 
                        'dto(salutationId)': $scope.salutation.value, 
                        'dto(titleId)': $scope.title.value,
                        'dto(name1)': aux.name1,
                        'dto(name2)':aux.name2,
                        'dto(street)':aux.street,
                        'dto(houseNumber)':aux.houseNumber,
                        'dto(additionalAddressLine)':aux.additionalAddressLine,
                        'dto(searchName)':aux.searchName,
                        'dto(keywords)':aux.keywords,
                        'dto(education)':aux.education,
                        'dto(languageId)':$scope.language.value,
                        'dto(birthday)':"",
                        'dto(isCustomer)':aux.isCustomer,
                        'dto(isSupplier)':aux.isSupplier,
                        'dto(isActive)':aux.isActive,
                        'countryId':$scope.country.value};
      
      console.log("---------9089887867",newEntity);                  ;
// ,'cityNameId':''
// ,'zip':''

      var request = $http({
        method: "post",
        
        url: apiUrlLocal+"/bmapp/Address/Update.do",
        // url: "http://localhost:8080/bm/bmapp/Contact/REST.do",
        transformRequest: transformRequestAsFormPost,
        data: newEntity
      });
      // Store the data-dump of the FORM scope.
      request.success(
        function(data, status, headers, config) {
          // call factory 
          PopupFactory.getPopup($scope,data);
          
        }
      ); 
    };

})

.controller('editOrganizationCtrl', function($cordovaCamera,bridgeService,$scope,COLOR_VIEW, $stateParams,apiUrlLocal,$localstorage) {
    $scope.apiUrlLocal = apiUrlLocal;
    $scope.colorFont = COLOR_VIEW;
    $scope.ntitle = "Edit Organization";
    // get contact for edit
    var mainData = bridgeService.getContact();
    $scope.entity = mainData.entity;
    $scope.maindata = mainData;

    $scope.takePicture = function() {
        var options = { 
            quality : 75, 
            destinationType : Camera.DestinationType.DATA_URL, 
            sourceType : Camera.PictureSourceType.CAMERA, 
            allowEdit : true,
            encodingType: Camera.EncodingType.JPEG,
            targetWidth: 300,
            targetHeight: 300,
            popoverOptions: CameraPopoverOptions,
            saveToPhotoAlbum: false
        };
 
        $cordovaCamera.getPicture(options).then(function(imageData) {
            $scope.imgURI = "data:image/jpeg;base64," + imageData;
        }, function(err) {
            // An error occured. Show a message to the user
        });
    }

    console.log("==EDIT ORGANIZATION CONTROLLER==  get maindata:", $scope.maindata);

    // var countryArray = $scope.maindata.countryArray;
    // $scope.countries = [];    
    // countryArray.forEach(function(country) {           
    //     $scope.countries.push({
    //       name: country.name,
    //       value:country.countryId
    //     });       
    //     if($scope.entity.countryId == country.countryId) {             
    //        $scope.country = $scope.countries[$scope.countries.length-1];  
    //     } 
    // });    

    // var languageArray = $scope.maindata.languageArray;
    // $scope.languages = [];    
    // languageArray.forEach(function(language) {           
    //     $scope.languages.push({
    //       name: language.name,
    //       value:language.languageId
    //     });       
    //     if($scope.entity.languageId == language.languageId) {             
    //        $scope.language = $scope.languages[$scope.languages.length-1];  
    //     } 
    // });    

    // $scope.foundation = new Date ( [$scope.entity.birthday.slice(0, 4), "/", $scope.entity.birthday.slice(4,6),"/", $scope.entity.birthday.slice(6)].join('') ).getTime();
    // console.log("=========fouendation",$scope.foundation);
})



.controller('EditContactPersonCtrl', function(bridgeService,$scope,COLOR_VIEW, $stateParams,apiUrlLocal,$localstorage) {
  $scope.apiUrlLocal = apiUrlLocal;
  $scope.colorFont = COLOR_VIEW;
  $scope.ntitle = "Edit Contact Person";
   
  var mainData = bridgeService.getContact();
  $scope.entity = mainData.entity;
  $scope.mainData = mainData;

  console.log("==EDIT CONTACT PERSON CONTROLLER==  get maindata:", $scope.mainData);

  var salutationArray = $scope.mainData.salutationArray;  
    $scope.salutations = [];    
    salutationArray.forEach(function(salutation) {           
      $scope.salutations.push({
        name: salutation.name,
        value:salutation.salutationId
      });       
      if($scope.entity.salutationId == salutation.salutationId) {             
         $scope.salutation = $scope.salutations[$scope.salutations.length-1];  
      } 
  });

  var titleArray = $scope.mainData.titleArray;  
    $scope.titles = [];    
    titleArray.forEach(function(title) {           
      $scope.titles.push({
        name: title.name,
        value: title.titleId
      });       
      if($scope.entity.titleId == title.titleId) {             
         $scope.title = $scope.titles[$scope.titles.length-1];  
      } 
  });

  var languageArray = $scope.mainData.languageArray;
  $scope.languages = [];    
  languageArray.forEach(function(language) {           
      $scope.languages.push({
        name: language.name,
        value:language.languageId
      });       
      if($scope.entity.languageId == language.languageId) {             
         $scope.language = $scope.languages[$scope.languages.length-1];  
      } 
  });   

  $scope.birthday = new Date ( [$scope.entity.birthday.slice(0, 4), "/", $scope.entity.birthday.slice(4,6),"/", $scope.entity.birthday.slice(6)].join('') ).getTime();
  console.log("=========birthday",$scope.birthday);
  
})

.controller('ContactsCtrl', function(allContact,PopupFactory,$localstorage,$filter,$ionicScrollDelegate,$window,$scope,COLOR_VIEW, Contact,$timeout,$ionicLoading,apiUrlLocal,$location, $state, $window,$ionicPopup) {
    
    $scope.apiUrlLocal = apiUrlLocal;
    $scope.colorFont = COLOR_VIEW;
    $scope.pagesintotal; 
    $scope.page = 1; 


    $scope.showSearchBar = false;
    $scope.apiUrlLocal = apiUrlLocal;
    $scope.newContacts = allContact.query({'pageParam(pageNumber)':$scope.page});
    $scope.contacts = [];
    
    $scope.asknext = false;
   

    console.log("FIRST CALL",$scope.newContacts);

    $scope.newContacts.$promise.then(function (results){
        
        // call factory 
        PopupFactory.getPopup($scope,results);
        
        console.log("THIS INFO",(results['mainData']));
  
        $scope.contacts = (results['mainData'])['list'];

        console.log('LIST OF THE FIRST CONTACTS',$scope.contacts);
        $scope.page = parseInt((results['mainData'])['pageInfo']['pageNumber']);
        $scope.pagesintotal = parseInt((results['mainData'])['pageInfo']['totalPages']);
        console.log("page integer", $scope.page);
        console.log("pages in total", $scope.pagesintotal);

        if ($scope.contacts.length > 0 && $scope.pagesintotal>$scope.page) {
          $scope.asknext = true;  
        };
        // $window.location.reload();
    })


$scope.doRefresh = function() {
    console.log("------------------1 do refresh principal");
    $scope.page=1;
    $scope.searchKey = "";
    $scope.pag = 1;
    $scope.$broadcast('scroll.infiniteScrollComplete');


  $scope.newContacts = allContact.query({'pageParam(pageNumber)':$scope.page});

  $scope.newContacts.$promise.then(function (results){

    // call factory 
    PopupFactory.getPopup($scope,results);

    if (results['forward'] == "") {
      $scope.contacts = (results['mainData'])['list'];
      $scope.pagesintotal = parseInt((results['mainData'])['pageInfo']['totalPages']);
      $scope.$broadcast('scroll.refreshComplete'); 
        
      $scope.pag=parseInt((results['mainData'])['pageInfo']['pageNumber']);
      $scope.totalpag=parseInt((results['mainData'])['pageInfo']['totalPages']);
      
      console.log('COMEBACK TO THE FIRST LIST',$scope.page);
      console.log('WITH THIS CONTACTS',$scope.contacts);
      console.log('PAGE #',$scope.page);
      console.log("pages in total on refresh", $scope.pagesintotal);
      
      if ($scope.contacts.length > 0 && $scope.pagesintotal>$scope.page) {
        $scope.asknext = true;
      };
      $ionicScrollDelegate.scrollTop();
    }
  });
};  

$scope.loadMore = function() {
    
    console.log("------------------1 loadMore principal");
  console.log('Loading more contacts');
  $scope.page = $scope.page + 1;
  $scope.newContacts = allContact.query({'pageParam(pageNumber)':$scope.page});
  $scope.newContacts.$promise.then(function(results){
    
    // call factory 
    PopupFactory.getPopup($scope,results);

    console.log("+++++new info",(results['mainData']));
    console.log("++++new page #", $scope.page);
    $scope.contacts = $scope.contacts.concat((results['mainData'])['list']);
    $scope.$broadcast('scroll.infiniteScrollComplete');
      console.log("++++new contacts list ", $scope.contacts);
      
      if ($scope.pagesintotal<=$scope.page+1) {
          $scope.asknext = false;  
        };
      
  });
    };


$scope.getContactUrl = function(item,type){  
  var accessRight = $localstorage.getObject('accessRight');
  accessRightContactPerson = $scope.accessRight.CONTACTPERSON.VIEW;  

  // IF CONTACT PERSON HAVE PERMISSION TO READ
  if (item.contactPersonAddressId != "" && accessRightContactPerson != "true") {
    return "#";
  }

  switch(type) {
    case 'contactPerson':
        return item.contactPersonAddressId ==='' ? '#/app/contactPerson?contactId=' +item.addressId +'&addressId='+ item.addressId + '&addressType=' + item.addressType : '#/app/contactPerson?contactId='+item.contactPersonAddressId+'&addressId='+item.contactPersonAddressId+'&contactPersonId='+item.addressId+'&addressType='+item.addressType2;  
        break;
    case 'organization':
        return item.contactPersonAddressId ==='' ? '#/app/organization?contactId=' +item.addressId +'&addressId='+ item.addressId + '&addressType=' + item.addressType : '#/app/organization?contactId='+item.contactPersonAddressId+'&addressId='+item.contactPersonAddressId+'&contactPersonId='+item.addressId+'&addressType='+item.addressType2;  
        break;
    case 'person':
        return item.contactPersonAddressId ==='' ? '#/app/person?contactId=' +item.addressId +'&addressId='+ item.addressId + '&addressType=' + item.addressType : '#/app/person?contactId='+item.contactPersonAddressId+'&addressId='+item.contactPersonAddressId+'&contactPersonId='+item.addressId+'&addressType='+item.addressType2;  
        break;        
    default:
        return "#";
  }    
};

$scope.searchcon = function(){
  $scope.showSearchBar = !$scope.showSearchBar;
}   

$scope.clearSearch = function () {
  
  $scope.searchKey = "";
  $scope.showSearchBar = !$scope.showSearchBar;

  $scope.page=1;
  $scope.searchKey = "";
  $scope.pag = 1;
  
  $scope.newContacts = allContact.query({'pageParam(pageNumber)':$scope.page});

  $scope.newContacts.$promise.then(function (results){

    // call factory 
    PopupFactory.getPopup($scope,results);

    if (results['forward'] == "") {
      $scope.contacts = (results['mainData'])['list'];
      $scope.pagesintotal = parseInt((results['mainData'])['pageInfo']['totalPages']);
      $scope.$broadcast('scroll.refreshComplete'); 
        
      $scope.pag=parseInt((results['mainData'])['pageInfo']['pageNumber']);
      $scope.totalpag=parseInt((results['mainData'])['pageInfo']['totalPages']);
      
      console.log('COMEBACK TO THE FIRST LIST',$scope.page);
      console.log('WITH THIS CONTACTS',$scope.contacts);
      console.log('PAGE #',$scope.page);
      console.log("pages in total on refresh", $scope.pagesintotal);
      
      if ($scope.contacts.length > 0 && $scope.pagesintotal>$scope.page) {
        $scope.asknext = true;
      };
    }
  });
}


        
            
$scope.search = function () {
  console.log("------------------1 search function");
    $scope.contacts = [];
    $scope.showSearchBar = !$scope.showSearchBar;
    $scope.buscados = allContact.query({'parameter(contactSearchName)':$scope.searchKey});
    $scope.asknext = false;

    $scope.buscados.$promise.then(function (results){
        
        // call factory 
        PopupFactory.getPopup($scope,results);    

        $scope.pag=parseInt((results['mainData'])['pageInfo']['pageNumber']);
        $scope.totalpag=parseInt((results['mainData'])['pageInfo']['totalPages']);
                
        $scope.contacts = (results['mainData'])['list'];
        console.log("search info", results['mainData']);
        console.log("list  of contacts, first search", $scope.contacts);
         
        if ($scope.contacts.length > 0 && $scope.totalpag>$scope.pag) {
          $scope.asknext = true;  
        }; 

        // if no exists items show message
        if ($scope.contacts.length == 0) {

          var message = $filter('translate')('NoItems');
          var messageRefresh = $filter('translate')('PulltoRefresh');
          // An alert dialog
          console.log("==CONTROLLER CONTACTS== alert if no exists items");
          var alertPopup = $ionicPopup.alert({
              title: message,
              template: messageRefresh
          });
        }
        $ionicScrollDelegate.scrollTop();
                
    });
      
    $scope.loadMore = function() {
      console.log("------------------1 loadMore dentro search");
      console.log("trying to load more of the search",$scope.pag);
      $scope.pag = $scope.pag +1;        
      $scope.buscados = allContact.query({'parameter(contactSearchName)':$scope.searchKey,'pageParam(pageNumber)':$scope.pag});
      $scope.buscados.$promise.then(function(results){

          // call factory 
          PopupFactory.getPopup($scope,results);

          $scope.totalpag=parseInt((results['mainData'])['pageInfo']['totalPages']);

          console.log("===== 000new info for search",(results['mainData']));
          $scope.contacts = $scope.contacts.concat((results['mainData'])['list']);
          $scope.$broadcast('scroll.infiniteScrollComplete');
          console.log("======0000new list of contacts for search",$scope.contacts);
                
      });
            
      if ($scope.totalpag<$scope.pag+1) {
        $scope.asknext = false;  
      };              
    };
}




 $scope.createp = function() {
     console.log("go to create person");
      $state.go('app.newperson'); 
    };
                            
})



.controller('newpCtrl', function (PopupFactory,$state,apiUrlLocal,$scope,$ionicModal,$http,transformRequestAsFormPost) {

  $scope.description = "New person"
  $scope.entity = {};

  $scope.saveChangePerson = function(){

    // console.log("==CONTROLLER CONTACT== save new person, data:",$scope.entity);

       // var aux = $scope.entity;

       // var newEntity = {'dto(version)': aux.recordUserId, 
       //                  'dto(recordUserId)': aux.version,
       //                  'dto(addressId)': aux.addressId, 
       //                  'dto(salutationId)': $scope.salutation.value, 
       //                  'dto(titleId)': $scope.title.value,
       //                  'dto(name1)': aux.name1,
       //                  'dto(name2)':aux.name2,
       //                  'dto(street)':aux.street,
       //                  'dto(houseNumber)':aux.houseNumber,
       //                  'dto(additionalAddressLine)':aux.additionalAddressLine,
       //                  'dto(searchName)':aux.searchName,
       //                  'dto(keywords)':aux.keywords,
       //                  'dto(education)':aux.education,
       //                  'dto(languageId)':$scope.language.value,
       //                  'dto(birthday)':"",
       //                  'dto(isCustomer)':aux.isCustomer,
       //                  'dto(isSupplier)':aux.isSupplier,
       //                  'dto(isActive)':aux.isActive,
       //                  'countryId':$scope.country.value};
      
      // console.log("---------9089887867",newEntity);                  ;
// ,'cityNameId':''
// ,'zip':''

      var newEntity = {'dto(name1)': "0009988",
                      'dto(name2)': "",
                      'dto(name3)': "",
                      'dto(addressType)':"1"};

      var request = $http({
        method: "post",
        
        url: apiUrlLocal+"/bmapp/Address/Create.do",
        transformRequest: transformRequestAsFormPost,
        data: newEntity
      });
      // Store the data-dump of the FORM scope.
      request.success(
        function(data, status, headers, config) {

          // call factory
          PopupFactory.getPopup($scope,data);

          if(data.forward == "Success")
          {
            console.log("person creation succesfull");          
            $state.go('app.contacts'); 
          }
        }
      );
  }
})


.controller('neworganizationCtrl', function ($ionicPopup,$cordovaImagePicker,$cordovaCamera,PopupFactory,transformRequestAsFormPost,apiUrlLocal,$scope,$ionicModal, AuthenticationService,$state,$http,$ionicLoading,$location, $state, $window,COLOR_VIEW) {
  $scope.colorFont = COLOR_VIEW;

  $scope.ntitle = "New Organization";

  $scope.choices = [];
  $scope.iframeWidth = $(window).width();

  $scope.addNewChoice = function(value,telecom) {
    var newItemNo = $scope.choices.length+1;
    $scope.choices.push({'id':newItemNo, value:value,telecomvalue:telecom.name});    
  };
    
  $scope.removeChoice = function(choice) {
    var index = $scope.choices.indexOf(choice);    
    $scope.choices.splice(index,1);
  };

  $scope.pickerImage= function ()
  {
    var options = {
    maximumImagesCount: 1,
    width: 300,
    height: 300,
    quality: 75
    };

    $cordovaImagePicker.getPictures(options)
      .then(function (results) {        
          $scope.imgURI = results[0];
      }, function(error) {
        // error getting photos
      })
  };
  
  $scope.takePicture = function() {
    var options = { 
        quality : 75, 
        destinationType : Camera.DestinationType.DATA_URL, 
        sourceType : Camera.PictureSourceType.CAMERA, 
        allowEdit : true,
        encodingType: Camera.EncodingType.JPEG,
        targetWidth: 300,
        targetHeight: 300,
        popoverOptions: CameraPopoverOptions,
        saveToPhotoAlbum: false
    };

    $cordovaCamera.getPicture(options).then(function(imageData) {
        $scope.imgURI = "data:image/jpeg;base64," + imageData;
    }, function(err) {
        // An error occured. Show a message to the user
    });
  }


  $scope.showPopup = function() {
  $scope.data = [];
  $scope.data.push({'id':1, value:true, name:"1"},{'id':2, value:false, name:"2"},{'id':3, value:true, name:"3"},{'id':4, value:false, name:"4"});    
  // An elaborate, custom popup
  var myPopup = $ionicPopup.show({
    template: '<div data-ng-repeat="d in data"><ion-toggle  ng-model="d.value" toggle-class="toggle-calm">{{d.name}}</ion-toggle></div>',
    title: 'Data access security',
    subTitle: 'Allowed user groups',
    scope: $scope,
    buttons: [      
      {
        text: '<b>OK</b>',
        type: 'button-positive',
        onTap: function(e) {
          // if (!$scope.data.wifi) {
          //   //don't allow the user to close unless he enters wifi password
          //   e.preventDefault();
          // } else {
            return $scope.data;
          // }
        }
      }
    ]
  });
  myPopup.then(function(res) {
    console.log('Tapped!', res);      
  });  
 };

 
  var request = $http({
        method: "get",        
        url: apiUrlLocal+"/bmapp/Address/Forward/Create.do",      
      });
    request.success(
      function(data, status, headers, config) {  

        // call factory 
        PopupFactory.getPopup($scope,results);

        console.log(data);
         var countryArray = data.mainData.countryArray;  
          $scope.countries = [];    
          countryArray.forEach(function(country) {           
              $scope.countries.push({
                name: country.name,
                value:country.countryId
              });                     
          });

          var languageArray = data.mainData.languageArray;
          $scope.languages = [];    
          languageArray.forEach(function(language) {           
              $scope.languages.push({
                name: language.name,
                value:language.languageId
              });                     
          }); 

          var telecomTypeArray = data.mainData.telecomTypeArray;
          $scope.telecoms = [];    
          telecomTypeArray.forEach(function(telecom) {           
              $scope.telecoms.push({
                name: telecom.telecomTypeName,
                value:telecom.telecomTypeId,
              });                     
          });    

      });

     
    

    $scope.updateCountry = function (ncountry)
    {
      $scope.country = ncountry;  
      var cityRequest = $http({
        method: "post",        
        url: apiUrlLocal+"/bmapp/Country/City.do",      
        data: {
            'countryId' : ncountry.value
         }
      });
      cityRequest.success(
        function(data, status, headers, config) {      

          // call factory 
          PopupFactory.getPopup($scope,results);

          var cityArray = data.mainData.cityArray;
          $scope.cities = [];    
          cityArray.forEach(function(city) {           
              $scope.cities.push({
                name: city.cityName,
                value:city.cityId, 
                zip: city.zip,             
              });                     
          });    
      });
   
    }

  $scope.saveOrganization = function() {
    // console.log("Save organization", $scope.imgURI);

    // var request = $http({
    //   method: "post",
    //   url: apiUrlLocal+"/bmapp/Address/Create.do",
    //   headers: {'Content-Type': 'multipart/form-data'},
    //   transformRequest: transformRequestAsFormPost,
    //   data: {
    //     'dto(addressType)': "0",
    //     'dto(name1)': "121212",
    //     'dto(name2)': "sdfsd",
    //     'dto(name3)': "gggg",
    //     // 'imageFile' : $scope.imgURI,
    //   }
    // });  
    // request.success(
    //   function(data, status, headers, config) {
    //     if(data.forward == "Success")
    //     {
    //       console.log("organization creation succesfull");          
    //       $state.go('app.contacts'); 
    //     }
    //     else
    //     {           
    //        PopupFactory.getPopup($scope,data);
    //     }
        
    //   }
    // );
  
  
   
   
  };
})


.controller('ContactPersonCtrl', function(allContact,$state,PopupFactory,bridgeService,$scope,COLOR_VIEW,$localstorage,$stateParams, Contact,apiUrlLocal) {
    $scope.apiUrlLocal = apiUrlLocal;
    $scope.colorFont = COLOR_VIEW;

  console.log("param1", $stateParams.contactId);
  console.log("param2", $stateParams.addressId);
  console.log("param3", $stateParams.contactPersonId);
  console.log("param4", $stateParams.addressType);

  $scope.contact = allContact.get({contactId: $stateParams.contactId, "dto(addressId)": $stateParams.addressId, "dto(contactPersonId)": $stateParams.contactPersonId, "dto(addressType)": $stateParams.addressType});

  $scope.contact.$promise.then(function (results){

    // call factory 
    PopupFactory.getPopup($scope,results);

    $scope.contact = results;
    console.log("==CONTROLLER CONTACTS== result detail contact:", $scope.contact);

    $scope.telecomss=results.mainData.entity.telecoms;
    console.log("list of telecoms",$scope.telecomss);

    $scope.firstGruoup = [];
    $scope.secondGruoup = [];
    $scope.auxEmail = [];
    $scope.auxFax = [];
    $scope.auxLink = [];    
    $scope.telecomss.forEach(function(telecom) {

      switch(telecom.telecomTypeType) {
          case 'PHONE':
              $scope.firstGruoup.push(telecom);
              break;
          case 'EMAIL':
              $scope.auxEmail.push(telecom);
              break;
          case 'OTHER':
              $scope.secondGruoup.push(telecom);
              break;
          case 'FAX':
              $scope.auxFax.push(telecom);
              break;         
          case 'LINK':
              $scope.auxLink.push(telecom);
              break;
      }     
    });

    $scope.firstGruoup = $scope.firstGruoup.concat($scope.auxEmail);
    $scope.secondGruoup = $scope.secondGruoup.concat($scope.auxFax);
    $scope.secondGruoup = $scope.secondGruoup.concat($scope.auxLink); 

    $scope.writeEmail = function(email)
    {      
        $state.go('app.newmail',{'to': email }); 
    }
    
    String.prototype.startsWith = function(prefix) {
      return this.indexOf(prefix) === 0;
    }

    $scope.go_to = function(link)
    {      
      if (!link.startsWith("http://")){
        window.open('http://'+link, '_system', 'location=yes'); return false;
      }
      else{
       window.open(link, '_system', 'location=yes'); return false; 
      }       
    }    

    $localstorage.setObject("EditContact",results.mainData);

    if (results.mainData.entity.countryId != "") {
      var countries = results.mainData.countryArray;
      countries.forEach(function(country) {
          if (country.countryId == results.mainData.entity.countryId) {
            $scope.countryName = country.name;
          }
      });
    }

    if (results.mainData.entity.titleId != "") {
      var titles = results.mainData.titleArray;
      titles.forEach(function(title) {
          if (title.titleId == results.mainData.entity.titleId) {
            $scope.titleperson = title.name;
          }
      });
    }


    // save contact for edit do not call service
    bridgeService.saveContact($scope.contact.mainData);

    $scope.firstGruoup = $scope.firstGruoup.sort(compare);
    $scope.secondGruoup = $scope.secondGruoup.sort(compare);

    function compare(a,b) {     
      return a.telecomTypePosition - b.telecomTypePosition        
    }
    
  });

})

.controller('OrganizationCtrl', function(allContact,$state,PopupFactory,bridgeService,$scope,COLOR_VIEW,$localstorage,$stateParams, Contact,apiUrlLocal) {
    $scope.apiUrlLocal = apiUrlLocal;
    $scope.colorFont = COLOR_VIEW;

  console.log("param1", $stateParams.contactId);
  console.log("param2", $stateParams.addressId);
  console.log("param3", $stateParams.contactPersonId);
  console.log("param4", $stateParams.addressType);

  $scope.contact = allContact.get({contactId: $stateParams.contactId, "dto(addressId)": $stateParams.addressId, "dto(contactPersonId)": $stateParams.contactPersonId, "dto(addressType)": $stateParams.addressType});

  $scope.contact.$promise.then(function (results){

    // call factory 
    PopupFactory.getPopup($scope,results);

    $scope.contact = results;
    console.log("==CONTROLLER CONTACTS== result detail contact:", $scope.contact);

    $scope.telecomss=results.mainData.entity.telecoms;
    console.log("list of telecoms",$scope.telecomss);

    $scope.firstGruoup = [];
    $scope.secondGruoup = [];
    $scope.auxEmail = [];
    $scope.auxFax = [];
    $scope.auxLink = [];    
    $scope.telecomss.forEach(function(telecom) {

      switch(telecom.telecomTypeType) {
          case 'PHONE':
              $scope.firstGruoup.push(telecom);
              break;
          case 'EMAIL':
              $scope.auxEmail.push(telecom);
              break;
          case 'OTHER':
              $scope.secondGruoup.push(telecom);
              break;
          case 'FAX':
              $scope.auxFax.push(telecom);
              break;         
          case 'LINK':
              $scope.auxLink.push(telecom);
              break;
      }     
    });

    



    $scope.firstGruoup = $scope.firstGruoup.concat($scope.auxEmail);
    $scope.secondGruoup = $scope.secondGruoup.concat($scope.auxFax);
    $scope.secondGruoup = $scope.secondGruoup.concat($scope.auxLink); 
   

    $scope.writeEmail = function(email)
    {      
        $state.go('app.newmail',{'to': email}); 
    }
    
    String.prototype.startsWith = function(prefix) {
      return this.indexOf(prefix) === 0;
    }

    $scope.go_to = function(link)
    {      
      if (!link.startsWith("http://")){
        window.open('http://'+link, '_system', 'location=yes'); return false;
      }
      else{
       window.open(link, '_system', 'location=yes'); return false; 
      }


       
    }    

    $localstorage.setObject("EditContact",results.mainData);

    if (results.mainData.entity.countryId != "") {
      var countries = results.mainData.countryArray;
      countries.forEach(function(country) {
          if (country.countryId == results.mainData.entity.countryId) {
            $scope.countryName = country.name;
          }
      });
    }   

    // save contact for edit do not call service
    bridgeService.saveContact($scope.contact.mainData);

    $scope.firstGruoup = $scope.firstGruoup.sort(compare);
    $scope.secondGruoup = $scope.secondGruoup.sort(compare);

    function compare(a,b) {     
      return a.telecomTypePosition - b.telecomTypePosition        
    }
  });

})

.controller('PersonCtrl', function(allContact,$state,PopupFactory,bridgeService,$scope,COLOR_VIEW,$localstorage,$stateParams, Contact,apiUrlLocal) {
    $scope.apiUrlLocal = apiUrlLocal;
    $scope.colorFont = COLOR_VIEW;

  console.log("param1", $stateParams.contactId);
  console.log("param2", $stateParams.addressId);
  console.log("param3", $stateParams.contactPersonId);
  console.log("param4", $stateParams.addressType);

  $scope.contact = allContact.get({contactId: $stateParams.contactId, "dto(addressId)": $stateParams.addressId, "dto(contactPersonId)": $stateParams.contactPersonId, "dto(addressType)": $stateParams.addressType});

  $scope.contact.$promise.then(function (results){

    // call factory 
    PopupFactory.getPopup($scope,results);

    $scope.contact = results;
    console.log("==CONTROLLER CONTACTS== result detail contact:", $scope.contact);

    $scope.telecomss=results.mainData.entity.telecoms;
    console.log("list of telecoms",$scope.telecomss);

    $scope.firstGruoup = [];
    $scope.secondGruoup = [];
    $scope.auxEmail = [];
    $scope.auxFax = [];
    $scope.auxLink = [];    
    $scope.telecomss.forEach(function(telecom) {

      switch(telecom.telecomTypeType) {
          case 'PHONE':
              $scope.firstGruoup.push(telecom);
              break;
          case 'EMAIL':
              $scope.auxEmail.push(telecom);
              break;
          case 'OTHER':
              $scope.secondGruoup.push(telecom);
              break;
          case 'FAX':
              $scope.auxFax.push(telecom);
              break;         
          case 'LINK':
              $scope.auxLink.push(telecom);
              break;
      }     
    });

    $scope.firstGruoup = $scope.firstGruoup.concat($scope.auxEmail);
    $scope.secondGruoup = $scope.secondGruoup.concat($scope.auxFax);
    $scope.secondGruoup = $scope.secondGruoup.concat($scope.auxLink);  

    $scope.writeEmail = function(email)
    {      
        $state.go('app.newmail',{'to': email }); 
    }

    String.prototype.startsWith = function(prefix) {
      return this.indexOf(prefix) === 0;
    }

    $scope.go_to = function(link)
    {      
      if (!link.startsWith("http://")){
        window.open('http://'+link, '_system', 'location=yes'); return false;
      }
      else{
       window.open(link, '_system', 'location=yes'); return false; 
      }       
    }  

    $localstorage.setObject("EditContact",results.mainData);

    if (results.mainData.entity.countryId != "") {
      var countries = results.mainData.countryArray;
      countries.forEach(function(country) {
          if (country.countryId == results.mainData.entity.countryId) {
            $scope.countryName = country.name;
          }
      });
    }    

    if (results.mainData.entity.titleId != "") {
      var titles = results.mainData.titleArray;
      titles.forEach(function(title) {
          if (title.titleId == results.mainData.entity.titleId) {
            $scope.titleperson = title.name;
          }
      });
    }

    // save contact for edit do not call service
    bridgeService.saveContact($scope.contact.mainData);

    $scope.firstGruoup = $scope.firstGruoup.sort(compare);
    $scope.secondGruoup = $scope.secondGruoup.sort(compare);

    function compare(a,b) {     
      return a.telecomTypePosition - b.telecomTypePosition        
    }
    
  });

})





