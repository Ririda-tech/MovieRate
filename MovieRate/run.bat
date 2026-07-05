@echo off
cd /d "%~dp0"
java -cp "out;lib\mysql-connector-j-8.3.0.jar" app.Main
pause
