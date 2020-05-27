
echo Deleting Old Text Functions
del .\Extensions\TextLibrary\extension.xml
del .\Extensions\TextLibrary\Lib\JavaStrings.jar
del .\Extensions\TextLibrary\Lib\DotNetStrings.dll

echo Copying Java and DOtNet Text Function Library Files
mkdir Extensions
mkdir Extensions\TextLibrary
mkdir Extensions\TextLibrary\Lib
copy ..\TextFunctionsSourceCode\JavaStrings\dist\JavaStrings.jar .\Extensions\TextLibrary\Lib
copy ..\TextFunctionsSourceCode\DotNetStrings\DotNetStrings\bin\Release\DotNetStrings.dll .\Extensions\TextLibrary\Lib
copy ..\TextFunctionsSourceCode\DotNetStrings\DotNetStrings\extensionxml\extension.xml .\Extensions\TextLibrary
copy ..\TextFunctionsSourceCode\DotNetStrings\DotNetStrings\extensionxml\*.* .\Extensions\TextLibrary