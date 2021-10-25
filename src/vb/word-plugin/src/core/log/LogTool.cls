VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
  Persistable = 0  'NotPersistable
  DataBindingBehavior = 0  'vbNone
  DataSourceBehavior  = 0  'vbNone
  MTSTransactionMode  = 0  'NotAnMTSObject
END
Attribute VB_Name = "LogMeBaby"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = True
Attribute VB_Ext_KEY = "SavedWithClassBuilder6" ,"Yes"
Attribute VB_Ext_KEY = "Top_Level" ,"Yes"
'===========================================
'LogTool Dll
'===========================================
'By: Behrooz Sangani
'Email: bs20014@yahoo.com
'Web: http://www.geocities.com/bs20014/
'===========================================
'Description:
'LogTool logs application events same as
'VB LogEvent. It's just a little simpler
'and is very easy to use. As an option
'you can generate message boxes after logging
'and save writing endless message boxes.
''''''''''''''''''''''''''''''''''''''''''''
'Copyright:
'Copyright (c) 2002 Behrooz Sangani
'You may use and destribute the source code or
'the compiled Dll inside your applications for free.
'However, you do not have permission to redestribute
'the source code in any case(CD,etc.)
''''''''''''''''''''''''''''''''''''''''''''
'HowTo:
'To see class methods and properties refer
'to ReadMe.txt

'UPDATE NOTE FOR v1.2:
'    Features added:
'        * Customizable Debug Window
'        * Predefined Messages

Public Enum LogType       'Type of Logging
    CommentLine = 0
    ErrorLog = 1
    WarningLog = 2
    InformationLog = 3
End Enum

Public Enum SaveType     'How data must be saved
    AppendToLog = 1
    OverWriteLog = 2
End Enum

'local variable(s) to hold property value(s)
Private mvarLogFile As String 'local copy
Private mvarShowMsgAfterLog As Boolean 'local copy
Private mvarAppTitle As String 'local copy

Private Const ForWriting = 2
Private Const ForAppending = 8
Dim f, fs, sDate, sTime, MsgType
Dim strData As String, Msg As String
'===========================================

'Application Title to be written as Log File Header
Public Property Let AppTitle(ByVal vData As String)
    mvarAppTitle = vData
    sTitle = mvarAppTitle
End Property
Public Property Get AppTitle() As String
    AppTitle = mvarAppTitle
End Property
'===========================================

'Defines whether to show a message box after logging
Public Property Let ShowMsgAfterLog(ByVal vData As Boolean)
    mvarShowMsgAfterLog = vData
End Property
Public Property Get ShowMsgAfterLog() As Boolean
    ShowMsgAfterLog = mvarShowMsgAfterLog
End Property
'===========================================

'Log file to save data to
Public Property Let LogFile(ByVal vData As String)
    mvarLogFile = vData
    'For tracker
    sFile = mvarLogFile
End Property
Public Property Get LogFile() As String
    LogFile = mvarLogFile
End Property
'===========================================

'Save data to log file
Public Function SaveToLog(Message As String, Optional LogType As LogType = InformationLog, Optional SaveType As SaveType = AppendToLog) As Long
Attribute SaveToLog.VB_UserMemId = 0

    On Error GoTo error

    'Check if the log file is set
    If LogFile = "" Then
        MsgBox "LogFile property not set!", vbCritical, "Error!"
        Exit Function
    End If

    Select Case SaveType
        Case 1 'appending
            Set fs = CreateObject("Scripting.FileSystemObject")
            'Check Existance of LogFile, if False create it
            If fs.FileExists(LogFile) = False Then
                'No appending, write new file header
                Set f = fs.OpenTextFile(LogFile, ForWriting, True)
                f.Write "*********************************LogTool" & vbCrLf
                f.Write "  " & AppTitle & " Log File" & vbCrLf
                f.Write "*********************************" & vbCrLf & vbCrLf
            Else
                'Open log file for appending
                Set f = fs.OpenTextFile(LogFile, ForAppending)
            End If
        Case 2 'writing
            'write header
            Set fs = CreateObject("Scripting.FileSystemObject")
            Set f = fs.OpenTextFile(LogFile, ForWriting, True)
            f.Write "*********************************LogTool" & vbCrLf
            f.Write "  " & AppTitle & " Log File" & vbCrLf
            f.Write "*********************************" & vbCrLf & vbCrLf
    End Select

    'Time stamp
    sDate = vbCrLf & ";" & Format(Date, "dd/mm/yy")
    sTime = Format(Time, "hh:mm:ss AM/PM") & ";" & vbCrLf
    strData = sDate & ";" & sTime

    'Log type stamp
    Select Case LogType
        Case 0 'Comment Line
            strData = "'"
        Case 1 'Error
            strData = strData & "  Error: "
        Case 2 'Warning
            strData = strData & "  Warning: "
        Case 3 'Information
            strData = strData & "  Information: "
    End Select

    'add message
    strData = strData & Message

    'write data and close log file
    f.Write strData & vbCrLf
    f.Close

    'if we must show a message box then show it
    Msg = Message
    If ShowMsgAfterLog = True Then ShowMsg LogType

    'Add to debug window
    'I added a little fancy method to type message in the debug window

    If frmDebug.txtDebug <> "" Then frmDebug.txtDebug = frmDebug.txtDebug & vbCrLf
    
    'REMOVED
    ' For i = 1 To Len(Msg & vbCrLf)
    '     TypeMsg Mid(Msg & vbCrLf, i, 1)
    ' Next i

    frmDebug.txtDebug = frmDebug.txtDebug & ">" & Msg
    frmDebug.txtDebug.SelStart = Len(frmDebug.txtDebug) - 1

    'was successful
    SaveToLog = 1

    Exit Function
error:      'Handle any unexpected error
    'Ooops!
    SaveToLog = 0
    MsgBox "An Error occured while saving data! Data was not saved...", vbCritical, "LogTool Error!"

End Function
'===========================================

' FUNCTION REMOVED

'Type the message in Debug Window
'Private Function TypeMsg(strMsg) As Long
'    For i = 1 To Len(strMsg)
'        frmDebug.txtDebug.SelStart = Len(frmDebug.txtDebug)
'        frmDebug.txtDebug.SelText = Mid(strMsg, i, 1)
'        DoEvents
'        Pause 0.01
'    Next i
'    frmDebug.txtDebug.SelStart = Len(frmDebug.txtDebug)
'    TypeMsg = 1
'End Function
'===========================================

'Show MsgBox
Private Function ShowMsg(LogType As LogType)

    Select Case LogType
        Case 1  'Critical error
            MsgType = vbCritical
            MsgTitle = "Error"
        Case 2  'Warning exclamation
            MsgType = vbExclamation
            MsgTitle = "Warning"
        Case 3  'Show Information
            MsgType = vbInformation
            MsgTitle = "Notice"
    End Select

    'Show it
    MsgBox Msg, MsgType, MsgTitle

End Function
'===========================================

'Show About Message
Public Sub About()
    'About LogTool
    MsgBox "Behrooz's LogTool DLL v" & App.Major & "." & App.Minor & "." & App.Revision & vbCrLf & "By: Behrooz Sangani <bs20014@yahoo.com>" & vbCrLf & "Web: http://www.geocities.com/bs20014" & vbCrLf & vbCrLf & "LogTool is an easy way to save all error, warning and information messages to custom log files" & vbCrLf & "Freeware version! Use for free and at your own risk", vbInformation, "About Behrooz's LogTool"
End Sub
'===========================================

'FUNCTION REMOVED
'will be added soon

'Show Tracker
'Public Sub ShowTracker()
 'Tracker Form
' frmTracker.Show
'End Sub
'===========================================

'Show Debug Window
Public Sub ShowDebugWindow(bShow As Boolean, Optional lLeft As Long, Optional lTop As Long, Optional lWidth As Long, Optional lHeight As Long, Optional lBackColor As Long, Optional lForeColor As Long)
    If bShow Then
        If lLeft <> 0 Then
            frmDebug.Move lLeft
            If lTop <> 0 Then frmDebug.Move lLeft, lTop
        End If
        If lWidth <> 0 Then frmDebug.Width = lWidth
        If lHeight <> 0 Then frmDebug.Height = lHeight
        If lBackColor <> 0 Then frmDebug.txtDebug.BackColor = lBackColor
        If lForeColor <> 0 Then frmDebug.txtDebug.ForeColor = lForeColor
        frmDebug.Show
        frmDebug.txtDebug.SelStart = Len(frmDebug.txtDebug) - 1
    Else
        frmDebug.Hide
    End If
End Sub
'===========================================

'On Terminate
Private Sub Class_Terminate()

    On Error Resume Next
    'Set objects to nothing on exit
    Set f = Nothing
    Set fs = Nothing

End Sub
