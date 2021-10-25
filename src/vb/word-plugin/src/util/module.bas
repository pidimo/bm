Attribute VB_Name = "Module1"
' ====== Begin General Declarations Module ===========

' ----Begin SHGetSpecialFolderLocation API Declarations----

' Declare Public variables.
Public Type ShortItemId
   cb As Long
   abID As Byte
End Type

Public Type ITEMIDLIST
   mkid As ShortItemId
End Type

' Declare API functions.
Public Declare Function SHGetPathFromIDList Lib "shell32.dll" _
   (ByVal pidl As Long, ByVal pszPath As String) As Long

Public Declare Function SHGetSpecialFolderLocation Lib _
   "shell32.dll" (ByVal hwndOwner As Long, ByVal nFolder _
   As Long, pidl As ITEMIDLIST) As Long

' ----End SHGetSpecialFolderLocation API Declarations----

' ----Begin Get 32 Bit Registry Entry Value Declarations----
Const HKEY_CURRENT_USER = &H80000001
Const ERROR_SUCCESS = 0&
Const REG_DWORD = 4
Const REG_BINARY = 3
Const REG_SZ = 1
Const REG_EXPAND_SZ = 2 ' Unicode nul terminated string
Const ERROR_NONE = 0
Const ERROR_BADDB = 1
Const ERROR_BADKEY = 2
Const ERROR_CANTOPEN = 3
Const ERROR_CANTREAD = 4
Const ERROR_CANTWRITE = 5
Const ERROR_OUTOFMEMORY = 6
Const ERROR_ARENA_TRASHED = 7
Const ERROR_ACCESS_DENIED = 8
Const ERROR_INVALID_PARAMETERS = 87
Const ERROR_NO_MORE_ITEMS = 259
Const KEY_ALL_ACCESS = &H3F
Const REG_OPTION_NON_VOLATILE = 0

Declare Function RegOpenKeyEx Lib "advapi32.dll" _
    Alias "RegOpenKeyExA" (ByVal hKey As Long, _
    ByVal lpSubKey As String, ByVal ulOptions As Long, _
    ByVal samDesired As Long, phkResult As Long) As Long
Declare Function RegCloseKey Lib "advapi32.dll" _
    (ByVal hKey As Long) As Long
Declare Function RegQueryValueExString Lib "advapi32.dll" Alias _
    "RegQueryValueExA" (ByVal hKey As Long, ByVal lpValueName As _
    String, ByVal lpReserved As Long, lpType As Long, ByVal lpData _
    As String, lpcbData As Long) As Long
Declare Function RegQueryValueExLong Lib "advapi32.dll" Alias _
    "RegQueryValueExA" (ByVal hKey As Long, ByVal lpValueName As _
    String, ByVal lpReserved As Long, lpType As Long, lpData As _
    Long, lpcbData As Long) As Long
Declare Function RegQueryValueExNULL Lib "advapi32.dll" Alias _
    "RegQueryValueExA" (ByVal hKey As Long, ByVal lpValueName As _
    String, ByVal lpReserved As Long, lpType As Long, ByVal lpData _
    As Long, lpcbData As Long) As Long
Declare Function RegQueryValueEx Lib "advapi32.dll" Alias _
   "RegQueryValueExA" (ByVal hKey As Long, ByVal lpValueName As String, _
   ByVal lpReserved As Long, lpType As Long, lpData As Any, lpcbData _
   As Long) As Long

' ----End Get 32 Bit Registry Entry Value Declarations----


