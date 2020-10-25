package org.hvdw.jexiftoolgui.controllers;

import org.hvdw.jexiftoolgui.MyVariables;
import org.hvdw.jexiftoolgui.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommandLineArguments {
    //public List<File> filesList = new ArrayList<File>();
    private final static Logger logger = (Logger) LoggerFactory.getLogger(CommandLineArguments.class);


    public static File[] ProcessArguments(List<File> filesList) {
        String[] args = MyVariables.getcommandLineArgs();
        //filesList = new ArrayList<File>();
        boolean exists;
        boolean isDirectory;
        boolean isFile;

        if (args.length > 0) {
            // Work this out
            logger.debug("args.length {}", args.length);
            //logger.info("The command-line arguments are:");
            for (String arg : args) {
                Path path = Paths.get(arg);
                File file = new File(arg);
                //logger.info("arg: {}", arg);

                if (file.exists()) {
                    if (file.isFile()) {
                        filesList.add(file);
                        logger.debug("adding file {}", file.toString());
                    } else if (file.isDirectory()) {
                        logger.debug("arg {} detected as directory", file.toString());
                        File[] listOfFiles = file.listFiles();
                        for (File filecontent : listOfFiles) {
                            if (filecontent.isFile()) {
                                logger.debug("parsing folder, adding file {} ", filecontent.toString());
                                filesList.add(filecontent);
                            }
                            // We are currently not going to recursively parse subfolders. We might end up with tens of thousands of files
                        }
                    }
                }
            }
        }
        //finally convert possible relative paths to canonical paths
        logger.debug("filesList.size() {}", filesList.size());
        File[] files = new File[filesList.size()];
        int counter = 0;
        for (File workfile : filesList) {
            try {
                String canonicalpath = workfile.getCanonicalPath();
                files[counter] = new File(canonicalpath);
            } catch (IOException e) {
                logger.error("workfile.getCanonicalPath() error {}", e);
                e.printStackTrace();
            }
            counter++;
        }
        logger.debug("commandline files: {}", files.toString());

        return files;
    }
}
