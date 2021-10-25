Attribute VB_Name = "ModMain"
'=========================================================================================
'  Main Module
'  Functions to read the log file
'=========================================================================================
'  Created By: Behrooz Sangani <bs20014@yahoo.com>
'  Published Date: 27/06/2002
'  WebSite: http://www.geocities.com/bs20014
'  Legal Copyright: Behrooz Sangani © 27/06/2002
'=========================================================================================

'This module belongs to tracker form(will come in the next version soon)

Global sFile As String
Global sTitle As String
Public HC As New Collection

Public Function ReadLine(ByVal LineNumber As Long, sFile As String, Optional SemiColonLoad As Boolean = False) As String
    On Error GoTo error

    Dim NextContext As String 'line from file
    Dim tmp, Tmp2, Tmp3, fPath

    ff = FreeFile
    Open sFile For Input As ff  'loop to read all lines
    While Not EOF(ff)
        Line Input #ff, NextContext
        If SemiColonLoad = True Then
            If Left(NextContext, 1) = ";" Then HC.Add NextContext
        Else
            HC.Add NextContext 'adding line to collection
        End If
    Wend
    Close ff

    'show what context file is needed
    ReadLine = HC.Item(LineNumber)


    Exit Function
error:
    'handle file error
    If Err.Number = 53 Then
        MsgBox "Information Access Error!" & vbCrLf & "Make sure files are not corrupted or removed.", vbCritical, "Error"
    Else
        MsgBox " Unexpected Error!", vbCritical, "Error"
    End If

    Close 'handle close after error

End Function

Function ReadFile(sFilePath As String) As String
    Dim fs, gf, ts
    Dim sData As String
    Set fs = CreateObject("Scripting.FileSystemObject")
    If Not fs.FileExists(sFilePath) Then Exit Function
    Set gf = fs.GetFile(sFilePath)
    Set ts = gf.OpenAsTextStream(1, -2)
    sData = ts.ReadAll
    ts.Close
    ReadFile = sData
End Function

'NOT REQUIRED(was used in TypeMsg Function)
Sub Pause(vInterval)
    Current = Timer
    Do While Timer - Current < Val(vInterval)
        DoEvents
    Loop
End Sub

