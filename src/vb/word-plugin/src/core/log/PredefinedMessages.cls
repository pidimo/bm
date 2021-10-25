VERSION 1.0 CLASS
BEGIN
  MultiUse = -1  'True
  Persistable = 0  'NotPersistable
  DataBindingBehavior = 0  'vbNone
  DataSourceBehavior  = 0  'vbNone
  MTSTransactionMode  = 0  'NotAnMTSObject
END
Attribute VB_Name = "PredefinedMessages"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = True
Attribute VB_PredeclaredId = False
Attribute VB_Exposed = True
'=========================================================================================
'  System Error Constants
'  Usage of system predefined error messages
'=========================================================================================
'  Created By: Behrooz Sangani <bs20014@yahoo.com>
'  Published Date: 12/06/2002
'  WebSite: http://www.geocities.com/bs20014/
'  Legal Copyright: Behrooz Sangani © 12/06/2002
'=========================================================================================

'Following enums contain system error messages

' -----------------------------------------
' Win32 API error code definitions
' -----------------------------------------
Public Enum WIN32API_ERRORS

' NO_ERROR
     NO_ERROR = 0 '  dderror

' The configuration registry database operation completed successfully.
     ERROR_SUCCESS = 0&

'   Incorrect function.
     ERROR_INVALID_FUNCTION = 1 '  dderror

'   The system cannot find the file specified.
     ERROR_FILE_NOT_FOUND = 2&

'   The system cannot find the path specified.
     ERROR_PATH_NOT_FOUND = 3&

'   The system cannot open the file.
     ERROR_TOO_MANY_OPEN_FILES = 4&

'   Access is denied.
     ERROR_ACCESS_DENIED = 5&

'   The handle is invalid.
     ERROR_INVALID_HANDLE = 6&

'   The storage control blocks were destroyed.
     ERROR_ARENA_TRASHED = 7&

'   Not enough storage is available to process this command.
     ERROR_NOT_ENOUGH_MEMORY = 8 '  dderror

'   The storage control block address is invalid.
     ERROR_INVALID_BLOCK = 9&

'   The environment is incorrect.
     ERROR_BAD_ENVIRONMENT = 10&

'   An attempt was made to load a program with an
'   incorrect format.
     ERROR_BAD_FORMAT = 11&

'   The access code is invalid.
     ERROR_INVALID_ACCESS = 12&

'   The data is invalid.
     ERROR_INVALID_DATA = 13&

'   Not enough storage is available to complete this operation.
     ERROR_OUTOFMEMORY = 14&

'   The system cannot find the drive specified.
     ERROR_INVALID_DRIVE = 15&

'   The directory cannot be removed.
     ERROR_CURRENT_DIRECTORY = 16&

'   The system cannot move the file
'   to a different disk drive.
     ERROR_NOT_SAME_DEVICE = 17&

'   There are no more files.
     ERROR_NO_MORE_FILES = 18&

'   The media is write protected.
     ERROR_WRITE_PROTECT = 19&

'   The system cannot find the device specified.
     ERROR_BAD_UNIT = 20&

'   The device is not ready.
     ERROR_NOT_READY = 21&

'   The device does not recognize the command.
     ERROR_BAD_COMMAND = 22&

'   Data error (cyclic redundancy check)
     ERROR_CRC = 23&

'   The program issued a command but the
'   command length is incorrect.
     ERROR_BAD_LENGTH = 24&

'   The drive cannot locate a specific
'   area or track on the disk.
     ERROR_SEEK = 25&

'   The specified disk or diskette cannot be accessed.
     ERROR_NOT_DOS_DISK = 26&

'   The drive cannot find the sector requested.
     ERROR_SECTOR_NOT_FOUND = 27&

'   The printer is out of paper.
     ERROR_OUT_OF_PAPER = 28&

'   The system cannot write to the specified device.
     ERROR_WRITE_FAULT = 29&

'   The system cannot read from the specified device.
     ERROR_READ_FAULT = 30&

'   A device attached to the system is not functioning.
     ERROR_GEN_FAILURE = 31&

'   The process cannot access the file because
'   it is being used by another process.
     ERROR_SHARING_VIOLATION = 32&

'   The process cannot access the file because
'   another process has locked a portion of the file.
     ERROR_LOCK_VIOLATION = 33&

'   The wrong diskette is in the drive.
'   Insert %2 (Volume Serial Number: %3)
'   into drive %1.
     ERROR_WRONG_DISK = 34&

'   Too many files opened for sharing.
     ERROR_SHARING_BUFFER_EXCEEDED = 36&

'   Reached end of file.
     ERROR_HANDLE_EOF = 38&

'   The disk is full.
     ERROR_HANDLE_DISK_FULL = 39&

'   The network request is not supported.
     ERROR_NOT_SUPPORTED = 50&

'   The remote computer is not available.
     ERROR_REM_NOT_LIST = 51&

'   A duplicate name exists on the network.
     ERROR_DUP_NAME = 52&

'   The network path was not found.
     ERROR_BAD_NETPATH = 53&

'   The network is busy.
     ERROR_NETWORK_BUSY = 54&

'   The specified network resource or device is no longer
'   available.
     ERROR_DEV_NOT_EXIST = 55 '  dderror

'   The network BIOS command limit has been reached.
     ERROR_TOO_MANY_CMDS = 56&

'   A network adapter hardware error occurred.
     ERROR_ADAP_HDW_ERR = 57&

'   The specified server cannot perform the requested
'   operation.
     ERROR_BAD_NET_RESP = 58&

'   An unexpected network error occurred.
     ERROR_UNEXP_NET_ERR = 59&

'   The remote adapter is not compatible.
     ERROR_BAD_REM_ADAP = 60&

'   The printer queue is full.
     ERROR_PRINTQ_FULL = 61&

'   Space to store the file waiting to be printed is
'   not available on the server.
     ERROR_NO_SPOOL_SPACE = 62&

'   Your file waiting to be printed was deleted.
     ERROR_PRINT_CANCELLED = 63&

'   The specified network name is no longer available.
     ERROR_NETNAME_DELETED = 64&

'   Network access is denied.
     ERROR_NETWORK_ACCESS_DENIED = 65&

'   The network resource type is not correct.
     ERROR_BAD_DEV_TYPE = 66&

'   The network name cannot be found.
     ERROR_BAD_NET_NAME = 67&

'   The name limit for the local computer network
'   adapter card was exceeded.
     ERROR_TOO_MANY_NAMES = 68&

'   The network BIOS session limit was exceeded.
     ERROR_TOO_MANY_SESS = 69&

'   The remote server has been paused or is in the
'   process of being started.
     ERROR_SHARING_PAUSED = 70&

'   The network request was not accepted.
     ERROR_REQ_NOT_ACCEP = 71&

'   The specified printer or disk device has been paused.
     ERROR_REDIR_PAUSED = 72&

'   The file exists.
     ERROR_FILE_EXISTS = 80&

'   The directory or file cannot be created.
     ERROR_CANNOT_MAKE = 82&

'   Fail on INT 24
     ERROR_FAIL_I24 = 83&

'   Storage to process this request is not available.
     ERROR_OUT_OF_STRUCTURES = 84&

'   The local device name is already in use.
     ERROR_ALREADY_ASSIGNED = 85&

'   The specified network password is not correct.
     ERROR_INVALID_PASSWORD = 86&

'   The parameter is incorrect.
     ERROR_INVALID_PARAMETER = 87 '  dderror

'   A write fault occurred on the network.
     ERROR_NET_WRITE_FAULT = 88&

'   The system cannot start another process at
'   this time.
     ERROR_NO_PROC_SLOTS = 89&

'   Cannot create another system semaphore.
     ERROR_TOO_MANY_SEMAPHORES = 100&

'   The exclusive semaphore is owned by another process.
     ERROR_EXCL_SEM_ALREADY_OWNED = 101&

'   The semaphore is set and cannot be closed.
     ERROR_SEM_IS_SET = 102&

'   The semaphore cannot be set again.
     ERROR_TOO_MANY_SEM_REQUESTS = 103&

'   Cannot request exclusive semaphores at interrupt time.
     ERROR_INVALID_AT_INTERRUPT_TIME = 104&

'   The previous ownership of this semaphore has ended.
     ERROR_SEM_OWNER_DIED = 105&

'   Insert the diskette for drive %1.
     ERROR_SEM_USER_LIMIT = 106&

'   Program stopped because alternate diskette was not inserted.
     ERROR_DISK_CHANGE = 107&

'   The disk is in use or locked by
'   another process.
     ERROR_DRIVE_LOCKED = 108&

'   The pipe has been ended.
     ERROR_BROKEN_PIPE = 109&

'   The system cannot open the
'   device or file specified.
     ERROR_OPEN_FAILED = 110&

'   The file name is too long.
     ERROR_BUFFER_OVERFLOW = 111&

'   There is not enough space on the disk.
     ERROR_DISK_FULL = 112&

'   No more internal file identifiers available.
     ERROR_NO_MORE_SEARCH_HANDLES = 113&

'   The target internal file identifier is incorrect.
     ERROR_INVALID_TARGET_HANDLE = 114&

'   The IOCTL call made by the application program is
'   not correct.
     ERROR_INVALID_CATEGORY = 117&

'   The verify-on-write switch parameter value is not
'   correct.
     ERROR_INVALID_VERIFY_SWITCH = 118&

'   The system does not support the command requested.
     ERROR_BAD_DRIVER_LEVEL = 119&

'   This function is only valid in Windows NT mode.
     ERROR_CALL_NOT_IMPLEMENTED = 120&

'   The semaphore timeout period has expired.
     ERROR_SEM_TIMEOUT = 121&

'   The data area passed to a system call is too
'   small.
     ERROR_INSUFFICIENT_BUFFER = 122 '  dderror

'   The filename, directory name, or volume label syntax is incorrect.
     ERROR_INVALID_NAME = 123&

'   The system call level is not correct.
     ERROR_INVALID_LEVEL = 124&

'   The disk has no volume label.
     ERROR_NO_VOLUME_LABEL = 125&

'   The specified module could not be found.
     ERROR_MOD_NOT_FOUND = 126&

'   The specified procedure could not be found.
     ERROR_PROC_NOT_FOUND = 127&

'   There are no child processes to wait for.
     ERROR_WAIT_NO_CHILDREN = 128&

'   The %1 application cannot be run in Windows NT mode.
     ERROR_CHILD_NOT_COMPLETE = 129&

'   Attempt to use a file handle to an open disk partition for an
'   operation other than raw disk I/O.
     ERROR_DIRECT_ACCESS_HANDLE = 130&

'   An attempt was made to move the file pointer before the beginning of the file.
     ERROR_NEGATIVE_SEEK = 131&

'   The file pointer cannot be set on the specified device or file.
     ERROR_SEEK_ON_DEVICE = 132&

'   A JOIN or SUBST command
'   cannot be used for a drive that
'   contains previously joined drives.
     ERROR_IS_JOIN_TARGET = 133&

'   An attempt was made to use a
'   JOIN or SUBST command on a drive that has
'   already been joined.
     ERROR_IS_JOINED = 134&

'   An attempt was made to use a
'   JOIN or SUBST command on a drive that has
'   already been substituted.
     ERROR_IS_SUBSTED = 135&

'   The system tried to delete
'   the JOIN of a drive that is not joined.
     ERROR_NOT_JOINED = 136&

'   The system tried to delete the
'   substitution of a drive that is not substituted.
     ERROR_NOT_SUBSTED = 137&

'   The system tried to join a drive
'   to a directory on a joined drive.
     ERROR_JOIN_TO_JOIN = 138&

'   The system tried to substitute a
'   drive to a directory on a substituted drive.
     ERROR_SUBST_TO_SUBST = 139&

'   The system tried to join a drive to
'   a directory on a substituted drive.
     ERROR_JOIN_TO_SUBST = 140&

'   The system tried to SUBST a drive
'   to a directory on a joined drive.
     ERROR_SUBST_TO_JOIN = 141&

'   The system cannot perform a JOIN or SUBST at this time.
     ERROR_BUSY_DRIVE = 142&

'   The system cannot join or substitute a
'   drive to or for a directory on the same drive.
     ERROR_SAME_DRIVE = 143&

'   The directory is not a subdirectory of the root directory.
     ERROR_DIR_NOT_ROOT = 144&

'   The directory is not empty.
     ERROR_DIR_NOT_EMPTY = 145&

'   The path specified is being used in
'   a substitute.
     ERROR_IS_SUBST_PATH = 146&

'   Not enough resources are available to
'   process this command.
     ERROR_IS_JOIN_PATH = 147&

'   The path specified cannot be used at this time.
     ERROR_PATH_BUSY = 148&

'   An attempt was made to join
'   or substitute a drive for which a directory
'   on the drive is the target of a previous
'   substitute.
     ERROR_IS_SUBST_TARGET = 149&

'   System trace information was not specified in your
'   CONFIG.SYS file, or tracing is disallowed.
     ERROR_SYSTEM_TRACE = 150&

'   The number of specified semaphore events for
'   DosMuxSemWait is not correct.
     ERROR_INVALID_EVENT_COUNT = 151&

'   DosMuxSemWait did not execute; too many semaphores
'   are already set.
     ERROR_TOO_MANY_MUXWAITERS = 152&

'   The DosMuxSemWait list is not correct.
     ERROR_INVALID_LIST_FORMAT = 153&

'   The volume label you entered exceeds the
'   11 character limit.  The first 11 characters were written
'   to disk.  Any characters that exceeded the 11 character limit
'   were automatically deleted.
     ERROR_LABEL_TOO_LONG = 154&

'   Cannot create another thread.
     ERROR_TOO_MANY_TCBS = 155&

'   The recipient process has refused the signal.
     ERROR_SIGNAL_REFUSED = 156&

'   The segment is already discarded and cannot be locked.
     ERROR_DISCARDED = 157&

'   The segment is already unlocked.
     ERROR_NOT_LOCKED = 158&

'   The address for the thread ID is not correct.
     ERROR_BAD_THREADID_ADDR = 159&

'   The argument string passed to DosExecPgm is not correct.
     ERROR_BAD_ARGUMENTS = 160&

'   The specified path is invalid.
     ERROR_BAD_PATHNAME = 161&

'   A signal is already pending.
     ERROR_SIGNAL_PENDING = 162&

'   No more threads can be created in the system.
     ERROR_MAX_THRDS_REACHED = 164&

'   Unable to lock a region of a file.
     ERROR_LOCK_FAILED = 167&

'   The requested resource is in use.
     ERROR_BUSY = 170&

'   A lock request was not outstanding for the supplied cancel region.
     ERROR_CANCEL_VIOLATION = 173&

'   The file system does not support atomic changes to the lock type.
     ERROR_ATOMIC_LOCKS_NOT_SUPPORTED = 174&

'   The system detected a segment number that was not correct.
     ERROR_INVALID_SEGMENT_NUMBER = 180&

'   The operating system cannot run %1.
     ERROR_INVALID_ORDINAL = 182&

'   Cannot create a file when that file already exists.
     ERROR_ALREADY_EXISTS = 183&

'   The flag passed is not correct.
     ERROR_INVALID_FLAG_NUMBER = 186&

'   The specified system semaphore name was not found.
     ERROR_SEM_NOT_FOUND = 187&

'   The operating system cannot run %1.
     ERROR_INVALID_STARTING_CODESEG = 188&

'   The operating system cannot run %1.
     ERROR_INVALID_STACKSEG = 189&

'   The operating system cannot run %1.
     ERROR_INVALID_MODULETYPE = 190&

'   Cannot run %1 in Windows NT mode.
     ERROR_INVALID_EXE_SIGNATURE = 191&

'   The operating system cannot run %1.
     ERROR_EXE_MARKED_INVALID = 192&

'   %1 is not a valid Windows NT application.
     ERROR_BAD_EXE_FORMAT = 193&

'   The operating system cannot run %1.
     ERROR_ITERATED_DATA_EXCEEDS_64k = 194&

'   The operating system cannot run %1.
     ERROR_INVALID_MINALLOCSIZE = 195&

'   The operating system cannot run this
'   application program.
     ERROR_DYNLINK_FROM_INVALID_RING = 196&

'   The operating system is not presently
'   configured to run this application.
     ERROR_IOPL_NOT_ENABLED = 197&

'   The operating system cannot run %1.
     ERROR_INVALID_SEGDPL = 198&

'   The operating system cannot run this
'   application program.
     ERROR_AUTODATASEG_EXCEEDS_64k = 199&

'   The code segment cannot be greater than or equal to 64KB.
     ERROR_RING2SEG_MUST_BE_MOVABLE = 200&

'   The operating system cannot run %1.
     ERROR_RELOC_CHAIN_XEEDS_SEGLIM = 201&

'   The operating system cannot run %1.
     ERROR_INFLOOP_IN_RELOC_CHAIN = 202&

'   The system could not find the environment
'   option that was entered.
     ERROR_ENVVAR_NOT_FOUND = 203&

'   No process in the command subtree has a
'   signal handler.
     ERROR_NO_SIGNAL_SENT = 205&

'   The filename or extension is too long.
     ERROR_FILENAME_EXCED_RANGE = 206&

'   The ring 2 stack is in use.
     ERROR_RING2_STACK_IN_USE = 207&

'   The Global filename characters,  or ?, are entered
'   incorrectly or too many Global filename characters are specified.
     ERROR_META_EXPANSION_TOO_LONG = 208&

'   The signal being posted is not correct.
     ERROR_INVALID_SIGNAL_NUMBER = 209&

'   The signal handler cannot be set.
     ERROR_THREAD_1_INACTIVE = 210&

'   The segment is locked and cannot be reallocated.
     ERROR_LOCKED = 212&

'   Too many dynamic link modules are attached to this
'   program or dynamic link module.
     ERROR_TOO_MANY_MODULES = 214&

'   Can't nest calls to LoadModule.
     ERROR_NESTING_NOT_ALLOWED = 215&

'   The pipe state is invalid.
     ERROR_BAD_PIPE = 230&

'   All pipe instances are busy.
     ERROR_PIPE_BUSY = 231&

'   The pipe is being closed.
     ERROR_NO_DATA = 232&

'   No process is on the other end of the pipe.
     ERROR_PIPE_NOT_CONNECTED = 233&

'   More data is available.
     ERROR_MORE_DATA = 234 '  dderror

'   The session was cancelled.
     ERROR_VC_DISCONNECTED = 240&

'   The specified extended attribute name was invalid.
     ERROR_INVALID_EA_NAME = 254&

'   The extended attributes are inconsistent.
     ERROR_EA_LIST_INCONSISTENT = 255&

'   No more data is available.
     ERROR_NO_MORE_ITEMS = 259&

'   The Copy API cannot be used.
     ERROR_CANNOT_COPY = 266&

'   The directory name is invalid.
     ERROR_DIRECTORY = 267&

'   The extended attributes did not fit in the buffer.
     ERROR_EAS_DIDNT_FIT = 275&

'   The extended attribute file on the mounted file system is corrupt.
     ERROR_EA_FILE_CORRUPT = 276&

'   The extended attribute table file is full.
     ERROR_EA_TABLE_FULL = 277&

'   The specified extended attribute handle is invalid.
     ERROR_INVALID_EA_HANDLE = 278&

'   The mounted file system does not support extended attributes.
     ERROR_EAS_NOT_SUPPORTED = 282&

'   Attempt to release mutex not owned by caller.
     ERROR_NOT_OWNER = 288&

'   Too many posts were made to a semaphore.
     ERROR_TOO_MANY_POSTS = 298&

'   The system cannot find message for message number 0x%1
'   in message file for %2.
     ERROR_MR_MID_NOT_FOUND = 317&

'   Attempt to access invalid address.
     ERROR_INVALID_ADDRESS = 487&

'   Arithmetic result exceeded 32 bits.
     ERROR_ARITHMETIC_OVERFLOW = 534&

'   There is a process on other end of the pipe.
     ERROR_PIPE_CONNECTED = 535&

'   Waiting for a process to open the other end of the pipe.
     ERROR_PIPE_LISTENING = 536&

'   Access to the extended attribute was denied.
     ERROR_EA_ACCESS_DENIED = 994&

'   The I/O operation has been aborted because of either a thread exit
'   or an application request.
     ERROR_OPERATION_ABORTED = 995&

'   Overlapped I/O event is not in a signalled state.
     ERROR_IO_INCOMPLETE = 996&

'   Overlapped I/O operation is in progress.
     ERROR_IO_PENDING = 997 '  dderror

'   Invalid access to memory location.
     ERROR_NOACCESS = 998&

'   Error performing inpage operation.
     ERROR_SWAPERROR = 999&

'   Recursion too deep, stack overflowed.
     ERROR_STACK_OVERFLOW = 1001&

'   The window cannot act on the sent message.
     ERROR_INVALID_MESSAGE = 1002&

'   Cannot complete this function.
     ERROR_CAN_NOT_COMPLETE = 1003&

'   Invalid flags.
     ERROR_INVALID_FLAGS = 1004&

'   The volume does not contain a recognized file system.
'   Please make sure that all required file system drivers are loaded and that the
'   volume is not corrupt.
     ERROR_UNRECOGNIZED_VOLUME = 1005&

'   The volume for a file has been externally altered such that the
'   opened file is no longer valid.
     ERROR_FILE_INVALID = 1006&

'   The requested operation cannot be performed in full-screen mode.
     ERROR_FULLSCREEN_MODE = 1007&

'   An attempt was made to reference a token that does not exist.
     ERROR_NO_TOKEN = 1008&

'   The configuration registry database is corrupt.
     ERROR_BADDB = 1009&

'   The configuration registry key is invalid.
     ERROR_BADKEY = 1010&

'   The configuration registry key could not be opened.
     ERROR_CANTOPEN = 1011&

'   The configuration registry key could not be read.
     ERROR_CANTREAD = 1012&

'   The configuration registry key could not be written.
     ERROR_CANTWRITE = 1013&

'   One of the files in the Registry database had to be recovered
'   by use of a log or alternate copy.  The recovery was successful.
     ERROR_REGISTRY_RECOVERED = 1014&

'   The Registry is corrupt. The structure of one of the files that contains
'   Registry data is corrupt, or the system's image of the file in memory
'   is corrupt, or the file could not be recovered because the alternate
'   copy or log was absent or corrupt.
     ERROR_REGISTRY_CORRUPT = 1015&

'   An I/O operation initiated by the Registry failed unrecoverably.
'   The Registry could not read in, or write out, or flush, one of the files
'   that contain the system's image of the Registry.
     ERROR_REGISTRY_IO_FAILED = 1016&

'   The system has attempted to load or restore a file into the Registry, but the
'   specified file is not in a Registry file format.
     ERROR_NOT_REGISTRY_FILE = 1017&

'   Illegal operation attempted on a Registry key which has been marked for deletion.
     ERROR_KEY_DELETED = 1018&

'   System could not allocate the required space in a Registry log.
     ERROR_NO_LOG_SPACE = 1019&

'   Cannot create a symbolic link in a Registry key that already
'   has subkeys or values.
     ERROR_KEY_HAS_CHILDREN = 1020&

'   Cannot create a stable subkey under a volatile parent key.
     ERROR_CHILD_MUST_BE_VOLATILE = 1021&

'   A notify change request is being completed and the information
'   is not being returned in the caller's buffer. The caller now
'   needs to enumerate the files to find the changes.
     ERROR_NOTIFY_ENUM_DIR = 1022&

'   A stop control has been sent to a service which other running services
'   are dependent on.
     ERROR_DEPENDENT_SERVICES_RUNNING = 1051&

'   The requested control is not valid for this service
     ERROR_INVALID_SERVICE_CONTROL = 1052&

'   The service did not respond to the start or control request in a timely
'   fashion.
     ERROR_SERVICE_REQUEST_TIMEOUT = 1053&

'   A thread could not be created for the service.
     ERROR_SERVICE_NO_THREAD = 1054&

'   The service database is locked.
     ERROR_SERVICE_DATABASE_LOCKED = 1055&

'   An instance of the service is already running.
     ERROR_SERVICE_ALREADY_RUNNING = 1056&

'   The account name is invalid or does not exist.
     ERROR_INVALID_SERVICE_ACCOUNT = 1057&

'   The specified service is disabled and cannot be started.
     ERROR_SERVICE_DISABLED = 1058&

'   Circular service dependency was specified.
     ERROR_CIRCULAR_DEPENDENCY = 1059&

'   The specified service does not exist as an installed service.
     ERROR_SERVICE_DOES_NOT_EXIST = 1060&

'   The service cannot accept control messages at this time.
     ERROR_SERVICE_CANNOT_ACCEPT_CTRL = 1061&

'   The service has not been started.
     ERROR_SERVICE_NOT_ACTIVE = 1062&

'   The service process could not connect to the service controller.
     ERROR_FAILED_SERVICE_CONTROLLER_CONNECT = 1063&

'   An exception occurred in the service when handling the control request.
     ERROR_EXCEPTION_IN_SERVICE = 1064&

'   The database specified does not exist.
     ERROR_DATABASE_DOES_NOT_EXIST = 1065&

'   The service has returned a service-specific error code.
     ERROR_SERVICE_SPECIFIC_ERROR = 1066&

'   The process terminated unexpectedly.
     ERROR_PROCESS_ABORTED = 1067&

'   The dependency service or group failed to start.
     ERROR_SERVICE_DEPENDENCY_FAIL = 1068&

'   The service did not start due to a logon failure.
     ERROR_SERVICE_LOGON_FAILED = 1069&

'   After starting, the service hung in a start-pending state.
     ERROR_SERVICE_START_HANG = 1070&

'   The specified service database lock is invalid.
     ERROR_INVALID_SERVICE_LOCK = 1071&

'   The specified service has been marked for deletion.
     ERROR_SERVICE_MARKED_FOR_DELETE = 1072&

'   The specified service already exists.
     ERROR_SERVICE_EXISTS = 1073&

'   The system is currently running with the last-known-good configuration.
     ERROR_ALREADY_RUNNING_LKG = 1074&

'   The dependency service does not exist or has been marked for
'   deletion.
     ERROR_SERVICE_DEPENDENCY_DELETED = 1075&

'   The current boot has already been accepted for use as the
'   last-known-good control set.
     ERROR_BOOT_ALREADY_ACCEPTED = 1076&

'   No attempts to start the service have been made since the last boot.
     ERROR_SERVICE_NEVER_STARTED = 1077&

'   The name is already in use as either a service name or a service display
'   name.
     ERROR_DUPLICATE_SERVICE_NAME = 1078&

'   The physical end of the tape has been reached.
     ERROR_END_OF_MEDIA = 1100&

'   A tape access reached a filemark.
     ERROR_FILEMARK_DETECTED = 1101&

'   Beginning of tape or partition was encountered.
     ERROR_BEGINNING_OF_MEDIA = 1102&

'   A tape access reached the end of a set of files.
     ERROR_SETMARK_DETECTED = 1103&

'   No more data is on the tape.
     ERROR_NO_DATA_DETECTED = 1104&

'   Tape could not be partitioned.
     ERROR_PARTITION_FAILURE = 1105&

'   When accessing a new tape of a multivolume partition, the current
'   blocksize is incorrect.
     ERROR_INVALID_BLOCK_LENGTH = 1106&

'   Tape partition information could not be found when loading a tape.
     ERROR_DEVICE_NOT_PARTITIONED = 1107&

'   Unable to lock the media eject mechanism.
     ERROR_UNABLE_TO_LOCK_MEDIA = 1108&

'   Unable to unload the media.
     ERROR_UNABLE_TO_UNLOAD_MEDIA = 1109&

'   Media in drive may have changed.
     ERROR_MEDIA_CHANGED = 1110&

'   The I/O bus was reset.
     ERROR_BUS_RESET = 1111&

'   No media in drive.
     ERROR_NO_MEDIA_IN_DRIVE = 1112&

'   No mapping for the Unicode character exists in the target multi-byte code page.
     ERROR_NO_UNICODE_TRANSLATION = 1113&

'   A dynamic link library (DLL) initialization routine failed.
     ERROR_DLL_INIT_FAILED = 1114&

'   A system shutdown is in progress.
     ERROR_SHUTDOWN_IN_PROGRESS = 1115&

'   Unable to abort the system shutdown because no shutdown was in progress.
     ERROR_NO_SHUTDOWN_IN_PROGRESS = 1116&

'   The request could not be performed because of an I/O device error.
     ERROR_IO_DEVICE = 1117&

'   No serial device was successfully initialized.  The serial driver will unload.
     ERROR_SERIAL_NO_DEVICE = 1118&

'   Unable to open a device that was sharing an interrupt request (IRQ)
'   with other devices. At least one other device that uses that IRQ
'   was already opened.
     ERROR_IRQ_BUSY = 1119&

'   A serial I/O operation was completed by another write to the serial port.
'   (The IOCTL_SERIAL_XOFF_COUNTER reached zero.)
     ERROR_MORE_WRITES = 1120&

'   A serial I/O operation completed because the time-out period expired.
'   (The IOCTL_SERIAL_XOFF_COUNTER did not reach zero.)
     ERROR_COUNTER_TIMEOUT = 1121&

'   No ID address mark was found on the floppy disk.
     ERROR_FLOPPY_ID_MARK_NOT_FOUND = 1122&

'   Mismatch between the floppy disk sector ID field and the floppy disk
'   controller track address.
     ERROR_FLOPPY_WRONG_CYLINDER = 1123&

'   The floppy disk controller reported an error that is not recognized
'   by the floppy disk driver.
     ERROR_FLOPPY_UNKNOWN_ERROR = 1124&

'   The floppy disk controller returned inconsistent results in its registers.
     ERROR_FLOPPY_BAD_REGISTERS = 1125&

'   While accessing the hard disk, a recalibrate operation failed, even after retries.
     ERROR_DISK_RECALIBRATE_FAILED = 1126&

'   While accessing the hard disk, a disk operation failed even after retries.
     ERROR_DISK_OPERATION_FAILED = 1127&

'   While accessing the hard disk, a disk controller reset was needed, but
'   even that failed.
     ERROR_DISK_RESET_FAILED = 1128&

'   Physical end of tape encountered.
     ERROR_EOM_OVERFLOW = 1129&

'   Not enough server storage is available to process this command.
     ERROR_NOT_ENOUGH_SERVER_MEMORY = 1130&

'   A potential deadlock condition has been detected.
     ERROR_POSSIBLE_DEADLOCK = 1131&

'   The base address or the file offset specified does not have the proper
'   alignment.
     ERROR_MAPPED_ALIGNMENT = 1132&

' NEW for Win32
     ERROR_INVALID_PIXEL_FORMAT = 2000
     ERROR_BAD_DRIVER = 2001
     ERROR_INVALID_WINDOW_STYLE = 2002
     ERROR_METAFILE_NOT_SUPPORTED = 2003
     ERROR_TRANSFORM_NOT_SUPPORTED = 2004
     ERROR_CLIPPING_NOT_SUPPORTED = 2005
     ERROR_UNKNOWN_PRINT_MONITOR = 3000
     ERROR_PRINTER_DRIVER_IN_USE = 3001
     ERROR_SPOOL_FILE_NOT_FOUND = 3002
     ERROR_SPL_NO_STARTDOC = 3003
     ERROR_SPL_NO_ADDJOB = 3004
     ERROR_PRINT_PROCESSOR_ALREADY_INSTALLED = 3005
     ERROR_PRINT_MONITOR_ALREADY_INSTALLED = 3006
     ERROR_WINS_INTERNAL = 4000
     ERROR_CAN_NOT_DEL_LOCAL_WINS = 4001
     ERROR_STATIC_INIT = 4002
     ERROR_INC_BACKUP = 4003
     ERROR_FULL_BACKUP = 4004
     ERROR_REC_NON_EXISTENT = 4005
     ERROR_RPL_NOT_ALLOWED = 4006
     SEVERITY_SUCCESS = 0
     SEVERITY_ERROR = 1
     FACILITY_NT_BIT = &H10000000
     E_UNEXPECTED = &H8000FFFF
     E_NOTIMPL = &H80004001
     E_OUTOFMEMORY = &H8007000E
     E_INVALIDARG = &H80070057
     E_NOINTERFACE = &H80004002
     E_POINTER = &H80004003
     E_HANDLE = &H80070006
     E_ABORT = &H80004004
     E_FAIL = &H80004005
     E_ACCESSDENIED = &H80070005
     CO_E_INIT_TLS = &H80004006
     CO_E_INIT_SHARED_ALLOCATOR = &H80004007
     CO_E_INIT_MEMORY_ALLOCATOR = &H80004008
     CO_E_INIT_CLASS_CACHE = &H80004009
     CO_E_INIT_RPC_CHANNEL = &H8000400A
     CO_E_INIT_TLS_SET_CHANNEL_CONTROL = &H8000400B
     CO_E_INIT_TLS_CHANNEL_CONTROL = &H8000400C
     CO_E_INIT_UNACCEPTED_USER_ALLOCATOR = &H8000400D
     CO_E_INIT_SCM_MUTEX_EXISTS = &H8000400E
     CO_E_INIT_SCM_FILE_MAPPING_EXISTS = &H8000400F
     CO_E_INIT_SCM_MAP_VIEW_OF_FILE = &H80004010
     CO_E_INIT_SCM_EXEC_FAILURE = &H80004011
     CO_E_INIT_ONLY_SINGLE_THREADED = &H80004012
     S_OK = &H0
     S_FALSE = &H1
     OLE_E_FIRST = &H80040000
     OLE_E_LAST = &H800400FF
     OLE_S_FIRST = &H40000
     OLE_S_LAST = &H400FF
     OLE_E_OLEVERB = &H80040000
     OLE_E_ADVF = &H80040001
     OLE_E_ENUM_NOMORE = &H80040002
     OLE_E_ADVISENOTSUPPORTED = &H80040003
     OLE_E_NOCONNECTION = &H80040004
     OLE_E_NOTRUNNING = &H80040005
     OLE_E_NOCACHE = &H80040006
     OLE_E_BLANK = &H80040007
     OLE_E_CLASSDIFF = &H80040008
     OLE_E_CANT_GETMONIKER = &H80040009
     OLE_E_CANT_BINDTOSOURCE = &H8004000A
     OLE_E_STATIC = &H8004000B
     OLE_E_PROMPTSAVECANCELLED = &H8004000C
     OLE_E_INVALIDRECT = &H8004000D
     OLE_E_WRONGCOMPOBJ = &H8004000E
     OLE_E_INVALIDHWND = &H8004000F
     OLE_E_NOT_INPLACEACTIVE = &H80040010
     OLE_E_CANTCONVERT = &H80040011
     OLE_E_NOSTORAGE = &H80040012
     DV_E_FORMATETC = &H80040064
     DV_E_DVTARGETDEVICE = &H80040065
     DV_E_STGMEDIUM = &H80040066
     DV_E_STATDATA = &H80040067
     DV_E_LINDEX = &H80040068
     DV_E_TYMED = &H80040069
     DV_E_CLIPFORMAT = &H8004006A
     DV_E_DVASPECT = &H8004006B
     DV_E_DVTARGETDEVICE_SIZE = &H8004006C
     DV_E_NOIVIEWOBJECT = &H8004006D
     DRAGDROP_E_FIRST = &H80040100
     DRAGDROP_E_LAST = &H8004010F
     DRAGDROP_S_FIRST = &H40100
     DRAGDROP_S_LAST = &H4010F
     DRAGDROP_E_NOTREGISTERED = &H80040100
     DRAGDROP_E_ALREADYREGISTERED = &H80040101
     DRAGDROP_E_INVALIDHWND = &H80040102
     CLASSFACTORY_E_FIRST = &H80040110
     CLASSFACTORY_E_LAST = &H8004011F
     CLASSFACTORY_S_FIRST = &H40110
     CLASSFACTORY_S_LAST = &H4011F
     CLASS_E_NOAGGREGATION = &H80040110
     CLASS_E_CLASSNOTAVAILABLE = &H80040111
     MARSHAL_E_FIRST = &H80040120
     MARSHAL_E_LAST = &H8004012F
     MARSHAL_S_FIRST = &H40120
     MARSHAL_S_LAST = &H4012F
     DATA_E_FIRST = &H80040130
     DATA_E_LAST = &H8004013F
     DATA_S_FIRST = &H40130
     DATA_S_LAST = &H4013F
     VIEW_E_FIRST = &H80040140
     VIEW_E_LAST = &H8004014F
     VIEW_S_FIRST = &H40140
     VIEW_S_LAST = &H4014F
     VIEW_E_DRAW = &H80040140
     REGDB_E_FIRST = &H80040150
     REGDB_E_LAST = &H8004015F
     REGDB_S_FIRST = &H40150
     REGDB_S_LAST = &H4015F
     REGDB_E_READREGDB = &H80040150
     REGDB_E_WRITEREGDB = &H80040151
     REGDB_E_KEYMISSING = &H80040152
     REGDB_E_INVALIDVALUE = &H80040153
     REGDB_E_CLASSNOTREG = &H80040154
     REGDB_E_IIDNOTREG = &H80040155
     CACHE_E_FIRST = &H80040170
     CACHE_E_LAST = &H8004017F
     CACHE_S_FIRST = &H40170
     CACHE_S_LAST = &H4017F
     CACHE_E_NOCACHE_UPDATED = &H80040170
     OLEOBJ_E_FIRST = &H80040180
     OLEOBJ_E_LAST = &H8004018F
     OLEOBJ_S_FIRST = &H40180
     OLEOBJ_S_LAST = &H4018F
     OLEOBJ_E_NOVERBS = &H80040180
     OLEOBJ_E_INVALIDVERB = &H80040181
     CLIENTSITE_E_FIRST = &H80040190
     CLIENTSITE_E_LAST = &H8004019F
     CLIENTSITE_S_FIRST = &H40190
     CLIENTSITE_S_LAST = &H4019F
     INPLACE_E_NOTUNDOABLE = &H800401A0
     INPLACE_E_NOTOOLSPACE = &H800401A1
     INPLACE_E_FIRST = &H800401A0
     INPLACE_E_LAST = &H800401AF
     INPLACE_S_FIRST = &H401A0
     INPLACE_S_LAST = &H401AF
     ENUM_E_FIRST = &H800401B0
     ENUM_E_LAST = &H800401BF
     ENUM_S_FIRST = &H401B0
     ENUM_S_LAST = &H401BF
     CONVERT10_E_FIRST = &H800401C0
     CONVERT10_E_LAST = &H800401CF
     CONVERT10_S_FIRST = &H401C0
     CONVERT10_S_LAST = &H401CF
     CONVERT10_E_OLESTREAM_GET = &H800401C0
     CONVERT10_E_OLESTREAM_PUT = &H800401C1
     CONVERT10_E_OLESTREAM_FMT = &H800401C2
     CONVERT10_E_OLESTREAM_BITMAP_TO_DIB = &H800401C3
     CONVERT10_E_STG_FMT = &H800401C4
     CONVERT10_E_STG_NO_STD_STREAM = &H800401C5
     CONVERT10_E_STG_DIB_TO_BITMAP = &H800401C6
     CLIPBRD_E_FIRST = &H800401D0
     CLIPBRD_E_LAST = &H800401DF
     CLIPBRD_S_FIRST = &H401D0
     CLIPBRD_S_LAST = &H401DF
     CLIPBRD_E_CANT_OPEN = &H800401D0
     CLIPBRD_E_CANT_EMPTY = &H800401D1
     CLIPBRD_E_CANT_SET = &H800401D2
     CLIPBRD_E_BAD_DATA = &H800401D3
     CLIPBRD_E_CANT_CLOSE = &H800401D4
     MK_E_FIRST = &H800401E0
     MK_E_LAST = &H800401EF
     MK_S_FIRST = &H401E0
     MK_S_LAST = &H401EF
     MK_E_CONNECTMANUALLY = &H800401E0
     MK_E_EXCEEDEDDEADLINE = &H800401E1
     MK_E_NEEDGENERIC = &H800401E2
     MK_E_UNAVAILABLE = &H800401E3
     MK_E_SYNTAX = &H800401E4
     MK_E_NOOBJECT = &H800401E5
     MK_E_INVALIDEXTENSION = &H800401E6
     MK_E_INTERMEDIATEINTERFACENOTSUPPORTED = &H800401E7
     MK_E_NOTBINDABLE = &H800401E8
     MK_E_NOTBOUND = &H800401E9
     MK_E_CANTOPENFILE = &H800401EA
     MK_E_MUSTBOTHERUSER = &H800401EB
     MK_E_NOINVERSE = &H800401EC
     MK_E_NOSTORAGE = &H800401ED
     MK_E_NOPREFIX = &H800401EE
     MK_E_ENUMERATION_FAILED = &H800401EF
     CO_E_FIRST = &H800401F0
     CO_E_LAST = &H800401FF
     CO_S_FIRST = &H401F0
     CO_S_LAST = &H401FF
     CO_E_NOTINITIALIZED = &H800401F0
     CO_E_ALREADYINITIALIZED = &H800401F1
     CO_E_CANTDETERMINECLASS = &H800401F2
     CO_E_CLASSSTRING = &H800401F3
     CO_E_IIDSTRING = &H800401F4
     CO_E_APPNOTFOUND = &H800401F5
     CO_E_APPSINGLEUSE = &H800401F6
     CO_E_ERRORINAPP = &H800401F7
     CO_E_DLLNOTFOUND = &H800401F8
     CO_E_ERRORINDLL = &H800401F9
     CO_E_WRONGOSFORAPP = &H800401FA
     CO_E_OBJNOTREG = &H800401FB
     CO_E_OBJISREG = &H800401FC
     CO_E_OBJNOTCONNECTED = &H800401FD
     CO_E_APPDIDNTREG = &H800401FE
     CO_E_RELEASED = &H800401FF
     OLE_S_USEREG = &H40000
     OLE_S_STATIC = &H40001
     OLE_S_MAC_CLIPFORMAT = &H40002
     DRAGDROP_S_DROP = &H40100
     DRAGDROP_S_CANCEL = &H40101
     DRAGDROP_S_USEDEFAULTCURSORS = &H40102
     DATA_S_SAMEFORMATETC = &H40130
     VIEW_S_ALREADY_FROZEN = &H40140
     CACHE_S_FORMATETC_NOTSUPPORTED = &H40170
     CACHE_S_SAMECACHE = &H40171
     CACHE_S_SOMECACHES_NOTUPDATED = &H40172
     OLEOBJ_S_INVALIDVERB = &H40180
     OLEOBJ_S_CANNOT_DOVERB_NOW = &H40181
     OLEOBJ_S_INVALIDHWND = &H40182
     INPLACE_S_TRUNCATED = &H401A0
     CONVERT10_S_NO_PRESENTATION = &H401C0
     MK_S_REDUCED_TO_SELF = &H401E2
     MK_S_ME = &H401E4
     MK_S_HIM = &H401E5
     MK_S_US = &H401E6
     MK_S_MONIKERALREADYREGISTERED = &H401E7
     CO_E_CLASS_CREATE_FAILED = &H80080001
     CO_E_SCM_ERROR = &H80080002
     CO_E_SCM_RPC_FAILURE = &H80080003
     CO_E_BAD_PATH = &H80080004
     CO_E_SERVER_EXEC_FAILURE = &H80080005
     CO_E_OBJSRV_RPC_FAILURE = &H80080006
     MK_E_NO_NORMALIZED = &H80080007
     CO_E_SERVER_STOPPING = &H80080008
     MEM_E_INVALID_ROOT = &H80080009
     MEM_E_INVALID_LINK = &H80080010
     MEM_E_INVALID_SIZE = &H80080011
     DISP_E_UNKNOWNINTERFACE = &H80020001
     DISP_E_MEMBERNOTFOUND = &H80020003
     DISP_E_PARAMNOTFOUND = &H80020004
     DISP_E_TYPEMISMATCH = &H80020005
     DISP_E_UNKNOWNNAME = &H80020006
     DISP_E_NONAMEDARGS = &H80020007
     DISP_E_BADVARTYPE = &H80020008
     DISP_E_EXCEPTION = &H80020009
     DISP_E_OVERFLOW = &H8002000A
     DISP_E_BADINDEX = &H8002000B
     DISP_E_UNKNOWNLCID = &H8002000C
     DISP_E_ARRAYISLOCKED = &H8002000D
     DISP_E_BADPARAMCOUNT = &H8002000E
     DISP_E_PARAMNOTOPTIONAL = &H8002000F
     DISP_E_BADCALLEE = &H80020010
     DISP_E_NOTACOLLECTION = &H80020011
     TYPE_E_BUFFERTOOSMALL = &H80028016
     TYPE_E_INVDATAREAD = &H80028018
     TYPE_E_UNSUPFORMAT = &H80028019
     TYPE_E_REGISTRYACCESS = &H8002801C
     TYPE_E_LIBNOTREGISTERED = &H8002801D
     TYPE_E_UNDEFINEDTYPE = &H80028027
     TYPE_E_QUALIFIEDNAMEDISALLOWED = &H80028028
     TYPE_E_INVALIDSTATE = &H80028029
     TYPE_E_WRONGTYPEKIND = &H8002802A
     TYPE_E_ELEMENTNOTFOUND = &H8002802B
     TYPE_E_AMBIGUOUSNAME = &H8002802C
     TYPE_E_NAMECONFLICT = &H8002802D
     TYPE_E_UNKNOWNLCID = &H8002802E
     TYPE_E_DLLFUNCTIONNOTFOUND = &H8002802F
     TYPE_E_BADMODULEKIND = &H800288BD
     TYPE_E_SIZETOOBIG = &H800288C5
     TYPE_E_DUPLICATEID = &H800288C6
     TYPE_E_INVALIDID = &H800288CF
     TYPE_E_TYPEMISMATCH = &H80028CA0
     TYPE_E_OUTOFBOUNDS = &H80028CA1
     TYPE_E_IOERROR = &H80028CA2
     TYPE_E_CANTCREATETMPFILE = &H80028CA3
     TYPE_E_CANTLOADLIBRARY = &H80029C4A
     TYPE_E_INCONSISTENTPROPFUNCS = &H80029C83
     TYPE_E_CIRCULARTYPE = &H80029C84
     STG_E_INVALIDFUNCTION = &H80030001
     STG_E_FILENOTFOUND = &H80030002
     STG_E_PATHNOTFOUND = &H80030003
     STG_E_TOOMANYOPENFILES = &H80030004
     STG_E_ACCESSDENIED = &H80030005
     STG_E_INVALIDHANDLE = &H80030006
     STG_E_INSUFFICIENTMEMORY = &H80030008
     STG_E_INVALIDPOINTER = &H80030009
     STG_E_NOMOREFILES = &H80030012
     STG_E_DISKISWRITEPROTECTED = &H80030013
     STG_E_SEEKERROR = &H80030019
     STG_E_WRITEFAULT = &H8003001D
     STG_E_READFAULT = &H8003001E
     STG_E_SHAREVIOLATION = &H80030020
     STG_E_LOCKVIOLATION = &H80030021
     STG_E_FILEALREADYEXISTS = &H80030050
     STG_E_INVALIDPARAMETER = &H80030057
     STG_E_MEDIUMFULL = &H80030070
     STG_E_ABNORMALAPIEXIT = &H800300FA
     STG_E_INVALIDHEADER = &H800300FB
     STG_E_INVALIDNAME = &H800300FC
     STG_E_UNKNOWN = &H800300FD
     STG_E_UNIMPLEMENTEDFUNCTION = &H800300FE
     STG_E_INVALIDFLAG = &H800300FF
     STG_E_INUSE = &H80030100
     STG_E_NOTCURRENT = &H80030101
     STG_E_REVERTED = &H80030102
     STG_E_CANTSAVE = &H80030103
     STG_E_OLDFORMAT = &H80030104
     STG_E_OLDDLL = &H80030105
     STG_E_SHAREREQUIRED = &H80030106
     STG_E_NOTFILEBASEDSTORAGE = &H80030107
     STG_E_EXTANTMARSHALLINGS = &H80030108
     STG_S_CONVERTED = &H30200
     RPC_E_CALL_REJECTED = &H80010001
     RPC_E_CALL_CANCELED = &H80010002
     RPC_E_CANTPOST_INSENDCALL = &H80010003
     RPC_E_CANTCALLOUT_INASYNCCALL = &H80010004
     RPC_E_CANTCALLOUT_INEXTERNALCALL = &H80010005
     RPC_E_CONNECTION_TERMINATED = &H80010006
     RPC_E_SERVER_DIED = &H80010007
     RPC_E_CLIENT_DIED = &H80010008
     RPC_E_INVALID_DATAPACKET = &H80010009
     RPC_E_CANTTRANSMIT_CALL = &H8001000A
     RPC_E_CLIENT_CANTMARSHAL_DATA = &H8001000B
     RPC_E_CLIENT_CANTUNMARSHAL_DATA = &H8001000C
     RPC_E_SERVER_CANTMARSHAL_DATA = &H8001000D
     RPC_E_SERVER_CANTUNMARSHAL_DATA = &H8001000E
     RPC_E_INVALID_DATA = &H8001000F
     RPC_E_INVALID_PARAMETER = &H80010010
     RPC_E_CANTCALLOUT_AGAIN = &H80010011
     RPC_E_SERVER_DIED_DNE = &H80010012
     RPC_E_SYS_CALL_FAILED = &H80010100
     RPC_E_OUT_OF_RESOURCES = &H80010101
     RPC_E_ATTEMPTED_MULTITHREAD = &H80010102
     RPC_E_NOT_REGISTERED = &H80010103
     RPC_E_FAULT = &H80010104
     RPC_E_SERVERFAULT = &H80010105
     RPC_E_CHANGED_MODE = &H80010106
     RPC_E_INVALIDMETHOD = &H80010107
     RPC_E_DISCONNECTED = &H80010108
     RPC_E_RETRY = &H80010109
     RPC_E_SERVERCALL_RETRYLATER = &H8001010A
     RPC_E_SERVERCALL_REJECTED = &H8001010B
     RPC_E_INVALID_CALLDATA = &H8001010C
     RPC_E_CANTCALLOUT_ININPUTSYNCCALL = &H8001010D
     RPC_E_WRONG_THREAD = &H8001010E
     RPC_E_THREAD_NOT_INIT = &H8001010F
     RPC_E_UNEXPECTED = &H8001FFFF

End Enum


' /////////////////////////
'                        //
'  Winnet32 Status Codes //
'                        //
' /////////////////////////

Public Enum WINNET32_ERRORS

'   The specified username is invalid.
     ERROR_BAD_USERNAME = 2202&

'   This network connection does not exist.
     ERROR_NOT_CONNECTED = 2250&

'   This network connection has files open or requests pending.
     ERROR_OPEN_FILES = 2401&

'   The device is in use by an active process and cannot be disconnected.
     ERROR_DEVICE_IN_USE = 2404&

'   The specified device name is invalid.
     ERROR_BAD_DEVICE = 1200&

'   The device is not currently connected but it is a remembered connection.
     ERROR_CONNECTION_UNAVAIL = 1201&

'   An attempt was made to remember a device that had previously been remembered.
     ERROR_DEVICE_ALREADY_REMEMBERED = 1202&

'   No network provider accepted the given network path.
     ERROR_NO_NET_OR_BAD_PATH = 1203&

'   The specified network provider name is invalid.
     ERROR_BAD_PROVIDER = 1204&

'   Unable to open the network connection profile.
     ERROR_CANNOT_OPEN_PROFILE = 1205&

'   The network connection profile is corrupt.
     ERROR_BAD_PROFILE = 1206&

'   Cannot enumerate a non-container.
     ERROR_NOT_CONTAINER = 1207&

'   An extended error has occurred.
     ERROR_EXTENDED_ERROR = 1208&

'   The format of the specified group name is invalid.
     ERROR_INVALID_GROUPNAME = 1209&

'   The format of the specified computer name is invalid.
     ERROR_INVALID_COMPUTERNAME = 1210&

'   The format of the specified event name is invalid.
     ERROR_INVALID_EVENTNAME = 1211&

'   The format of the specified domain name is invalid.
     ERROR_INVALID_DOMAINNAME = 1212&

'   The format of the specified service name is invalid.
     ERROR_INVALID_SERVICENAME = 1213&

'   The format of the specified network name is invalid.
     ERROR_INVALID_NETNAME = 1214&

'   The format of the specified share name is invalid.
     ERROR_INVALID_SHARENAME = 1215&

'   The format of the specified password is invalid.
     ERROR_INVALID_PASSWORDNAME = 1216&

'   The format of the specified message name is invalid.
     ERROR_INVALID_MESSAGENAME = 1217&

'   The format of the specified message destination is invalid.
     ERROR_INVALID_MESSAGEDEST = 1218&

'   The credentials supplied conflict with an existing set of credentials.
     ERROR_SESSION_CREDENTIAL_CONFLICT = 1219&

'   An attempt was made to establish a session to a Lan Manager server, but there
'   are already too many sessions established to that server.
     ERROR_REMOTE_SESSION_LIMIT_EXCEEDED = 1220&

'   The workgroup or domain name is already in use by another computer on the
'   network.
     ERROR_DUP_DOMAINNAME = 1221&

'   The network is not present or not started.
     ERROR_NO_NETWORK = 1222&

End Enum

' /////////////////////////
'                        //
'  Security Status Codes //
'                        //
' /////////////////////////

Public Enum SECURITY_ERRORS

'   Not all privileges referenced are assigned to the caller.
     ERROR_NOT_ALL_ASSIGNED = 1300&

'   Some mapping between account names and security IDs was not done.
     ERROR_SOME_NOT_MAPPED = 1301&

'   No system quota limits are specifically set for this account.
     ERROR_NO_QUOTAS_FOR_ACCOUNT = 1302&

'   No encryption key is available.  A well-known encryption key was returned.
     ERROR_LOCAL_USER_SESSION_KEY = 1303&

'   The NT password is too complex to be converted to a LAN Manager
'   password.  The LAN Manager password returned is a NULL string.
     ERROR_NULL_LM_PASSWORD = 1304&

'   The revision level is unknown.
     ERROR_UNKNOWN_REVISION = 1305&

'   Indicates two revision levels are incompatible.
     ERROR_REVISION_MISMATCH = 1306&

'   This security ID may not be assigned as the owner of this object.
     ERROR_INVALID_OWNER = 1307&

'   This security ID may not be assigned as the primary group of an object.
     ERROR_INVALID_PRIMARY_GROUP = 1308&

'   An attempt has been made to operate on an impersonation token
'   by a thread that is not currently impersonating a client.
     ERROR_NO_IMPERSONATION_TOKEN = 1309&

'   The group may not be disabled.
     ERROR_CANT_DISABLE_MANDATORY = 1310&

'   There are currently no logon servers available to service the logon
'   request.
     ERROR_NO_LOGON_SERVERS = 1311&

'    A specified logon session does not exist.  It may already have
'    been terminated.
     ERROR_NO_SUCH_LOGON_SESSION = 1312&

'    A specified privilege does not exist.
     ERROR_NO_SUCH_PRIVILEGE = 1313&

'    A required privilege is not held by the client.
     ERROR_PRIVILEGE_NOT_HELD = 1314&

'   The name provided is not a properly formed account name.
     ERROR_INVALID_ACCOUNT_NAME = 1315&

'   The specified user already exists.
     ERROR_USER_EXISTS = 1316&

'   The specified user does not exist.
     ERROR_NO_SUCH_USER = 1317&

'   The specified group already exists.
     ERROR_GROUP_EXISTS = 1318&

'   The specified group does not exist.
     ERROR_NO_SUCH_GROUP = 1319&

'   Either the specified user account is already a member of the specified
'   group, or the specified group cannot be deleted because it contains
'   a member.
     ERROR_MEMBER_IN_GROUP = 1320&

'   The specified user account is not a member of the specified group account.
     ERROR_MEMBER_NOT_IN_GROUP = 1321&

'   The last remaining administration account cannot be disabled
'   or deleted.
     ERROR_LAST_ADMIN = 1322&

'   Unable to update the password.  The value provided as the current
'   password is incorrect.
     ERROR_WRONG_PASSWORD = 1323&

'   Unable to update the password.  The value provided for the new password
'   contains values that are not allowed in passwords.
     ERROR_ILL_FORMED_PASSWORD = 1324&

'   Unable to update the password because a password update rule has been
'   violated.
     ERROR_PASSWORD_RESTRICTION = 1325&

'   Logon failure: unknown user name or bad password.
     ERROR_LOGON_FAILURE = 1326&

'   Logon failure: user account restriction.
     ERROR_ACCOUNT_RESTRICTION = 1327&

'   Logon failure: account logon time restriction violation.
     ERROR_INVALID_LOGON_HOURS = 1328&

'   Logon failure: user not allowed to log on to this computer.
     ERROR_INVALID_WORKSTATION = 1329&

'   Logon failure: the specified account password has expired.
     ERROR_PASSWORD_EXPIRED = 1330&

'   Logon failure: account currently disabled.
     ERROR_ACCOUNT_DISABLED = 1331&

'   No mapping between account names and security IDs was done.
     ERROR_NONE_MAPPED = 1332&

'   Too many local user identifiers (LUIDs) were requested at one time.
     ERROR_TOO_MANY_LUIDS_REQUESTED = 1333&

'   No more local user identifiers (LUIDs) are available.
     ERROR_LUIDS_EXHAUSTED = 1334&

'   The subauthority part of a security ID is invalid for this particular use.
     ERROR_INVALID_SUB_AUTHORITY = 1335&

'   The access control list (ACL) structure is invalid.
     ERROR_INVALID_ACL = 1336&

'   The security ID structure is invalid.
     ERROR_INVALID_SID = 1337&

'   The security descriptor structure is invalid.
     ERROR_INVALID_SECURITY_DESCR = 1338&

'   The inherited access control list (ACL) or access control entry (ACE)
'   could not be built.
     ERROR_BAD_INHERITANCE_ACL = 1340&

'   The server is currently disabled.
     ERROR_SERVER_DISABLED = 1341&

'   The server is currently enabled.
     ERROR_SERVER_NOT_DISABLED = 1342&

'   The value provided was an invalid value for an identifier authority.
     ERROR_INVALID_ID_AUTHORITY = 1343&

'   No more memory is available for security information updates.
     ERROR_ALLOTTED_SPACE_EXCEEDED = 1344&

'   The specified attributes are invalid, or incompatible with the
'   attributes for the group as a whole.
     ERROR_INVALID_GROUP_ATTRIBUTES = 1345&

'   Either a required impersonation level was not provided, or the
'   provided impersonation level is invalid.
     ERROR_BAD_IMPERSONATION_LEVEL = 1346&

'   Cannot open an anonymous level security token.
     ERROR_CANT_OPEN_ANONYMOUS = 1347&

'   The validation information class requested was invalid.
     ERROR_BAD_VALIDATION_CLASS = 1348&

'   The type of the token is inappropriate for its attempted use.
     ERROR_BAD_TOKEN_TYPE = 1349&

'   Unable to perform a security operation on an object
'   which has no associated security.
     ERROR_NO_SECURITY_ON_OBJECT = 1350&

'   Indicates a Windows NT Advanced Server could not be contacted or that
'   objects within the domain are protected such that necessary
'   information could not be retrieved.
     ERROR_CANT_ACCESS_DOMAIN_INFO = 1351&

'   The security account manager (SAM) or local security
'   authority (LSA) server was in the wrong state to perform
'   the security operation.
     ERROR_INVALID_SERVER_STATE = 1352&

'   The domain was in the wrong state to perform the security operation.
     ERROR_INVALID_DOMAIN_STATE = 1353&

'   This operation is only allowed for the Primary Domain Controller of the domain.
     ERROR_INVALID_DOMAIN_ROLE = 1354&

'   The specified domain did not exist.
     ERROR_NO_SUCH_DOMAIN = 1355&

'   The specified domain already exists.
     ERROR_DOMAIN_EXISTS = 1356&

'   An attempt was made to exceed the limit on the number of domains per server.
     ERROR_DOMAIN_LIMIT_EXCEEDED = 1357&

'   Unable to complete the requested operation because of either a
'   catastrophic media failure or a data structure corruption on the disk.
     ERROR_INTERNAL_DB_CORRUPTION = 1358&

'   The security account database contains an internal inconsistency.
     ERROR_INTERNAL_ERROR = 1359&

'   Generic access types were contained in an access mask which should
'   already be mapped to non-generic types.
     ERROR_GENERIC_NOT_MAPPED = 1360&

'   A security descriptor is not in the right format (absolute or self-relative).
     ERROR_BAD_DESCRIPTOR_FORMAT = 1361&

'   The requested action is restricted for use by logon processes
'   only.  The calling process has not registered as a logon process.
     ERROR_NOT_LOGON_PROCESS = 1362&

'   Cannot start a new logon session with an ID that is already in use.
     ERROR_LOGON_SESSION_EXISTS = 1363&

'   A specified authentication package is unknown.
     ERROR_NO_SUCH_PACKAGE = 1364&

'   The logon session is not in a state that is consistent with the
'   requested operation.
     ERROR_BAD_LOGON_SESSION_STATE = 1365&

'   The logon session ID is already in use.
     ERROR_LOGON_SESSION_COLLISION = 1366&

'   A logon request contained an invalid logon type value.
     ERROR_INVALID_LOGON_TYPE = 1367&

'   Unable to impersonate via a named pipe until data has been read
'   from that pipe.
     ERROR_CANNOT_IMPERSONATE = 1368&

'   The transaction state of a Registry subtree is incompatible with the
'   requested operation.
     ERROR_RXACT_INVALID_STATE = 1369&

'   An internal security database corruption has been encountered.
     ERROR_RXACT_COMMIT_FAILURE = 1370&

'   Cannot perform this operation on built-in accounts.
     ERROR_SPECIAL_ACCOUNT = 1371&

'   Cannot perform this operation on this built-in special group.
     ERROR_SPECIAL_GROUP = 1372&

'   Cannot perform this operation on this built-in special user.
     ERROR_SPECIAL_USER = 1373&

'   The user cannot be removed from a group because the group
'   is currently the user's primary group.
     ERROR_MEMBERS_PRIMARY_GROUP = 1374&

'   The token is already in use as a primary token.
     ERROR_TOKEN_ALREADY_IN_USE = 1375&

'   The specified local group does not exist.
     ERROR_NO_SUCH_ALIAS = 1376&

'   The specified account name is not a member of the local group.
     ERROR_MEMBER_NOT_IN_ALIAS = 1377&

'   The specified account name is already a member of the local group.
     ERROR_MEMBER_IN_ALIAS = 1378&

'   The specified local group already exists.
     ERROR_ALIAS_EXISTS = 1379&

'   Logon failure: the user has not been granted the requested
'   logon type at this computer.
     ERROR_LOGON_NOT_GRANTED = 1380&

'   The maximum number of secrets that may be stored in a single system has been
'   exceeded.
     ERROR_TOO_MANY_SECRETS = 1381&

'   The length of a secret exceeds the maximum length allowed.
     ERROR_SECRET_TOO_LONG = 1382&

'   The local security authority database contains an internal inconsistency.
     ERROR_INTERNAL_DB_ERROR = 1383&

'   During a logon attempt, the user's security context accumulated too many
'   security IDs.
     ERROR_TOO_MANY_CONTEXT_IDS = 1384&

'   Logon failure: the user has not been granted the requested logon type
'   at this computer.
     ERROR_LOGON_TYPE_NOT_GRANTED = 1385&

'   A cross-encrypted password is necessary to change a user password.
     ERROR_NT_CROSS_ENCRYPTION_REQUIRED = 1386&

'   A new member could not be added to a local group because the member does
'   not exist.
     ERROR_NO_SUCH_MEMBER = 1387&

'   A new member could not be added to a local group because the member has the
'   wrong account type.
     ERROR_INVALID_MEMBER = 1388&

'   Too many security IDs have been specified.
     ERROR_TOO_MANY_SIDS = 1389&

'   A cross-encrypted password is necessary to change this user password.
     ERROR_LM_CROSS_ENCRYPTION_REQUIRED = 1390&

'   Indicates an ACL contains no inheritable components
     ERROR_NO_INHERITANCE = 1391&

'   The file or directory is corrupt and non-readable.
     ERROR_FILE_CORRUPT = 1392&

'   The disk structure is corrupt and non-readable.
     ERROR_DISK_CORRUPT = 1393&

'   There is no user session key for the specified logon session.
     ERROR_NO_USER_SESSION_KEY = 1394&

End Enum
'  End of security error codes


' /////////////////////////
'                        //
'  WinUser Error Codes   //
'                        //
' /////////////////////////

Public Enum WINUSER_ERRORS

'   Invalid window handle.
     ERROR_INVALID_WINDOW_HANDLE = 1400&

'   Invalid menu handle.
     ERROR_INVALID_MENU_HANDLE = 1401&

'   Invalid cursor handle.
     ERROR_INVALID_CURSOR_HANDLE = 1402&

'   Invalid accelerator table handle.
     ERROR_INVALID_ACCEL_HANDLE = 1403&

'   Invalid hook handle.
     ERROR_INVALID_HOOK_HANDLE = 1404&

'   Invalid handle to a multiple-window position structure.
     ERROR_INVALID_DWP_HANDLE = 1405&

'   Cannot create a top-level child window.
     ERROR_TLW_WITH_WSCHILD = 1406&

'   Cannot find window class.
     ERROR_CANNOT_FIND_WND_CLASS = 1407&

'   Invalid window, belongs to other thread.
     ERROR_WINDOW_OF_OTHER_THREAD = 1408&

'   Hot key is already registered.
     ERROR_HOTKEY_ALREADY_REGISTERED = 1409&

'   Class already exists.
     ERROR_CLASS_ALREADY_EXISTS = 1410&

'   Class does not exist.
     ERROR_CLASS_DOES_NOT_EXIST = 1411&

'   Class still has open windows.
     ERROR_CLASS_HAS_WINDOWS = 1412&

'   Invalid index.
     ERROR_INVALID_INDEX = 1413&

'   Invalid icon handle.
     ERROR_INVALID_ICON_HANDLE = 1414&

'   Using private DIALOG window words.
     ERROR_PRIVATE_DIALOG_INDEX = 1415&

'   The listbox identifier was not found.
     ERROR_LISTBOX_ID_NOT_FOUND = 1416&

'   No wildcards were found.
     ERROR_NO_WILDCARD_CHARACTERS = 1417&

'   Thread does not have a clipboard open.
     ERROR_CLIPBOARD_NOT_OPEN = 1418&

'   Hot key is not registered.
     ERROR_HOTKEY_NOT_REGISTERED = 1419&

'   The window is not a valid dialog window.
     ERROR_WINDOW_NOT_DIALOG = 1420&

'   Control ID not found.
     ERROR_CONTROL_ID_NOT_FOUND = 1421&

'   Invalid message for a combo box because it does not have an edit control.
     ERROR_INVALID_COMBOBOX_MESSAGE = 1422&

'   The window is not a combo box.
     ERROR_WINDOW_NOT_COMBOBOX = 1423&

'   Height must be less than 256.
     ERROR_INVALID_EDIT_HEIGHT = 1424&

'   Invalid device context (DC) handle.
     ERROR_DC_NOT_FOUND = 1425&

'   Invalid hook procedure type.
     ERROR_INVALID_HOOK_FILTER = 1426&

'   Invalid hook procedure.
     ERROR_INVALID_FILTER_PROC = 1427&

'   Cannot set non-local hook without a module handle.
     ERROR_HOOK_NEEDS_HMOD = 1428&

'   This hook procedure can only be set Globally.
'
     ERROR_PUBLIC_ONLY_HOOK = 1429&

'   The journal hook procedure is already installed.
     ERROR_JOURNAL_HOOK_SET = 1430&

'   The hook procedure is not installed.
     ERROR_HOOK_NOT_INSTALLED = 1431&

'   Invalid message for single-selection listbox.
     ERROR_INVALID_LB_MESSAGE = 1432&

'   LB_SETCOUNT sent to non-lazy listbox.
     ERROR_SETCOUNT_ON_BAD_LB = 1433&

'   This list box does not support tab stops.
     ERROR_LB_WITHOUT_TABSTOPS = 1434&

'   Cannot destroy object created by another thread.
     ERROR_DESTROY_OBJECT_OF_OTHER_THREAD = 1435&

'   Child windows cannot have menus.
     ERROR_CHILD_WINDOW_MENU = 1436&

'   The window does not have a system menu.
     ERROR_NO_SYSTEM_MENU = 1437&

'   Invalid message box style.
     ERROR_INVALID_MSGBOX_STYLE = 1438&

'   Invalid system-wide (SPI_) parameter.
     ERROR_INVALID_SPI_VALUE = 1439&

'   Screen already locked.
     ERROR_SCREEN_ALREADY_LOCKED = 1440&

'   All handles to windows in a multiple-window position structure must
'   have the same parent.
     ERROR_HWNDS_HAVE_DIFF_PARENT = 1441&

'   The window is not a child window.
     ERROR_NOT_CHILD_WINDOW = 1442&

'   Invalid GW_ command.
     ERROR_INVALID_GW_COMMAND = 1443&

'   Invalid thread identifier.
     ERROR_INVALID_THREAD_ID = 1444&

'   Cannot process a message from a window that is not a multiple document
'   interface (MDI) window.
     ERROR_NON_MDICHILD_WINDOW = 1445&

'   Popup menu already active.
     ERROR_POPUP_ALREADY_ACTIVE = 1446&

'   The window does not have scroll bars.
     ERROR_NO_SCROLLBARS = 1447&

'   Scroll bar range cannot be greater than 0x7FFF.
     ERROR_INVALID_SCROLLBAR_RANGE = 1448&

'   Cannot show or remove the window in the way specified.
     ERROR_INVALID_SHOWWIN_COMMAND = 1449&

End Enum
'  End of WinUser error codes


' /////////////////////////
'                        //
'  Eventlog Status Codes //
'                        //
' /////////////////////////

Public Enum EVENTLOG_ERRORS

'   The event log file is corrupt.
     ERROR_EVENTLOG_FILE_CORRUPT = 1500&

'   No event log file could be opened, so the event logging service did not start.
     ERROR_EVENTLOG_CANT_START = 1501&

'   The event log file is full.
     ERROR_LOG_FILE_FULL = 1502&

'   The event log file has changed between reads.
     ERROR_EVENTLOG_FILE_CHANGED = 1503&

End Enum

'  End of eventlog error codes


' /////////////////////////
'                        //
'    RPC Status Codes    //
'                        //
' /////////////////////////
Public Enum RPC_ERRORS

'   The string binding is invalid.
     RPC_S_INVALID_STRING_BINDING = 1700&

'   The binding handle is not the correct type.
     RPC_S_WRONG_KIND_OF_BINDING = 1701&

'   The binding handle is invalid.
     RPC_S_INVALID_BINDING = 1702&

'   The RPC protocol sequence is not supported.
     RPC_S_PROTSEQ_NOT_SUPPORTED = 1703&

'   The RPC protocol sequence is invalid.
     RPC_S_INVALID_RPC_PROTSEQ = 1704&

'   The string universal unique identifier (UUID) is invalid.
     RPC_S_INVALID_STRING_UUID = 1705&

'   The endpoint format is invalid.
     RPC_S_INVALID_ENDPOINT_FORMAT = 1706&

'   The network address is invalid.
     RPC_S_INVALID_NET_ADDR = 1707&

'   No endpoint was found.
     RPC_S_NO_ENDPOINT_FOUND = 1708&

'   The timeout value is invalid.
     RPC_S_INVALID_TIMEOUT = 1709&

'   The object universal unique identifier (UUID) was not found.
     RPC_S_OBJECT_NOT_FOUND = 1710&

'   The object universal unique identifier (UUID) has already been registered.
     RPC_S_ALREADY_REGISTERED = 1711&

'   The type universal unique identifier (UUID) has already been registered.
     RPC_S_TYPE_ALREADY_REGISTERED = 1712&

'   The RPC server is already listening.
     RPC_S_ALREADY_LISTENING = 1713&

'   No protocol sequences have been registered.
     RPC_S_NO_PROTSEQS_REGISTERED = 1714&

'   The RPC server is not listening.
     RPC_S_NOT_LISTENING = 1715&

'   The manager type is unknown.
     RPC_S_UNKNOWN_MGR_TYPE = 1716&

'   The interface is unknown.
     RPC_S_UNKNOWN_IF = 1717&

'   There are no bindings.
     RPC_S_NO_BINDINGS = 1718&

'   There are no protocol sequences.
     RPC_S_NO_PROTSEQS = 1719&

'   The endpoint cannot be created.
     RPC_S_CANT_CREATE_ENDPOINT = 1720&

'   Not enough resources are available to complete this operation.
     RPC_S_OUT_OF_RESOURCES = 1721&

'   The RPC server is unavailable.
     RPC_S_SERVER_UNAVAILABLE = 1722&

'   The RPC server is too busy to complete this operation.
     RPC_S_SERVER_TOO_BUSY = 1723&

'   The network options are invalid.
     RPC_S_INVALID_NETWORK_OPTIONS = 1724&

'   There is not a remote procedure call active in this thread.
     RPC_S_NO_CALL_ACTIVE = 1725&

'   The remote procedure call failed.
     RPC_S_CALL_FAILED = 1726&

'   The remote procedure call failed and did not execute.
     RPC_S_CALL_FAILED_DNE = 1727&

'   A remote procedure call (RPC) protocol error occurred.
     RPC_S_PROTOCOL_ERROR = 1728&

'   The transfer syntax is not supported by the RPC server.
     RPC_S_UNSUPPORTED_TRANS_SYN = 1730&

'   The universal unique identifier (UUID) type is not supported.
     RPC_S_UNSUPPORTED_TYPE = 1732&

'   The tag is invalid.
     RPC_S_INVALID_TAG = 1733&

'   The array bounds are invalid.
     RPC_S_INVALID_BOUND = 1734&

'   The binding does not contain an entry name.
     RPC_S_NO_ENTRY_NAME = 1735&

'   The name syntax is invalid.
     RPC_S_INVALID_NAME_SYNTAX = 1736&

'   The name syntax is not supported.
     RPC_S_UNSUPPORTED_NAME_SYNTAX = 1737&

'   No network address is available to use to construct a universal
'   unique identifier (UUID).
     RPC_S_UUID_NO_ADDRESS = 1739&

'   The endpoint is a duplicate.
     RPC_S_DUPLICATE_ENDPOINT = 1740&

'   The authentication type is unknown.
     RPC_S_UNKNOWN_AUTHN_TYPE = 1741&

'   The maximum number of calls is too small.
     RPC_S_MAX_CALLS_TOO_SMALL = 1742&

'   The string is too long.
     RPC_S_STRING_TOO_LONG = 1743&

'   The RPC protocol sequence was not found.
     RPC_S_PROTSEQ_NOT_FOUND = 1744&

'   The procedure number is out of range.
     RPC_S_PROCNUM_OUT_OF_RANGE = 1745&

'   The binding does not contain any authentication information.
     RPC_S_BINDING_HAS_NO_AUTH = 1746&

'   The authentication service is unknown.
     RPC_S_UNKNOWN_AUTHN_SERVICE = 1747&

'   The authentication level is unknown.
     RPC_S_UNKNOWN_AUTHN_LEVEL = 1748&

'   The security context is invalid.
     RPC_S_INVALID_AUTH_IDENTITY = 1749&

'   The authorization service is unknown.
     RPC_S_UNKNOWN_AUTHZ_SERVICE = 1750&

'   The entry is invalid.
     EPT_S_INVALID_ENTRY = 1751&

'   The server endpoint cannot perform the operation.
     EPT_S_CANT_PERFORM_OP = 1752&

'   There are no more endpoints available from the endpoint mapper.
     EPT_S_NOT_REGISTERED = 1753&

'   No interfaces have been exported.
     RPC_S_NOTHING_TO_EXPORT = 1754&

'   The entry name is incomplete.
     RPC_S_INCOMPLETE_NAME = 1755&

'   The version option is invalid.
     RPC_S_INVALID_VERS_OPTION = 1756&

'   There are no more members.
     RPC_S_NO_MORE_MEMBERS = 1757&

'   There is nothing to unexport.
     RPC_S_NOT_ALL_OBJS_UNEXPORTED = 1758&

'   The interface was not found.
     RPC_S_INTERFACE_NOT_FOUND = 1759&

'   The entry already exists.
     RPC_S_ENTRY_ALREADY_EXISTS = 1760&

'   The entry is not found.
     RPC_S_ENTRY_NOT_FOUND = 1761&

'   The name service is unavailable.
     RPC_S_NAME_SERVICE_UNAVAILABLE = 1762&

'   The network address family is invalid.
     RPC_S_INVALID_NAF_ID = 1763&

'   The requested operation is not supported.
     RPC_S_CANNOT_SUPPORT = 1764&

'   No security context is available to allow impersonation.
     RPC_S_NO_CONTEXT_AVAILABLE = 1765&

'   An internal error occurred in a remote procedure call (RPC).
     RPC_S_INTERNAL_ERROR = 1766&

'   The RPC server attempted an integer division by zero.'
     RPC_S_ZERO_DIVIDE = 1767&

'   An addressing error occurred in the RPC server.
     RPC_S_ADDRESS_ERROR = 1768&

'   A floating-point operation at the RPC server caused a division by zero.
     RPC_S_FP_DIV_ZERO = 1769&

'   A floating-point underflow occurred at the RPC server.
     RPC_S_FP_UNDERFLOW = 1770&

'   A floating-point overflow occurred at the RPC server.
     RPC_S_FP_OVERFLOW = 1771&

'   The list of RPC servers available for the binding of auto handles
'   has been exhausted.
     RPC_X_NO_MORE_ENTRIES = 1772&

'   Unable to open the character translation table file.
     RPC_X_SS_CHAR_TRANS_OPEN_FAIL = 1773&

'   The file containing the character translation table has fewer than
'   512 bytes.
     RPC_X_SS_CHAR_TRANS_SHORT_FILE = 1774&

'   A null context handle was passed from the client to the host during
'   a remote procedure call.
     RPC_X_SS_IN_NULL_CONTEXT = 1775&

'   The context handle changed during a remote procedure call.
     RPC_X_SS_CONTEXT_DAMAGED = 1777&

'   The binding handles passed to a remote procedure call do not match.
     RPC_X_SS_HANDLES_MISMATCH = 1778&

'   The stub is unable to get the remote procedure call handle.
     RPC_X_SS_CANNOT_GET_CALL_HANDLE = 1779&

'   A null reference pointer was passed to the stub.
     RPC_X_NULL_REF_POINTER = 1780&

'   The enumeration value is out of range.
     RPC_X_ENUM_VALUE_OUT_OF_RANGE = 1781&

'   The byte count is too small.
     RPC_X_BYTE_COUNT_TOO_SMALL = 1782&

'   The stub received bad data.
     RPC_X_BAD_STUB_DATA = 1783&

'   The supplied user buffer is not valid for the requested operation.
     ERROR_INVALID_USER_BUFFER = 1784&

'   The disk media is not recognized.  It may not be formatted.
     ERROR_UNRECOGNIZED_MEDIA = 1785&

'   The workstation does not have a trust secret.
     ERROR_NO_TRUST_LSA_SECRET = 1786&

'   The SAM database on the Windows NT Advanced Server does not have a computer
'   account for this workstation trust relationship.
     ERROR_NO_TRUST_SAM_ACCOUNT = 1787&

'   The trust relationship between the primary domain and the trusted
'   domain failed.
     ERROR_TRUSTED_DOMAIN_FAILURE = 1788&

'   The trust relationship between this workstation and the primary
'   domain failed.
     ERROR_TRUSTED_RELATIONSHIP_FAILURE = 1789&

'   The network logon failed.
     ERROR_TRUST_FAILURE = 1790&

'   A remote procedure call is already in progress for this thread.
     RPC_S_CALL_IN_PROGRESS = 1791&

'   An attempt was made to logon, but the network logon service was not started.
     ERROR_NETLOGON_NOT_STARTED = 1792&

'   The user's account has expired.
     ERROR_ACCOUNT_EXPIRED = 1793&

'   The redirector is in use and cannot be unloaded.
     ERROR_REDIRECTOR_HAS_OPEN_HANDLES = 1794&

'   The specified printer driver is already installed.
     ERROR_PRINTER_DRIVER_ALREADY_INSTALLED = 1795&

'   The specified port is unknown.
     ERROR_UNKNOWN_PORT = 1796&

'   The printer driver is unknown.
     ERROR_UNKNOWN_PRINTER_DRIVER = 1797&

'   The print processor is unknown.
'
     ERROR_UNKNOWN_PRINTPROCESSOR = 1798&

'   The specified separator file is invalid.
     ERROR_INVALID_SEPARATOR_FILE = 1799&

'   The specified priority is invalid.
     ERROR_INVALID_PRIORITY = 1800&

'   The printer name is invalid.
     ERROR_INVALID_PRINTER_NAME = 1801&

'   The printer already exists.
     ERROR_PRINTER_ALREADY_EXISTS = 1802&

'   The printer command is invalid.
     ERROR_INVALID_PRINTER_COMMAND = 1803&

'   The specified datatype is invalid.
     ERROR_INVALID_DATATYPE = 1804&

'   The Environment specified is invalid.
     ERROR_INVALID_ENVIRONMENT = 1805&

'   There are no more bindings.
     RPC_S_NO_MORE_BINDINGS = 1806&

'   The account used is an interdomain trust account.  Use your Global user account or local user account to access this server.
     ERROR_NOLOGON_INTERDOMAIN_TRUST_ACCOUNT = 1807&

'   The account used is a Computer Account.  Use your Global user account or local user account to access this server.
     ERROR_NOLOGON_WORKSTATION_TRUST_ACCOUNT = 1808&

'   The account used is an server trust account.  Use your Global user account or local user account to access this server.
     ERROR_NOLOGON_SERVER_TRUST_ACCOUNT = 1809&

'   The name or security ID (SID) of the domain specified is inconsistent
'   with the trust information for that domain.
     ERROR_DOMAIN_TRUST_INCONSISTENT = 1810&

'   The server is in use and cannot be unloaded.
     ERROR_SERVER_HAS_OPEN_HANDLES = 1811&

'   The specified image file did not contain a resource section.
     ERROR_RESOURCE_DATA_NOT_FOUND = 1812&

'   The specified resource type can not be found in the image file.
     ERROR_RESOURCE_TYPE_NOT_FOUND = 1813&

'   The specified resource name can not be found in the image file.
     ERROR_RESOURCE_NAME_NOT_FOUND = 1814&

'   The specified resource language ID cannot be found in the image file.
     ERROR_RESOURCE_LANG_NOT_FOUND = 1815&

'   Not enough quota is available to process this command.
     ERROR_NOT_ENOUGH_QUOTA = 1816&

'   The group member was not found.
     RPC_S_GROUP_MEMBER_NOT_FOUND = 1898&

'   The endpoint mapper database could not be created.
     EPT_S_CANT_CREATE = 1899&

'   The object universal unique identifier (UUID) is the nil UUID.
     RPC_S_INVALID_OBJECT = 1900&

'   The specified time is invalid.
     ERROR_INVALID_TIME = 1901&

'   The specified Form name is invalid.
     ERROR_INVALID_FORM_NAME = 1902&

'   The specified Form size is invalid
     ERROR_INVALID_FORM_SIZE = 1903&

'   The specified Printer handle is already being waited on
     ERROR_ALREADY_WAITING = 1904&

'   The specified Printer has been deleted
     ERROR_PRINTER_DELETED = 1905&

'   The state of the Printer is invalid
     ERROR_INVALID_PRINTER_STATE = 1906&

'   The list of servers for this workgroup is not currently available
     ERROR_NO_BROWSER_SERVERS_FOUND = 6118&

End Enum

Public Type Errors
    Win32API_Error As WIN32API_ERRORS
    Winnet_Error As WINNET32_ERRORS
    Security_Error As SECURITY_ERRORS
    WinUser_Error As WINUSER_ERRORS
    EventLog_Error As EVENTLOG_ERRORS
    RPC_Error As RPC_ERRORS
End Type

'================================================
'ERROR API calls

Private Const FORMAT_MESSAGE_ALLOCATE_BUFFER = &H100
Private Const FORMAT_MESSAGE_FROM_SYSTEM = &H1000
Private Const LANG_NEUTRAL = &H0
Private Const SUBLANG_DEFAULT = &H1

Private Declare Function GetLastError Lib "kernel32" () As Long
Private Declare Sub SetLastError Lib "kernel32" (ByVal dwErrCode As Long)
Private Declare Function FormatMessage Lib "kernel32" Alias "FormatMessageA" (ByVal dwFlags As Long, lpSource As Any, ByVal dwMessageId As Long, ByVal dwLanguageId As Long, ByVal lpBuffer As String, ByVal nSize As Long, Arguments As Long) As Long

'================================================
'ERROR SHOW FUNCTIONS

Public Function Win32APIErr(ByVal ErrCode As WIN32API_ERRORS) As String
    Dim Buffer As String

    Buffer = Space(200)
    SetLastError ErrCode
    FormatMessage FORMAT_MESSAGE_FROM_SYSTEM, ByVal 0&, GetLastError, LANG_NEUTRAL, Buffer, 200, ByVal 0&
    Win32APIErr = Replace(Trim(Buffer), vbCrLf, "")

End Function

Public Function Winnet32Err(ByVal ErrCode As WINNET32_ERRORS) As String
    Dim Buffer As String

    Buffer = Space(200)
    SetLastError ErrCode
    FormatMessage FORMAT_MESSAGE_FROM_SYSTEM, ByVal 0&, GetLastError, LANG_NEUTRAL, Buffer, 200, ByVal 0&
    Winnet32Err = Replace(Trim(Buffer), vbCrLf, "")

End Function

Public Function SecurityErr(ByVal ErrCode As SECURITY_ERRORS) As String
    Dim Buffer As String

    Buffer = Space(200)
    SetLastError ErrCode
    FormatMessage FORMAT_MESSAGE_FROM_SYSTEM, ByVal 0&, GetLastError, LANG_NEUTRAL, Buffer, 200, ByVal 0&
    SecurityErr = Replace(Trim(Buffer), vbCrLf, "")

End Function

Public Function WinUserErr(ByVal ErrCode As WINUSER_ERRORS) As String
    Dim Buffer As String

    Buffer = Space(200)
    SetLastError ErrCode
    FormatMessage FORMAT_MESSAGE_FROM_SYSTEM, ByVal 0&, GetLastError, LANG_NEUTRAL, Buffer, 200, ByVal 0&
    WinUserErr = Replace(Trim(Buffer), vbCrLf, "")

End Function

Public Function EventLogErr(ByVal ErrCode As EVENTLOG_ERRORS) As String
    Dim Buffer As String

    Buffer = Space(200)
    SetLastError ErrCode
    FormatMessage FORMAT_MESSAGE_FROM_SYSTEM, ByVal 0&, GetLastError, LANG_NEUTRAL, Buffer, 200, ByVal 0&
    EventLogErr = Replace(Trim(Buffer), vbCrLf, "")

End Function

Public Function RpcErr(ByVal ErrCode As RPC_ERRORS) As String
    Dim Buffer As String

    Buffer = Space(200)
    SetLastError ErrCode
    FormatMessage FORMAT_MESSAGE_FROM_SYSTEM, ByVal 0&, GetLastError, LANG_NEUTRAL, Buffer, 200, ByVal 0&
    RpcErr = Replace(Trim(Buffer), vbCrLf, "")

End Function


