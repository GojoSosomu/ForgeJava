@rem --------------------------------------------------------------------------
@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Resolve paths to make them shorter.
set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.

for %%i in ("%DIRNAME%") do set APP_HOME=%%~fi
set APP_BASE_NAME=%~n0

@rem --------------------------------------------------------------------------
@rem *** PORTABILITY FIXES START HERE ***
@rem --------------------------------------------------------------------------

@rem 1. Check for local JDK folder in the project root
set LOCAL_JDK_PATH=%APP_HOME%\jdk

if exist "%LOCAL_JDK_PATH%\bin\java.exe" (
echo [PORTABILITY] Found local JDK in: %LOCAL_JDK_PATH%
@rem Ensure quotes around path with spaces
set JAVA_HOME="%LOCAL_JDK_PATH%"
) else (
echo [PORTABILITY] Local JDK not found. Falling back to system JAVA_HOME or PATH.
)

@rem 2. Set portable Gradle cache location relative to the project root
@rem NOTE: -g is a GRADLE command-line argument, not a JVM argument.
@rem We must pass it after -jar.
set GRADLE_CMD_OPTS=-g "%APP_HOME%\local_gradle_cache"

@rem --------------------------------------------------------------------------
@rem *** PORTABILITY FIXES END HERE ***
@rem --------------------------------------------------------------------------

@rem Add default JVM options here. You can also set JAVA_OPTS and GRADLE_OPTS system environment variables.
set DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH. 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation, or place a JDK in the 'jdk' folder. 1>&2

goto fail

:findJavaFromJavaHome
@rem Remove surrounding quotes if they exist, then append /bin/java.exe
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE="%JAVA_HOME%\bin\java.exe"

@rem Use quotes when checking existence
if exist %JAVA_EXE% goto execute

echo. 1>&2
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME% 1>&2
echo. 1>&2
echo Please set the JAVA_HOME variable in your environment to match the 1>&2
echo location of your Java installation. 1>&2

goto fail

:execute
@rem Setup the command line

@rem Execute Gradle: Note the use of GRADLE_CMD_OPTS after -jar
@rem %* expands to all command-line arguments passed to gradlew.bat
%JAVA_EXE% %DEFAULT_JVM_OPTS% %JAVA_OPTS% "-Dorg.gradle.appname=%APP_BASE_NAME%" -jar "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" %GRADLE_CMD_OPTS% %*

:fail
if "%OS%"=="Windows_NT" endlocal