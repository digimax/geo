package com.digimax.geo.services.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

/**
 * Created by jonwilliams on 2014-06-02.
 */
public class WebRefresherImpl implements WebRefresher {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebRefresherImpl.class);


    @Override
    public void spawnRefresher(List<String> webUrls)
    {
//        ThreadManager.createThreadForCurrentRequest(new Spawn(webUrls)).start();
        new Thread(new Spawn(webUrls)).start();
        LOGGER.debug("DONE creating and running SPAWN thread.");
    }

    public static final class Spawn implements Runnable {

        private final static Logger LOGGER = LoggerFactory.getLogger(Spawn.class);

        private List<String> webUrls;

        public Spawn(List<String> webUrls) {
            this.webUrls = webUrls;
        }

        @Override
        public void run() {
            while (true) {
                long instant = 0;
                for (String webUrl : webUrls) {
                    instant = System.currentTimeMillis();
                    LOGGER.debug("{}::Loading {} ...", new Date(), webUrl);
                    downloadURL(webUrl);
                    long finished = System.currentTimeMillis();
                    LOGGER.debug("           ... SUCCESS in {} ms", finished-instant);
                    if (finished-instant>5000) {
                        LOGGER.debug("^^^          ***************** >5sec warning *****************");
                    }
                }
                try {
                    int sleepTime = 120000;
                    LOGGER.debug("{}::Put THREAD TO SLEEP for {} seconds", new Date() ,sleepTime/1000);
                    instant = System.currentTimeMillis();
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    LOGGER.error("ERROR: Thread Interrupted", e);
                } finally {
                    long finishTime = System.currentTimeMillis();
                    LOGGER.debug("           ...thread slept {} seconds.", (finishTime-instant)/1000);
                }
            }
        }

        private String downloadURL(String theURL)
        {
            int readTimeout = 60000;
            URL u;
            InputStream inputStream = null;
            String string;
            StringBuilder stringBuilder = new StringBuilder();

            try
            {
                u = new URL(theURL);
                URLConnection urlConnection = u.openConnection();
                urlConnection.setReadTimeout(readTimeout);
                inputStream = urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while ((string = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(string).append("\n");
                }
            }
            catch (MalformedURLException mue)
            {
                LOGGER.error("ERROR - a MalformedURLException happened.", mue);
            }
            catch (IOException ioe)
            {
                LOGGER.error("ERROR- an IOException happened. > {} seconds to read page.", readTimeout/1000, ioe);
            }
            finally
            {
                try
                {
                    if (inputStream!=null) {
                        inputStream.close();
                    }
                }
                catch (IOException ioe)
                {
                    LOGGER.debug("ERROR closing stream", ioe);
                }
            }
            return stringBuilder.toString();
        }
    }
}
