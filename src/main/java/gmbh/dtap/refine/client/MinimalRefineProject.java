package gmbh.dtap.refine.client;

import gmbh.dtap.refine.api.CustomMetadata;
import gmbh.dtap.refine.api.ImportOptionMetadata;
import gmbh.dtap.refine.api.RefineProject;
import gmbh.dtap.refine.api.RefineProjectLocation;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;

import static org.apache.http.util.Asserts.notEmpty;
import static org.apache.http.util.Asserts.notNull;

/**
 * A minimal implementations of {@link RefineProject}.
 *
 * @since 0.1.0
 */
public class MinimalRefineProject implements RefineProject {

   private RefineProjectLocation location;
   private String name;
   private OffsetDateTime created;
   private OffsetDateTime modified;
   private String creator;
   private String contributors;
   private String subject;
   private String description;
   private long rowCount;
   private CustomMetadata customMetadata;
   private List<ImportOptionMetadata> importOptionMetadata;

   MinimalRefineProject(String name, RefineProjectLocation location) {
      notEmpty(name, "name");
      notNull(location, "location");
      this.name = name;
      this.location = location;
   }

   @Override
   public String getId() {
      return location.getId();
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public URL getUrl() {
      return location.getUrl();
   }

   @Override
   public RefineProjectLocation getLocation() {
      return location;
   }

   @Override
   public OffsetDateTime getCreated() {
      return created;
   }

   void setCreated(OffsetDateTime created) {
      this.created = created;
   }

   @Override
   public OffsetDateTime getModified() {
      return modified;
   }

   void setModified(OffsetDateTime modified) {
      this.modified = modified;
   }

   @Override
   public String getCreator() {
      return creator;
   }

   void setCreator(String creator) {
      this.creator = creator;
   }

   @Override
   public String getContributors() {
      return contributors;
   }

   void setContributors(String contributors) {
      this.contributors = contributors;
   }

   @Override
   public String getSubject() {
      return subject;
   }

   void setSubject(String subject) {
      this.subject = subject;
   }

   @Override
   public String getDescription() {
      return description;
   }

   void setDescription(String description) {
      this.description = description;
   }

   @Override
   public long getRowCount() {
      return rowCount;
   }

   void setRowCount(long rowCount) {
      this.rowCount = rowCount;
   }

   @Override
   public CustomMetadata getCustomMetadata() {
      return customMetadata;
   }

   void setCustomMetadata(CustomMetadata customMetadata) {
      this.customMetadata = customMetadata;
   }

   @Override
   public List<ImportOptionMetadata> getImportOptionMetadata() {
      return importOptionMetadata;
   }

   void setImportOptionMetadata(List<ImportOptionMetadata> importOptionMetadata) {
      this.importOptionMetadata = importOptionMetadata;
   }

   @Override
   public String toString() {
      return "MinimalRefineProject{" +
            "location=" + location +
            ", name='" + name + '\'' +
            ", created=" + created +
            ", modified=" + modified +
            ", creator='" + creator + '\'' +
            ", contributors='" + contributors + '\'' +
            ", subject='" + subject + '\'' +
            ", description='" + description + '\'' +
            ", rowCount=" + rowCount +
            ", customMetadata=" + customMetadata +
            ", importOptionMetadata=" + importOptionMetadata +
            '}';
   }
}
