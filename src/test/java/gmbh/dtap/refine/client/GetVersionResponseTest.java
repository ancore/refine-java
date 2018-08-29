package gmbh.dtap.refine.client;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Tests for {@link GetVersionResponse}.
 */
public class GetVersionResponseTest {

   @Test
   public void should_have_all_constructor_parameters() {
      GetVersionResponse response = new GetVersionResponse("fullName", "fullVersion", "version", "revision");
      assertThat(response.getFullName()).isEqualTo("fullName");
      assertThat(response.getFullVersion()).isEqualTo("fullVersion");
      assertThat(response.getVersion()).isEqualTo("version");
      assertThat(response.getRevision()).isEqualTo("revision");
   }
}
