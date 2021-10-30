======== Additional mac info =================

= necessary tools: hfsplus or hfsplus-tool, hfsprogs
= the packr tool (>= verion 4) to create a mac bundle from your java jar (https://github.com/libgdx/packr)
= the lib dmg package (https://github.com/fanquake/libdmg-hfsplus)
== Note: In this folder you will find a Ubuntu focal build x86-64 built dmg that should be pretty "universal" for 64bit linuxes.
= (optional) the icnsutils package to create an icon pack (optional as the appIcon.icns is already there)

# Building the bundle and packing it in a compressed dmg is "explained" in the "make_bundle.sh" script
# Simply follow/read the commands

======== open a compressed dmg on linux (uncompressed is easy using the above necessary tools) =================
Use/install 7z

7z x <some>.dmg

This will deliver "something" like:
Extracting  0.MBR
Extracting  1.Primary GPT Header
Extracting  2.Primary GPT Table
Extracting  3.free
Extracting  4.hfs
Extracting  5.free
Extracting  6.Backup GPT Table
Extracting  7.Backup GPT Header

For above example do:
mount -o loop -t hfsplus 4.hfs

(Simpler ones might giveExtracting  0.ddm
Extracting  1.Apple_partition_map
Extracting  2.hfs
Extracting  3.free

Then do: mount -o loop -t hfsplus 2.hfs)

(dmg2img is also mentioned but does not work on 10.6+ dmg compressed files)


======== creating/unpacking mac icns file ====
install icnsutils package

Make 5 sets of png files
icon_16px.png
icon_32px.png
icon_48px.png
icon_128px.png
icon_256px.png
icon_512px.png

then do: png2icns icon.icns icon_*px.png

extracting: icns2png x icons.icns


