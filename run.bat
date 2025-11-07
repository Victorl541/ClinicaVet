@echo off
setlocal enabledelayedexpansion

REM Determinar javac y java (prefiere JAVA_HOME si está definido)
if defined JAVA_HOME (
  set "JAVAC=%JAVA_HOME%\bin\javac"
  set "JAVA=%JAVA_HOME%\bin\java"
) else (
  set "JAVAC=javac"
  set "JAVA=java"
)

REM Crear carpeta de salida si no existe
if not exist out\classes (
  mkdir out\classes
)

REM Generar lista de fuentes en sources.txt (en la misma carpeta del .bat)
if exist "%~dp0sources.txt" del "%~dp0sources.txt"
for /R "%~dp0src" %%f in (*.java) do (
  echo %%f>>"%~dp0sources.txt"
)

echo Compilando fuentes...
"%JAVAC%" -encoding UTF-8 -d out\classes @"%~dp0sources.txt"
if errorlevel 1 (
  echo.
  echo Compilación fallida. Mostrando lista de fuentes generadas:
  type "%~dp0sources.txt"
  pause
  endlocal
  exit /b 1
)

echo Compilación OK.
echo Lanzando la aplicación (se abrirá la ventana Swing)...
start "ClinicaVet" "%JAVA%" -cp "out\classes;lib\*" com.clinicavet.App

endlocal
