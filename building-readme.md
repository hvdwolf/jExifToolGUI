# Building jExiftoolGUI

Building jExiftoolGUI is not really straightforward. The combination of gradle and IntellIJ is not a 100% easy "one-click-does-it-all" excercise.
I did not find a nice "one-click-does-it-all" method of building jExifToolGUI yet. Building artifacts is also a hit and miss excercise: Sometimes it works and creates a correct jar and sometimes it doesn't.
If some java/gradle/IntellIJ expert has info, please let me know.

## Initial steps to do

- Download the source code. Either as zip or via "git clone .."

- Start IntellIJ via "idea &" or via the menu.
- Import the project. Select "Import project from another model" (2nd radiobutton) and select "Gradle" as option.
- Go to (menu) File -> Settings -> Build, Execution, Deployment -> Build Tools -> Gradle. Make sure Gradle is select as "Build and run using" and "Run tests using".
- Go to (menu) File -> Settings -> Editor -> GUI Designer and set **Generate GUI into: Java source code** (This is a vital step!)
- Select (menu) Build -> Build Project

- Now open a terminal (command prompt) and "cd" into your project folder.<br>
-- On linux/MacOS use : `./gradlew assemble buildDependents shadowJar shadowDistZip`<br>
-- On Windows use: `gradlew.bat assemble buildDependents shadowJar shadowDistZip`

When done check the build folder.<br>
* `/build/libs` will contain `jExifToolGUI-all.jar` and `jExifToolGUI.jar`. The big "-all-jar" is the jar file that contains all dependency jars inside as well. This is the "one in all" jar.
the `jExifToolGUI.jar` can't be started by itself. You have to provede the depenedency jars via a `classpath` statement.<br>
* `build/distributions` will contain 2 zip files and 2 tar files (fot the user's preference). 
The `jExifToolGUI.tar (.zip)` will contain the "small" `jExifToolGUI.jar` bundled with all dependency jars in a `lib` folder. 
The `jExifToolGUI-shadow.tar (.zip)` contains the "big" `jExifToolGUI-all.jar` with all dependency jars inside it.

I always use the `build\libs\jExifToolGUI-all.jar` but rename it as `jExifToolGUI.jar`.


## Next steps when working on the project.
Every time you modify the Gui (one of the forms), or even a label text as part of the gui (form), you need to build first in IntelliJ. Sometimes it is best to do a "build -> Rebuild Project".<br>
Then you go into the terminal/command prompt again and do the "./gradlew assemble buildDependents shadowJar shadowDistZip" again.
And again: Take the `build\libs\jExifToolGUI-all.jar` (and rename it as `jExifToolGUI.jar`).

## Troubleshooting
Sometimes "something" doesn't work anymore, or you get "strange" error messages. The project sometimes get "corrupt" (due to some bugs in IntellIJ?)<br>
- Remove the `build` folder completely and do in IntellIJ a "Build -> Rebuild Project", followed by the `./gradlew assemble buildDependents shadowJar shadowDistZip`.
In 99% of the cases, everything will be OK again.