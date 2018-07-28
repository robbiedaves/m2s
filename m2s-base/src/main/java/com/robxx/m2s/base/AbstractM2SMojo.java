package com.robxx.m2s.base;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractM2SMojo extends AbstractMojo {

    /**
     * Project the plugin is called from.
     */
    @Parameter(property = "project", defaultValue = "${project}", required = true)
    protected MavenProject project;

    @Parameter(property = "modelDirectory", defaultValue = "model", required = true)
    protected String modelDirectory;

    /**
     * Represents the base package used for generated java classes.
     */
    @Parameter(property = "basePackage", defaultValue = "com.robxx", required = true)
    protected String basePackage;

    /**
     * Directory to write generated code to.
     */
    @Parameter(property = "outputDirectory")
    protected File outputDirectory;

    private List<File> modelFiles;


  ///  private List<Model> models = new ArrayList<>();

    public CodeGeneratorConfig getCodeGeneratorConfig(){
        CodeGeneratorConfig config = new CodeGeneratorConfig();
        return config;
    }

    String generatedSourcesDir() {
        return (outputDirectory != null) ? outputDirectory.getAbsolutePath() :
                project.getBuild().getDirectory() + File.separator + "generated-sources";
    }

    private List<File> getResourceFiles(File workingDir) {
        List<File> dataFile = new ArrayList<File>();
        if (workingDir.exists()) {
            File[] files = workingDir.listFiles();
            for (File eachFile : files) {
                if (eachFile.isDirectory()) {
                    dataFile.addAll(getResourceFiles(eachFile));
                } else if (eachFile.getName().endsWith(".yaml") || eachFile.getName().endsWith(".yml")) {
                    dataFile.add(eachFile);
                }
            }
        }
        return dataFile;
    }

    public void execute() throws MojoExecutionException, MojoFailureException {

        // Add the generated source folder to the compile path
        project.addCompileSourceRoot(generatedSourcesDir());

        getLog().info("m2s - Model-2-Source plugin starting...");

        // Get all the files in the resource folder
        List<Resource> res = project.getBuild().getResources();
        String resource = "";
        if (res != null && res.size() >= 1) {
            resource =  res.get(0).getDirectory();
        }
        resource += "/" + modelDirectory;
        this.modelFiles = getResourceFiles(new File(resource));

        for (File f : this.modelFiles) {
            getLog().info("File = " + f.getPath() + "     " + f.getName());
        }

        getLog().info("m2s - Model-2-Source plugin ended.");

    }

    public abstract List<AbstractM2SModel> loadModels();
}
