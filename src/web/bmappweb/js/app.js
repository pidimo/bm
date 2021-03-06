// Ionic Starter App
    // var db = null;
// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
var starter = angular.module('starter', ['ionic','starter.constants','ui.router','starter.rolesroutes','starter.scheduleroutes','underscore', 'ngCordova', 'pascalprecht.translate', 'starter.controllers','starter.services','starter.webmailroutes','starter.contactroutes','ngResource'])

.run(function($translate,$cordovaNetwork,$ionicPopup,$ionicPlatform, $translate,$rootScope, $location, AuthenticationService, RoleService, SessionService) {
  
  
  $rootScope.$on('$stateChangeStart', function (ev, to, toParams, from, fromParams) {

    
    
    
    
    // IF AUTHENTICATION IS FALSE GO TO LOGIN
    // var authentication = AuthenticationService.isLoggedIn();
    // if ( authentication == false ) {
    //   console.log("==VALIDATE ROUTE== USER NO AUTHENTICATION");
    //   $location.path('/login');
    // }
    // else {
      console.log("==VALIDATE ROUTE== AUTHENTICATION TRUE");
      //VALIDATE ROUTE
        // var rout = validateRoute($location.url());
        // console.log("==VALIDATE ROUTE== URL LOCATION: ",$location.url());

      var permisos = RoleService.validateRoleAdmin(SessionService.currentUser);
      
      //IF VALIDATE ROUTE FALSE GO TO /app
      // if (rout == false) {
      //   console.log("==VALIDATE ROUTE== YOU CAN NOT SEE THIS WINDOW");
      //   ev.preventDefault();
      //   $location.path('/login');
      // }
    // }
  });

  $ionicPlatform.ready(function() {

    var language = navigator.language;
    if( language.indexOf("fr") != -1){
      $translate.use("fr");  
    }
    else{
      if (language.indexOf("de") != -1) {
        $translate.use("de");
      }
      else{
        if (language.indexOf("es") != -1) {
          $translate.use("es");
        }
      }
    }  
       
    StatusBar.overlaysWebView(false);

    var isOnline = $cordovaNetwork.isOnline()
    if(!isOnline)
    {
      var alertPopup = $ionicPopup.alert({
        title: 'No Internet Connection',
        template: 'Sorry, no Internet connectivity detected. Please reconnect and try again.'
      });
      alertPopup.then(function(res) {
        ionic.Platform.exitApp();
      });    
    }

    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    if (window.cordova && window.cordova.plugins.Keyboard) 
    {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }
    if (window.StatusBar) 
    {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }
    if (typeof navigator.globalization !== "undefined"){
      navigator.globalization.getPreferredLanguage(function(language) {
       //alert((language.value).split("-")[0]);
       $translate.use((language.value).split("-")[0]).then(function(data) {
       //  alert(language.value);
       // $translate.use(language.value).then(function(data) {
        console.log("SUCCESS -> " + data);
      }, function(error) {
        console.log("ERROR -> " + error);
      });
     },null);
    }
  });
})

.config(function($translateProvider) {
  $translateProvider.translations("en", {
    Monday: "Monday",
    Tuesday: "Tuesday",
    Wednesday: "Wednesday",
    Thursday: "Thursday",
    Friday: "Friday",
    Saturday: "Saturday",
    Sunday: "Sunday",
    
    
    Funtion: "Function",
    From: "From",
    To: "To",
    DateMail: "Date",
    Subject: "Subject",
    
    Description: "Description",
    CheckFolder: "Please check your folder BMapp.",
    Downloaded: "Downloaded",
    SelectAnother: "Please select another folder.",
    PulltoRefresh: "Please pull to refresh.",
    NoItems: "No Items",
    Messagefailed: "Please check your credentials.",
    MessageRequired: "Logon credentials invalid session.",
    Loading: "More features under construccion",
    Start: "Start",  
    ReadMail: 'Read mail', 
    Mailboxes: 'Mailboxes',  
    Inbox: "Inbox",
    SentItems: "Sent Items",
    DraftItems: "Draft Items",
    Trash: "Trash",
    Outbox: "Outbox",


    Login: "Log on",
    Username: "Username",
    Password:   "Password",
    Company: "Company",
    Menu: "Menu",
    Contacts:   "Contacts",
    Scheduler: "Scheduler",
    Webmail: "Webmail",
    Logout: "Log out",
      Detail: "Detail",
      Address: "Address",
      Street: "Street",
      Zip: "Zip",
      City: "City",
      Communication: "Communication Info",
      Info: "Info",
      Search: "Search",
      Previous: "Previous",
      Today: "Today",
      Next: "Next",
      Month: "Month",
      Week: "Week",
      Day: "Day",
      AppointmentDetail: "Appointment Detail",
      Title: "Title",
      Type: "Type",
      Priority: "Priority",
      StartDate: "Start Date",
    Thisisanalldayappointment: "This is an all day appointment", 
      EndDate: "End Date",
      Recurrent: "Recurrent",
      Location: "Location",
      Reminder: "Reminder",
      Private: "Private"
      

  });
  $translateProvider.translations("es", {
    Monday: "Lunes",
    Tuesday: "Martes",
    Wednesday: "Mi??rcoles",
    Thursday: "Jueves",
    Friday: "Viernes",
    Saturday: "S??bado",
    Sunday: "Domingo",

    Funtion: "Funcion",
    From: "De",
    To: "A",
    DateMail: "Fecha",
    Subject: "Asunto",
    
    Description: "Descripcion",
    CheckFolder: "Por favor revise su carpeta de BMapp.",
    Downloaded: "Descargado",
    SelectAnother: "Por favor, seleccione otra carpeta.",
    PulltoRefresh: "Por favor, deslice para refrescar.",
    NoItems: "No hay art??culos",
    Messagefailed: "Por favor compruebe sus credenciales.",
    MessageRequired: "Credenciales de inicio de sesi??n no v??lidos.",
    Loading: "Mas funcionalidades en construccion",
    Start: "Inicio",  
    ReadMail: 'Leer email', 
    Mailboxes: 'Carpetas',
    Inbox: "Bandeja de entrada",
    SentItems: "Elementos enviados",
    DraftItems: "Borrador",
    Trash: "Basurero",
    Outbox: "Bandeja de salida",

    Login: "Ingresar",
    Username: "Usuario",
    Password: "Contrase??a",
    Company: "Compa??ia",
    Menu: "Menu",

    Contacts:   "Contactos",
    Scheduler: "Calendario",
    Webmail: "Webmail",
    Logout: "Salir", 
    
    Detail: "Detalle",
      Address: "Direccion",
      Street: "Calle",
      Zip: "Zip",
      City: "Ciudad",
      Communication: "Informaci??n de comunicaciones",
      Info: "Info",
      Search: "Buscar",
      Previous: "Anterior",
      Today: "Hoy",
      Next: "Siguiente",
      Month: "Mes",
      Week: "Semana",
      Day: "Dia",
      AppointmentDetail: "Detalle de Evento",
      Title: "Titulo",
      Type: "Tipo",
      Priority: "Prioridad",
      StartDate: "Fecha de inicio",
    Thisisanalldayappointment: " Evento de todo el d??a ", 
      EndDate: "Fecha de finalizaci??n",
      Recurrent: "Recurrente",
      Location: "Localizaci??n",
      Reminder: "Recordatorio",
      Private: "Privado"
  });



  $translateProvider.translations("de", {
    Monday: "Montag",
    Tuesday: "Dienstag",
    Wednesday: "Mittwoch",
    Thursday: "Donnerstag",
    Friday: "Freitag",
    Saturday: "Samstag",
    Sunday: "Sonntag",

    Funtion: "Funktion",
    From: "Von",
    To: "An",
    DateMail: "Datum",
    Subject: "Betreff",
    
    Description: "Beschreibung",
    CheckFolder: "Bitte ??berpr??fen Sie Ihre Ordner BMapp.",
    Downloaded: "Heruntergeladen",
    SelectAnother: "Bitte w??hlen Sie einen anderen Ordner",
    PulltoRefresh: "Bitte ziehen Sie zur Erfrischung.",
    NoItems: "Keine Eintr??ge",
    Messagefailed: "Bitte ??berpr??fen Sie Ihre Anmeldeinformationen.",
    MessageRequired: "Anmeldeinformationen ung??ltig Sitzung.",
    Loading: "Mehrere Funktionen unter construccion",
    Start: "Anfang", 
    ReadMail: 'eMail lesen', 
    Mailboxes: 'Mailboxes',
    Inbox: "Posteingang",
    SentItems: "Gesendet",
    DraftItems: "Entwurf",
    Trash: "Papierkorb",
    Outbox: "Postausgang",

    Login: "Anmeldung",
    Username: "Benutzername",
    Password: "Kennwort",
    Company: "Firma",
    Menu: "Men??",

    Contacts:   "Kontakte",
    Scheduler: "Termine",
    Webmail: "Webmail",
    Logout: "Abmelden", 

      Detail: "Allgemein",
      Address: "Anschrift",
      Street: "Stra??e",
      Zip: "PLZ",
      City: "Ort",
      Communication: "Kontakt",
      Info: "Info",
      Search: "Suche",
      Previous: "fr??her",
      Today: "heute",
      Next: "n??chster",
      Month: "Monat",
      Week: "Woche",
      Day: "Tag",
      AppointmentDetail: "Termin Allgemein",
      Title: "Termin",
      Type: "Terminart",
      Priority: "Priorit??t",
     StartDate: "Beginn",
    Thisisanalldayappointment: "ganzt??gig", 
      EndDate: "Ende",
      Recurrent: "Wiederkehrender Termin",
      Location: "Ort",
      Reminder: "Erinnerung ",
      Private: "Privat"
  });



  $translateProvider.translations("fr", {
    Monday: "Lundi",
    Tuesday: "Mardi",
    Wednesday: "Mercredi",
    Thursday: "Jeudi",
    Friday: "Vendredi",
    Saturday: "Samedi",
    Sunday: "Dimanche",

    Funtion: "Fonction",
    From: "De",
    To: "A",
    DateMail: "Date",
    Subject: "Affaire", 
      
    Description: "Description",
    CheckFolder: "Se il vous pla??t v??rifier votre dossier BMapp.", 
    Downloaded: "T??l??charg??", 
    SelectAnother: "Se il vous pla??t s??lectionner un autre dossier", 
    PulltoRefresh: "Se il vous pla??t tirer pour rafra??chir.", 
    NoItems: "Aucun article", 
    Messagefailed: "Se il vous pla??t v??rifier vos informations d'identification.",
    MessageRequired: "R??f??rences de connexion de session valide.",  
    Loading: "Plusieurs fonctions sous construccion", 
    Start: "D??but", 
    ReadMail: 'Lire e-mail',         
    Mailboxes: 'Dossiers',    
    Inbox: "Plateau d'entr??e",
    SentItems: "El??ments envoy??s",
    DraftItems: "Brouillon",
    Trash: "Corbeille",
    Outbox: "Plateau de sortie",

    Login: "Connexion",
    Username: "Utilisateur",
    Password: "Mot de passe",
    Company: "Soci??t??",
    Menu: "Menu",

    Contacts:   "Contacts",
    Scheduler: "Scheduler",
    Webmail: "Webmail",
    Logout: " Sortir",

      Detail: "D??tail",
      Address: "Adresse",
      Street: "Rue",
      Zip: "Zip",
      City: "Ville",
      Communication: "Information de communications",
      Info: "Infos",
      Search: "Rechercher",
      Previous: "pr??c??dent",
      Today: "aujourd'hui",
      Next: "suivant",
      Month: "Mois",
      Week: "Semaine",
      Day: "Jour",
      AppointmentDetail: "??v??nement D??tail",
      Title: "Titre",
      Type: "Tipy",
      Priority: "Priorit??",
      StartDate: "Date de d??but",
    Thisisanalldayappointment: "??v??nement de toute la journ??e", 
      EndDate: "Fin de date",
      Recurrent: "R??current(e)",
      Location: "Localisation",
      Reminder: "Rappel",
      Private: "Priv??"
  });

  $translateProvider.preferredLanguage("en");
  $translateProvider.fallbackLanguage("en");
});
