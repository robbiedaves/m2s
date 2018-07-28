package com.robxx;

import com.robxx.m2s.base.AbstractM2SModel;
import com.robxx.m2s.base.AbstractM2SMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.List;

@Mojo(name = "gensrc", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class MyMojo extends AbstractM2SMojo
{

    @Override
    public List<AbstractM2SModel> loadModels() {
        return null;
    }
}
