VERSION 5.00
Begin VB.Form Form1 
   Caption         =   "Form1"
   ClientHeight    =   3195
   ClientLeft      =   60
   ClientTop       =   345
   ClientWidth     =   4680
   LinkTopic       =   "Form1"
   ScaleHeight     =   3195
   ScaleWidth      =   4680
   StartUpPosition =   3  'Windows Default
   Visible         =   0   'False
End
Attribute VB_Name = "Form1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False






Private Declare Function SHGetSpecialFolderPath Lib "shell32" Alias "SHGetSpecialFolderPathA" (ByVal hwndOwner As Long, ByVal lpszPath As String, ByVal nFolder As Long, ByVal fCreate As Long) As Long



Private Const CSIDL_APPDATA As Long = &H1A

Private Const MAX_PATH As Long = 260



Public Function getApplicationDataPath() As String

Dim sBuffer As String



sBuffer = String$(MAX_PATH, vbNullChar)

Call SHGetSpecialFolderPath(1, sBuffer, CSIDL_APPDATA, 0&)

getApplicationDataPath = Left$(sBuffer, InStr(1, sBuffer, vbNullChar) - 1)

End Function



Private Sub copyXML()

Dim path As String

Dim fs As Object

On Error GoTo pestes

path = getApplicationDataPath & "\Elwis-Plugin"

Set fs = CreateObject("Scripting.FileSystemObject")


If Not fs.FolderExists(path) Then

'fs.DeleteFolder path, True

fs.CreateFolder path

End If


fs.CopyFile CurDir & "\Elwis-Data\TemplateFields.xml", path & "\TemplateFields.xml", True

fs.CopyFile CurDir & "\Elwis-Data\TableFields.xml", path & "\TableFields.xml", True

fs.CopyFile CurDir & "\Elwis-Data\XMLResources_en.xml", path & "\XMLResources_en.xml", True

fs.CopyFile CurDir & "\Elwis-Data\XMLResources_es.xml", path & "\XMLResources_es.xml", True

fs.CopyFile CurDir & "\Elwis-Data\XMLResources_de.xml", path & "\XMLResources_de.xml", True

fs.CopyFile CurDir & "\Elwis-Data\XMLResources_fr.xml", path & "\XMLResources_fr.xml", True

pestes:

Set fs = Nothing

End Sub





Private Sub Form_Load()

Dim fs As Object

Dim path As String

Dim app As Word.Application

Dim cmdbar

copyXML



    On Error Resume Next

    Set fs = CreateObject("Scripting.FileSystemObject")
    Set app = New Word.Application

    

    path = app.Options.DefaultFilePath(wdStartupPath)

     'MsgBox "Param-" & Command & "-"


 
    'First version
    Set cmdbar = app.CommandBars("Elwis")
    cmdbar.Delete
    Set cmdbar = Nothing
    
    '20060525 version
    Set cmdbar = app.CommandBars("Elwis Tools")
    cmdbar.Delete
    Set cmdbar = Nothing
    
    Set cmdbar = app.CommandBars("Template Tools")
    cmdbar.Delete
    Set cmdbar = Nothing
    
    'Current version 1.2
    Set cmdbar = app.CommandBars("BM Plugin")
    cmdbar.Delete
    Set cmdbar = Nothing
    
    Set cmdbar = app.CommandBars("BM Variables")
    cmdbar.Delete
    Set cmdbar = Nothing

    Debug.Print "Closing Word"

    app.Quit
    
    Set app = Nothing
    
    If Command = "-I" Then
        fs.CopyFile CurDir & "\Elwis.dot", path & "\Elwis.dot", True
    Else
        fs.DeleteFile path & "\Elwis.dot", True
    End If
    
    
    Set fs = Nothing

Unload Me

Exit Sub

'MsgBox Err.Description & " - " & Err.Source

End Sub



