Attribute VB_Name = "basBlowfishFileFns"
Option Explicit
Option Base 0

' basBlowfishFileFns: Wrapper functions to process files with Blowfish

' Version 6. November 2003. Moved CBC functions into this module.
'            Added check for length of IV in CBC versions.
' Version 5. January 2002. Separated from basBlowfishFns
' First published October 2000.
'************************* COPYRIGHT NOTICE*************************
' This code was originally written in Visual Basic by David Ireland
' and is copyright (c) 2000-2 D.I. Management Services Pty Limited,
' all rights reserved.

' You are free to use this code as part of your own applications
' provided you keep this copyright notice intact and acknowledge
' its authorship with the words:

'   "Contains cryptography software by David Ireland of
'   DI Management Services Pty Ltd <www.di-mgt.com.au>."

' If you use it as part of a web site, please include a link
' to our site in the form
' <A HREF="http://www.di-mgt.com.au/crypto.html">Cryptography
' Software Code</a>

' This code may only be used as part of an application. It may
' not be reproduced or distributed separately by any means without
' the express written permission of the author.

' David Ireland and DI Management Services Pty Limited make no
' representations concerning either the merchantability of this
' software or the suitability of this software for any particular
' purpose. It is provided "as is" without express or implied
' warranty of any kind.

' Please forward comments or bug reports to <code@di-mgt.com.au>.
' The latest version of this source code can be downloaded from
' www.di-mgt.com.au/crypto.html.
'****************** END OF COPYRIGHT NOTICE*************************

' The functions in this module are:
' blf_FileEnc(sFileIn, sFileOut): Enciphers file with name sFileIn
'   with current key and writes output to new file sFileOut
' blf_FileDec(sFileIn, sFileOut): Deciphers file with name sFileIn
'   with current key and writes output to new file sFileOut
' blf_FileEncCBC(): Enciphers file using CBC mode.
' blf_FileDecCBC(): Deciphers file using CBC mode.

' To set current key, call blf_KeyInit(aKey())
'   where aKey() is the key as an array of Bytes

' Use faster API call to copy bytes
Private Declare Sub CopyMemory Lib "kernel32" Alias "RtlMoveMemory" _
    (ByVal lpDestination As Any, ByVal lpSource As Any, ByVal Length As Long)

Public Function blf_FileEnc(sFileIn As String, sFileOut As String) As Boolean
' Encrypts file with name <sFileIn> and writes ciphertext to new file <sFileOut>
' Requires key and boxes to be initialised
' Any existing file <sFileOut> will be overwritten.
    Dim hFileIn As Long
    Dim hFileOut As Long
    Dim aBytes(7) As Byte   ' Block of 8 bytes
    Dim i As Long
    Dim nLen As Long, nPad As Long, nBlocks As Long
    
    blf_FileEnc = False
    
    ' Open files for input and output
    ' Use Windows API functions to avoid unwanted padding
    hFileIn = ap_OpenFileReadOnly(sFileIn)
    If hFileIn = -1 Then
        Error 68    ' Add your own error handler here ...
    End If
    
    hFileOut = ap_OpenFileWrite(sFileOut)
    If hFileOut = -1 Then
        ' Version 4: Added close file on error
        ' Thanks to Robert Garofalo for this.
        Call ap_CloseFile(hFileIn)
        Error 68    ' ... and here
    End If
    
    ' Calculate padding required & no of 8-byte blocks
    nLen = FileLen(sFileIn)
    nBlocks = nLen \ 8
    nPad = (nBlocks + 1) * 8 - nLen
    
    ' Read in file in 8-byte blocks
    For i = 1 To nBlocks
        ' Fetch next block
        Call ap_GetBytes(hFileIn, aBytes, 8)
        ' Encrypt and write to output file
        Call blf_EncryptBytes(aBytes)
        Call ap_PutBytes(hFileOut, aBytes, 8)
    Next
    
    ' Read in and pad the last block
    Call ap_GetBytes(hFileIn, aBytes, 8)
    For i = 1 To nPad
        aBytes(8 - i) = nPad
    Next
    ' Encrypt and write to output file
    Call blf_EncryptBytes(aBytes)
    Call ap_PutBytes(hFileOut, aBytes, 8)
    
    Call ap_CloseFile(hFileIn)
    Call ap_CloseFile(hFileOut)
    
    blf_FileEnc = True
    
End Function

Public Function blf_FileDec(sFileIn As String, sFileOut As String) As Boolean
' Decrypts file with name <sFileIn> and writes plaintext to new file <sFileOut>
' Requires key and boxes to be initialised
' Any existing file <sFileOut> will be overwritten.
    Dim hFileIn As Long
    Dim hFileOut As Long
    Dim aBytes(7) As Byte   ' Block of 8 bytes
    Dim i As Long
    Dim nLen As Long, nPad As Long, nBlocks As Long
    
    blf_FileDec = False
    
    ' Open files for input and output
    ' Use Windows API functions to avoid unwanted padding
    hFileIn = ap_OpenFileReadOnly(sFileIn)
    If hFileIn = -1 Then
        Error 68    ' Add your own error handler here ...
    End If
    
    hFileOut = ap_OpenFileWrite(sFileOut)
    If hFileOut = -1 Then
        ' Version 4: Added close file on error
        Call ap_CloseFile(hFileIn)
        Error 68    ' ... and here
    End If
    
    ' Calculate no of 8-byte blocks to read (should be a multiple of 8)
    nLen = FileLen(sFileIn)
    nBlocks = nLen \ 8
    
    ' Read in file in 8-byte blocks except last block
    For i = 1 To nBlocks - 1
        ' Fetch next block
        Call ap_GetBytes(hFileIn, aBytes, 8)
        ' Decrypt and write to output file
        Call blf_DecryptBytes(aBytes)
        Call ap_PutBytes(hFileOut, aBytes, 8)
    Next
    
    ' Read in the last block
    Call ap_GetBytes(hFileIn, aBytes, 8)
    ' Decrypt
    Call blf_DecryptBytes(aBytes)
    ' Write excluding padding
    nPad = aBytes(7)
    If (nPad <= 0 Or nPad > 8) Then
        ' Version 4: Added check on padding value
        nPad = 0
    End If
    Call ap_PutBytes(hFileOut, aBytes, 8 - nPad)
    
    Call ap_CloseFile(hFileIn)
    Call ap_CloseFile(hFileOut)
    
    blf_FileDec = True
    
End Function

Public Function blf_FileEncCBC(sFileIn As String, sFileOut As String, _
    strIV As String) As Boolean
' Encrypts file with name <sFileIn> and writes ciphertext to new file <sFileOut>
' in CBC mode using hex string <strIV> as initialisation vector
' Requires key and boxes to be initialised
' Any existing file <sFileOut> will be overwritten.
    Dim hFileIn As Long
    Dim hFileOut As Long
    Dim i As Long
    Dim nLen As Long, nPad As Long, nBlocks As Long
    Dim aBytes(7) As Byte   ' Block of 8 bytes
    Dim aReg() As Byte    ' Feedback register
    
    ' Open files for input and output
    ' Use Windows API functions to avoid unwanted padding
    ' Version 4: Changes to error handling as noted.
    hFileIn = ap_OpenFileReadOnly(sFileIn)
    If hFileIn = -1 Then
        Error 68    ' Add your own error handler here
    End If
    hFileOut = ap_OpenFileWrite(sFileOut)
    If hFileOut = -1 Then
        ' Version 4: Close input file before exit
        ' Thanks to Robert Garofalo for this.
        Call ap_CloseFile(hFileIn)
        Error 68    ' Add your own error handler here
    End If
    
    ' Convert IV hex string into bytes
    ' Version 5: use new conversion function
    aReg() = cv_BytesFromHex(strIV)
    ' Version 6: make sure it is exactly 8 bytes
    ReDim Preserve aReg(7)
    
    ' Calculate padding required & no of 8-byte blocks
    nLen = FileLen(sFileIn)
    nBlocks = nLen \ 8
    nPad = (nBlocks + 1) * 8 - nLen
    
    ' Read in file in 8-byte blocks
    For i = 1 To nBlocks
        ' Fetch next block
        Call ap_GetBytes(hFileIn, aBytes, 8)
        ' XOR with feedback register
        Call bXorBytes(aBytes, aReg, 8)
        ' Encrypt and write to output file
        Call blf_EncryptBytes(aBytes)
        ' Store in feedback register
        CopyMemory VarPtr(aReg(0)), VarPtr(aBytes(0)), 8&
        ' Write to file
        Call ap_PutBytes(hFileOut, aBytes, 8)
    Next
    
    ' Read in and pad the last block
    Call ap_GetBytes(hFileIn, aBytes, 8)
    For i = 1 To nPad
        aBytes(8 - i) = nPad
    Next
    ' XOR with feedback register
    Call bXorBytes(aBytes, aReg, 8)
    ' Encrypt it
    Call blf_EncryptBytes(aBytes)
    ' Write to output file
    Call ap_PutBytes(hFileOut, aBytes, 8)
    
    Call ap_CloseFile(hFileIn)
    Call ap_CloseFile(hFileOut)
    
End Function

Public Function blf_FileDecCBC(sFileIn As String, sFileOut As String, _
    strIV As String) As Boolean
' Decrypts file with name <sFileIn> and writes plaintext to new file <sFileOut>
' in CBC mode using hex string <strIV> as initialisation vector
' Requires key and boxes to be initialised
' Any existing file <sFileOut> will be overwritten.
    Dim hFileIn As Long
    Dim hFileOut As Long
    Dim i As Long
    Dim nLen As Long, nPad As Long, nBlocks As Long
    Dim aBytes(7) As Byte   ' Block of 8 bytes
    Dim aReg() As Byte     ' Feedback register
    Dim aStore(7) As Byte   ' Temp store for cipher block
    
    ' Open files for input and output
    ' Use Windows API functions to avoid unwanted padding
    ' Version 4: Changes to error handling as noted.
    hFileIn = ap_OpenFileReadOnly(sFileIn)
    If hFileIn = -1 Then
        Error 68    ' Add your own error handler here
    End If
    hFileOut = ap_OpenFileWrite(sFileOut)
    If hFileOut = -1 Then
        ' Version 4: Close input file before exit
        ' Thanks to Robert Garofalo for this.
        Call ap_CloseFile(hFileIn)
        Error 68    ' Add your own error handler here
    End If
    
    ' Convert IV hex string into bytes
    ' Version 5: use new conversion function
    aReg() = cv_BytesFromHex(strIV)
    ' Version 6: make sure it is exactly 8 bytes
    ReDim Preserve aReg(7)
    
    ' Calculate no of 8-byte blocks to read (should be a multiple of 8)
    nLen = FileLen(sFileIn)
    nBlocks = nLen \ 8
    
    ' Read in file in 8-byte blocks except last block
    For i = 1 To nBlocks - 1
        ' Fetch next cipher block
        Call ap_GetBytes(hFileIn, aBytes, 8)
        ' Store it
        CopyMemory VarPtr(aStore(0)), VarPtr(aBytes(0)), 8&
        ' Decrypt it
        Call blf_DecryptBytes(aBytes)
        ' XOR with feedback register
        Call bXorBytes(aBytes, aReg, 8)
        ' Save cipher block in register
        CopyMemory VarPtr(aReg(0)), VarPtr(aStore(0)), 8&
        ' Write to output file
        Call ap_PutBytes(hFileOut, aBytes, 8)
    Next
    
    ' Read in the last block
    Call ap_GetBytes(hFileIn, aBytes, 8)
    ' Decrypt
    Call blf_DecryptBytes(aBytes)
    ' XOR with feedback register
    Call bXorBytes(aBytes, aReg, 8)
    ' Write excluding padding
    nPad = aBytes(7)
    ' Version 4: Improved check, just in case!
    If nPad > 8 Or nPad < 0 Then
        nPad = 0
    End If
    Call ap_PutBytes(hFileOut, aBytes, 8 - nPad)
    
    Call ap_CloseFile(hFileIn)
    Call ap_CloseFile(hFileOut)
    
End Function

Private Sub bXorBytes(aByt1() As Byte, aByt2() As Byte, nBytes As Long)
' XOR's bytes in array aByt1 with array aByt2
' Returns results in aByt1
' i.e. aByt1() = aByt1() XOR aByt2()
    Dim i As Long
    For i = 0 To nBytes - 1
        aByt1(i) = aByt1(i) Xor aByt2(i)
    Next
End Sub


