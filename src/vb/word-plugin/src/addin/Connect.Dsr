VERSION 5.00
Begin {AC0714F6-3D04-11D1-AE7D-00A0C90F26F4} Connect 
   ClientHeight    =   8655
   ClientLeft      =   1740
   ClientTop       =   1545
   ClientWidth     =   12120
   _ExtentX        =   21378
   _ExtentY        =   15266
   _Version        =   393216
   Description     =   "Plantilla de proyecto de complemento"
   DisplayName     =   "Mi complemento"
   AppName         =   "Microsoft Word"
   AppVer          =   "Microsoft Word 9.0"
   LoadName        =   "Startup"
   LoadBehavior    =   3
   RegLocation     =   "HKEY_CURRENT_USER\Software\Microsoft\Office\Word"
End
Attribute VB_Name = "Connect"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = True
Attribute VB_Ext_KEY = "RVB_ModelStereotype" ,"AddInDesigner"
Option Explicit
   Dim oBar As Office.CommandBar
   Dim oBar1 As Office.CommandBar

Private Function getRegisterKey(Name As String) As String
getRegisterKey = regQuery_A_Key(HKEY_CURRENT_USER, _
                                "Software\Elwis-Client\Config", _
                                Name)
End Function

Private Sub setRegisterKey(Name As String, value As String)
regCreate_Key_Value HKEY_CURRENT_USER, _
                          "Software\Elwis-Client\Config", _
                          Name, value, False
End Sub
         
   Private Sub AddinInstance_OnConnection(ByVal Application As Object, _
    ByVal ConnectMode As AddInDesignerObjects.ext_ConnectMode, _
    ByVal AddInInst As Object, custom() As Variant)
    Dim oCommandBars As Office.CommandBars
    
    If getRegisterKey("firstrun") = "true" Then
    On Error GoTo noFound
    setRegisterKey "firstrun", "false"
    
    Dim cmb As Office.CommandBar
    Debug.Print "Log 0 (before Application.CommandBars)"
    Set cmb = Application.CommandBars("BM Plugin")
    cmb.Delete
    Set cmb = Nothing
    
    Set cmb = Application.CommandBars("BM Variables")
    cmb.Delete
    Set cmb = Nothing
    
    'For some strange reason an error was thrown when Application.CommandBars were
    'called the first time (a clean install) so, for that reason I moved this line
    'as the second line after the If.
    'setRegisterKey "firstrun", "false"
    GoTo foundBar
noFound:
    Debug.Print "FAIL: " & Err.Description
    End If
foundBar:
    Dim dll As Object
    Set dll = CreateObject("ElwisCore.Core")
    
    dll.setWordApplication Application, AddInInst.ProgId
    
    AddInInst.Object = dll
    
    
   End Sub

   Private Sub AddinInstance_OnDisconnection(ByVal RemoveMode As _
      AddInDesignerObjects.ext_DisconnectMode, custom() As Variant)
      On Error Resume Next
      
    End Sub
