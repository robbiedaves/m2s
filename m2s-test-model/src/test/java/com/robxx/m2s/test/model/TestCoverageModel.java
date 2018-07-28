package com.robxx.m2s.test.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import junit.framework.TestCase;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.File;
import java.net.URL;

public class TestCoverageModel  extends TestCase {

    public void testCoverages() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            URL url = this.getClass().getClassLoader().getResource("coverages.yml");
            Coverage user = mapper.readValue(url, Coverage.class);
            System.out.println(ReflectionToStringBuilder.toString(user, ToStringStyle.MULTI_LINE_STYLE));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
