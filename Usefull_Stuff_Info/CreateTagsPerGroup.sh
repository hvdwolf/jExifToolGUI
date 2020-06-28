#!/bin/sh

if [ "$1" = "" ]
then
        printf "\n\nYou have to provide which tags you want to save\n\n"
        printf "all => CreateTagsPerGroup.sh all\n"
        printf "or\n"
        printf "writable => CreateTagsPerGroup.sh writable\n\n"
        exit
fi

if [ "$1" = "all" ]
then
        tagoption="-list"
        DIR="all-tags-text-dir"
        printf "Create all the text files for all tags.\nThis will take quite long.\n\n"
else
        tagoption="-listw"
        DIR="writable-tags-text-dir"
        printf "Create all the text files for all writable tags.\nThis will take quite long.\n\n"
fi

rm -rf "$DIR"
mkdir -p "$DIR"
#exiftoolpath="/media/harryvanderwolf/128GB/software/Image-ExifTool-11.44"
exiftoolpath="/usr/bin"


$exiftoolpath/exiftool ${tagoption} -Adobe:all > "$DIR"/Adobe.txt
#$exiftoolpath/exiftool ${tagoption} -AdobeAPP14:all > "$DIR"/AdobeAPP14.txt
$exiftoolpath/exiftool ${tagoption} -APP12:all > "$DIR"/APP12.txt
$exiftoolpath/exiftool ${tagoption} -APP13:all > "$DIR"/APP13.txt
$exiftoolpath/exiftool ${tagoption} -APP14:all > "$DIR"/APP14.txt
$exiftoolpath/exiftool ${tagoption} -APP15:all > "$DIR"/APP15.txt
$exiftoolpath/exiftool ${tagoption} -APP5:all > "$DIR"/APP5.txt
$exiftoolpath/exiftool ${tagoption} -APP6:all > "$DIR"/APP6.txt
$exiftoolpath/exiftool ${tagoption} -CanonCustom:all > "$DIR"/CanonCustom.txt
$exiftoolpath/exiftool ${tagoption} -CanonVRD:all > "$DIR"/CanonVRD.txt
#$exiftoolpath/exiftool ${tagoption} -CIFF:all > "$DIR"/CIFF.txt
$exiftoolpath/exiftool ${tagoption} -Composite:all > "$DIR"/Composite.txt
$exiftoolpath/exiftool ${tagoption} -EXIF:all > "$DIR"/EXIF.txt
#$exiftoolpath/exiftool ${tagoption} -ExifIFD:all > "$DIR"/ExifIFD.txt
$exiftoolpath/exiftool ${tagoption} -File:all > "$DIR"/File.txt
$exiftoolpath/exiftool ${tagoption} -FlashPix:all > "$DIR"/FlashPix.txt
$exiftoolpath/exiftool ${tagoption} -FotoStation:all > "$DIR"/FotoStation.txt
$exiftoolpath/exiftool ${tagoption} -GeoTIFF:all > "$DIR"/GeoTIFF.txt
$exiftoolpath/exiftool ${tagoption} -GPS:all > "$DIR"/GPS.txt
$exiftoolpath/exiftool ${tagoption} -ICC-header:all > "$DIR"/ICC-header.txt
$exiftoolpath/exiftool ${tagoption} -ICC-meas:all > "$DIR"/ICC-meas.txt
$exiftoolpath/exiftool ${tagoption} -ICC-view:all > "$DIR"/ICC-view.txt
$exiftoolpath/exiftool ${tagoption} -ICC_Profile:all > "$DIR"/ICC_Profile.txt
#$exiftoolpath/exiftool ${tagoption} -IFD0:all > "$DIR"/IFD0.txt
#$exiftoolpath/exiftool ${tagoption} -IFD1:all > "$DIR"/IFD1.txt
#$exiftoolpath/exiftool ${tagoption} -IFD2:all > "$DIR"/IFD2.txt
#$exiftoolpath/exiftool ${tagoption} -IFD3:all > "$DIR"/IFD3.txt
#$exiftoolpath/exiftool ${tagoption} -InteropIFD:all > "$DIR"/InteropIFD.txt
$exiftoolpath/exiftool ${tagoption} -IPTC:all > "$DIR"/IPTC.txt
$exiftoolpath/exiftool ${tagoption} -JFIF:all > "$DIR"/JFIF.txt
$exiftoolpath/exiftool ${tagoption} -JPEG2000:all > "$DIR"/JPEG2000.txt
#$exiftoolpath/exiftool ${tagoption} -KodakMeta:all > "$DIR"/KodakMeta.txt
$exiftoolpath/exiftool ${tagoption} -MakerNotes:all > "$DIR"/MakerNotes.txt
$exiftoolpath/exiftool ${tagoption} -Meta:all > "$DIR"/Meta.txt
$exiftoolpath/exiftool ${tagoption} -MPF:all > "$DIR"/MPF.txt
$exiftoolpath/exiftool ${tagoption} -NikonCapture:all > "$DIR"/NikonCapture.txt
$exiftoolpath/exiftool ${tagoption} -PhotoMechanic:all > "$DIR"/PhotoMechanic.txt
$exiftoolpath/exiftool ${tagoption} -PNG:all > "$DIR"/PNG.txt
$exiftoolpath/exiftool ${tagoption} -PrintIM:all > "$DIR"/PrintIM.txt
#$exiftoolpath/exiftool ${tagoption} -RicohRMETA:all > "$DIR"/RicohRMETA.txt
$exiftoolpath/exiftool ${tagoption} -RMETA:all > "$DIR"/RMETA.txt
$exiftoolpath/exiftool ${tagoption} -System:all > "$DIR"/System.txt
$exiftoolpath/exiftool ${tagoption} -XMP-dc:all > "$DIR"/XMP-dc.txt
$exiftoolpath/exiftool ${tagoption} -XMP-photoshop:all > "$DIR"/XMP-photoshop.txt
$exiftoolpath/exiftool ${tagoption} -XMP-rdf:all > "$DIR"/XMP-rdf.txt
$exiftoolpath/exiftool ${tagoption} -XMP-x:all > "$DIR"/XMP-x.txt
$exiftoolpath/exiftool ${tagoption} -XMP-xmpBJ:all > "$DIR"/XMP-xmpBJ.txt
$exiftoolpath/exiftool ${tagoption} -XMP-xmpMM:all > "$DIR"/XMP-xmpMM.txt
#$exiftoolpath/exiftool ${tagoption} -XMP-xmpRightsApple:all > "$DIR"/XMP-xmpRightsApple.txt
$exiftoolpath/exiftool ${tagoption} -Canon:all > "$DIR"/Canon.txt
$exiftoolpath/exiftool ${tagoption} -Casio:all > "$DIR"/Casio.txt
$exiftoolpath/exiftool ${tagoption} -HP:all > "$DIR"/HP.txt
$exiftoolpath/exiftool ${tagoption} -Kodak:all > "$DIR"/Kodak.txt
$exiftoolpath/exiftool ${tagoption} -Leaf:all > "$DIR"/Leaf.txt
$exiftoolpath/exiftool ${tagoption} -Minolta:all > "$DIR"/Minolta.txt
$exiftoolpath/exiftool ${tagoption} -Motorola:all > "$DIR"/Motorola.txt
$exiftoolpath/exiftool ${tagoption} -Nikon:all > "$DIR"/Nikon.txt
$exiftoolpath/exiftool ${tagoption} -Olympus:all > "$DIR"/Olympus.txt
$exiftoolpath/exiftool ${tagoption} -Panasonic:all > "$DIR"/Panasonic.txt
$exiftoolpath/exiftool ${tagoption} -Pentax:all > "$DIR"/Pentax.txt
$exiftoolpath/exiftool ${tagoption} -Ricoh:all > "$DIR"/Ricoh.txt
$exiftoolpath/exiftool ${tagoption} -Samsung:all > "$DIR"/Samsung.txt
$exiftoolpath/exiftool ${tagoption} -Sanyo:all > "$DIR"/Sanyo.txt
$exiftoolpath/exiftool ${tagoption} -Sony:all > "$DIR"/Sony.txt

printf "Now delete the ones that are actually not correctly produced\n\n"
cd "$DIR" 
grep -l exiftool *.txt | xargs rm

printf "Now make the text file have one tag per line; Then remove blank lines\n\n"
for i in *.txt;
do
  if [ "$1" = "all" ]
  then
      # We have the lines with "Available xyz tags:"; change space to underscore\n"
      sed -i '/^Available/s/ /_/g' "$i";
  else
      # We have the lines with "Writable xyz tags:"; change space to underscore\n"
      sed -i '/^Writable/s/ /_/g' "$i";
  fi
  # Change every space for a line feed
  sed -i 's/ /\n/g' "$i";
  # Remove all blank lines
  sed -i '/^[[:space:]]*$/d' "$i";
  if [ "$1" = "all" ]
  then
      # Change all underscores back to spaces
      sed -i '/^Available/s/_/ /g' "$i";
  else
      # Change all underscores back to spaces
      sed -i '/^Writable/s/_/ /g' "$i";
  fi
done

printf "\n\nYour tag text files are in folder \"${DIR}\"\n\n"
