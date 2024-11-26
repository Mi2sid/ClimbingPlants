@echo off
setlocal

:: Chemin vers le dossier processing
set PROCESSING_PATH=C:\Users\nonod\Downloads\processing-4.3-windows-x64\processing-4.3

:: clear
if exist build rmdir /s /q build
mkdir build


javac -cp "%PROCESSING_PATH%\core\library\core.jar;%PROCESSING_PATH%\core\library\jogl-all.jar;%PROCESSING_PATH%\core\library\gluegen-rt.jar;" -d build src\Camera.java src\struct\Triple.java src\struct\Pair.java src\enums\Colors.java src\particle\Node.java src\particle\Strcuture.java src\geometry\Face.java src\geometry\Mesh.java src\geometry\Vec3f.java src\geometry\Ray.java src\geometry\HalfEdge.java src\PApp.java
java -Xmx8G -cp "build;%PROCESSING_PATH%\core\library\core.jar;%PROCESSING_PATH%\core\library\jogl-all.jar;%PROCESSING_PATH%\core\library\gluegen-rt.jar;" -Djava.library.path=%PROCESSING_PATH%\core\library\windows-amd64 PApp
