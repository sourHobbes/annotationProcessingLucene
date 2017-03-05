package com.sdugar.lucene;


import com.sdugar.lucene.annotation.CustomAnnotation;
import org.apache.lucene.queryparser.classic.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.sdugar.lucene.LuceneMain.createIndex;
import static com.sdugar.lucene.LuceneMain.searchIndexWithPhraseQuery;
import static com.sdugar.lucene.LuceneMain.searchIndexWithQueryParser;

/**
 * Unit test for simple LuceneMain.
 */
public class LuceneMainTestBase {

    public static final Logger LOG = LoggerFactory.getLogger( LuceneMainTestBase.class );

    @BeforeClass
    public void setup() throws IOException {
        createIndex();
        LOG.info("setup completed");
    }

    @Test
    @CustomAnnotation
    public void basicTest() {
        Assert.assertTrue( true );
        LOG.info("Basic test cleared");
    }

    @Test
    @CustomAnnotation()
    public void queriesTest() throws IOException, ParseException, URISyntaxException {
        searchIndexWithPhraseQuery("french", "fries", 0);
        searchIndexWithPhraseQuery("hamburger", "steak", 0);
        searchIndexWithPhraseQuery("hamburger", "steak", 1);
        searchIndexWithPhraseQuery("hamburger", "steak", 2);
        searchIndexWithPhraseQuery("hamburger", "steak", 3);

        searchIndexWithQueryParser("french fries"); // BooleanQuery
        searchIndexWithQueryParser("\"french fries\""); // PhaseQuery
        searchIndexWithQueryParser("\"hamburger steak\"~1"); // PhaseQuery
        searchIndexWithQueryParser("\"hamburger steak\"~2"); // PhaseQuery
    }

    @Test
    public void localeFileQueries() throws IOException, ParseException, URISyntaxException {

    }
}
