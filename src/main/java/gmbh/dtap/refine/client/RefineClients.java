package gmbh.dtap.refine.client;

import org.apache.http.impl.client.HttpClients;

import java.net.MalformedURLException;
import java.net.URL;

public interface RefineClients {

   static RefineClient create(String url) throws MalformedURLException {
      return new RefineClient(new URL(url), HttpClients.createDefault());
   }
}
