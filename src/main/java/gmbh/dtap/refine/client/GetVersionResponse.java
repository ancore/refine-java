package gmbh.dtap.refine.client;

/**
 * This class represents the response from the {@link GetVersionCommand}.
 */
public class GetVersionResponse {

   private final String fullName;
   private final String fullVersion;
   private final String version;
   private final String revision;

   /**
    * Contructor
    *
    * @param fullName    the full name
    * @param fullVersion the full version
    * @param version     the version
    * @param revision    the revision
    */
   GetVersionResponse(String fullName, String fullVersion, String version, String revision) {
      this.fullName = fullName;
      this.fullVersion = fullVersion;
      this.version = version;
      this.revision = revision;
   }

   /**
    * Returns the full name.
    *
    * @return the full name
    */
   public String getFullName() {
      return fullName;
   }

   /**
    * Returns the full version.
    *
    * @return the full version
    */
   public String getFullVersion() {
      return fullVersion;
   }

   /**
    * Returns the version.
    *
    * @return the version
    */
   public String getVersion() {
      return version;
   }

   /**
    * Returns the revision.
    *
    * @return the revision
    */
   public String getRevision() {
      return revision;
   }

   @Override
   public String toString() {
      return getFullName();
   }
}
