
set DIR="xml-dir"
rm -rf "%DIR%"
mkdir %DIR%
set exiftoolpath="C:\Users\387640\Downloads\java\"

%exiftoolpath%exiftool -listx -Adobe:all > "%DIR%"\Adobe.xml
REM %exiftoolpath%exiftool -listx -AdobeAPP14:all > "%DIR%"\AdobeAPP14.xml
%exiftoolpath%exiftool -listx -APP12:all > "%DIR%"\APP12.xml
%exiftoolpath%exiftool -listx -APP13:all > "%DIR%"\APP13.xml
%exiftoolpath%exiftool -listx -APP14:all > "%DIR%"\APP14.xml
%exiftoolpath%exiftool -listx -APP15:all > "%DIR%"\APP15.xml
%exiftoolpath%exiftool -listx -APP5:all > "%DIR%"\APP5.xml
%exiftoolpath%exiftool -listx -APP6:all > "%DIR%"\APP6.xml
%exiftoolpath%exiftool -listx -CanonCustom:all > "%DIR%"\CanonCustom.xml
%exiftoolpath%exiftool -listx -CanonVRD:all > "%DIR%"\CanonVRD.xml
REM %exiftoolpath%exiftool -listx -CIFF:all > "%DIR%"\CIFF.xml
%exiftoolpath%exiftool -listx -Composite:all > "%DIR%"\Composite.xml
%exiftoolpath%exiftool -listx -EXIF:all > "%DIR%"\EXIF.xml
REM %exiftoolpath%exiftool -listx -ExifIFD:all > "%DIR%"\ExifIFD.xml
%exiftoolpath%exiftool -listx -File:all > "%DIR%"\File.xml
%exiftoolpath%exiftool -listx -FlashPix:all > "%DIR%"\FlashPix.xml
%exiftoolpath%exiftool -listx -FotoStation:all > "%DIR%"\FotoStation.xml
%exiftoolpath%exiftool -listx -GeoTIFF:all > "%DIR%"\GeoTIFF.xml
%exiftoolpath%exiftool -listx -GPS:all > "%DIR%"\GPS.xml
%exiftoolpath%exiftool -listx -ICC-header:all > "%DIR%"\ICC-header.xml
%exiftoolpath%exiftool -listx -ICC-meas:all > "%DIR%"\ICC-meas.xml
%exiftoolpath%exiftool -listx -ICC-view:all > "%DIR%"\ICC-view.xml
%exiftoolpath%exiftool -listx -ICC_Profile:all > "%DIR%"\ICC_Profile.xml
REM %exiftoolpath%exiftool -listx -IFD0:all > "%DIR%"\IFD0.xml
REM %exiftoolpath%exiftool -listx -IFD1:all > "%DIR%"\IFD1.xml
REM %exiftoolpath%exiftool -listx -IFD2:all > "%DIR%"\IFD2.xml
REM %exiftoolpath%exiftool -listx -IFD3:all > "%DIR%"\IFD3.xml
REM %exiftoolpath%exiftool -listx -InteropIFD:all > "%DIR%"\InteropIFD.xml
%exiftoolpath%exiftool -listx -IPTC:all > "%DIR%"\IPTC.xml
%exiftoolpath%exiftool -listx -JFIF:all > "%DIR%"\JFIF.xml
%exiftoolpath%exiftool -listx -JPEG2000:all > "%DIR%"\JPEG2000.xml
REM %exiftoolpath%exiftool -listx -KodakMeta:all > "%DIR%"\KodakMeta.xml
%exiftoolpath%exiftool -listx -MakerNotes:all > "%DIR%"\MakerNotes.xml
%exiftoolpath%exiftool -listx -Meta:all > "%DIR%"\Meta.xml
%exiftoolpath%exiftool -listx -MPF:all > "%DIR%"\MPF.xml
%exiftoolpath%exiftool -listx -NikonCapture:all > "%DIR%"\NikonCapture.xml
%exiftoolpath%exiftool -listx -PhotoMechanic:all > "%DIR%"\PhotoMechanic.xml
%exiftoolpath%exiftool -listx -PNG:all > "%DIR%"\PNG.xml
%exiftoolpath%exiftool -listx -PrintIM:all > "%DIR%"\PrintIM.xml
REM %exiftoolpath%exiftool -listx -RicohRMETA:all > "%DIR%"\RicohRMETA.xml
%exiftoolpath%exiftool -listx -RMETA:all > "%DIR%"\RMETA.xml
%exiftoolpath%exiftool -listx -System:all > "%DIR%"\System.xml
%exiftoolpath%exiftool -listx -XMP-dc:all > "%DIR%"\XMP-dc.xml
%exiftoolpath%exiftool -listx -XMP-photoshop:all > "%DIR%"\XMP-photoshop.xml
%exiftoolpath%exiftool -listx -XMP-rdf:all > "%DIR%"\XMP-rdf.xml
%exiftoolpath%exiftool -listx -XMP-x:all > "%DIR%"\XMP-x.xml
%exiftoolpath%exiftool -listx -XMP-xmpBJ:all > "%DIR%"\XMP-xmpBJ.xml
%exiftoolpath%exiftool -listx -XMP-xmpMM:all > "%DIR%"\XMP-xmpMM.xml
REM %exiftoolpath%exiftool -listx -XMP-xmpRightsApple:all > "%DIR%"\XMP-xmpRightsApple.xml
%exiftoolpath%exiftool -listx -Canon:all > "%DIR%"\Canon.xml
%exiftoolpath%exiftool -listx -Casio:all > "%DIR%"\Casio.xml
%exiftoolpath%exiftool -listx -HP:all > "%DIR%"\HP.xml
%exiftoolpath%exiftool -listx -Kodak:all > "%DIR%"\Kodak.xml
%exiftoolpath%exiftool -listx -Leaf:all > "%DIR%"\Leaf.xml
%exiftoolpath%exiftool -listx -Minolta:all > "%DIR%"\Minolta.xml
%exiftoolpath%exiftool -listx -Motorola:all > "%DIR%"\Motorola.xml
%exiftoolpath%exiftool -listx -Nikon:all > "%DIR%"\Nikon.xml
%exiftoolpath%exiftool -listx -Olympus:all > "%DIR%"\Olympus.xml
%exiftoolpath%exiftool -listx -Panasonic:all > "%DIR%"\Panasonic.xml
%exiftoolpath%exiftool -listx -Pentax:all > "%DIR%"\Pentax.xml
%exiftoolpath%exiftool -listx -Ricoh:all > "%DIR%"\Ricoh.xml
%exiftoolpath%exiftool -listx -Samsung:all > "%DIR%"\Samsung.xml
%exiftoolpath%exiftool -listx -Sanyo:all > "%DIR%"\Sanyo.xml
%exiftoolpath%exiftool -listx -Sony:all > "%DIR%"\Sony.xml

