Attribute VB_Name = "TimerUitl"
'###########################################################################################
'## Made By     : Michel Posseth  ,Rotterdam , Netherlands                                ##
'##
'## Contact     : michel@posseth.com                                                      ##
'## Note        : if you might have any questions about this routine feel free to contact ##
'##               me i think / hope that the below documentation is so rich that you don`t##
'##               have to                                                                 ##
'## Limitations :  NONE !! use it as you like don`t bother to name me :-) altough it would##
'## be nice if you did :-) serious i don`t expect anything back and so can you from me :-)##
'###########################################################################################
'code is documented in Dutch and English but neither language has an documetation difference
'
'NL : wat is de functie van deze code ? :
' dit is een routine die zich gedraagt als een timer met als verschil dat
' 1 : het werkt volledig vanuit code (dus je app heeft geen form nodig)
' 2 : de waarde is zoals je wilt : minuten , uren, dagen
' 3 : wordt aangestuurd vanuit Windows eigen klok functie dus snelheid van computer
'     heeft geen invloed !!
' 4 : neemt geen meetbare resources in beslag  dit in tegenstelling tot een timer
'     control , met een statische counter
' 5 : je moet het op de juiste mannier gebruiken zie documentatie anders crasht je
'     programma of erger nog de hele computer
'EN : wat is de function of this code ? :
' this is a routine that behavous itself like a timer with as main diference that
' 1: it works from code ( so youre app doesn`t need a form)
' 2: the value is as you want minutes , hours , days
' 3: driven from windows own timer function so the speed of the computer doesn`t affect it
' 4: No resources taken away !! this in comparisation to a timer control with a
'    static counter ( this sure afects the rest of youre routines )
' 5: you  must use it at the right way ( see documetation ) otherwise youre program
'    or worse computer will crash !! ( for sure !! )
Option Explicit

' NL: API`s om de timer te aktiveren en te killen EN: API`s to activate and kill the timer
Declare Function SetTimer Lib "user32" (ByVal hwnd As Long, ByVal nIDEvent As Long, ByVal uElapse As Long, ByVal lpTimerfunc As Long) As Long
Declare Function KillTimer Lib "user32" (ByVal hwnd As Long, ByVal nIDEvent As Long) As Long

Private TimerID As Long 'NL: zetten we hem mee aan en uit EN: put on and of with this ID
Public TimerActive As Boolean 'NL :staat hij nu aan of niet EN: is the timer active ?
Public Sub ActiveerTimer(ByVal seconds As Long)
'NL: zoals de standaard timer control is de default miliseconden met een reken sommetje
'maak je er makkelijk seconden minuten uren of dagen van !
'probeer dat maar eens met een standaard timer control !

'EN: like the basic timer control the default is Miliseconds
'but with this simple calculation you could convert it to minutes hours or even days !
'try that with the standard timer control !!!
seconds = seconds * 1000
'EN: always check if there is already an instance of this timer running
'if there is disable it first and start with the new value
'NL: altijd eerst even controleren of de codetimer niet al aktief is
If TimerActive Then Call DeactiveerTimer
TimerActive = True
TimerID = SetTimer(0, 0, seconds, AddressOf Timer_CBK)
End Sub
Public Sub DeactiveerTimer()
KillTimer 0, TimerID
TimerActive = False
'NL: controleer altijd of de timer uitgezet is voordat je je programma afsluit
'doe je dit niet dan zal of je programma of zelfs de hele computer crashen
'EN: if youre program is shutting down Never !! forget to kill the Timer first
'not shutting it down means the program or computer will crash !! ( for sure )
End Sub
Sub Timer_CBK(ByVal hwnd As Long, ByVal uMsg As Long, ByVal idevent As Long, ByVal Systime As Long)
On Error GoTo killT

If Not helpAssistent Is Nothing Then
    helpAssistent.Close
End If

killT:
If TimerActive Then Call DeactiveerTimer
'de timer gaat door en door als je hem niet deactiveerd zelfs als je het programma afsluit
'the timer will go on and on if you don`t deactivate it even if you shut down the program
End Sub



